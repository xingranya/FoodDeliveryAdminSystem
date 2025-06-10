package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Tag;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // 导入TableCellRenderer
import javax.swing.table.TableCellEditor; // 导入TableCellEditor
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * 标签管理面板
 * 负责标签的显示、新增、编辑和删除功能
 */
public class TagManagementPanel extends JPanel {

    private JTable tagTable; // 标签表格
    private DefaultTableModel tableModel; // 表格模型
    private JTextField tagNameField; // 标签名称输入框
    private JButton saveButton; // 保存按钮
    private JButton cancelButton; // 取消按钮
    private Tag currentTag; // 当前编辑的标签对象

    public TagManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("标签名称:"));
        tagNameField = new JTextField(20);
        topPanel.add(tagNameField);

        saveButton = new JButton("新增");
        saveButton.addActionListener(e -> saveTag());
        topPanel.add(saveButton);

        cancelButton = new JButton("取消编辑");
        cancelButton.addActionListener(e -> clearForm());
        cancelButton.setVisible(false); // 初始隐藏
        topPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);

        // 标签列表表格
        String[] columnNames = {"ID", "名称", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        tagTable = new JTable(tableModel);
        tagTable.setRowHeight(30);
        tagTable.getTableHeader().setReorderingAllowed(false);

        // 设置操作列的渲染器和编辑器
        tagTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        tagTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(tagTable);
        add(scrollPane, BorderLayout.CENTER);

        loadTags(); // 加载初始数据
    }

    /**
     * 加载标签数据到表格
     */
    private void loadTags() {
        tableModel.setRowCount(0); // 清空现有数据
        List<Tag> tags = DataService.getAllTags();
        for (Tag tag : tags) {
            Vector<Object> row = new Vector<>();
            row.add(tag.getId());
            row.add(tag.getName());
            row.add("操作");
            tableModel.addRow(row);
        }
        clearForm(); // 刷新后清空表单
    }

    /**
     * 保存标签数据（新增或编辑）
     */
    private void saveTag() {
        String name = tagNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "标签名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (currentTag == null) {
            // 新增
            Tag newTag = new Tag(0, name); // ID由数据库生成
            DataService.addTag(newTag);
            JOptionPane.showMessageDialog(this, "标签添加成功！");
        } else {
            // 编辑
            currentTag.setName(name);
            DataService.updateTag(currentTag);
            JOptionPane.showMessageDialog(this, "标签更新成功！");
        }
        loadTags();
    }

    /**
     * 清空表单并重置为新增模式
     */
    private void clearForm() {
        tagNameField.setText("");
        saveButton.setText("新增");
        cancelButton.setVisible(false);
        currentTag = null;
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
                currentRow = tagTable.getSelectedRow();
                int tagId = (int) tableModel.getValueAt(currentRow, 0);
                String tagName = (String) tableModel.getValueAt(currentRow, 1);
                currentTag = new Tag(tagId, tagName);
                tagNameField.setText(tagName);
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
                int confirm = JOptionPane.showConfirmDialog(tagTable, "确定要删除这条标签吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentRow = tagTable.getSelectedRow();
                    int tagId = (int) tableModel.getValueAt(currentRow, 0);
                    DataService.deleteTag(tagId);
                    loadTags();
                    JOptionPane.showMessageDialog(tagTable, "删除成功！");
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


