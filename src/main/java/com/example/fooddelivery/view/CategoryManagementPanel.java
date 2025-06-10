package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Category;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // 导入TableCellRenderer
import javax.swing.table.TableCellEditor; // 导入TableCellEditor
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * 分类管理面板
 * 负责分类的显示、新增、编辑和删除功能
 */
public class CategoryManagementPanel extends JPanel {

    private JTable categoryTable; // 分类表格
    private DefaultTableModel tableModel; // 表格模型
    private JTextField categoryNameField; // 分类名称输入框
    private JButton saveButton; // 保存按钮
    private JButton cancelButton; // 取消按钮
    private Category currentCategory; // 当前编辑的分类对象

    public CategoryManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("分类名称:"));
        categoryNameField = new JTextField(20);
        topPanel.add(categoryNameField);

        saveButton = new JButton("新增");
        saveButton.addActionListener(e -> saveCategory());
        topPanel.add(saveButton);

        cancelButton = new JButton("取消编辑");
        cancelButton.addActionListener(e -> clearForm());
        cancelButton.setVisible(false); // 初始隐藏
        topPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);

        // 分类列表表格
        String[] columnNames = {"ID", "名称", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        categoryTable = new JTable(tableModel);
        categoryTable.setRowHeight(30);
        categoryTable.getTableHeader().setReorderingAllowed(false);

        // 设置操作列的渲染器和编辑器
        categoryTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        categoryTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(categoryTable);
        add(scrollPane, BorderLayout.CENTER);

        loadCategories(); // 加载初始数据
    }

    /**
     * 加载分类数据到表格
     */
    private void loadCategories() {
        tableModel.setRowCount(0); // 清空现有数据
        List<Category> categories = DataService.getAllCategories();
        for (Category category : categories) {
            Vector<Object> row = new Vector<>();
            row.add(category.getId());
            row.add(category.getName());
            row.add("操作");
            tableModel.addRow(row);
        }
        clearForm(); // 刷新后清空表单
    }

    /**
     * 保存分类数据（新增或编辑）
     */
    private void saveCategory() {
        String name = categoryNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "分类名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (currentCategory == null) {
            // 新增
            Category newCategory = new Category(0, name); // ID由数据库生成
            DataService.addCategory(newCategory);
            JOptionPane.showMessageDialog(this, "分类添加成功！");
        } else {
            // 编辑
            currentCategory.setName(name);
            DataService.updateCategory(currentCategory);
            JOptionPane.showMessageDialog(this, "分类更新成功！");
        }
        loadCategories();
    }

    /**
     * 清空表单并重置为新增模式
     */
    private void clearForm() {
        categoryNameField.setText("");
        saveButton.setText("新增");
        cancelButton.setVisible(false);
        currentCategory = null;
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
            editButton.setBackground(new Color(70, 130, 180));
            editButton.setForeground(Color.WHITE);
            editButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            add(editButton);

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
                currentRow = categoryTable.getSelectedRow();
                int categoryId = (int) tableModel.getValueAt(currentRow, 0);
                String categoryName = (String) tableModel.getValueAt(currentRow, 1);
                currentCategory = new Category(categoryId, categoryName);
                categoryNameField.setText(categoryName);
                saveButton.setText("更新");
                cancelButton.setVisible(true);
            });
            panel.add(editButton);

            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(categoryTable, "确定要删除这条分类吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentRow = categoryTable.getSelectedRow();
                    int categoryId = (int) tableModel.getValueAt(currentRow, 0);
                    DataService.deleteCategory(categoryId);
                    loadCategories();
                    JOptionPane.showMessageDialog(categoryTable, "删除成功！");
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


