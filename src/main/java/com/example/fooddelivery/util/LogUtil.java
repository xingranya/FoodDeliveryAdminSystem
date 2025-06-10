package com.example.fooddelivery.util;

import com.example.fooddelivery.model.Log;
import java.util.Date;

/**
 * 日志工具类
 * 用于统一管理系统的日志记录
 */
public class LogUtil {
    private static int currentOperatorId = 1; // 默认操作员ID，实际应用中应该从登录用户获取

    /**
     * 设置当前操作员ID
     * @param operatorId 操作员ID
     */
    public static void setCurrentOperatorId(int operatorId) {
        currentOperatorId = operatorId;
    }

    /**
     * 记录操作日志
     * @param operation 操作内容
     */
    public static void logOperation(String operation) {
        Log log = new Log(0, currentOperatorId, operation, new Date());
        DataService.addLog(log);
    }

    /**
     * 记录添加评论的日志
     * @param userId 用户ID
     * @param dishId 菜品ID
     */
    public static void logAddComment(int userId, int dishId) {
        String operation = String.format("添加评论 - 用户ID: %d, 菜品ID: %d", userId, dishId);
        logOperation(operation);
    }

    /**
     * 记录删除评论的日志
     * @param commentId 评论ID
     */
    public static void logDeleteComment(int commentId) {
        String operation = String.format("删除评论 - 评论ID: %d", commentId);
        logOperation(operation);
    }

    /**
     * 记录添加菜品的日志
     * @param dishName 菜品名称
     */
    public static void logAddDish(String dishName) {
        String operation = String.format("添加菜品 - 菜品名称: %s", dishName);
        logOperation(operation);
    }

    /**
     * 记录删除菜品的日志
     * @param dishId 菜品ID
     */
    public static void logDeleteDish(int dishId) {
        String operation = String.format("删除菜品 - 菜品ID: %d", dishId);
        logOperation(operation);
    }

    /**
     * 记录更新菜品的日志
     * @param dishId 菜品ID
     * @param dishName 菜品名称
     */
    public static void logUpdateDish(int dishId, String dishName) {
        String operation = String.format("更新菜品 - 菜品ID: %d, 菜品名称: %s", dishId, dishName);
        logOperation(operation);
    }
} 