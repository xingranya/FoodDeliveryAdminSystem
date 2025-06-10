package com.example.fooddelivery.model;

import java.util.Date;
import java.util.List;

/**
 * 订单实体类
 */
public class Order {
    private int id;
    private int userId; // 下单用户ID
    private Date orderTime; // 下单时间
    private double totalAmount; // 总金额
    private String status; // 例如："待支付", "已完成", "已取消"
    private List<OrderItem> dishItems; // 订单包含的菜品列表

    // 构造函数
    public Order(int id, int userId, Date orderTime, double totalAmount, String status, List<OrderItem> dishItems) {
        this.id = id;
        this.userId = userId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.status = status;
        this.dishItems = dishItems;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getDishItems() {
        return dishItems;
    }

    public void setDishItems(List<OrderItem> dishItems) {
        this.dishItems = dishItems;
    }

    @Override
    public String toString() {
        return "Order{" +
               "id=" + id +
               ", userId=" + userId +
               ", orderTime=" + orderTime +
               ", totalAmount=" + totalAmount +
               ", status=\'" + status + "\\\'" +
               ", dishItems=" + dishItems +
               "}";
    }
}


