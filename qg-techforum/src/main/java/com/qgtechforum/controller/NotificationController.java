package com.qgtechforum.controller;

import com.BaseServlet;
import com.qgtechforum.model.Notification;
import com.qgtechforum.model.Result;
import com.qgtechforum.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/Notification/*")
public class NotificationController extends BaseServlet {
    //创建Service对象
    private NotificationService notificationService = new NotificationService();

    //重写doGet方法
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取动态路径
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/list")) {
            //获取用户id
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取用户通知列表
            List<Notification> notifications = notificationService.getNotifications(userId);
            if (notifications != null) {
                sendJsonResponse(resp,new Result(200,"获取用户通知列表成功",notifications));
            } else {
                sendJsonResponse(resp,new Result(400,"获取用户通知列表失败",null));
            }
        }
    }

    //重写doPut方法
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取动态路径
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/read")) {
            int notificationId = Integer.parseInt(req.getParameter("notificationId"));
            //标记通知为已读
            notificationService.markNotificationAsRead(notificationId);
            sendJsonResponse(resp,new Result(200,"标记通知为已读成功",null));
        }
    }

    //重写doPost方法，用于发送通知
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取动态路径
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/send")) {
            //获取用户id
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取通知内容
            String content = req.getParameter("content");
            //发送通知
            notificationService.sendNotification(userId, content);
            sendJsonResponse(resp,new Result(200,"发送通知成功",null));
        }
    }
}
