package com.itheima.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itheima.mapper.ArticleMapper;
import com.itheima.mapper.CommentMapper;
import com.itheima.mapper.StatisticMapper;
import com.itheima.model.domain.Article;
import com.itheima.model.domain.Statistic;
import com.itheima.service.ArticleService;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articlesList = articleMapper.selectArticleWithPage();
        PageInfo<Article> pageInfo = new PageInfo<>(articlesList);
        return pageInfo;
    }

    @Override
    public Article selectArticleWithId(Integer id) {
        Article article = articleMapper.selectArticleWithId(id);
        return article;
    }


    // 统计前10的热度文章信息
    @Override
    public List<Article> getHeatArticles( ) {
        List<Statistic> list = statisticMapper.getStatistic();
        List<Article> articlelist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Article article = articleMapper.selectArticleWithId(list.get(i).getArticleId());
            article.setHits(list.get(i).getHits());
            article.setCommentsNum(list.get(i).getCommentsNum());
            articlelist.add(article);
            if(i>=9){
                break;
            }
        }
        return articlelist;
    }

    @Override
    public void publish(Article article) {
        //去表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        //将当前的时间设置为文章的发布时间
        article.setCreated(new Date());

//        发布一篇文章的同时，需要将该文章的点击量和评论量也设置出来
        article.setHits(0);
        article.setCommentsNum(0);
//        调用mapper层，插入article文章对象
        articleMapper.publishArticle(article);
    }

    @Override
    public void updateArticleWithId(Article article) {
        article.setModified(new Date());
        articleMapper.updateArticle(article);
    }

    @Override
    public void deleteArticleWithId(int id) {
        // 删除文章
        articleMapper.deleteArticleWithId(id);
    }

    @Override
    public Integer findArticleCount() {
        return articleMapper.findArticleCount();
    }

    @Override
    public List<Article> selectNewArticle() {
        return articleMapper.selectNewArticle();
    }

}
