package encell;

import java.sql.*;

public class Database {
    private Connection conn;

    public Database() throws SQLException {
        // Автоматическое создание базы данных в памяти (или в файле)
        this.conn = DriverManager.getConnection("jdbc:h2:./woodshop", "sa", "");
        createTables();
    }

    private void createTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Таблица продуктов
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    price DOUBLE NOT NULL,
                    quantity INT NOT NULL
                )
            """);

            // Таблица клиентов
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS customers (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    phone VARCHAR(20) NOT NULL,
                    email VARCHAR(255) NOT NULL
                )
            """);

            // Таблица заказов
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    customer_id INT,
                    product_id INT,
                    date DATE NOT NULL,
                    status VARCHAR(50) DEFAULT 'Не выполнен',
                    FOREIGN KEY(customer_id) REFERENCES customers(id),
                    FOREIGN KEY(product_id) REFERENCES products(id)
                )
            """);

            // Таблица сотрудников
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS employees (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    position VARCHAR(255) NOT NULL,
                    phone VARCHAR(20) NOT NULL
                )
            """);
        }
    }

    public void addProduct(String name, double price, int quantity) throws SQLException {
        String sql = "INSERT INTO products (name, price, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getAllProducts() throws SQLException {
        String sql = "SELECT * FROM products";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public void addCustomer(String name, String phone, String email) throws SQLException {
        String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
}