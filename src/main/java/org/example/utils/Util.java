package org.example.utils;

import org.example.entity.User;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

public interface Util {

    TestConnection testConnection = TestConnection.getInstance();

    static Optional<User> getUserById(Long id) {
        String query = String.format("SELECT * FROM users WHERE id = '%s';", id);

        try (Statement statement = testConnection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            if (resultSet.next()) {
                User user = new User();
                user.setId(Long.valueOf(resultSet.getInt("id")));
                user.setName(resultSet.getString("name"));
                user.setState(States.valueOf(resultSet.getString("state")));
                return Optional.of(user);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying the database", e);
        }
    }

    static void save(User user) {
        String query = "INSERT INTO users (id, name, state) VALUES (?, ?, ?) RETURNING id";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setLong(1, user.getId());
            pstmt.setString(2, user.getName());
            pstmt.setString(3, String.valueOf(user.getState()));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user.setId(rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String MY_CARDS = "my cards";
    String ADD_CARD = "add card";
    String TRANSFER = "transfer";
    String HISTORY = "history";
    String DEPOSIT = "deposit";
    String[][] userMenu = {
            {MY_CARDS, ADD_CARD},
            {TRANSFER, HISTORY},
            {DEPOSIT}
    };
}
