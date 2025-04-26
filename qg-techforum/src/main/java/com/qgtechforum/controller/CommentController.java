package com.qgtechforum.controller;

import com.BaseServlet;
import com.qgtechforum.model.Comment;
import com.qgtechforum.model.Result;
import com.qgtechforum.service.CommentService;
import com.qgtechforum.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/Comment/*")
public class CommentController extends BaseServlet {
    //创建CommentService对象
    private CommentService commentService = new CommentService();

    //重写doPut方法
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取动态路径
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/like")) {
            int commentId = Integer.parseInt(req.getParameter("commentId"));
            boolean liked = commentService.likeComment(commentId);
            if (liked) {
                sendJsonResponse(resp,new Result(200, "点赞成功", null));
            } else {
                sendJsonResponse(resp,new Result(500, "点赞失败", null));
            }
        }
    }

    //重写doGet
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取动态路径
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/getComments")) {
            int postId = Integer.parseInt(req.getParameter("postId"));
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            if (comments!= null) {
                sendJsonResponse(resp,new Result(200, "获取成功", comments));
            } else {
                sendJsonResponse(resp,new Result(500, "获取失败", null));
            }
        }
    }

    //重写doPost
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取动态路径
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/create")) {
            //获取请求体中的数据
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null){
                sb.append(line);
            }
            //将请求体中的数据转换为Comment对象
            Comment comment = JsonUtils.fromJson(sb.toString(), Comment.class);
            //调用CommentService中的createComment方法，将Comment对象存入数据库中
            Comment createdComment = commentService.createComment(comment);
            //判断是否创建成功
            if (createdComment!= null){
                sendJsonResponse(resp,new Result(200, "创建成功", createdComment));
            } else {
                sendJsonResponse(resp,new Result(500, "创建失败", null));
            }
        }
    }
}
