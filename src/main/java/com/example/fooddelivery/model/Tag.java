package com.example.fooddelivery.model;

/**
 * 标签实体类
 */
public class Tag {
    private int id;
    private String name;

    // 构造函数
    public Tag(int id, String name) {
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
        return "Tag{" +
               "id=" + id +
               ", name=\'" + name + "\'";
    }
}


