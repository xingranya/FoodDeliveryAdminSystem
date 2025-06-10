package com.example.fooddelivery.model;

import java.util.Date;

/**
 * 日志实体类
 */
public class Log {
    private int id;
    private int operatorId; // 操作员ID
    private String operation; // 操作内容
    private Date operationTime; // 操作时间

    // 构造函数
    public Log(int id, int operatorId, String operation, Date operationTime) {
        this.id = id;
        this.operatorId = operatorId;
        this.operation = operation;
        this.operationTime = operationTime;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    @Override
    public String toString() {
        return "Log{" +
               "id=" + id +
               ", operatorId=" + operatorId +
               ", operation=\'" + operation + "\\\"" +
               ", operationTime=" + operationTime +
               "}";
    }
}


