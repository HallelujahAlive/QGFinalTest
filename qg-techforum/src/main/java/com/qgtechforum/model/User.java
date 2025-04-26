package com.qgtechforum.model;

import java.util.List;
//用户可以上传头像
//包含普通⽤⼾和系统管理员,也就是说类中需要布尔值来表示用户是普通用户还是系统管理员
//可以关注⽤⼾和板块
//所以需要存放该用户关注的用户的集合，以及该用户关注的板块的集合
public class User {
    private int id;               //主键
    private String username;      //用户名
    private String password;      //密码
    private String nickname;      //昵称
    private String avatar;        //头像
    private boolean isAdmin;      //是否为管理员
    //创建集合存放粉丝,这里用Integer类型而不是User类型是为了轻量度，因为User类型中包含了很多信息，而Integer类型中只包含了id
    private List<Integer> followUsers;
    //创建集合存放关注的板块，同样也是为了轻量储存
    private List<Integer> followBoards;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickname;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Integer> getFollowUsers() {
        return followUsers;
    }

    public void setFollowUsers(List<Integer> followUsers) {
        this.followUsers = followUsers;
    }

    public List<Integer> getFollowBoards() {
        return followBoards;
    }

    public void setFollowBoards(List<Integer> followBoards) {
        this.followBoards = followBoards;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
