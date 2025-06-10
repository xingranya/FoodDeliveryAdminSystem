package com.example.fooddelivery.view;

import com.example.fooddelivery.model.Category;
import com.example.fooddelivery.model.Dish;
import com.example.fooddelivery.model.Tag;
import com.example.fooddelivery.util.DataService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品新增/编辑对话框
 */
public class DishDialog extends JDialog {

    private JTextField nameField;
    private JTextField priceField;
    private JComboBox<String> statusComboBox;
    private JTextArea descriptionArea;
    private JComboBox<String> categoryComboBox;
    private JComboBox<String> tagComboBox; // 用于单选标签

    private Dish currentDish; // 当前编辑的菜品对象，如果是新增则为null
    private boolean succeeded = false; // 标记操作是否成功

    public DishDialog(Window owner, Dish dish) {
        super(owner, dish == null ? "新增菜品" : "编辑菜品", ModalityType.APPLICATION_MODAL);
        this.currentDish = dish;
        setSize(400, 500);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 菜品名称
        formPanel.add(new JLabel("名称:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        // 菜品价格
        formPanel.add(new JLabel("价格:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        // 菜品状态
        formPanel.add(new JLabel("状态:"));
        statusComboBox = new JComboBox<>(new String[]{"上架", "下架"});
        formPanel.add(statusComboBox);

        // 菜品简介
        formPanel.add(new JLabel("简介:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);
        formPanel.add(descriptionScrollPane);

        // 菜品分类
        formPanel.add(new JLabel("分类:"));
        List<Category> categories = DataService.getAllCategories();
        String[] categoryNames = categories.stream().map(Category::getName).toArray(String[]::new);
        categoryComboBox = new JComboBox<>(categoryNames);
        formPanel.add(categoryComboBox);

        // 菜品标签（单选下拉）
        formPanel.add(new JLabel("标签:"));
        List<Tag> tags = DataService.getAllTags();
        String[] tagNames = tags.stream().map(Tag::getName).toArray(String[]::new);
        tagComboBox = new JComboBox<>(tagNames);
        formPanel.add(tagComboBox);

        add(formPanel, BorderLayout.CENTER);

        // 底部按钮区域
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> saveDish());
        buttonPanel.add(saveButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // 如果是编辑模式，填充现有数据
        if (currentDish != null) {
            fillFormData();
        }
    }

    /**
     * 填充表单数据（编辑模式）
     */
    private void fillFormData() {
        nameField.setText(currentDish.getName());
        priceField.setText(String.valueOf(currentDish.getPrice()));
        statusComboBox.setSelectedItem(currentDish.getStatus());
        descriptionArea.setText(currentDish.getDescription());

        // 设置分类选中项
        DataService.getAllCategories().stream()
                .filter(c -> c.getId() == currentDish.getCategoryId())
                .findFirst()
                .ifPresent(c -> categoryComboBox.setSelectedItem(c.getName()));

        // 设置标签选中项（只取第一个标签）
        List<Integer> selectedTagIds = currentDish.getTagIds();
        List<Tag> allTags = DataService.getAllTags();
        if (!selectedTagIds.isEmpty()) {
            for (int i = 0; i < allTags.size(); i++) {
                if (selectedTagIds.get(0) == allTags.get(i).getId()) {
                    tagComboBox.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    /**
     * 保存菜品数据
     */
    private void saveDish() {
        String name = nameField.getText().trim();
        String priceStr = priceField.getText().trim();
        String status = (String) statusComboBox.getSelectedItem();
        String description = descriptionArea.getText().trim();
        String categoryName = (String) categoryComboBox.getSelectedItem();
        String tagName = (String) tagComboBox.getSelectedItem();

        // 数据校验
        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写所有必填项！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "价格必须是有效的非负数字！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 获取分类ID
        int categoryId = DataService.getAllCategories().stream()
                .filter(c -> c.getName().equals(categoryName))
                .map(Category::getId)
                .findFirst()
                .orElse(-1); // 应该不会发生，因为是从现有分类中选择

        // 获取标签ID列表（只选一个标签）
        List<Integer> tagIds = DataService.getAllTags().stream()
                .filter(tag -> tag.getName().equals(tagName))
                .map(Tag::getId)
                .collect(Collectors.toList());

        if (currentDish == null) {
            // 新增菜品
            Dish newDish = new Dish(name, price, status, description, categoryId, tagIds);
            DataService.addDish(newDish);
            JOptionPane.showMessageDialog(this, "菜品添加成功！");
        } else {
            // 编辑菜品
            currentDish.setName(name);
            currentDish.setPrice(price);
            currentDish.setStatus(status);
            currentDish.setDescription(description);
            currentDish.setCategoryId(categoryId);
            currentDish.setTagIds(tagIds);
            DataService.updateDish(currentDish);
            JOptionPane.showMessageDialog(this, "菜品更新成功！");
        }
        succeeded = true;
        dispose(); // 关闭对话框
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}


