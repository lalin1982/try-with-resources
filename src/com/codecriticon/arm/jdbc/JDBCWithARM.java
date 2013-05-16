package com.codecriticon.arm.jdbc;

import java.sql.*;

public class JDBCWithARM {

    public static void main(String[] args) {
        try(Connection conn = DriverManager.getConnection("jdbc:h2:mem:test", "sa", "");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM INFORMATION_SCHEMA.USERS")) {
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.getCause();
        }
    }
}
