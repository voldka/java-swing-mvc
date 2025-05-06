package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String SERVER_NAME = "localhost";
    private static final String DATABASE_NAME = "QuanLyKho";

    // Thông tin đăng nhập SQL Server
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "1234";

    private static Connection connection = null;

    public static synchronized Connection getConnection() {
        try {
            // Kiểm tra xem connection có còn hoạt động không
            if (connection == null || connection.isClosed()) {
                // Load the SQL Server JDBC driver
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

                // Connection URL for SQL Server Authentication
                String url = "jdbc:sqlserver://" + SERVER_NAME + ";databaseName=" + DATABASE_NAME
                        + ";user=" + USERNAME + ";password=" + PASSWORD + ";trustServerCertificate=true";

                System.out.println("Đang kết nối đến SQL Server...");
                connection = DriverManager.getConnection(url);
                System.out.println("Kết nối đến SQL Server thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQL Server JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi kết nối SQL Server: " + e.getMessage());
            e.printStackTrace();
            connection = null; // Đặt lại connection về null trong trường hợp lỗi
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("SQL Server connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}