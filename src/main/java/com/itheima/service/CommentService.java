package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Comment;

import java.util.List;

public interface CommentService {
    // 获取文章下的评论
    public PageInfo<Comment> getComments(Integer aid, int page, int count);

    // 用户发表评论
    public void pushComment(Comment comment);

    //获取全部的评论
    public PageInfo<Comment> getCommentsAll(int page,int count);

    //删除评论
    public void deleteCommentWithId(Integer id);

    //获取评论表中有几条数据
    public Integer findCommentCount();

    // 后台查询最新几条评论
    public List<Comment> selectNewComment();

    //查询某篇文章的评论数
    public Integer findArticleCommentCount(Integer id);
}
