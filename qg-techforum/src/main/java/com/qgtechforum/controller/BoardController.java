package com.qgtechforum.controller;

import com.BaseServlet;
import com.qgtechforum.model.Board;
import com.qgtechforum.model.Result;
import com.qgtechforum.service.BoardService;
import com.qgtechforum.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/Board/*")
public class BoardController extends BaseServlet {
    private BoardService boardService = new BoardService();

    //重写doPost方法，用于处理POST请求
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求路径中的信息
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/create")){
            //获取请求体
            BufferedReader reader = req.getReader();
            //创建StringBuilder对象，用于存储请求体中的数据
            StringBuilder sb = new StringBuilder();
            //由于请求体中的数据是一行一行的，所以定义一个字符串用来接收每一行的请求体数据，然后拼接到StringBuilder对象中
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            Board board = JsonUtils.fromJson(sb.toString(), Board.class);
            //调用service层的方法
            Board createBoard = boardService.createBoard(board);
            //判断是否创建成功
            if (createBoard!= null){
                //调用BaseServlet中的sendJsonResponse方法，将Board对象转换为Json数据并返回
                sendJsonResponse(resp,new Result(200, "板块创建成功", createBoard));
            } else {
                //如果createBoard对象为空，说明创建板块失败
                sendJsonResponse(resp,new Result(500,"板块创建失败",null));
            }
        }
    }

    //重写doGet方法，用于处理GET请求,主要用于获取板块信息
    protected void doGet(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        //获取请求路径中的信息
        String pathInfo = req.getPathInfo();
        //如果请求路径是获取所有板块信息
        if (pathInfo.equals("/all")){
            //创建List集合，存放等会service返回的板块集合
            List<Board> boards = boardService.getAllBoards();
            sendJsonResponse(resp,new Result(200,"获取所有板块信息成功",boards));
        } else if (pathInfo.equals("/approved")){
            List<Board> boards = boardService.getApprovedBoards();
            sendJsonResponse(resp,new Result(200,"获取审核通过的板块信息成功",boards));
        } else if(pathInfo.equals("/search")) {
            String keyword = req.getParameter("keyword");
            //调用service层的方法
            List<Board> boards = boardService.searchBoards(keyword);
            //判断是否获取成功
            if (boards!= null) {
                sendJsonResponse(resp,new Result(200,"获取板块信息成功",boards));
            } else {
                sendJsonResponse(resp,new Result(500,"获取板块信息失败",null));
            }
        } else if (pathInfo.equals("/hot")) {
            int limit = Integer.parseInt(req.getParameter("limit"));
            List<Board> boards = boardService.getHotBoards(limit);
            if (boards!= null) {
                sendJsonResponse(resp,new Result(200,"获取热门板块成功",boards));
            } else {
                sendJsonResponse(resp,new Result(500,"获取热门板块失败",null));
            }
        } else if (pathInfo.startsWith("/")) {
            //通过板块id获取板块信息
            try {
                int boardId = Integer.parseInt(pathInfo.substring(1));
                Board board = boardService.getBoardById(boardId);
                if (board != null) {
                    sendJsonResponse(resp,new Result(200,"获取板块成功",board));
                } else {
                    //发送404是因为404代表资源不存在
                    sendJsonResponse(resp,new Result(404,"板块不存在",null));
                }
            } catch (NumberFormatException e) {
                //状态码400意思是请求参数错误
                sendJsonResponse(resp,new Result(400,"无效的板块ID",null));
            }

        }
    }

    //重写doPut方法，主要用于更新数据的请求，比如发送审核通过的请求
    @Override
    protected void doPut(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        //获取请求路径中的信息
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/approve")){
            //获取要审核通过的板块的id
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            //调用service层的方法
            boolean approved = boardService.approveBoard(boardId);
            if (approved) {
                sendJsonResponse(resp,new Result(200,"板块审核成功",null));
            } else {
                sendJsonResponse(resp,new Result(500,"板块审核失败",null));
            }
        }
    }

    //重写doDelete方法，主要用于删除数据的请求，比如删除板块中的某个帖子
    @Override
    protected void doDelete(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        //获取请求路径中的信息
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/deletePost")){
            //不仅要获取板块的id，还要获取板块中所有帖子的id
            int postId = Integer.parseInt(req.getParameter("postId"));
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            //调用service层的方法
            boolean deleted = boardService.deletePostInBoard(postId,boardId);
            //判断板块是否删除成功
            if (deleted){
                sendJsonResponse(resp,new Result(200,"帖子删除成功",null));
            } else {
                sendJsonResponse(resp,new Result(500,"帖子删除失败",null));
            }
        } else if (pathInfo.equals("banUser")) {
            //获取用户id和板块的id
            int userId = Integer.parseInt(req.getParameter("userId"));
            int boardId = Integer.parseInt(req.getParameter("boardId"));
            //调用service层的方法
            boolean banned = boardService.banUserInBoard(userId,boardId);
            //判断封禁用户是否成功
            if (banned) {
                sendJsonResponse(resp,new Result(200,"用户封禁成功",null));
            } else {
                sendJsonResponse(resp,new Result(500,"用户封禁失败",null));
            }
        }
    }
}
