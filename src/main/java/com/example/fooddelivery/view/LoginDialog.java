package com.example.fooddelivery.view;

import com.example.fooddelivery.model.User;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import java.awt.*;

public class LoginDialog extends JDialog {
    private boolean loginSuccess = false;
    
    public LoginDialog(Frame parent) {
        super(parent, "登录", true);
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);

        // 创建标题面板
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(51, 51, 51));
        JLabel titleLabel = new JLabel("外卖菜品管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // 创建主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户名输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        mainPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(220, 30));
        mainPanel.add(usernameField, gbc);

        // 密码输入
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        mainPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        JPasswordField passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(220, 30));
        mainPanel.add(passwordField, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginButton = new JButton("登录");
        JButton cancelButton = new JButton("取消");
        
        // 设置按钮样式
        loginButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));

        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

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