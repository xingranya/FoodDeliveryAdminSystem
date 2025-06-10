package com.example.fooddelivery.view;

import com.example.fooddelivery.model.User;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer; // 导入TableCellRenderer
import javax.swing.table.TableCellEditor; // 导入TableCellEditor
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * 用户管理面板
 * 负责用户的显示、新增、编辑和删除功能
 */
public class UserManagementPanel extends JPanel {

    private JTable userTable; // 用户表格
    private DefaultTableModel tableModel; // 表格模型
    private JTextField usernameField; // 用户名输入框
    private JPasswordField passwordField; // 密码输入框
    private JComboBox<String> roleComboBox; // 角色选择框
    private JButton saveButton; // 保存按钮
    private JButton cancelButton; // 取消按钮
    private User currentUser; // 当前编辑的用户对象

    public UserManagementPanel() {
        setLayout(new BorderLayout());

        // 顶部操作区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setBackground(Color.WHITE);

        topPanel.add(new JLabel("用户名:"));
        usernameField = new JTextField(15);
        topPanel.add(usernameField);

        topPanel.add(new JLabel("密码:"));
        passwordField = new JPasswordField(15);
        topPanel.add(passwordField);

        topPanel.add(new JLabel("角色:"));
        roleComboBox = new JComboBox<>(new String[]{"管理员", "普通用户"});
        topPanel.add(roleComboBox);

        saveButton = new JButton("新增");
        saveButton.addActionListener(e -> saveUser());
        topPanel.add(saveButton);

        cancelButton = new JButton("取消编辑");
        cancelButton.addActionListener(e -> clearForm());
        cancelButton.setVisible(false); // 初始隐藏
        topPanel.add(cancelButton);

        add(topPanel, BorderLayout.NORTH);

        // 用户列表表格
        String[] columnNames = {"ID", "用户名", "角色", "操作"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };
        userTable = new JTable(tableModel);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setReorderingAllowed(false);

        // 设置操作列的渲染器和编辑器
        userTable.getColumn("操作").setCellRenderer(new ButtonRenderer());
        userTable.getColumn("操作").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        loadUsers(); // 加载初始数据
    }

    /**
     * 加载用户数据到表格
     */
    private void loadUsers() {
        tableModel.setRowCount(0); // 清空现有数据
        List<User> users = DataService.getAllUsers();
        for (User user : users) {
            Vector<Object> row = new Vector<>();
            row.add(user.getId());
            row.add(user.getUsername());
            row.add(user.getRole());
            row.add("操作");
            tableModel.addRow(row);
        }
        clearForm(); // 刷新后清空表单
    }

    /**
     * 保存用户数据（新增或编辑）
     */
    private void saveUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (currentUser == null) {
            // 新增
            User newUser = new User(0, username, password, role); // ID由数据库生成
            DataService.addUser(newUser);
            JOptionPane.showMessageDialog(this, "用户添加成功！");
        } else {
            // 编辑
            currentUser.setUsername(username);
            currentUser.setPassword(password);
            currentUser.setRole(role);
            DataService.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "用户更新成功！");
        }
        loadUsers();
    }

    /**
     * 清空表单并重置为新增模式
     */
    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
        saveButton.setText("新增");
        cancelButton.setVisible(false);
        currentUser = null;
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
                currentRow = userTable.getSelectedRow();
                int userId = (int) tableModel.getValueAt(currentRow, 0);
                String username = (String) tableModel.getValueAt(currentRow, 1);
                String role = (String) tableModel.getValueAt(currentRow, 2);
                // 密码不从表格中获取，因为表格不显示密码，需要从DataService中获取完整User对象
                currentUser = DataService.getAllUsers().stream()
                        .filter(u -> u.getId() == userId)
                        .findFirst().orElse(null);
                if (currentUser != null) {
                    usernameField.setText(currentUser.getUsername());
                    passwordField.setText(currentUser.getPassword()); // 填充原始密码
                    roleComboBox.setSelectedItem(currentUser.getRole());
                    saveButton.setText("更新");
                    cancelButton.setVisible(true);
                }
            });
            panel.add(editButton);

            deleteButton = new JButton("删除");
            deleteButton.setFocusPainted(false);
            deleteButton.setBackground(new Color(220, 20, 60));
            deleteButton.setForeground(Color.WHITE);
            deleteButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            deleteButton.addActionListener(e -> {
                fireEditingStopped();
                int confirm = JOptionPane.showConfirmDialog(userTable, "确定要删除这条用户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    currentRow = userTable.getSelectedRow();
                    int userId = (int) tableModel.getValueAt(currentRow, 0);
                    DataService.deleteUser(userId);
                    loadUsers();
                    JOptionPane.showMessageDialog(userTable, "删除成功！");
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


