package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final RowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private static final JdbcUserResultSetExtractor EXTRACTOR = new JdbcUserResultSetExtractor();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validateFields(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            int updatedLines = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource);

            if (updatedLines == 0) {
                return null;
            }

            jdbcTemplate.update("DELETE FROM user_role WHERE user_id=?", user.getId());
        }

        if (!user.getRoles().isEmpty()) {
            batchInsertRole(user.getRoles(), user.getId());
        }

        return user;
    }

    private void batchInsertRole(Set<Role> roles, int userId) {
        jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?, ?)",
                roles,
                roles.size(),
                (ps, argument) -> {
                    ps.setInt(1, userId);
                    ps.setString(2, argument.name());
                });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM (users u LEFT OUTER JOIN user_role r ON u.id = r.user_id) WHERE u.id=?", EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM (users u LEFT OUTER JOIN user_role r ON u.id = r.user_id) WHERE u.email=?", EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM (users u LEFT OUTER JOIN user_role r ON u.id = r.user_id) ORDER BY u.name, u.email", EXTRACTOR);
    }

    private static class JdbcUserResultSetExtractor implements ResultSetExtractor<List<User>> {

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            final Map<Integer, User> resultUserMap = new LinkedHashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                String roleStr = rs.getString("role");
                User user = resultUserMap.get(id);
                if (user != null && roleStr != null) {
                    Role role = Role.valueOf(roleStr);
                    user.getRoles().add(role);
                } else {
                    user = ROW_MAPPER.mapRow(rs, rs.getRow());
                    EnumSet<Role> roles = roleStr != null ? EnumSet.of(Role.valueOf(roleStr)) : EnumSet.noneOf(Role.class);
                    user.setRoles(roles);
                    resultUserMap.put(user.getId(), user);
                }
            }

            return new ArrayList<>(resultUserMap.values());
        }
    }
}
