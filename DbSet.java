package com.example.database;

import java.sql.*;

public class DbSet {
    protected Connection CreateConnection() {
        Connection conn;
        try {
            ConnectionString cs = new ConnectionString();
            conn = DriverManager.getConnection(cs.url, cs.user, cs.password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Statement CreateStatement() {
        try {
            var conn = CreateConnection();
            var stmt = conn.createStatement();
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected ResultSet ExecuteQuery(String query) {
        try {
            var stmt = CreateStatement();
            var result = stmt.executeQuery(query);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected int ExecuteUpdate(String query) {
        try {
            var stmt = CreateStatement();
            var result = stmt.executeUpdate(query);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public PreparedStatement PrepareStatement(String query) {
        try {
            var conn = CreateConnection();
            var stmt = conn.prepareStatement(query);
            return stmt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
