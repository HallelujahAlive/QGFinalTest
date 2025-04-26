package com.qgtechforum.dao;

import com.qgtechforum.model.Notification;
import com.qgtechforum.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationDao {
    //发送通知
    public void sendNotification(int userId, String content) {
        String sql = "insert into notifications (user_id, content,create_time,is_read) values (?, ?,?,false)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, userId);
                 pstmt.setString(2, content);
                 pstmt.setTimestamp(3,new java.sql.Timestamp(new Date().getTime()));
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //获取用户的通知列表
    public List<Notification> getNotifications(int userId) {
        //order by create_time desc 按照创建时间降序排序,最新的在最前面
        String sql = "select * from notifications where user_id = ? order by create_time desc";
        List<Notification> notifications = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, userId);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Notification notification = new Notification();
                     notification.setId(rs.getInt("id"));
                     notification.setUserId(rs.getInt("user_id"));
                     notification.setContent(rs.getString("content"));
                     notification.setCreateTime(rs.getTimestamp("create_time"));
                     notification.setRead(rs.getBoolean("is_read"));
                     notifications.add(notification);
                 }
        } catch (SQLException e) {
                throw new RuntimeException(e);
        }
        return notifications;
    }

    //标记通知为已读
    public void markNotificationAsRead(int notificationId) {
        String sql = "update notifications set is_read = true where id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, notificationId);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
