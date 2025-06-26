# 外卖后台管理系统

![Java](https://img.shields.io/badge/Java-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.8-orange)
![MySQL](https://img.shields.io/badge/MySQL-8.0-lightgrey)
![Swing](https://img.shields.io/badge/UI-Java%20Swing-green)

一个基于 Java Swing 和 MySQL 开发的桌面端外卖平台后台管理系统。提供了一个直观、易用的图形化界面，用于管理平台的核心业务数据。

## ✨ 功能列表

-   **菜品管理**: 增、删、改、查菜品信息，支持按名称模糊搜索。
-   **分类管理**: 管理菜品的分类信息。
-   **标签管理**: 为菜品添加和管理标签。
-   **订单管理**: 查看和管理用户订单详情。
-   **评论管理**: 浏览和删除用户评论。
-   **用户管理**: 增、删、改、查平台用户信息。
-   **日志管理**: 查看系统关键操作的日志记录。
-   **其他模块**: 预留了运营管理、统计分析和系统信息等扩展模块。

## 🛠️ 技术栈

-   **核心语言**: Java 17
-   **UI 框架**: Java Swing
-   **数据库**: MySQL 8.0+
-   **数据访问**: 原生 JDBC
-   **项目管理**: Apache Maven

## 🚀 快速开始

请遵循以下步骤来配置和运行项目。

### 1. 环境准备

确保您的开发环境中已安装以下软件：

-   **JDK 17** 或更高版本
-   **Maven 3.6** 或更高版本
-   **MySQL 8.0** 或更高版本
-   一个你喜欢的 IDE (如 IntelliJ IDEA, Eclipse)

### 2. 数据库设置

1.  **创建数据库**:
    在 MySQL 中创建一个名为 `java_food` 的数据库。

    ```sql
    CREATE DATABASE IF NOT EXISTS java_food CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
    ```

2.  **导入数据表**:
    使用数据库客户端，执行项目根目录下的 `java_food.sql` 文件，以创建所需的表结构和初始数据。

3.  **配置数据库连接**:
    打开源文件 `src/main/java/com/example/fooddelivery/util/DBUtil.java`。
    根据您自己的 MySQL 环境，修改以下三行连接参数：

    ```java
    // src/main/java/com/example/fooddelivery/util/DBUtil.java

    private static final String URL = "jdbc:mysql://localhost:3306/java_food?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
    private static final String USER = "root"; // <-- 修改为您的数据库用户名
    private static final String PASSWORD = "xingran8"; // <-- 修改为您的数据库密码
    ```

### 3. 运行项目

#### 方式一：使用 IDE (推荐)

1.  使用 IntelliJ IDEA 或 Eclipse 打开项目 (选择 `pom.xml` 文件导入)。
2.  IDE 会自动下载 Maven 依赖。
3.  找到并运行主类 `com.example.fooddelivery.view.MainFrame.java`。

#### 方式二：使用 Maven 命令行

1.  打开终端或命令行工具，导航到项目的根目录。
2.  使用 Maven 编译项目：
    ```bash
    mvn clean compile
    ```
3.  运行应用程序：
    ```bash
    mvn exec:java -Dexec.mainClass="com.example.fooddelivery.view.MainFrame"
    ```

## 📸 应用截图

*(这里可以添加一些应用程序运行时的截图)*

![系统主界面](placeholder.png)
![菜品管理](placeholder.png)

## 📂 项目结构

```
FoodDeliveryAdminSystem/
├── lib/                      # 存放JDBC驱动等本地依赖
├── src/main/java/
│   └── com/example/fooddelivery/
│       ├── model/            # 数据模型 (POJO)
│       ├── util/             # 工具类 (数据库连接等)
│       └── view/             # 视图层 (Swing UI)
├── java_food.sql             # 数据库脚本
└── pom.xml                   # Maven 配置文件
```