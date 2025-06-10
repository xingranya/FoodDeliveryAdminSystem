package com.example.fooddelivery.model;

import java.util.List;

/**
 * 菜品实体类
 */
public class Dish {
    private int id;
    private String name;
    private double price;
    private String status; // 例如："上架", "下架"
    private String description;
    private int categoryId; // 所属分类ID
    private List<Integer> tagIds; // 所属标签ID列表

    // 构造函数 (用于从数据库读取时)
    public Dish(int id, String name, double price, String status, String description, int categoryId, List<Integer> tagIds) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.status = status;
        this.description = description;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
    }

    // 构造函数 (用于新增时，ID由数据库生成)
    public Dish(String name, double price, String status, String description, int categoryId, List<Integer> tagIds) {
        this.name = name;
        this.price = price;
        this.status = status;
        this.description = description;
        this.categoryId = categoryId;
        this.tagIds = tagIds;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public List<Integer> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Integer> tagIds) {
        this.tagIds = tagIds;
    }

    @Override
    public String toString() {
        return "Dish{" +
               "id=" + id +
               ", name=\'" + name + "\'" +
               ", price=" + price +
               ", status=\'" + status + "\'" +
               ", description=\'" + description + "\'" +
               ", categoryId=" + categoryId +
               ", tagIds=" + tagIds +
               "}";
    }
}


