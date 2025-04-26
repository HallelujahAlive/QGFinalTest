package com.qgtechforum.controller;

import com.BaseServlet;
import com.qgtechforum.model.Post;
import com.qgtechforum.model.Result;
import com.qgtechforum.service.PostService;
import com.qgtechforum.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/Post/*")
public class PostController extends BaseServlet {
    private PostService postService = new PostService();

    //重写doPost方法，用于处理POST请求
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求路径中的信息
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/create")){
            //获取请求体
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null){
                sb.append(line);
            }
            //调用JsonUtils工具类的方法，将Json数据转换为Post对象
            Post post = JsonUtils.fromJson(sb.toString(), Post.class);
            //调用service层的方法
            Post createPost = postService.createPost(post);
            //判断是否创建成功
            if (createPost!= null){
                sendJsonResponse(resp,new Result(200, "帖子创建成功", createPost));
            } else {
                sendJsonResponse(resp,new Result(500,"帖子创建失败",null));
            }
        }
    }

    //重写doGet方法，用于处理GET请求,主要用于获取帖子信息
    @Override
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        //通过帖子id查找获取该帖子信息
        if (pathInfo.startsWith("/board")) {
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            List<Post> posts = postService.getPostsByBoardId(boardId);
            //判断是否获取成功
            if (posts!= null){
                sendJsonResponse(resp,new Result(200,"获取该板块下的帖子成功",posts));
            } else {
                sendJsonResponse(resp,new Result(500,"获取该板块下的帖子失败",null));
            }
        } else if (pathInfo.equals("/searchInBoard")) {
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            String keyword = req.getParameter("keyword");
            List<Post> posts = postService.searchPostsInBoard(boardId, keyword);
            if (posts!= null){
                sendJsonResponse(resp,new Result(200,"搜索成功",posts));
            } else {
                sendJsonResponse(resp,new Result(500,"搜索失败",null));
            }
        } else if (pathInfo.equals("/latest")) {
            //获取最新帖子
            int limit = Integer.parseInt(req.getParameter("limit"));
            List<Post> posts = postService.getLatestPosts(limit);
            if (posts!= null){
                sendJsonResponse(resp,new Result(200,"获取最新帖子成功",posts));
            } else {
                sendJsonResponse(resp,new Result(500,"获取最新帖子失败",null));
            }
        } else if (pathInfo.equals("/hot")) {
            //获取热门帖子
            int limit = Integer.parseInt(req.getParameter("limit"));
            List<Post> posts = postService.getHotPosts(limit);
            if (posts!= null){
                sendJsonResponse(resp,new Result(200,"获取热门帖子成功",posts));
            } else {
                sendJsonResponse(resp,new Result(500,"获取热门帖子失败",null));
            }
        }else if (pathInfo.startsWith("/")) {  //根据帖子id查找获取该帖子信息
            try {
                int postId = Integer.parseInt(pathInfo.substring(1));
                Post post = postService.getPostById(postId);
                if (post != null) {
                    sendJsonResponse(resp, new Result(200, "获取帖子信息成功", post));
                } else {
                    sendJsonResponse(resp, new Result(404, "未找到该帖子", null));
                }
            } catch (NumberFormatException e) {
                sendJsonResponse(resp, new Result(400, "无效的帖子ID", null));
            }
        }
    }

    //重写doPut放大，用于点赞帖子和收藏帖子导致的点赞数和收藏数的变化
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/like")){
            int postId = Integer.parseInt(req.getParameter("postId"));
            boolean liked = postService.likePost(postId);
            if (liked){
                sendJsonResponse(resp,new Result(200,"帖子点赞成功",null));
            } else {
                sendJsonResponse(resp,new Result(500,"帖子点赞失败",null));
            }
        } else if (pathInfo.equals("/collect")){
            int postId = Integer.parseInt(req.getParameter("postId"));
            boolean collected = postService.collectPost(postId);
            if (collected){
                sendJsonResponse(resp,new Result(200,"帖子收藏成功",null));
            } else {
                sendJsonResponse(resp,new Result(500,"帖子收藏失败",null));
            }
        }
    }
}
