package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Order;
import com.example.fooddelivery.model.OrderItem;
import com.example.fooddelivery.model.Dish;
import com.example.fooddelivery.util.DataService;
import com.example.fooddelivery.util.LogUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // 导入TableCellRenderer
import javax.swing.table.TableCellEditor; // 导入TableCellEditor
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
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

        JButton addButton = new JButton("添加订单");
        addButton.addActionListener(e -> showAddOrderDialog());
        topPanel.add(addButton);

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
     * 显示添加订单对话框
     */
    private void showAddOrderDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "添加订单", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        // 创建主面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 创建基本信息面板
        JPanel basicInfoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户ID输入
        gbc.gridx = 0;
        gbc.gridy = 0;
        basicInfoPanel.add(new JLabel("用户ID:"), gbc);
        gbc.gridx = 1;
        JTextField userIdField = new JTextField(20);
        basicInfoPanel.add(userIdField, gbc);

        // 订单状态选择
        gbc.gridx = 0;
        gbc.gridy = 1;
        basicInfoPanel.add(new JLabel("订单状态:"), gbc);
        gbc.gridx = 1;
        String[] statuses = {"待支付", "已完成", "已取消"};
        JComboBox<String> statusComboBox = new JComboBox<>(statuses);
        basicInfoPanel.add(statusComboBox, gbc);

        mainPanel.add(basicInfoPanel, BorderLayout.NORTH);

        // 创建订单项面板
        JPanel orderItemsPanel = new JPanel(new BorderLayout());
        orderItemsPanel.setBorder(BorderFactory.createTitledBorder("订单项"));

        // 订单项表格
        String[] columnNames = {"菜品ID", "数量", "单价", "小计", "操作"};
        DefaultTableModel itemsTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1; // 只允许编辑数量列
            }
        };
        JTable itemsTable = new JTable(itemsTableModel);
        itemsTable.setRowHeight(30);

        // 添加数量变化监听器
        itemsTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 1) { // 数量列
                int row = e.getFirstRow();
                try {
                    int quantity = Integer.parseInt(itemsTable.getValueAt(row, 1).toString());
                    double price = Double.parseDouble(itemsTable.getValueAt(row, 2).toString());
                    double subtotal = quantity * price;
                    itemsTable.setValueAt(String.format("%.2f", subtotal), row, 3);
                } catch (NumberFormatException ex) {
                    // 忽略无效输入
                }
            }
        });

        JScrollPane itemsScrollPane = new JScrollPane(itemsTable);
        orderItemsPanel.add(itemsScrollPane, BorderLayout.CENTER);

        // 添加订单项按钮面板
        JPanel addItemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addItemButton = new JButton("添加菜品");
        addItemButton.addActionListener(e -> {
            // 获取所有可用菜品
            List<Dish> dishes = DataService.getAllDishes();
            if (dishes.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "没有可用的菜品！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 创建菜品选择对话框
            JDialog dishDialog = new JDialog(dialog, "选择菜品", true);
            dishDialog.setLayout(new BorderLayout());
            dishDialog.setSize(400, 300);
            dishDialog.setLocationRelativeTo(dialog);

            // 创建菜品列表
            JList<Dish> dishList = new JList<>(new Vector<>(dishes));
            dishList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Dish) {
                        Dish dish = (Dish) value;
                        setText(String.format("%s (ID: %d, 价格: %.2f)", dish.getName(), dish.getId(), dish.getPrice()));
                    }
                    return this;
                }
            });

            JScrollPane listScrollPane = new JScrollPane(dishList);
            dishDialog.add(listScrollPane, BorderLayout.CENTER);

            // 添加确认按钮
            JButton confirmButton = new JButton("确认");
            confirmButton.addActionListener(ev -> {
                Dish selectedDish = dishList.getSelectedValue();
                if (selectedDish != null) {
                    Vector<Object> row = new Vector<>();
                    row.add(selectedDish.getId());
                    row.add(1); // 默认数量为1
                    row.add(String.format("%.2f", selectedDish.getPrice()));
                    row.add(String.format("%.2f", selectedDish.getPrice()));
                    itemsTableModel.addRow(row);
                }
                dishDialog.dispose();
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(confirmButton);
            dishDialog.add(buttonPanel, BorderLayout.SOUTH);

            dishDialog.setVisible(true);
        });
        addItemPanel.add(addItemButton);
        orderItemsPanel.add(addItemPanel, BorderLayout.SOUTH);

        mainPanel.add(orderItemsPanel, BorderLayout.CENTER);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("保存");
        JButton cancelButton = new JButton("取消");

        saveButton.addActionListener(e -> {
            try {
                int userId = Integer.parseInt(userIdField.getText().trim());
                String status = (String) statusComboBox.getSelectedItem();
                
                if (itemsTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(dialog, "请至少添加一个菜品！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 计算总金额
                double totalAmount = 0;
                List<OrderItem> orderItems = new ArrayList<>();
                for (int i = 0; i < itemsTableModel.getRowCount(); i++) {
                    int dishId = (int) itemsTableModel.getValueAt(i, 0);
                    int quantity = Integer.parseInt(itemsTableModel.getValueAt(i, 1).toString());
                    double price = Double.parseDouble(itemsTableModel.getValueAt(i, 2).toString());
                    totalAmount += quantity * price;
                    orderItems.add(new OrderItem(dishId, quantity, price));
                }

                // 创建订单对象
                Order order = new Order(0, userId, new Date(), totalAmount, status, orderItems);
                DataService.addOrder(order);
                
                // 记录添加订单的日志
                LogUtil.logOperation(String.format("添加订单 - 用户ID: %d, 总金额: %.2f", userId, totalAmount));
                
                loadOrders();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "订单添加成功！");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "请输入有效的用户ID和数量！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
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


