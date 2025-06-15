package com.example.fooddelivery.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.Vector;

/**
 * 日志管理面板（仅GUI，无实际功能）
 */
public class LogManagementPanel extends JPanel {
    private JTable logTable;
    private DefaultTableModel tableModel;

    public LogManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadLogs());
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        // 日志列表表格
        String[] columnNames = {"ID", "操作员ID", "操作内容", "操作时间", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        logTable = new JTable(tableModel);
        logTable.setRowHeight(30);
        logTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);

        loadLogs();
    }

    /**
     * 加载日志数据到表格（无实际数据）
     */
    private void loadLogs() {
        tableModel.setRowCount(0); // 清空现有数据
        // 不加载任何数据
    }
}


