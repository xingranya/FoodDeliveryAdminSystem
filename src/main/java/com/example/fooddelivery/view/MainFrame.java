package com.example.fooddelivery.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主界面框架
 * 包含顶部导航栏、左侧菜单栏和内容显示区域
 */
public class MainFrame extends JFrame {

    private JPanel contentPanel; // 内容显示区域
    private CardLayout cardLayout; // 用于切换内容面板的布局管理器

    public MainFrame() {
        setTitle("菜品后台管理系统");
        setSize(1200, 800); // 设置窗口大小
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置默认关闭操作
        setLocationRelativeTo(null); // 窗口居中显示

        // 设置主布局为 BorderLayout
        setLayout(new BorderLayout());

        // 1. 创建顶部面板
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // 2. 创建左侧菜单面板
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);

        // 3. 创建内容显示面板
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        add(contentPanel, BorderLayout.CENTER);

        // 添加各个功能模块的面板到内容面板
        // 菜品管理面板 (Placeholder)
        DishManagementPanel dishManagementPanel = new DishManagementPanel();
        contentPanel.add(dishManagementPanel, "DishManagement");

        // 分类管理面板 (Placeholder)
        CategoryManagementPanel categoryManagementPanel = new CategoryManagementPanel();
        contentPanel.add(categoryManagementPanel, "CategoryManagement");

        // 标签管理面板 (Placeholder)
        TagManagementPanel tagManagementPanel = new TagManagementPanel();
        contentPanel.add(tagManagementPanel, "TagManagement");

        // 订单管理面板 (Placeholder)
        OrderManagementPanel orderManagementPanel = new OrderManagementPanel();
        contentPanel.add(orderManagementPanel, "OrderManagement");

        // 评论管理面板 (Placeholder)
        CommentManagementPanel commentManagementPanel = new CommentManagementPanel();
        contentPanel.add(commentManagementPanel, "CommentManagement");

        // 用户管理面板 (Placeholder)
        UserManagementPanel userManagementPanel = new UserManagementPanel();
        contentPanel.add(userManagementPanel, "UserManagement");

        // 运营管理面板 (Placeholder)
        OperationManagementPanel operationManagementPanel = new OperationManagementPanel();
        contentPanel.add(operationManagementPanel, "OperationManagement");

        // 日志管理面板 (Placeholder)
        LogManagementPanel logManagementPanel = new LogManagementPanel();
        contentPanel.add(logManagementPanel, "LogManagement");

        // 统计分析面板 (Placeholder)
        StatisticsAnalysisPanel statisticsAnalysisPanel = new StatisticsAnalysisPanel();
        contentPanel.add(statisticsAnalysisPanel, "StatisticsAnalysis");

        // 系统信息面板 (Placeholder)
        SystemInfoPanel systemInfoPanel = new SystemInfoPanel();
        contentPanel.add(systemInfoPanel, "SystemInfo");

        // 默认显示菜品管理界面
        cardLayout.show(contentPanel, "DishManagement");
    }

    /**
     * 创建顶部面板
     * @return 顶部面板
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(50, 50, 50)); // 深灰色背景
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(getWidth(), 60)); // 设置高度

        JLabel titleLabel = new JLabel("菜品后台管理系统");
        titleLabel.setForeground(Color.WHITE); // 白色字体
        titleLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 24)); // 字体和大小
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0)); // 左边距
        panel.add(titleLabel, BorderLayout.WEST);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        userInfoPanel.setOpaque(false); // 透明背景

        JButton previewButton = new JButton("前台预览");
        previewButton.setForeground(Color.WHITE);
        previewButton.setBackground(new Color(70, 70, 70));
        previewButton.setFocusPainted(false);
        previewButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        userInfoPanel.add(previewButton);

        JLabel adminLabel = new JLabel("管理员[admin123]");
        adminLabel.setForeground(Color.WHITE);
        userInfoPanel.add(adminLabel);

        JButton logoutButton = new JButton("退出");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(70, 70, 70));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        logoutButton.addActionListener(e -> {
            // 退出逻辑
            JOptionPane.showMessageDialog(this, "您已退出登录！");
            System.exit(0);
        });
        userInfoPanel.add(logoutButton);

        panel.add(userInfoPanel, BorderLayout.EAST);

        return panel;
    }

    /**
     * 创建左侧菜单面板
     * @return 左侧菜单面板
     */
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 40, 50)); // 深蓝色背景
        panel.setLayout(new GridLayout(0, 1, 0, 10)); // 垂直布局，组件之间有10像素间距
        panel.setPreferredSize(new Dimension(200, getHeight())); // 设置宽度
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // 内边距

        // 菜单项列表
        String[] menuItems = {
                "菜品管理", "分类管理", "标签管理", "订单管理", "评论管理",
                "用户管理", "运营管理", "日志管理", "统计分析", "系统信息"
        };
        String[] cardNames = {
                "DishManagement", "CategoryManagement", "TagManagement", "OrderManagement", "CommentManagement",
                "UserManagement", "OperationManagement", "LogManagement", "StatisticsAnalysis", "SystemInfo"
        };

        for (int i = 0; i < menuItems.length; i++) {
            JButton menuItemButton = createMenuItemButton(menuItems[i], cardNames[i]);
            panel.add(menuItemButton);
        }

        return panel;
    }

    /**
     * 创建菜单项按钮
     * @param text 按钮文本
     * @param cardName 对应内容面板的名称
     * @return 菜单项按钮
     */
    private JButton createMenuItemButton(String text, String cardName) {
        JButton button = new JButton(text);
        button.setForeground(Color.WHITE); // 白色字体
        button.setBackground(new Color(40, 50, 60)); // 按钮背景色
        button.setFont(new Font("Microsoft YaHei", Font.PLAIN, 16)); // 字体和大小
        button.setHorizontalAlignment(SwingConstants.LEFT); // 文本左对齐
        button.setFocusPainted(false); // 不绘制焦点框
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // 内边距

        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(60, 70, 80));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(40, 50, 60));
            }
        });

        // 添加点击事件，切换内容面板
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, cardName);
                // 可以在这里添加选中菜单项的样式变化
            }
        });
        return button;
    }

    public static void main(String[] args) {
        // 确保Swing GUI在事件调度线程中运行
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}


