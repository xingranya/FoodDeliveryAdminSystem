package com.example.fooddelivery.model;

/**
 * 分类实体类
 */
public class Category {
    private int id;
    private String name;

    // 构造函数
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public String toString() {
        return "Category{" +
               "id=" + id +
               ", name=\'" + name + "\'" +
               "}";
    }
}


