package com.qgtechforum.service;

import com.qgtechforum.dao.BoardDao;
import com.qgtechforum.model.Board;

import java.util.List;

//service包主要的作用就是将dao包中的方法封装起来，提供给controller包使用
public class BoardService {
    //首先是创建一个BoardDao对象
    private BoardDao boardDao = new BoardDao();

    //首先是封装创建板块的方法
    public Board createBoard(Board board){
        //调用boardDao中的createBoard方法，将board对象传入
        return boardDao.createBoard(board);
    }


    //根据板块的id获取板块
    public Board getBoardById(int boardId){
        //调用boardDao中的getBoardById方法，将id传入
        return boardDao.getBoardById(boardId);
    }

    //获取所有板块
    public List<Board> getAllBoards(){
        //调用boardDao中的getAllBoards方法
        return boardDao.getAllBoards();
    }

    //获取审核通过的板块
    public List<Board> getApprovedBoards(){
        //调用boardDao中的getApprovedBoards方法
        return boardDao.getApprovedBoards();
    }

    //管理员权限：批准板块创建申请通过
    public boolean approveBoard(int boardId){
        //调用boardDao中的approveBoard方法，将id传入
        return boardDao.approveBoard(boardId);
    }

    //管理员权限：删除板块中的某个帖子
    public boolean deletePostInBoard(int postId,int boardId){
        //调用boardDao中的deleteBoard方法，将id传入
        return boardDao.deletePostInBoard(postId,boardId);
    }

    //吧主权限:封禁用户在该板块发布帖子
    public boolean banUserInBoard(int userId,int boardId){
        //调用boardDao中的banUserInBoard方法，将id传入
        return boardDao.banUserInBoard(userId,boardId);
    }

    //根据关键字查询板块
    public List<Board> searchBoards(String keyword){
        //调用boardDao中的searchBoards方法，将keyword传入
        return boardDao.searchBoards(keyword);
    }

    //获取热门板块
    public List<Board> getHotBoards(int limit){
        return boardDao.getHotBoards(limit);
    }
}
