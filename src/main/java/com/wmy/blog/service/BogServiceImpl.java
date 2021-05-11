package com.wmy.blog.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.wmy.blog.NotFoundException;
import com.wmy.blog.dao.BlogRepository;
import com.wmy.blog.pojo.Blog;
import com.wmy.blog.pojo.Type;
import com.wmy.blog.util.MarkdownUtils;
import com.wmy.blog.util.MyBeanUtils;
import com.wmy.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author wmy
 * @date 2021/4/30 20:54
 */
@Service
public class BogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;

    /**
     * 获取单个blog
     *
     * @param id
     * @return
     */
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }


    /**
     * 前端MarkDown转HTML
     *
     * @param id
     * @return
     */
    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = blog.getContent();
        b.setContent(MarkdownUtils.markdownToHtml(content));
        blogRepository.updateViews(id);//博客访问量+1
        return b;
    }


    /**
     * 后端条件查询博客
     */
    @Override
    public Page<Blog> listBlogs(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    /**
     * 前端根据标签查询博客
     */
    @Override
    public Page<Blog> listBlogs(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    /**
     * 后端分页查询所有博客
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    /**
     * 前端分页查询发布的博客
     *
     * @param pageable
     * @return
     */
    @Override
    public Page<Blog> listPublishedBlogs(Pageable pageable) {
        return blogRepository.listPublishedBlogs(pageable);
    }

    /**
     * 前端分页全局搜索发布的blogs
     *
     * @param pageable
     * @param query
     * @return
     */
    @Override
    public Page<Blog> listBlogs(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    /**
     * 前端推荐博客
     *
     * @param size
     * @return
     */
    @Override
    public List<Blog> listRecommendBlogsTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTopBlogs(pageable);
    }

    /**
     * 前端博客归档
     *
     * @return
     */
    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYear();
        Map<String, List<Blog>> map = new LinkedHashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    /**
     * 前端统计已发布博客的数目
     *
     * @return
     */
    @Override
    public Long countPublishedBlogs() {

        return blogRepository.countBlogsByPublished(true);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) { //新增
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        } else {

            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if (b == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
