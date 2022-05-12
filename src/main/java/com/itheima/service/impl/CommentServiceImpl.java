package com.itheima.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.CommentMapper;
import com.itheima.mapper.StatisticMapper;
import com.itheima.model.domain.Comment;
import com.itheima.model.domain.Statistic;
import com.itheima.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private StatisticMapper statisticMapper;

    // 根据文章id分页查询评论
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;
    }

    // 用户发表评论
    @Override
    public void pushComment(Comment comment){
        commentMapper.pushComment(comment);
        // 更新文章评论数据量
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }
    //获取全部评论
    @Override
    public PageInfo<Comment> getCommentsAll(int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> commentsAll = commentMapper.getCommentsAll();
        PageInfo<Comment> commentPageInfo = new PageInfo<>(commentsAll);
        return commentPageInfo;
    }

    @Override
    public void deleteCommentWithId(Integer id) {
        commentMapper.deleteCommentWithId(id);
    }

    @Override
    public Integer findCommentCount() {
        return commentMapper.findCommentCount();
    }

    @Override
    public List<Comment> selectNewComment() {
        return commentMapper.selectNewComment();
    }

    @Override
    public Integer findArticleCommentCount(Integer id) {
        return commentMapper.findArticleCommentCount(id);
    }

}
