package com.example.fooddelivery.view;

import javax.swing.*;
import java.awt.*;

/**
 * 运营管理面板
 * 负责运营相关功能的显示
 */
public class OperationManagementPanel extends JPanel {

    public OperationManagementPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("运营管理界面", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);
    }
}


