package com.qgtechforum.service;

import com.qgtechforum.dao.NotificationDao;
import com.qgtechforum.model.Notification;

import java.util.List;

public class NotificationService {
    //创建NotificationDao对象
    private NotificationDao notificationDao = new NotificationDao();
    //发送通知
    public void sendNotification(int userId, String content) {
        notificationDao.sendNotification(userId, content);
    }

    //获取用户通知列表
    public List<Notification> getNotifications(int userId) {
        return notificationDao.getNotifications(userId);
    }

    //标记通知为已读
    public void markNotificationAsRead(int notificationId) {
        notificationDao.markNotificationAsRead(notificationId);
    }
}
