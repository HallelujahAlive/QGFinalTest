package com.qgtechforum.dao;

import com.qgtechforum.model.Post;
import com.qgtechforum.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDao {
    // 实现帖子相关的数据库操作方法

    //首先是创建帖子
    public Post createPost(Post post) {
        String sql = "insert into posts (author_id,board_id,title,content,image_url) values (?,?,?,?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setInt(1, post.getAuthorId());
            pstmt.setInt(2, post.getBoardId());
            pstmt.setString(3, post.getTitle());
            pstmt.setString(4, post.getContent());
            pstmt.setString(5, post.getImages());
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                post.setId(rs.getInt(1));
            }
            // 插入成功后返回post对象，留着之后要用到
            return post;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 然后是通过id获取帖子
    public Post getPostById(int postId) {
        String sql = "select * from posts where id = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, postId);
                 ResultSet rs = pstmt.executeQuery();
                 if (rs.next()) {
                     Post post = new Post();
                     post.setId(rs.getInt("id"));
                     post.setAuthorId(rs.getInt("author_id"));
                     post.setBoardId(rs.getInt("board_id"));
                     post.setTitle(rs.getString("title"));
                     post.setContent(rs.getString("content"));
                     post.setImages(rs.getString("image_url"));
                     post.setLikes(rs.getInt("likes"));
                     post.setCollections(rs.getInt("collections"));
                     return post;
                 }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //然后是通过板块id获取帖子，因为之后显示某个板块的时候需要展示所有帖子
    public List<Post> getPostsByBoardId(int boardId) {
        String sql = "select * from posts where board_id =?";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, boardId);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Post post = new Post();
                     post.setId(rs.getInt("id"));
                     post.setAuthorId(rs.getInt("author_id"));
                     post.setBoardId(rs.getInt("board_id"));
                     post.setTitle(rs.getString("title"));
                     post.setContent(rs.getString("content"));
                     post.setImages(rs.getString("image_url"));
                     post.setLikes(rs.getInt("likes"));
                     post.setCollections(rs.getInt("collections"));
                     posts.add(post);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    //点赞帖子
    public boolean likePost(int postId) {
        String sql = "update posts set likes = likes + 1 where id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, postId);
                 int rows = pstmt.executeUpdate();
                 return rows > 0;
        } catch (SQLException e) {
                throw new RuntimeException(e);
        }
    }

    //收藏帖子
    public boolean collectPost(int postId) {
        String sql = "update posts set collections = collections + 1 where id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, postId);
                 int rows = pstmt.executeUpdate();
                 return rows > 0;
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //根据关键字在板块内搜索帖子
    public List<Post> searchPostsInBoard(int boardId,String keyword) {
        String sql = "select * from posts where board_id = ? and (title like ? or content like ?)";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, boardId);
                 pstmt.setString(2, "%" + keyword + "%");
                 pstmt.setString(3, "%" + keyword + "%");
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     //从数据库中获取帖子信息并封装成Post对象
                     Post post = new Post();
                     post.setId(rs.getInt("id"));
                     post.setBoardId(rs.getInt("board_id"));
                     post.setAuthorId(rs.getInt("author_id"));
                     post.setTitle(rs.getString("title"));
                     post.setContent(rs.getString("content"));
                     post.setImages(rs.getString("image_url"));
                     post.setLikes(rs.getInt("likes"));
                     post.setCollections(rs.getInt("collections"));
                     //将Post对象添加到List集合中
                     posts.add(post);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    //获取最新帖子(按时间排序)
    public List<Post> getLatestPosts(int limit) {
        //created_at desc limit的意思是按照创建时间降序排列，limit是限制获取的帖子数量，创建的时间排的越晚那么排的就越前
        String sql = "select * from posts order by create_time desc limit ?";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, limit);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Post post = new Post();
                     post.setId(rs.getInt("id"));
                     post.setBoardId(rs.getInt("board_id"));
                     post.setAuthorId(rs.getInt("author_id"));
                     post.setTitle(rs.getString("title"));
                     post.setContent(rs.getString("content"));
                     post.setImages(rs.getString("image_url"));
                     post.setLikes(rs.getInt("likes"));
                     post.setCollections(rs.getInt("collections"));
                     posts.add(post);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }

    //获取热门帖子（按点赞量排序），点赞量越多排的越前
    public List<Post> getHotPosts(int limit) {
        //按照点赞量降序排序，limit是限制获取的帖子数量
        String sql = "select * from posts order by likes desc limit ?";
        List<Post> posts = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 pstmt.setInt(1, limit);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     Post post = new Post();
                     post.setId(rs.getInt("id"));
                     post.setBoardId(rs.getInt("board_id"));
                     post.setAuthorId(rs.getInt("author_id"));
                     post.setTitle(rs.getString("title"));
                     post.setContent(rs.getString("content"));
                     post.setImages(rs.getString("image_url"));
                     post.setLikes(rs.getInt("likes"));
                     post.setCollections(rs.getInt("collections"));
                     posts.add(post);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return posts;
    }
}
