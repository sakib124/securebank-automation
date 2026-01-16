package com.securebank.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
	//enter your database connection details here
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankapp_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private Connection connection;

    public void openConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet queryTransactionById(String txnId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, txnId);
        return stmt.executeQuery();
    }

    public ResultSet queryWithdrawalsByAccountId(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? AND transaction_type = 'withdrawal'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        return stmt.executeQuery();
    }

    public ResultSet queryAllTransactionsByAccountId(int accountId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM transactions WHERE account_id = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        return stmt.executeQuery();
    }

    public List<String> queryTransactionIdsByAccountIdAndDate(int accountId, String from, String to) throws SQLException {
        String sql = "SELECT transaction_id FROM transactions WHERE account_id = ? AND transaction_date BETWEEN ? AND ? ORDER BY transaction_date DESC";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        stmt.setString(2, from);
        stmt.setString(3, to);
        ResultSet rs = stmt.executeQuery();
        List<String> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getString("transaction_id"));
        }
        rs.close();
        stmt.close();
        return ids;
    }

    public int getUserIdByUsername(String username) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        int userId = -1;
        if (rs.next()) {
            userId = rs.getInt("user_id");
        }
        rs.close();
        stmt.close();
        return userId;
    }

    public int getAccountIdByUserIdAndType(int userId, String accountType) throws SQLException {
        String sql = "SELECT account_id FROM accounts WHERE user_id = ? AND account_type = ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, userId);
        stmt.setString(2, accountType.toLowerCase());
        ResultSet rs = stmt.executeQuery();
        int accountId = -1;
        if (rs.next()) {
            accountId = rs.getInt("account_id");
        }
        rs.close();
        stmt.close();
        return accountId;
    }

    public ResultSet queryLatestTransactionForAccount(int accountId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC, transaction_id DESC LIMIT 1";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, accountId);
        return stmt.executeQuery();
    }

    // Add more helper methods as needed for queries, updates, etc.
}