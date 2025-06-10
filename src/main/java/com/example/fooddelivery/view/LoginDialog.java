package com.example.fooddelivery.view;

import com.example.fooddelivery.model.User;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean loginSuccess = false;
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    public LoginDialog(Frame parent) {
        super(parent, "登录", true);
        setLayout(new BorderLayout());
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setResizable(false);

        // 创建主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户名输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("用户名:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        // 密码输入
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("密码:"), gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton loginButton = new JButton("登录");
        JButton cancelButton = new JButton("取消");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            // 首先尝试使用默认账号密码
            if (username.equals(DEFAULT_USERNAME) && password.equals(DEFAULT_PASSWORD)) {
                loginSuccess = true;
                dispose();
                return;
            }

            // 然后尝试从数据库验证
            User user = DataService.validateUser(username, password);
            if (user != null) {
                loginSuccess = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            System.exit(0);
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }
} 