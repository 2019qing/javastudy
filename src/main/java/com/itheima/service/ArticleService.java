package com.itheima.service;

import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Article;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ArticleService {
    //分页查询文章列表 page：页数  count：每页的记录数
    public PageInfo<Article> selectArticleWithPage(Integer page,Integer count);

    //定义根据文章id查询文章详细信息的方法
    public Article selectArticleWithId(Integer id);

    public List<Article> getHeatArticles();

    //定义发布文章的方法
    public void publish(Article article);

    //定义方法修改文章
    public void updateArticleWithId(Article article);

    // 根据主键删除文章
    public void deleteArticleWithId(int id);

    //查询表中有多少条数据
    public Integer findArticleCount();

    // 后台查询最新几条文章
    public List<Article> selectNewArticle();
}
