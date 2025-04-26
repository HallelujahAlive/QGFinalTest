package com.qgtechforum.service;

import com.qgtechforum.dao.PostDao;
import com.qgtechforum.model.Post;

import java.util.List;

public class PostService {
    private PostDao postDao = new PostDao();

    //创建帖子
    public Post createPost(Post post) {
        return postDao.createPost(post);
    }

    //通过id获取帖子
    public Post getPostById(int postId) {
        return postDao.getPostById(postId);
    }

    //通过板块id获取板块内所有帖子
    public List<Post> getPostsByBoardId(int boardId) {
        return postDao.getPostsByBoardId(boardId);
    }

    //点赞帖子
    public boolean likePost(int postId) {
        return postDao.likePost(postId);
    }

    //收藏帖子
    public boolean collectPost(int postId) {
        return postDao.collectPost(postId);
    }

    //根据关键字搜索板块内的帖子
    public List<Post> searchPostsInBoard(int boardId, String keyword) {
        return postDao.searchPostsInBoard(boardId, keyword);
    }

    //获取最新帖子
    public List<Post> getLatestPosts(int limit) {
        return postDao.getLatestPosts(limit);
    }

    //获取热门帖子
    public List<Post> getHotPosts(int limit) {
        return postDao.getHotPosts(limit);
    }
}
