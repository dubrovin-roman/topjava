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

import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        ValidationUtil.validateFields(validator, user);

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
        List<User> users = jdbcTemplate.query("SELECT * FROM (users u LEFT OUTER JOIN user_role r ON u.id = r.user_id) WHERE u.id=?", new JdbcUserResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM (users u LEFT OUTER JOIN user_role r ON u.id = r.user_id) WHERE u.email=?", new JdbcUserResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM (users u LEFT OUTER JOIN user_role r ON u.id = r.user_id) ORDER BY u.name, u.email", new JdbcUserResultSetExtractor());
    }

    private static class JdbcUserResultSetExtractor implements ResultSetExtractor<List<User>> {
        private static final RowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

        private final Map<Integer, User> resultUserMap = new HashMap<>();

        @Override
        public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
            while (rs.next()) {
                int id = rs.getInt("id");
                String roleStr = rs.getString("role");

                if (resultUserMap.containsKey(id)) {
                    if (roleStr != null) {
                        Role role = Role.valueOf(roleStr);
                        User user = resultUserMap.get(id);
                        user.getRoles().add(role);
                    }
                } else {
                    User user = ROW_MAPPER.mapRow(rs, rs.getRow());
                    EnumSet<Role> roles = roleStr != null ? EnumSet.of(Role.valueOf(roleStr)) : EnumSet.noneOf(Role.class);
                    user.setRoles(roles);
                    resultUserMap.put(user.getId(), user);
                }
            }

            return resultUserMap.values()
                    .stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .toList();
        }
    }
}
