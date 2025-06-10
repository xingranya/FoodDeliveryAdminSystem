package com.example.fooddelivery.view;

import javax.swing.*;
import java.awt.*;

/**
 * 系统信息面板
 * 负责显示系统相关信息
 */
public class SystemInfoPanel extends JPanel {

    public SystemInfoPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("系统信息界面", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);
    }
}


