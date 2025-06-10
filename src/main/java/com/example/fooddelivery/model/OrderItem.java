package com.example.fooddelivery.model;

/**
 * 订单项实体类
 */
public class OrderItem {
    private int dishId; // 菜品ID
    private int quantity; // 数量
    private double price; // 购买时价格

    // 构造函数
    public OrderItem(int dishId, int quantity, double price) {
        this.dishId = dishId;
        this.quantity = quantity;
        this.price = price;
    }

    // Getter 和 Setter 方法
    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
               "dishId=" + dishId +
               ", quantity=" + quantity +
               ", price=" + price +
               "}";
    }
}


