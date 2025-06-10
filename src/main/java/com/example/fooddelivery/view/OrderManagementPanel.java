package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.OrderItem;
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
 * 订单管理面板
 * 负责订单的显示, 查看详情和删除功能
 */
public class OrderManagementPanel extends JPanel {

    private JTable orderTable; // 订单表格
    private DefaultTableModel tableModel; // 表格模型

    public OrderManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域 (目前只有刷新)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadOrders());
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        // 订单列表表格
        String[] columnNames = {"ID", "用户ID", "下单时间", "总金额", "状态", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setRowHeight(30);
        orderTable.getTableHeader().setReorderingAllowed(false);

        // 设置操作列的渲染器和编辑器
        orderTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        orderTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        loadOrders(); // 加载初始数据
    }

    /**
     * 加载订单数据到表格
     */
    private void loadOrders() {
        tableModel.setRowCount(0); // 清空现有数据
        List<Order> orders = DataService.getAllOrders();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Order order : orders) {
            Vector<Object> row = new Vector<>();
            row.add(order.getId());
            row.add(order.getUserId());
            row.add(sdf.format(order.getOrderTime()));
            row.add(order.getTotalAmount());
            row.add(order.getStatus());
            row.add("操作");
            tableModel.addRow(row);
        }
    }

    /**
     * 显示订单详情对话框
     * @param order 待显示的订单对象
     */
    private void showOrderDetailDialog(Order order) {
        StringBuilder detail = new StringBuilder();
        detail.append("订单ID: ").append(order.getId()).append("\n");
        detail.append("用户ID: ").append(order.getUserId()).append("\n");
        detail.append("下单时间: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderTime())).append("\n");
        detail.append("总金额: ").append(String.format("%.2f", order.getTotalAmount())).append("\n");
        detail.append("状态: ").append(order.getStatus()).append("\n\n");
        detail.append("订单项:\n");
        for (OrderItem item : order.getDishItems()) {
            detail.append("  - 菜品ID: ").append(item.getDishId())
                  .append(", 数量: ").append(item.getQuantity())
                  .append(", 价格: ").append(String.format("%.2f", item.getPrice()))
                  .append("\n");
        }

        JTextArea textArea = new JTextArea(detail.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "订单详情", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 表格操作列的渲染器
     */
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton detailButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            detailButton = new JButton("详情");
            detailButton.setFocusPainted(false);
            detailButton.setBackground(new Color(70, 130, 180));
            detailButton.setForeground(Color.WHITE);
            detailButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(detailButton);

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
        private JButton detailButton;
        private JButton deleteButton;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            detailButton = new JButton("详情");
            detailButton.setFocusPainted(false);
            detailButton.setBackground(new Color(70, 130, 180));
            detailButton.setForeground(Color.WHITE);
            detailButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            detailButton.addActionListener(e -> {
                fireEditingStopped();
                currentRow = orderTable.getSelectedRow();
                int orderId = (int) tableModel.getValueAt(currentRow, 0);
                Order orderToView = DataService.getAllOrders().stream()
                        .filter(o -> o.getId() == orderId)
                        .findFirst().orElse(null);
                if (orderToView != null) {
                    showOrderDetailDialog(orderToView);
                }
            });
            panel.add(detailButton);

            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(orderTable, "确定要删除这条订单吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentRow = orderTable.getSelectedRow();
                    int orderId = (int) tableModel.getValueAt(currentRow, 0);
                    DataService.deleteOrder(orderId);
                    loadOrders();
                    JOptionPane.showMessageDialog(orderTable, "删除成功！");
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


