package encell;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Main {
    private JFrame frame;
    private Database db;

    public Main() throws SQLException {
        db = new Database();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Магазин Дерева");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));

        JButton addProductButton = new JButton("Добавить продукт");
        addProductButton.addActionListener(e -> openAddProductForm());
        panel.add(addProductButton);

        JButton viewProductsButton = new JButton("Просмотреть продукты");
        viewProductsButton.addActionListener(e -> viewProducts());
        panel.add(viewProductsButton);

        JButton addCustomerButton = new JButton("Добавить клиента");
        addCustomerButton.addActionListener(e -> openAddCustomerForm());
        panel.add(addCustomerButton);

        JButton viewCustomersButton = new JButton("Просмотреть клиентов");
        viewCustomersButton.addActionListener(e -> viewCustomers());
        panel.add(viewCustomersButton);

        JButton exitButton = new JButton("Выход");
        exitButton.addActionListener(e -> {
            try {
                db.close();
                System.exit(0);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        panel.add(exitButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private void openAddProductForm() {
        JFrame form = new JFrame("Добавление продукта");
        form.setSize(400, 200);
        form.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Название:");
        JTextField nameField = new JTextField();
        JLabel priceLabel = new JLabel("Цена:");
        JTextField priceField = new JTextField();
        JLabel quantityLabel = new JLabel("Количество:");
        JTextField quantityField = new JTextField();

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                db.addProduct(name, price, quantity);
                JOptionPane.showMessageDialog(form, "Продукт успешно добавлен!");
                form.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Ошибка: " + ex.getMessage());
            }
        });

        form.add(nameLabel);
        form.add(nameField);
        form.add(priceLabel);
        form.add(priceField);
        form.add(quantityLabel);
        form.add(quantityField);
        form.add(saveButton);

        form.setVisible(true);
    }

    private void viewProducts() {
        try {
            StringBuilder sb = new StringBuilder("Список продуктов:\n");
            var rs = db.getAllProducts();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id"))
                        .append(", Название: ").append(rs.getString("name"))
                        .append(", Цена: ").append(rs.getDouble("price"))
                        .append(", Количество: ").append(rs.getInt("quantity"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Ошибка при получении данных: " + e.getMessage());
        }
    }

    private void openAddCustomerForm() {
        JFrame form = new JFrame("Добавление клиента");
        form.setSize(400, 200);
        form.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Имя:");
        JTextField nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Телефон:");
        JTextField phoneField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();

        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String email = emailField.getText();
                db.addCustomer(name, phone, email);
                JOptionPane.showMessageDialog(form, "Клиент успешно добавлен!");
                form.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(form, "Ошибка: " + ex.getMessage());
            }
        });

        form.add(nameLabel);
        form.add(nameField);
        form.add(phoneLabel);
        form.add(phoneField);
        form.add(emailLabel);
        form.add(emailField);
        form.add(saveButton);

        form.setVisible(true);
    }

    private void viewCustomers() {
        try {
            StringBuilder sb = new StringBuilder("Список клиентов:\n");
            var rs = db.getAllCustomers();
            while (rs.next()) {
                sb.append("ID: ").append(rs.getInt("id"))
                        .append(", Имя: ").append(rs.getString("name"))
                        .append(", Телефон: ").append(rs.getString("phone"))
                        .append(", Email: ").append(rs.getString("email"))
                        .append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Ошибка при получении данных: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new Main();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}