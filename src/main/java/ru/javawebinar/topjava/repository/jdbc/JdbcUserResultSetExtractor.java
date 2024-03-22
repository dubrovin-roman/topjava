package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JdbcUserResultSetExtractor implements ResultSetExtractor<List<User>> {
    List<User> resultUserList = new ArrayList<>();

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        while (rs.next()) {
            int id = rs.getInt("id");
            String roleStr = rs.getString("role");

            if (isContainsUserById(id)) {
                if (roleStr != null) {
                    Role role = Role.valueOf(roleStr);
                    User user = getUserById(id);
                    user.getRoles().add(role);
                }
            } else {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getDate("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));

                if (roleStr != null) {
                    Role role = Role.valueOf(roleStr);
                    user.setRoles(List.of(role));
                }
                resultUserList.add(user);
            }
        }
        return resultUserList;
    }

    private boolean isContainsUserById(int id) {
        return resultUserList.stream()
                .anyMatch(user -> user.getId() != null && user.getId() == id);
    }

    private User getUserById(int id) {
        return resultUserList.stream()
                .filter(user -> user.getId() != null && user.getId() == id)
                .findFirst().get();
    }
}
