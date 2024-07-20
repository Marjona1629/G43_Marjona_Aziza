package org.example.service;

import org.example.entity.User;
import org.example.utils.States;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.Util.testConnection;

public class ServiceMethods {

    public void updateUserName(User user, String newName){
        Long id = user.getId();
        String checkQuery = "SELECT COUNT(*) FROM users WHERE id = '" + id + "';";
        String updateQuery = "UPDATE users SET name = '" + newName + "' WHERE id = '" + id + "';";

        try (Statement statement = testConnection.getConnection().createStatement()) {
            if (statement.executeQuery(checkQuery).next()) {
                statement.executeUpdate(updateQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateState(User user, States state) {
        Long id = user.getId();
        String updateQuery = "UPDATE users SET state = ? WHERE id = ?";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, state.name());
            pstmt.setLong(2, id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String showUserCards(Long userId) {
        String query = "SELECT id, number, balance FROM card WHERE user_id = " + userId + ";";
        StringBuilder result = new StringBuilder();

        try (Statement statement = testConnection.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String cardNumber = resultSet.getString("number");
                BigDecimal cardBalance = resultSet.getBigDecimal("balance");

                result.append("Card number: ").append(cardNumber)
                        .append(" ,  balance: ").append(cardBalance)
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    public Long addNewCard(Long id, String text) {
        String insertQuery = "INSERT INTO card (number, password, balance, user_id) VALUES (?, ?, 0.0, ?) RETURNING id";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, text);
            pstmt.setString(2, "no-password");
            pstmt.setLong(3, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkDuplicateNumber(String cardNumber) {
        String checkQuery = "SELECT COUNT(*) FROM card WHERE number = ?;";

        try (PreparedStatement preparedStatement = testConnection.getConnection().prepareStatement(checkQuery)) {
            preparedStatement.setString(1, cardNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public void setCardPassword(String password, Long cardId) {
        String updateQuery = "UPDATE card SET password = ? WHERE id = ?";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, password);
            pstmt.setLong(2, cardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCardBalance(String newBalance, Long cardId) {
        String updateQuery = "UPDATE card SET balance = ? WHERE id = ?";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setBigDecimal(1, BigDecimal.valueOf(Long.parseLong(newBalance)));
            pstmt.setLong(2, cardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getUserCards(Long userId) {
        List<String> cardNumbers = new ArrayList<>();
        String query = "SELECT number FROM card WHERE user_id = ?";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, userId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    cardNumbers.add(resultSet.getString("number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardNumbers;
    }

    public void transferMoney(String amount, Long cardId) {
        String updateQuery = "UPDATE card SET balance = balance + ? WHERE id = ?";

        try (Connection conn = testConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setBigDecimal(1, BigDecimal.valueOf(Integer.parseInt(amount)));
            pstmt.setLong(2, cardId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}