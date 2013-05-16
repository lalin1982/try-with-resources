package com.codecriticon.arm.jdbc;

import java.sql.*;

public class JDBCBeforeARM {

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.USERS");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.getCause();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.getCause();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.getCause();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.getCause();
                }
            }

        }
    }
}
