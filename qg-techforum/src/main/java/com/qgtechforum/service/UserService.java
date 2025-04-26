package com.qgtechforum.service;

import com.qgtechforum.dao.UserDao;
import com.qgtechforum.model.User;

import java.util.List;

public class UserService {
    private UserDao userDao = new UserDao();

    //注册用户
    public User register(User user){
        return userDao.register(user);
    }

    //登录用户
    public User login(String username,String password){
        return userDao.login(username, password);
    }

    //修改用户昵称
    public User updateNickname(String nickname, int id){
        return userDao.updateNickname(nickname, id);
    }

    //修改用户头像
    public User updateAvatar(int id,String avatar){
        return userDao.updateAvatar(id, avatar);
    }

    //修改用户密码
    public User updatePassword(int id,String password){
        return userDao.updatePassword(id, password);
    }

    //通过用户id获取用户信息
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    //获取该用户的用户关注表
    public List<User> getFollowUsers(int userId) {
        return userDao.getFollowUsers(userId);
    }

    //关注某个用户
    public void followUser(int userId, int followUserId) {
        userDao.followUser(userId, followUserId);
    }

    //取消关注某个用户
    public void unfollowUser(int userId, int followUserId) {
        userDao.unfollowUser(userId, followUserId);
    }

    //关注某个板块
    public void followBoard(int userId, int boardId) {
        userDao.followBoard(userId, boardId);
    }

    //取消关注某个板块
    public void unfollowBoard(int userId, int boardId) {
        userDao.unfollowBoard(userId, boardId);
    }

    //获取用户关注的板块
    public List<Integer> getFollowedBoards(int userId) {
        return userDao.getFollowedBoards(userId);
    }

    //用户申请创建板块
    public void applyForBoard(int userId, String boardName) {
        userDao.applyForBoard(userId, boardName);
    }

    //管理员审核板块申请
    public boolean approveBoardApplication(int applicationId) {
        return userDao.approveBoardApplication(applicationId);
    }

    //吧主权限：发布板块公告
    public void publishBoardAnnouncement(int boardId, String content) {
        userDao.publishBoardAnnouncement(boardId, content);
    }

    //用户举报帖子或者用户
    public void report(int reporterId,String reportedType,int reportedId,String reason) {
        userDao.report(reporterId, reportedType, reportedId, reason);
    }

    //记录用户帖子足迹
    public void recordPostFootPrint(int userId, int postId) {
        userDao.recordPostFootPrint(userId, postId);
    }

    //获取用户帖子足迹
    public List<Integer> getPostFootPrints(int userId) {
        return userDao.getPostFootPrints(userId);
    }
}
