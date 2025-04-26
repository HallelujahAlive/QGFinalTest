package com.qgtechforum.dao;

import com.qgtechforum.model.User;
import com.qgtechforum.utils.DBUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//dao层负责数据库的增删改查等操作
public class UserDao {
    //这个类需要User对象，因为需要将用户输入的用户名，密码，昵称存入数据库中，而将这些信息存入数据库由负责
    public User register(User user){
        //将用户输入的用户名，密码，昵称存入数据库中
        String sql = "insert into users (username,password,nickName) values (?,?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickName());
            int i = pstmt.executeUpdate();
            if (i > 0){
                //获取插入的主键
                ResultSet rs = pstmt.getGeneratedKeys();
                //判断是否有主键，如果有就将主键存入user对象中
                if (rs.next()){
                    //将主键存入user对象中
                    user.setId(rs.getInt(1));
                }
                /*
                为什么要返回user呢？因为在注册成功后，需要将用户的信息存入session中，所以需要返回user对象
                存了session就不用总是从数据库中查询了，这样会影响性能，所以需要将用户的信息存入session中
                而session是由servlet来管理的，所以需要返回user对象，才能将user对象存入session中
                */
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public User login(String username,String password){
        String sql = "select * from users where username = ? and password = ?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setString(1, username);
                 pstmt.setString(2, password);
                 ResultSet rs = pstmt.executeQuery();
                 if (rs.next()){
                     //将查询到的用户信息存入user对象中
                     User user = new User();
                     user.setId(rs.getInt("id"));
                     user.setUsername(rs.getString("username"));
                     user.setPassword(rs.getString("password"));
                     user.setNickName(rs.getString("nickname"));
                     user.setAvatar(rs.getString("avatar_url"));
                     user.setAdmin(rs.getBoolean("isAdmin"));
                     //后续要将user存入session
                     return user;
                 }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //修改用户的昵称
    public User updateNickname(String nickname, int id){
        String sql = "update users set nickname =? where id = ? ";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setString(1, nickname);
                 pstmt.setInt(2, id);
                 pstmt.executeUpdate();
                 //调用getUserById方法，获取修改后的用户信息
                 return getUserById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //修改用户的头像
    public User updateAvatar(int id,String avatar){
        String sql = "update users set avatar_url =? where id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setString(1, avatar);
                 pstmt.setInt(2, id);
                 pstmt.executeUpdate();
                 return getUserById(id);
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //修改用户的密码
    public User updatePassword(int id,String password){
        String sql = "update users set password =? where id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setString(1, password);
                 pstmt.setInt(2, id);
                 pstmt.executeUpdate();
                 return getUserById(id);
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(int id) {
        String sql = "select * from users where id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, id);
                 ResultSet rs = pstmt.executeQuery();
                 if (rs.next()){
                     User user = new User();
                     user.setId(rs.getInt("id"));
                     user.setUsername(rs.getString("username"));
                     user.setPassword(rs.getString("password"));
                     user.setNickName(rs.getString("nickname"));
                     user.setAvatar(rs.getString("avatar_url"));
                     user.setAdmin(rs.getBoolean("isAdmin"));
                     return user;
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    //获取该用户的用户关注表
    public List<User> getFollowUsers(int id) {
        String sql = "select * from users where id in (select followed_user_id from user_follows where user_id = ?)";
        List<User> followedUsers = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, id);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     User user = new User();
                     user.setId(rs.getInt("id"));
                     user.setUsername(rs.getString("username"));
                     user.setPassword(rs.getString("password"));
                     user.setNickName(rs.getString("nickname"));
                     user.setAvatar(rs.getString("avatar_url"));
                     user.setAdmin(rs.getBoolean("isAdmin"));
                     followedUsers.add(user);
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return followedUsers;
    }

    //关注某个用户
    public void followUser(int userId, int followedUserId) {
        String sql = "insert into user_follows (user_id, followed_user_id) values (?, ?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 pstmt.setInt(2, followedUserId);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //取消关注某个用户
    public void unfollowUser(int userId, int followedUserId) {
        String sql = "delete from user_follows where user_id =? and followed_user_id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 pstmt.setInt(2, followedUserId);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //关注某个板块
    public void followBoard(int userId, int boardId) {
        String sql = "insert into user_followed_boards (user_id, board_id) values (?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 pstmt.setInt(2, boardId);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //取消关注某个板块
    public void unfollowBoard(int userId, int boardId) {
        String sql = "delete from user_followed_boards where user_id =? and board_id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 pstmt.setInt(2, boardId);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //获取用户关注的板块
    public List<Integer> getFollowedBoards(int userId) {
        String sql = "select board_id from user_followed_boards where user_id =?";
        List<Integer> boardIds = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()) {
                     boardIds.add(rs.getInt("board_id"));
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return boardIds;
    }

    //用户申请创建板块
    public void applyForBoard(int userId,String boardName){
        String sql = "insert into board_applications (user_id, board_name) values (?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 pstmt.setString(2, boardName);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //管理员审核版块申请
    public boolean approveBoardApplication(int applicationId) {
        String sql = "update board_applications set status = 'approved' where id =?";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, applicationId);
                 int i = pstmt.executeUpdate();
                 if (i > 0){
                     //获取申请信息
                     String selectSql = "select user_id, board_name from board_applications where id =?";
                     try (PreparedStatement selectPstmt = conn.prepareStatement(selectSql)){
                         selectPstmt.setInt(1, applicationId);
                         ResultSet rs = selectPstmt.executeQuery();
                         if (rs.next()){
                             int userId = rs.getInt("user_id");
                             String boardName = rs.getString("board_name");
                             //创建板块
                             String insertSql = "insert into boards (boardName, board_master_id,is_approved) values (?,?,?)";
                             try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)){
                                 insertPstmt.setString(1, boardName);
                                 insertPstmt.setInt(2, userId);
                                 insertPstmt.setBoolean(3, true);
                                 insertPstmt.executeUpdate();
                             }
                         }
                     }
                     return true;
                 }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //吧主发布板块公告
    public void publishBoardAnnouncement(int boardId,String content){
        String sql = "insert into board_announcements (board_id, content) values (?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, boardId);
                 pstmt.setString(2, content);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //用户举报帖子或用户
    public void report(int reporterId,String reportedType,int reportedId,String reason){
        String sql = "insert into reports (reporter_id, reported_type, reported_id, reason) values (?,?,?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, reporterId);
                 pstmt.setString(2, reportedType);
                 pstmt.setInt(3, reportedId);
                 pstmt.setString(4, reason);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //记录用户帖子足迹
    public void recordPostFootPrint(int userId,int postId){
        String sql = "insert into post_footprints (user_id, post_id) values (?,?)";
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 pstmt.setInt(2, postId);
                 pstmt.executeUpdate();
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //获取用户帖子足迹，返回帖子id集合
    public List<Integer> getPostFootPrints(int userId){
        String sql = "select post_id from post_footprints where user_id =?";
        List<Integer> postIds = new ArrayList<>();
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                 pstmt.setInt(1, userId);
                 ResultSet rs = pstmt.executeQuery();
                 while (rs.next()){
                     postIds.add(rs.getInt("post_id"));
                 }
             } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return postIds;
    }
}
