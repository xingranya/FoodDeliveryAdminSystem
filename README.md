# 外卖平台运营管理系统

## 项目概述

本项目是一个基于Java Swing开发的外卖平台运营管理系统，旨在提供一个直观、易用的桌面应用程序，用于管理外卖平台的菜品、分类、标签、订单、评论、用户、运营日志和系统信息等。

**主要功能模块：**

*   **菜品管理：** 菜品的增删改查，支持按名称搜索。
*   **分类管理：** 菜品分类的增删改查。
*   **标签管理：** 菜品标签的增删改查。
*   **订单管理：** 订单的查看和管理。
*   **评论管理：** 用户评论的查看和管理。
*   **用户管理：** 用户信息的增删改查。
*   **运营管理：** 预留模块，可扩展其他运营相关功能。
*   **日志管理：** 系统操作日志的查看和管理。
*   **统计分析：** 预留模块，可扩展数据统计和分析功能。
*   **系统信息：** 预留模块，可显示系统相关信息。

## 技术栈

*   **前端：** Java Swing (GUI)
*   **后端：** Java (纯JDBC)
*   **数据库：** MySQL
*   **JDBC驱动：** MySQL Connector/J 8.0.33

## 环境要求

*   Java Development Kit (JDK) 17 或更高版本
*   MySQL 数据库

## 项目设置与运行

### 1. 数据库配置

1.  **创建数据库：** 在MySQL中创建一个名为 `java_food` 的数据库。

    ```sql
    CREATE DATABASE IF NOT EXISTS java_food CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    USE java_food;
    ```

2.  **导入表结构：** 执行项目根目录下的 `db_schema.sql` 文件中的SQL语句，创建所需的表结构。

3.  **更新数据库连接信息：**
    打开 `FoodDeliveryAdminSystem/src/main/java/com/example/fooddelivery/util/DBUtil.java` 文件，根据您的MySQL配置修改以下常量：

    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/java_food?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root";
    private static final String PASSWORD = "xingran8";
    ```

### 2. 编译与运行

1.  **下载JDBC驱动：** 项目中已包含 `mysql-connector-j-8.0.33.jar` 在 `FoodDeliveryAdminSystem/lib/` 目录下。

2.  **编译项目：**
    打开命令行或终端，进入 `FoodDeliveryAdminSystem` 目录，执行以下命令编译所有Java文件：

    ```bash
    find src/main/java -name "*.java" > sources.txt
    javac -cp lib/mysql-connector-j-8.0.33.jar @sources.txt -d out
    ```

3.  **运行应用程序：**
    继续在 `FoodDeliveryAdminSystem` 目录下执行以下命令：

    ```bash
    java -cp out:lib/mysql-connector-j-8.0.33.jar com.example.fooddelivery.view.MainFrame
    ```
    *   **Windows 用户请注意：** 将 `-cp out:lib/mysql-connector-j-8.0.33.jar` 中的 `:` 替换为 `;`，即：
        `java -cp out;lib/mysql-connector-j-8.0.33.jar com.example.fooddelivery.view.MainFrame`

## 代码结构与说明

```
FoodDeliveryAdminSystem/
├── lib/                                # 存放JDBC驱动JAR包
│   └── mysql-connector-j-8.0.33.jar
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   └── fooddelivery/
│                       ├── model/      # 数据模型 (实体类)
│                       │   ├── Category.java
│                       │   ├── Comment.java
│                       │   ├── Dish.java
│                       │   ├── Log.java
│                       │   ├── Order.java
│                       │   ├── OrderItem.java
│                       │   ├── Tag.java
│                       │   └── User.java
│                       ├── util/       # 工具类 (数据库连接、数据服务)
│                       │   ├── DBUtil.java
│                       │   └── DataService.java
│                       └── view/       # GUI界面 (Swing面板和对话框)
│                           ├── CategoryManagementPanel.java
│                           ├── CommentManagementPanel.java
│                           ├── DishDialog.java
│                           ├── DishManagementPanel.java
│                           ├── LogManagementPanel.java
│                           ├── MainFrame.java
│                           ├── OrderManagementPanel.java
│                           ├── OperationManagementPanel.java
│                           ├── StatisticsAnalysisPanel.java
│                           ├── SystemInfoPanel.java
│                           ├── TagManagementPanel.java
│                           └── UserManagementPanel.java
└── db_schema.sql                       # 数据库表结构定义
```

### 关键组件说明

*   **`model` 包：** 包含了所有业务实体类，如 `Dish` (菜品), `Category` (分类), `User` (用户) 等。这些类是纯粹的Java Bean，用于封装数据。

*   **`util` 包：**
    *   `DBUtil.java`：数据库连接工具类，负责建立和关闭MySQL数据库连接。它提供了静态方法来获取 `Connection` 对象，并安全地关闭 `ResultSet`, `PreparedStatement` 和 `Connection`。
    *   `DataService.java`：数据服务类，负责所有业务逻辑与数据库的交互。它包含了对 `Dish`, `Category`, `Tag`, `User`, `Order`, `Comment`, `Log` 等实体进行增删改查的方法。所有数据库操作都通过JDBC直接完成，并使用了 `PreparedStatement` 来防止SQL注入。

*   **`view` 包：** 包含了所有Swing GUI界面组件。
    *   `MainFrame.java`：主窗口，包含了左侧导航菜单和右侧内容面板。通过点击左侧菜单，右侧内容面板会切换显示不同的管理模块。
    *   `XXXManagementPanel.java` (例如 `DishManagementPanel.java`)：每个管理模块对应一个JPanel，负责显示该模块的数据表格、搜索功能、新增/编辑/删除按钮等。这些面板通过 `DataService` 与数据库进行交互。
    *   `DishDialog.java`：用于菜品新增和编辑的对话框。它是一个模态对话框，用于收集或显示菜品的详细信息。

## 注意事项

*   **GUI环境：** 本项目是一个桌面应用程序，需要在支持图形界面的环境中运行。在某些无头（headless）服务器环境下可能无法直接运行。
*   **错误处理：** 代码中包含了基本的异常捕获和打印，但在生产环境中，建议使用更完善的日志框架（如Log4j, SLF4J）进行日志记录和错误处理。
*   **安全性：** 本项目主要用于教学和演示目的，未包含高级安全特性（如密码加密、用户认证和授权）。在实际生产应用中，这些是必不可少的。
*   **可扩展性：** 项目结构清晰，易于扩展新的功能模块或集成其他第三方库。

