package com.example.fooddelivery.util;

import com.example.fooddelivery.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * 数据服务类
 * 用于管理各种实体数据，通过JDBC与MySQL数据库交互
 */
public class DataService {

    // Dish 相关操作

    /**
     * 从数据库获取所有菜品
     * @return 菜品列表
     */
    public static List<Dish> getAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.price, d.status, d.description, d.category_id, GROUP_CONCAT(dt.tag_id) AS tag_ids FROM dishes d LEFT JOIN dish_tags dt ON d.id = dt.dish_id GROUP BY d.id";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String status = rs.getString("status");
                String description = rs.getString("description");
                int categoryId = rs.getInt("category_id");
                String tagIdsStr = rs.getString("tag_ids");
                List<Integer> tagIds = new ArrayList<>();
                if (tagIdsStr != null && !tagIdsStr.isEmpty()) {
                    for (String tagId : tagIdsStr.split(",")) {
                        tagIds.add(Integer.parseInt(tagId));
                    }
                }
                dishes.add(new Dish(id, name, price, status, description, categoryId, tagIds));
            }
        } catch (SQLException e) {
            System.err.println("获取所有菜品失败: " + e.getMessage());
            e.printStackTrace();
        }
        return dishes;
    }

    /**
     * 向数据库添加菜品
     * @param dish 待添加的菜品对象
     */
    public static void addDish(Dish dish) {
        String sql = "INSERT INTO dishes (name, price, status, description, category_id) VALUES (?, ?, ?, ?, ?)";
        String tagSql = "INSERT INTO dish_tags (dish_id, tag_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, dish.getName());
            pstmt.setDouble(2, dish.getPrice());
            pstmt.setString(3, dish.getStatus());
            pstmt.setString(4, dish.getDescription());
            pstmt.setInt(5, dish.getCategoryId());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                dish.setId(generatedId); // 设置生成的ID

                // 插入标签关联
                if (dish.getTagIds() != null && !dish.getTagIds().isEmpty()) {
                    try (PreparedStatement tagPstmt = conn.prepareStatement(tagSql)) {
                        for (Integer tagId : dish.getTagIds()) {
                            tagPstmt.setInt(1, generatedId);
                            tagPstmt.setInt(2, tagId);
                            tagPstmt.addBatch();
                        }
                        tagPstmt.executeBatch();
                    }
                }
            }
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            System.err.println("添加菜品失败: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            DBUtil.closeConnection(rs, pstmt, conn);
        }
    }

    /**
     * 更新数据库中的菜品
     * @param updatedDish 待更新的菜品对象
     */
    public static void updateDish(Dish updatedDish) {
        String sql = "UPDATE dishes SET name = ?, price = ?, status = ?, description = ?, category_id = ? WHERE id = ?";
        String deleteTagSql = "DELETE FROM dish_tags WHERE dish_id = ?";
        String insertTagSql = "INSERT INTO dish_tags (dish_id, tag_id) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, updatedDish.getName());
            pstmt.setDouble(2, updatedDish.getPrice());
            pstmt.setString(3, updatedDish.getStatus());
            pstmt.setString(4, updatedDish.getDescription());
            pstmt.setInt(5, updatedDish.getCategoryId());
            pstmt.setInt(6, updatedDish.getId());
            pstmt.executeUpdate();

            // 先删除旧的标签关联
            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteTagSql)) {
                deletePstmt.setInt(1, updatedDish.getId());
                deletePstmt.executeUpdate();
            }

            // 再插入新的标签关联
            if (updatedDish.getTagIds() != null && !updatedDish.getTagIds().isEmpty()) {
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertTagSql)) {
                    for (Integer tagId : updatedDish.getTagIds()) {
                        insertPstmt.setInt(1, updatedDish.getId());
                        insertPstmt.setInt(2, tagId);
                        insertPstmt.addBatch();
                    }
                    insertPstmt.executeBatch();
                }
            }
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            System.err.println("更新菜品失败: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            DBUtil.closeConnection(pstmt, conn);
        }
    }

    /**
     * 从数据库删除菜品
     * @param dishId 待删除菜品的ID
     */
    public static void deleteDish(int dishId) {
        String sql = "DELETE FROM dishes WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dishId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除菜品失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据名称搜索菜品
     * @param name 菜品名称关键词
     * @return 匹配的菜品列表
     */
    public static List<Dish> searchDishesByName(String name) {
        List<Dish> dishes = new ArrayList<>();
        String sql = "SELECT d.id, d.name, d.price, d.status, d.description, d.category_id, GROUP_CONCAT(dt.tag_id) AS tag_ids FROM dishes d LEFT JOIN dish_tags dt ON d.id = dt.dish_id WHERE d.name LIKE ? GROUP BY d.id";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String dishName = rs.getString("name");
                    double price = rs.getDouble("price");
                    String status = rs.getString("status");
                    String description = rs.getString("description");
                    int categoryId = rs.getInt("category_id");
                    String tagIdsStr = rs.getString("tag_ids");
                    List<Integer> tagIds = new ArrayList<>();
                    if (tagIdsStr != null && !tagIdsStr.isEmpty()) {
                        for (String tagId : tagIdsStr.split(",")) {
                            tagIds.add(Integer.parseInt(tagId));
                        }
                    }
                    dishes.add(new Dish(id, dishName, price, status, description, categoryId, tagIds));
                }
            }
        } catch (SQLException e) {
            System.err.println("搜索菜品失败: " + e.getMessage());
            e.printStackTrace();
        }
        return dishes;
    }

    // Category 相关操作

    /**
     * 从数据库获取所有分类
     * @return 分类列表
     */
    public static List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM categories";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("获取所有分类失败: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * 向数据库添加分类
     * @param category 待添加的分类对象
     */
    public static void addCategory(Category category) {
        String sql = "INSERT INTO categories (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, category.getName());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    category.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("添加分类失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库中的分类
     * @param updatedCategory 待更新的分类对象
     */
    public static void updateCategory(Category updatedCategory) {
        String sql = "UPDATE categories SET name = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedCategory.getName());
            pstmt.setInt(2, updatedCategory.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("更新分类失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库删除分类
     * @param categoryId 待删除分类的ID
     */
    public static void deleteCategory(int categoryId) {
        String sql = "DELETE FROM categories WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除分类失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Tag 相关操作

    /**
     * 从数据库获取所有标签
     * @return 标签列表
     */
    public static List<Tag> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        String sql = "SELECT id, name FROM tags";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tags.add(new Tag(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            System.err.println("获取所有标签失败: " + e.getMessage());
            e.printStackTrace();
        }
        return tags;
    }

    /**
     * 向数据库添加标签
     * @param tag 待添加的标签对象
     */
    public static void addTag(Tag tag) {
        String sql = "INSERT INTO tags (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, tag.getName());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    tag.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("添加标签失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库中的标签
     * @param updatedTag 待更新的标签对象
     */
    public static void updateTag(Tag updatedTag) {
        String sql = "UPDATE tags SET name = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedTag.getName());
            pstmt.setInt(2, updatedTag.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("更新标签失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库删除标签
     * @param tagId 待删除标签的ID
     */
    public static void deleteTag(int tagId) {
        String sql = "DELETE FROM tags WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, tagId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除标签失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // User 相关操作

    /**
     * 从数据库获取所有用户
     * @return 用户列表
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, password, role FROM users";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            System.err.println("获取所有用户失败: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    /**
     * 向数据库添加用户
     * @param user 待添加的用户对象
     */
    public static void addUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("添加用户失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库中的用户
     * @param updatedUser 待更新的用户对象
     */
    public static void updateUser(User updatedUser) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, updatedUser.getUsername());
            pstmt.setString(2, updatedUser.getPassword());
            pstmt.setString(3, updatedUser.getRole());
            pstmt.setInt(4, updatedUser.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("更新用户失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库删除用户
     * @param userId 待删除用户的ID
     */
    public static void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除用户失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 验证用户登录
     * @param username 用户名
     * @param password 密码
     * @return 如果验证成功返回用户对象，否则返回null
     */
    public static User validateUser(String username, String password) {
        String sql = "SELECT id, username, password, role FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("验证用户失败: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Order 相关操作

    /**
     * 从数据库获取所有订单
     * @return 订单列表
     */
    public static List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT id, user_id, order_time, total_amount, status FROM orders";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int orderId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                Date orderTime = rs.getTimestamp("order_time");
                double totalAmount = rs.getDouble("total_amount");
                String status = rs.getString("status");
                // 获取订单项
                List<OrderItem> orderItems = getOrderItemsByOrderId(orderId);
                orders.add(new Order(orderId, userId, orderTime, totalAmount, status, orderItems));
            }
        } catch (SQLException e) {
            System.err.println("获取所有订单失败: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    /**
     * 根据订单ID获取订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    private static List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String sql = "SELECT dish_id, quantity, price FROM order_items WHERE order_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderItems.add(new OrderItem(rs.getInt("dish_id"), rs.getInt("quantity"), rs.getDouble("price")));
                }
            }
        } catch (SQLException e) {
            System.err.println("获取订单项失败: " + e.getMessage());
            e.printStackTrace();
        }
        return orderItems;
    }

    /**
     * 向数据库添加订单
     * @param order 待添加的订单对象
     */
    public static void addOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, order_time, total_amount, status) VALUES (?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, dish_id, quantity, price) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, order.getUserId());
            pstmt.setTimestamp(2, new Timestamp(order.getOrderTime().getTime()));
            pstmt.setDouble(3, order.getTotalAmount());
            pstmt.setString(4, order.getStatus());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                order.setId(generatedId); // 设置生成的ID

                // 插入订单项
                if (order.getDishItems() != null && !order.getDishItems().isEmpty()) {
                    try (PreparedStatement itemPstmt = conn.prepareStatement(itemSql)) {
                        for (OrderItem item : order.getDishItems()) {
                            itemPstmt.setInt(1, generatedId);
                            itemPstmt.setInt(2, item.getDishId());
                            itemPstmt.setInt(3, item.getQuantity());
                            itemPstmt.setDouble(4, item.getPrice());
                            itemPstmt.addBatch();
                        }
                        itemPstmt.executeBatch();
                    }
                }
            }
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            System.err.println("添加订单失败: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            DBUtil.closeConnection(rs, pstmt, conn);
        }
    }

    /**
     * 更新数据库中的订单
     * @param updatedOrder 待更新的订单对象
     */
    public static void updateOrder(Order updatedOrder) {
        String sql = "UPDATE orders SET user_id = ?, order_time = ?, total_amount = ?, status = ? WHERE id = ?";
        String deleteItemSql = "DELETE FROM order_items WHERE order_id = ?";
        String insertItemSql = "INSERT INTO order_items (order_id, dish_id, quantity, price) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 开启事务

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, updatedOrder.getUserId());
            pstmt.setTimestamp(2, new Timestamp(updatedOrder.getOrderTime().getTime()));
            pstmt.setDouble(3, updatedOrder.getTotalAmount());
            pstmt.setString(4, updatedOrder.getStatus());
            pstmt.setInt(5, updatedOrder.getId());
            pstmt.executeUpdate();

            // 先删除旧的订单项
            try (PreparedStatement deletePstmt = conn.prepareStatement(deleteItemSql)) {
                deletePstmt.setInt(1, updatedOrder.getId());
                deletePstmt.executeUpdate();
            }

            // 再插入新的订单项
            if (updatedOrder.getDishItems() != null && !updatedOrder.getDishItems().isEmpty()) {
                try (PreparedStatement insertPstmt = conn.prepareStatement(insertItemSql)) {
                    for (OrderItem item : updatedOrder.getDishItems()) {
                        insertPstmt.setInt(1, updatedOrder.getId());
                        insertPstmt.setInt(2, item.getDishId());
                        insertPstmt.setInt(3, item.getQuantity());
                        insertPstmt.setDouble(4, item.getPrice());
                        insertPstmt.addBatch();
                    }
                    insertPstmt.executeBatch();
                }
            }
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            System.err.println("更新订单失败: " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback(); // 回滚事务
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            DBUtil.closeConnection(pstmt, conn);
        }
    }

    /**
     * 从数据库删除订单
     * @param orderId 待删除订单的ID
     */
    public static void deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除订单失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Comment 相关操作

    /**
     * 从数据库获取所有评论
     * @return 评论列表
     */
    public static List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT id, user_id, dish_id, rating, content, comment_time FROM comments";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                comments.add(new Comment(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("dish_id"), rs.getInt("rating"), rs.getString("content"), rs.getTimestamp("comment_time")));
            }
        } catch (SQLException e) {
            System.err.println("获取所有评论失败: " + e.getMessage());
            e.printStackTrace();
        }
        return comments;
    }

    /**
     * 向数据库添加评论
     * @param comment 待添加的评论对象
     */
    public static void addComment(Comment comment) {
        String sql = "INSERT INTO comments (user_id, dish_id, rating, content, comment_time) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, comment.getUserId());
            pstmt.setInt(2, comment.getDishId());
            pstmt.setInt(3, comment.getRating());
            pstmt.setString(4, comment.getContent());
            pstmt.setTimestamp(5, new Timestamp(comment.getCommentTime().getTime()));
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    comment.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("添加评论失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 更新数据库中的评论
     * @param updatedComment 待更新的评论对象
     */
    public static void updateComment(Comment updatedComment) {
        String sql = "UPDATE comments SET user_id = ?, dish_id = ?, rating = ?, content = ?, comment_time = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, updatedComment.getUserId());
            pstmt.setInt(2, updatedComment.getDishId());
            pstmt.setInt(3, updatedComment.getRating());
            pstmt.setString(4, updatedComment.getContent());
            pstmt.setTimestamp(5, new Timestamp(updatedComment.getCommentTime().getTime()));
            pstmt.setInt(6, updatedComment.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("更新评论失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库删除评论
     * @param commentId 待删除评论的ID
     */
    public static void deleteComment(int commentId) {
        String sql = "DELETE FROM comments WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, commentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("删除评论失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 关闭数据库相关资源
     * @param rs ResultSet对象
     * @param stmt Statement或PreparedStatement对象
     * @param conn Connection对象
     */
    public static void closeConnection(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection(Statement stmt, Connection conn) {
        closeConnection(null, stmt, conn);
    }
}


