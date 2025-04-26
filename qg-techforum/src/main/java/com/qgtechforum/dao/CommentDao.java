package com.qgtechforum.dao;

import com.qgtechforum.model.Comment;
import com.qgtechforum.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {
    //留言评论
    public Comment createComment(Comment comment) {
        String sql = "insert into comments (post_id, user_id, content) values (?,?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, comment.getPostId());
                 pstmt.setInt(2, comment.getAuthorId());
                 pstmt.setString(3, comment.getContent());
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comment;
    }

    //通过帖子id获取评论
    public List<Comment> getCommentsByPostId(int postId) {
        //按照时间顺序获取评论，最新发布的评论在最前面
        String sql = "select * from comments where post_id =? order by create_time desc";
        List<Comment> comments = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, postId);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Comment comment = new Comment();
                     comment.setId(rs.getInt("id"));
                     comment.setPostId(rs.getInt("post_id"));
                     comment.setAuthorId(rs.getInt("user_id"));
                     comment.setContent(rs.getString("content"));
                     comment.setLikes(rs.getInt("likes"));
                     comment.setCreateTime(rs.getTimestamp("create_time"));
                     comments.add(comment);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return comments;
    }

    //点赞评论：用户点开帖子后可以给帖子内的评论点赞
    public boolean likeComment(int commentId) {
        String sql = "update comments set likes = likes + 1 where id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, commentId);
                 int i = pstmt.executeUpdate();
                 return i > 0;
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
