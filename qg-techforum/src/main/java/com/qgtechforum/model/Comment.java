package com.qgtechforum.model;

import java.util.Date;

//评论的主要组成部分就是内容和评论时间,评论时间是Date类型,所以需要导包,要导的包是java.util.Date
public class Comment {
    private int id;               //主键
    private int authorId;         //作者id
    private int postId;           //帖子id
    private String content;       //内容
    private int likes;            //点赞数
    private Date createTime;      //创建时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
