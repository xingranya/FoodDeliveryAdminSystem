package com.example.fooddelivery.view;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统信息面板
 * 显示系统的基本信息，包括作者、版本、时间等
 */
public class SystemInfoPanel extends JPanel {
    
    private JLabel timeLabel;
    private Timer timer;
    
    public SystemInfoPanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        
        // 创建主面板，使用GridBagLayout进行布局
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 系统标题
        JLabel titleLabel = new JLabel("系统信息");
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // 作者信息
        JLabel authorLabel = new JLabel("作者：李伟明");
        authorLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(authorLabel, gbc);
        
        // 版本信息
        JLabel versionLabel = new JLabel("版本：1.0.3");
        versionLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        gbc.gridy = 2;
        mainPanel.add(versionLabel, gbc);
        
        // 开发时间
        JLabel developTimeLabel = new JLabel("开发时间：2025-06-13");
        developTimeLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        gbc.gridy = 3;
        mainPanel.add(developTimeLabel, gbc);
        
        // 当前时间
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16));
        gbc.gridy = 4;
        mainPanel.add(timeLabel, gbc);
        
        // 创建定时器，每秒更新一次时间
        timer = new Timer(1000, e -> updateTime());
        timer.start();
        
        // 添加主面板到中心
        add(mainPanel, BorderLayout.CENTER);
        
        // 添加版权信息
        JLabel copyrightLabel = new JLabel("© 2025 Food Delivery Admin System. All rights reserved.");
        copyrightLabel.setFont(new Font("Microsoft YaHei", Font.PLAIN, 12));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(copyrightLabel, BorderLayout.SOUTH);
    }
    
    /**
     * 更新时间显示
     */
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeLabel.setText("当前时间：" + sdf.format(new Date()));
    }
    
    /**
     * 在面板被移除时停止定时器
     */
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (timer != null) {
            timer.stop();
        }
    }
}


