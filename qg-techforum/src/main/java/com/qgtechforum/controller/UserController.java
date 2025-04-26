package com.qgtechforum.controller;

import com.BaseServlet;
import com.qgtechforum.model.Result;
import com.qgtechforum.model.User;
import com.qgtechforum.service.UserService;
import com.qgtechforum.utils.JsonUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

//service包的作用是提供业务逻辑层，用于处理业务逻辑
@WebServlet("/User/*")
public class UserController extends BaseServlet {
    private UserService userService = new UserService();

    //重写doPost方法，用于处理POST请求
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //首先是获取请求的路径的最后一段,也就是方法名
        //pathInfo是获取请求路径中的动态部分，例如：/User/getUserById/1
        //如果请求路径是/User/getUserById/1，那么pathInfo就是/getUserById/1
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/register")){

            //使用getReader方法获取请求体中的数据,reader的作用是读取请求体中的数据
            //请求体储存的内容主要是用户输入的用户名，密码，昵称等信息
            BufferedReader reader = req.getReader();
            //创建StringBuilder对象，用于存储请求体中的数据（获取的数据是一行一行的）
            StringBuilder sb = new StringBuilder();
            //创建一个字符串变量，用于存储读取到的一行数据
            String line;
            while ((line = reader.readLine()) != null){
                //将读取到的一行数据存入sb中
                sb.append(line);
            }
            //由于sb存储的数据是Json数据格式，所以要转换为字符串，然后再转换为User对象
            User user = JsonUtils.fromJson(sb.toString(), User.class);
            //调用UserService中的register方法，将user对象存入数据库中
            User registeredUser = userService.register(user);
            //判断是否注册成功
            if (registeredUser != null){
                //调用BaseServlet中的sendJsonResponse方法，将User对象转换为Json数据并返回
                //使用Result类来封装返回的数据
                sendJsonResponse(resp,new Result(200, "注册成功", registeredUser));
            } else {
                sendJsonResponse(resp,new Result(500, "注册失败", null));
            }

        } else if (pathInfo.equals("/login")){

            String username = req.getParameter("username");
            String password = req.getParameter("password");
            User user = userService.login(username, password);
            if (user!= null){
                sendJsonResponse(resp,new Result(200, "登录成功", user));
            } else {
                //如果登录失败，返回401，表示未授权，也就是用户名或密码错误
                sendJsonResponse(resp,new Result(401, "登录失败", null));
            }
        } else if (pathInfo.equals("/followUser")) {

            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取被关注的用户id，并将被关注的用户id转换成Integer类型
            int followUserId = Integer.parseInt(req.getParameter("followUserId"));
            //调用UserService中的followUser方法，将用户id和被关注的用户id传入
            userService.followUser(userId, followUserId);
            //发送成功响应
            sendJsonResponse(resp,new Result(200, "关注成功", null));

        } else if (pathInfo.equals("/followBoard")) {

            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取被关注的板块id，并将被关注的板块id转换成Integer类型
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            //调用UserService中的followBoard方法，将用户id和被关注的板块id传入
            userService.followBoard(userId, boardId);
            //发送成功响应
            sendJsonResponse(resp,new Result(200, "关注成功", null));

        } else if (pathInfo.equals("/report")) {

            //举报
            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取被举报的用户id，并将被举报的用户id转换成Integer类型
            int reportedUserId = Integer.parseInt(req.getParameter("reportedUserId"));
            //获取举报的类型：即是举报用户还是举报帖子
            String reportType = req.getParameter("reportType");
            //获取举报原因
            String reportReason = req.getParameter("reportReason");
            //调用UserService中的report方法，将用户id和被举报的用户id传入
            userService.report(userId, reportType,reportedUserId, reportReason);
            //发送成功响应
            sendJsonResponse(resp,new Result(200, "举报成功", null));
        }

    }

    //重写doGet方法，用于处理GET请求，例如通过用户Id获取用户信息，获取用户关注的用户，获取用户关注的板块
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/getUserById")){
            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //调用UserService中的getUserById方法，将用户id传入
            User user = userService.getUserById(userId);
            //判断是否获取成功
            if (user!= null){
                sendJsonResponse(resp,new Result(200, "获取成功", user));
            } else {
                sendJsonResponse(resp,new Result(500, "获取失败", null));
            }
        } else if (pathInfo.equals("/getFollowedUsers")){
            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //调用UserService中的getFollowUsers方法，将用户id传入
            List<User> followUsers = userService.getFollowUsers(userId);
            //判断是否获取成功
            if (followUsers!= null){
                sendJsonResponse(resp,new Result(200, "获取成功", followUsers));
            } else {
                sendJsonResponse(resp,new Result(500, "获取失败", null));
            }

        } else if (pathInfo.equals("/getFollowedBoards")){

            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //调用UserService中的getFollowedBoards方法，将用户id传入
            List<Integer> followedBoards = userService.getFollowedBoards(userId);
            //判断是否获取成功
            if (followedBoards!= null){
                sendJsonResponse(resp,new Result(200, "获取成功", followedBoards));
            } else {
                sendJsonResponse(resp,new Result(500, "获取失败", null));
            }

        } else if (pathInfo.equals("/getFootprints")) {

            //获取帖子足迹
            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //调用UserService中的getFootprints方法，将用户id传入
            List<Integer> footprints = userService.getPostFootPrints(userId);
            //判断是否获取成功
            if (footprints!= null){
                sendJsonResponse(resp,new Result(200, "获取成功", footprints));
            } else {
                sendJsonResponse(resp,new Result(500, "获取失败", null));
            }

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
            if (pathInfo.equals("/updateNickname")){

                //获取用户id，并将用户id转换成Integer类型
                int userId = Integer.parseInt(req.getParameter("userId"));
                //获取用户输入的新昵称
                String newNickname = req.getParameter("newNickname");
                //调用UserService中的updateNickname方法，将用户id和新昵称传入
                User user = userService.updateNickname(newNickname, userId);
                //判断是否修改成功
                if (user!= null){
                    sendJsonResponse(resp,new Result(200, "昵称更新成功", user));
                } else {
                    sendJsonResponse(resp,new Result(500, "昵称更新失败", null));
                }

            } else if (pathInfo.equals("/updateAvatar")){

                int userId = Integer.parseInt(req.getParameter("userId"));
                String avatar = req.getParameter("avatar");
                //调用UserService中的updateAvatar方法，将用户id和新头像传入
                User user = userService.updateAvatar(userId, avatar);
                //判断是否修改成功
                if (user!= null){
                    sendJsonResponse(resp,new Result(200, "头像更新成功", user));
                } else {
                    sendJsonResponse(resp,new Result(500, "头像更新失败", null));
                }
            } else if (pathInfo.equals("/updatePassword")){
                int userId = Integer.parseInt(req.getParameter("userId"));
                String newPassword = req.getParameter("newPassword");
                //调用UserService中的updatePassword方法，将用户id和新密码传入
                User user = userService.updatePassword(userId, newPassword);
                //判断是否修改成功
                if (user!= null){
                    sendJsonResponse(resp,new Result(200, "密码更新成功", user));
                } else {
                    sendJsonResponse(resp,new Result(500, "密码更新失败", null));
                }
            }
    }

    //重写doDelete
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/unfollowUser")){
            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取被关注的用户id，并将被关注的用户id转换成Integer类型
            int followUserId = Integer.parseInt(req.getParameter("followUserId"));
            //调用UserService中的unfollowUser方法，将用户id和被关注的用户id传入
            userService.unfollowUser(userId, followUserId);
            //发送成功响应
            sendJsonResponse(resp,new Result(200, "取消关注成功", null));

        } else if (pathInfo.equals("/unfollowBoard")){
            //获取用户id，并将用户id转换成Integer类型
            int userId = Integer.parseInt(req.getParameter("userId"));
            //获取被关注的板块id，并将被关注的板块id转换成Integer类型
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            //调用UserService中的unfollowBoard方法，将用户id和被关注的板块id传入
            userService.unfollowBoard(userId, boardId);
            //发送成功响应
            sendJsonResponse(resp,new Result(200, "取消关注成功", null));
        }
    }
}
