package com.qgtechforum.service;

import com.qgtechforum.dao.CommentDao;
import com.qgtechforum.model.Comment;

import java.util.List;


public class CommentService{
    private CommentDao commentDao = new CommentDao();

    //留言评论
    public Comment createComment(Comment comment) {
        return commentDao.createComment(comment);
    }

    //通过帖子id获取评论
    public List<Comment> getCommentsByPostId(int postId) {
        return commentDao.getCommentsByPostId(postId);
    }

    //点赞评论
    public boolean likeComment(int commentId) {
        return commentDao.likeComment(commentId);
    }
}
