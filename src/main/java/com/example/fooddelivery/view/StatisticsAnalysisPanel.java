package com.example.fooddelivery.view;

import javax.swing.*;
import java.awt.*;

/**
 * 统计分析面板
 * 负责统计分析功能的显示
 */
public class StatisticsAnalysisPanel extends JPanel {

    public StatisticsAnalysisPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("统计分析界面", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        add(titleLabel, BorderLayout.CENTER);
    }
}


