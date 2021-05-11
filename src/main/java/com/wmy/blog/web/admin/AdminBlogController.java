package com.wmy.blog.web.admin;

import com.wmy.blog.pojo.Blog;
import com.wmy.blog.pojo.User;
import com.wmy.blog.service.BlogService;
import com.wmy.blog.service.TagService;
import com.wmy.blog.service.TypeService;
import com.wmy.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author wmy
 * @date 2021/4/30 9:57
 */
@Controller
@RequestMapping("admin")
public class AdminBlogController {

    private static final String INSERT = "admin/blogs-insert";
    private static final String LIST_BLOGS = "admin/blogs";
    private static final String REDIRECT_LIST_BLOGS = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 后端分页查询所有blogs
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public String listBlogs(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                            BlogQuery blog, Model model) {
        model.addAttribute("types", typeService.listTypes());
        model.addAttribute("page", blogService.listBlogs(pageable, blog));
        return LIST_BLOGS;
    }

    /**
     * 后端条件查询blogs
     *
     * @param pageable
     * @param blog
     * @param model
     * @return
     */
    @PostMapping("/blogs/search")
    public String searchBlogs(@PageableDefault(size = 4, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                              BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlogs(pageable, blog));
        return "admin/blogs :: blogList";
    }

    /**
     * 后端跳转添加博客页面
     *
     * @param model
     * @return
     */
    @GetMapping("/blogs/insert")
    public String insertBlogPage(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INSERT;
    }

    /**
     * 后端修改博客页面
     *
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}/update")
    public String updateBlogPage(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init(); //初始化tagIds
        model.addAttribute("blog", blog);
        return INSERT;
    }

    /**
     * 向页面传递所有types和tags
     *
     * @param model
     */
    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listTypes());
        model.addAttribute("tags", tagService.listTags());
    }

    /**
     * 后端新增博客
     *
     * @param blog
     * @param attributes
     * @param session
     * @return
     */
    @PostMapping("/blogs")
    public String insertBlog(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTags(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);

        }
        if (b == null) {
            attributes.addFlashAttribute("message", "新增博客失败");
        } else {
            attributes.addFlashAttribute("message", "新增博客成功");
        }
        return REDIRECT_LIST_BLOGS;
    }

    /**
     * 后端删除博客
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Long id, RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除博客成功");
        return REDIRECT_LIST_BLOGS;
    }
}
