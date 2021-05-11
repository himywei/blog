package com.wmy.blog.service;

import com.wmy.blog.pojo.Blog;
import com.wmy.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author wmy
 * @date 2021/4/30 20:50
 */
public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(Long id);

    Page<Blog> listBlogs(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlogs(Long tagId, Pageable pageable);

    Page<Blog> listBlogs(Pageable pageable);

    Page<Blog> listPublishedBlogs(Pageable pageable);


    Page<Blog> listBlogs(String query, Pageable pageable);

    List<Blog> listRecommendBlogsTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countPublishedBlogs();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);


}
