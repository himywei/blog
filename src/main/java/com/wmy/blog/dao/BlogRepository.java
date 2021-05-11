package com.wmy.blog.dao;

import com.wmy.blog.pojo.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wmy
 * @date 2021/4/30 20:54
 */

public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {
    //JpaSpecificationExecutor实现了动态组合查询方法

    /**
     * 前端查询 top 个 blog
     */
    @Query("select b from Blog b where b.recommend = true ")
    List<Blog> findTopBlogs(Pageable pageable);

    /**
     * 前端分页查询发布的博客
     */
    @Query("select b from Blog b")
    Page<Blog> listPublishedBlogs(Pageable pageable);


    /**
     * 前端全局搜索已发布的博客
     *
     * @param query
     * @param pageable
     * @return
     */
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1 ")
    Page<Blog> findByQuery(String query, Pageable pageable);

    /**
     * 前端修改博客访问量
     *
     * @param id
     * @return
     */
    @Modifying
    @Query("update Blog b set b.views=b.views+1 where b.id =?1")
    int updateViews(Long id);

    /**
     * 前端归档已发布的博客
     *
     * @return
     */
    @Query("select function('date_format',b.updateTime,'%Y') as year from Blog b group by function('date_format',b.updateTime,'%Y') order by function('date_format',b.updateTime,'%Y') desc ")
    List<String> findGroupYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1")
    List<Blog> findByYear(String year);

    /**
     * 前端查询发布博客的数量
     *
     * @param published
     * @return
     */
    Long countBlogsByPublished(Boolean published);
}
