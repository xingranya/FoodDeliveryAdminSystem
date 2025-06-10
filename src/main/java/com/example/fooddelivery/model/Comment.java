package com.example.fooddelivery.model;

import java.util.Date;

/**
 * 评论实体类
 */
public class Comment {
    private int id;
    private int userId; // 评论用户ID
    private int dishId; // 评论菜品ID
    private int rating; // 评分，1-5星
    private String content; // 评论内容
    private Date commentTime; // 评论时间

    // 构造函数
    public Comment(int id, int userId, int dishId, int rating, String content, Date commentTime) {
        this.id = id;
        this.userId = userId;
        this.dishId = dishId;
        this.rating = rating;
        this.content = content;
        this.commentTime = commentTime;
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

    public int getDishId() {
        return dishId;
    }

    public void setDishId(int dishId) {
        this.dishId = dishId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
               "id=" + id +
               ", userId=" + userId +
               ", dishId=" + dishId +
               ", rating=" + rating +
               ", content=\'" + content + "\\\"" +
               ", commentTime=" + commentTime +
               "}";
    }
}


