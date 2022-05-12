package com.itheima.web.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.model.domain.Article;
import com.itheima.model.domain.Comment;
import com.itheima.service.ArticleService;
import com.itheima.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private ArticleService articleServiceImpl;

    @Autowired
    private CommentService commentServiceImpl;


    @GetMapping(value = "/")
    private String index(HttpServletRequest request) {
        return this.index(request, 1, 5);
    }

    //两种传参风格：restFul风格：将参数作为路径的一部分，所以需要使用注解PathVariable
    //web开发的附带风格：路径后面：参数名=参数值，RequestParam使用这个注解接收这个参数
    @GetMapping(value = "/page/{p}")
    public String index(HttpServletRequest request, @PathVariable("p") int page, @RequestParam(value = "count", defaultValue = "5") int count) {

        PageInfo<Article> articles = articleServiceImpl.selectArticleWithPage(page, count);
        // 获取文章热度统计信息
        List<Article> articleList = articleServiceImpl.getHeatArticles();
        request.setAttribute("articles", articles);
        request.setAttribute("articleList", articleList);

        return "client/index";
    }
    //处理通过标题进入博客的详细的页面请求
    @GetMapping(value = "/article/{id}")
    public String getArticleById(@PathVariable("id")Integer id,HttpServletRequest request){
        //调用service层的代码
        Article article = articleServiceImpl.selectArticleWithId(id);
        if(article!=null){
            // 查询封装评论相关数据
            getArticleComments(request, article);
            request.setAttribute("article",article);
            return "client/articleDetails";
        }else {
            logger.warn("查询文章详情结果为空，查询文章id: "+id);
            // 未找到对应文章页面，跳转到提示页
            return "comm/error_404";
        }
    }

    // 查询文章的评论信息，并补充到文章详情里面
    private void getArticleComments(HttpServletRequest request, Article article) {
        if (article.getAllowComment()) {
            // cp表示评论页码，commentPage
            String cp = request.getParameter("cp");
            cp = StringUtils.isBlank(cp) ? "1" : cp;
            request.setAttribute("cp", cp);
            PageInfo<Comment> comments = commentServiceImpl.getComments(article.getId(),Integer.parseInt(cp),3);
            request.setAttribute("cp", cp);
            request.setAttribute("comments", comments);
        }
    }
}
