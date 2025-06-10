package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Dish;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // 导入TableCellRenderer
import javax.swing.table.TableCellEditor; // 导入TableCellEditor
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * 菜品管理面板
 * 负责菜品的显示、搜索、新增、编辑和删除功能
 */
public class DishManagementPanel extends JPanel {

    private JTable dishTable; // 菜品表格
    private DefaultTableModel tableModel; // 表格模型
    private JTextField searchField; // 搜索输入框
    private JLabel totalDataLabel; // 总数据条数标签

    public DishManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JButton addButton = new JButton("新增");
        addButton.addActionListener(e -> showDishDialog(null));
        topPanel.add(addButton);

        JButton batchDeleteButton = new JButton("批量删除");
        batchDeleteButton.addActionListener(e -> batchDeleteDishes());
        topPanel.add(batchDeleteButton);

        topPanel.add(new JLabel("名称"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> searchDishes());
        topPanel.add(searchButton);

        add(topPanel, BorderLayout.NORTH);

        // 菜品列表表格
        String[] columnNames = {"序号", "名称", "价格", "状态", "简介", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 只有操作列可编辑（用于按钮渲染）
                return column == getColumnCount() - 1;
            }
        };
        dishTable = new JTable(tableModel);
        dishTable.setRowHeight(30);
        dishTable.getTableHeader().setReorderingAllowed(false); // 禁止列拖动

        // 设置操作列的渲染器和编辑器
        dishTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        dishTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(dishTable);
        add(scrollPane, BorderLayout.CENTER);

        // 底部信息和分页区域 (简化，只显示总条数)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);
        totalDataLabel = new JLabel("共 " + DataService.getAllDishes().size() + " 条数据");
        bottomPanel.add(totalDataLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        loadDishes(); // 加载初始数据
    }

    /**
     * 加载菜品数据到表格
     */
    private void loadDishes() {
        tableModel.setRowCount(0); // 清空现有数据
        List<Dish> dishes = DataService.getAllDishes();
        for (Dish dish : dishes) {
            Vector<Object> row = new Vector<>();
            row.add(dish.getId());
            row.add(dish.getName());
            row.add(dish.getPrice());
            row.add(dish.getStatus());
            row.add(dish.getDescription());
            row.add("操作"); // 占位符，实际显示为按钮
            tableModel.addRow(row);
        }
        // 更新底部总条数
        totalDataLabel.setText("共 " + dishes.size() + " 条数据");
    }

    /**
     * 搜索菜品
     */
    private void searchDishes() {
        String searchText = searchField.getText().trim();
        tableModel.setRowCount(0); // 清空现有数据
        List<Dish> dishes = DataService.searchDishesByName(searchText);
        for (Dish dish : dishes) {
            Vector<Object> row = new Vector<>();
            row.add(dish.getId());
            row.add(dish.getName());
            row.add(dish.getPrice());
            row.add(dish.getStatus());
            row.add(dish.getDescription());
            row.add("操作");
            tableModel.addRow(row);
        }
        // 更新底部总条数
        totalDataLabel.setText("共 " + dishes.size() + " 条数据");
    }

    /**
     * 显示菜品编辑/新增对话框
     * @param dish 待编辑的菜品对象，如果为null则表示新增
     */
    private void showDishDialog(Dish dish) {
        DishDialog dialog = new DishDialog(SwingUtilities.getWindowAncestor(this), dish);
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            loadDishes(); // 对话框成功关闭后刷新列表
        }
    }

    /**
     * 批量删除菜品
     */
    private void batchDeleteDishes() {
        int[] selectedRows = dishTable.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的菜品！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除选中的 " + selectedRows.length + " 条菜品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // 从后往前删除，避免索引问题
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int dishId = (int) tableModel.getValueAt(selectedRows[i], 0);
                DataService.deleteDish(dishId);
            }
            loadDishes();
            JOptionPane.showMessageDialog(this, "删除成功！");
        }
    }

    /**
     * 表格操作列的渲染器
     */
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("编辑");
            editButton.setFocusPainted(false);
            editButton.setBackground(new Color(70, 130, 180)); // 蓝色
            editButton.setForeground(Color.WHITE);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(editButton);

            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60)); // 红色
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
        private JButton editButton;
        private JButton deleteButton;
        private int currentRow;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("编辑");
            editButton.setFocusPainted(false);
            editButton.setBackground(new Color(70, 130, 180));
            editButton.setForeground(Color.WHITE);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            editButton.addActionListener(e -> {
                fireEditingStopped();
                int dishId = (int) dishTable.getValueAt(currentRow, 0);
                Dish dishToEdit = DataService.getAllDishes().stream()
                        .filter(d -> d.getId() == dishId)
                        .findFirst().orElse(null);
                showDishDialog(dishToEdit);
            });
            panel.add(editButton);

            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(dishTable, "确定要删除这条菜品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    int dishId = (int) dishTable.getValueAt(currentRow, 0);
                    DataService.deleteDish(dishId);
                    loadDishes();
                    JOptionPane.showMessageDialog(dishTable, "删除成功！");
                }
            });
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentRow = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "操作"; // 返回一个占位符，实际不重要
        }
    }
}


