package com.itheima.web.admin;

import com.github.pagehelper.PageInfo;
import com.itheima.model.ResponseData.ArticleResponseData;
import com.itheima.model.ResponseData.StaticticsBo;
import com.itheima.model.domain.Article;
import com.itheima.model.domain.Comment;
import com.itheima.service.ArticleService;
import com.itheima.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ArticleService articleServiceImpl;

    @Autowired
    private CommentService commentServiceImpl;

    @RequestMapping(value = {"", "/index"})
    public String adminIndex(HttpServletRequest request){
        Integer articleCount = articleServiceImpl.findArticleCount();
        Integer commentCount = commentServiceImpl.findCommentCount();

        List<Article> articleList = articleServiceImpl.selectNewArticle();
        List<Comment> commentList = commentServiceImpl.selectNewComment();

        for (Article article : articleList) {
            article.setCommentsNum(commentServiceImpl.findArticleCommentCount(article.getId()));
        }
//        Integer articleCommentCount1 = commentServiceImpl.findArticleCommentCount(articleList.get(0).getId());
//        Integer articleCommentCount2 = commentServiceImpl.findArticleCommentCount(articleList.get(1).getId());
//        Integer articleCommentCount3 = commentServiceImpl.findArticleCommentCount(articleList.get(2).getId());
//        Integer articleCommentCount4 = commentServiceImpl.findArticleCommentCount(articleList.get(3).getId());
//
//        articleList.get(0).setCommentsNum(articleCommentCount1);
//        articleList.get(1).setCommentsNum(articleCommentCount2);
//        articleList.get(2).setCommentsNum(articleCommentCount3);
//        articleList.get(3).setCommentsNum(articleCommentCount4);

        System.out.println(articleList);
        request.setAttribute("articleList",articleList);
        request.setAttribute("commentList",commentList);
        request.setAttribute("articleCount",articleCount);
        request.setAttribute("commentCount",commentCount);
        return "back/index";
    }


//    @PostMapping(value = "/article/queryCountByArticleId")
//    public String queryCountByArticleId(@RequestParam int id ,HttpServletRequest request) {
//        Integer articleCommentCount = commentServiceImpl.findArticleCommentCount(articleList.get(1).getId());
//        System.out.println(articleCommentCount);
//        request.setAttribute("articleCommentCount",articleCommentCount);
//        return "back/index";
//    }

    //定义方法处理，发布文章的请求
    @RequestMapping(value="/article/toEditPage")
    public String topublishArticle(){
        return "back/article_edit";
    }

    //定义方法处理，文章管理的请求
    @RequestMapping(value = "/article")
    public String backArcitle(@RequestParam(value = "page",defaultValue = "1") int page,
                              @RequestParam(value = "count",defaultValue = "10") int count, HttpServletRequest request){
        PageInfo<Article> pageInfo = articleServiceImpl.selectArticleWithPage(page, count);
//        System.out.println(pageInfo);
        request.setAttribute("articles",pageInfo);
        return "back/article_list";
    }

    //定义方法处理发布文章的请求
    @PostMapping(value = "/article/publish")
    @ResponseBody
    public ArticleResponseData publicArticle(Article article){
        if(StringUtils.isBlank(article.getCategories())){
            article.setCategories("默认分类");
        }
        try{
            articleServiceImpl.publish(article);
            return ArticleResponseData.ok();
        }catch (Exception e){
            return ArticleResponseData.fail();
        }
    }

    //定义方法处理修改文章的请求
    @PostMapping(value = "/article/modify")
    @ResponseBody
    public ArticleResponseData modifyArticle(Article article){

        try{
            articleServiceImpl.updateArticleWithId(article);
            return ArticleResponseData.ok();
        }catch (Exception e){
            System.out.println(e);
            return ArticleResponseData.fail();

        }
    }

    //定义方法，处理文章编辑请求
    @GetMapping(value = "/article/{id}")
    public String editArticleById(@PathVariable("id")Integer id,HttpServletRequest request){
        //调用service层的代码
        Article article = articleServiceImpl.selectArticleWithId(id);
        request.setAttribute("contents",article);

        return "back/article_edit";
    }

    // 文章删除
    @PostMapping(value = "/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id) {
        try {
            articleServiceImpl.deleteArticleWithId(id);
            logger.info("文章删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("文章删除失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //评论管理
    @RequestMapping(value = "/comment")
    public String indexComment(@RequestParam(value = "page",defaultValue = "1") int page,
                               @RequestParam(value = "count",defaultValue = "10") int count,
                               HttpServletRequest request){
        PageInfo<Comment> pageInfo = commentServiceImpl.getCommentsAll(page, count);
//        System.out.println(pageInfo);
        request.setAttribute("comments",pageInfo);
        return "back/comment_list";
    }

    //删除评论
    @RequestMapping(value = "/deleteComment")
    @ResponseBody
    public ArticleResponseData deleteComment(@RequestParam int id) {
        System.out.println(id);
        try {
            commentServiceImpl.deleteCommentWithId(id);
            logger.info("评论删除成功");
            return ArticleResponseData.ok();
        } catch (Exception e) {
            logger.error("评论删除失败，错误信息: "+e.getMessage());
            return ArticleResponseData.fail();
        }
    }
}
