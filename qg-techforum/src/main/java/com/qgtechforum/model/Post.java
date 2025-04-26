package com.qgtechforum.model;

import java.util.List;

//帖子主要是评论，所以必须包含存放评论的集合
//帖子主要内容包括标题，内容，图片，作者，板块
public class Post {
    private int id;               //帖子id
    private int boardId;          //板块id
    private int authorId;         //作者id
    private String title;         //标题
    private String content;       //内容
    private String images;        //图片
    private int likes;            //点赞数
    private int collections;      //收藏数
    //评论数需要用Comment类型的集合来储存
    private List<Comment> comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getCollections() {
        return collections;
    }

    public void setCollections(int collections) {
        this.collections = collections;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
