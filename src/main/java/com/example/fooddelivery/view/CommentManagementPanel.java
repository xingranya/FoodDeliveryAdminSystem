package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Comment;
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
 * 评论管理面板
 * 负责评论的显示和删除功能
 */
public class CommentManagementPanel extends JPanel {

    private JTable commentTable; // 评论表格
    private DefaultTableModel tableModel; // 表格模型

    public CommentManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域 (目前只有刷新)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        JButton refreshButton = new JButton("刷新");
        refreshButton.addActionListener(e -> loadComments());
        topPanel.add(refreshButton);

        add(topPanel, BorderLayout.NORTH);

        // 评论列表表格
        String[] columnNames = {"ID", "用户ID", "菜品ID", "评分", "内容", "评论时间", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        commentTable = new JTable(tableModel);
        commentTable.setRowHeight(30);
        commentTable.getTableHeader().setReorderingAllowed(false);

        // 设置操作列的渲染器和编辑器
        commentTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        commentTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(commentTable);
        add(scrollPane, BorderLayout.CENTER);

        loadComments(); // 加载初始数据
    }

    /**
     * 加载评论数据到表格
     */
    private void loadComments() {
        tableModel.setRowCount(0); // 清空现有数据
        List<Comment> comments = DataService.getAllComments();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Comment comment : comments) {
            Vector<Object> row = new Vector<>();
            row.add(comment.getId());
            row.add(comment.getUserId());
            row.add(comment.getDishId());
            row.add(comment.getRating());
            row.add(comment.getContent());
            row.add(sdf.format(comment.getCommentTime()));
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
                int confirm = JOptionPane.showConfirmDialog(commentTable, "确定要删除这条评论吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentRow = commentTable.getSelectedRow();
                    int commentId = (int) tableModel.getValueAt(currentRow, 0);
                    DataService.deleteComment(commentId);
                    loadComments();
                    JOptionPane.showMessageDialog(commentTable, "删除成功！");
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


