package com.itheima.mapper;

import com.itheima.model.domain.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //查询所有文章并根据文章的id进行降序排序
    @Select("select * from t_article order by id desc")
    public List<Article> selectArticleWithPage();

    //根据文章id查询文章信息
    @Select("SELECT * FROM t_article WHERE id=#{id}")
    public Article selectArticleWithId(Integer id);

    //定义发布文章的方法
    @Insert("INSERT INTO t_article (title,created,modified,tags,categories," +
            " allow_comment, thumbnail, content)" +
            " VALUES (#{title},#{created}, #{modified}, #{tags}, #{categories}," +
            " #{allowComment}, #{thumbnail}, #{content})")
    @Options(useGeneratedKeys = true,keyColumn = "id",keyProperty = "id")
    public Integer publishArticle(Article article);

    //定义方法修改文章
    public Integer updateArticle(Article article);

    // 通过id删除文章
    @Delete("DELETE FROM t_article WHERE id=#{id}")
    public void deleteArticleWithId(int id);

    //查询表中有多少条数据
    @Select("SELECT COUNT(id) FROM t_article")
    public Integer findArticleCount();

    // 后台查询最新几条文章
    @Select("SELECT * FROM t_article ORDER BY id DESC LIMIT 4")
    public List<Article> selectNewArticle();


}
