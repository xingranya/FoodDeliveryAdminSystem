package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Log;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // 导入TableCellRenderer
import javax.swing.table.TableCellEditor; // 导入TableCellEditor
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Vector;

/**
 * 日志管理面板
 * 负责日志的显示和删除功能
 */
public class LogManagementPanel extends JPanel {

    private JTable logTable; // 日志表格
    private DefaultTableModel tableModel; // 表格模型

    public LogManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域 (目前只有刷新)
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
                return column == getColumnCount() - 1;
            }
        };
        logTable = new JTable(tableModel);
        logTable.setRowHeight(30);
        logTable.getTableHeader().setReorderingAllowed(false);

        // 设置操作列的渲染器和编辑器
        logTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        logTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(logTable);
        add(scrollPane, BorderLayout.CENTER);

        loadLogs(); // 加载初始数据
    }

    /**
     * 加载日志数据到表格
     */
    private void loadLogs() {
        tableModel.setRowCount(0); // 清空现有数据
        List<Log> logs = DataService.getAllLogs();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Log log : logs) {
            Vector<Object> row = new Vector<>();
            row.add(log.getId());
            row.add(log.getOperatorId());
            row.add(log.getOperation());
            row.add(sdf.format(log.getOperationTime()));
            row.add("操作");
            tableModel.addRow(row);
        }
    }

    /**
     * 表格操作列的渲染器
     */
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * 表格操作列的编辑器
     */
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton deleteButton;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(logTable, "确定要删除这条日志吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentRow = logTable.getSelectedRow();
                    int logId = (int) tableModel.getValueAt(currentRow, 0);
                    DataService.deleteLog(logId);
                    loadLogs();
                    JOptionPane.showMessageDialog(logTable, "删除成功！");
                }
            });
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "操作";
        }
    }
}


