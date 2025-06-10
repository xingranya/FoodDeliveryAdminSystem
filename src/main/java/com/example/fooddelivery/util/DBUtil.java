package com.example.fooddelivery.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库连接工具类
 */
public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/java_food?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            // 加载MySQL JDBC驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("无法加载MySQL JDBC驱动: " + e.getMessage());
            e.printStackTrace();
            // 实际应用中，这里应该抛出运行时异常或进行更完善的错误处理
        }
    }

    /**
     * 获取数据库连接
     * @return 数据库连接对象
     * @throws SQLException 如果连接失败
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 关闭数据库连接
     * @param connection 待关闭的连接对象
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("关闭数据库连接失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭ResultSet、PreparedStatement和Connection
     * @param rs ResultSet对象
     * @param pstmt PreparedStatement对象
     * @param conn Connection对象
     */
    public static void closeConnection(ResultSet rs, PreparedStatement pstmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("关闭ResultSet失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("关闭PreparedStatement失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("关闭Connection失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭PreparedStatement和Connection
     * @param pstmt PreparedStatement对象
     * @param conn Connection对象
     */
    public static void closeConnection(PreparedStatement pstmt, Connection conn) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("关闭PreparedStatement失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("关闭Connection失败: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // 简单的测试连接
        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                System.out.println("数据库连接成功！");
            } else {
                System.out.println("数据库连接失败！");
            }
        } catch (SQLException e) {
            System.err.println("数据库连接异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
}


