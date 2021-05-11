package com.wmy.blog.web;

import com.wmy.blog.service.BlogService;
import com.wmy.blog.service.TagService;
import com.wmy.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    /**
     * 前端index主页面
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(@PageableDefault(size = 6, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model) {
        model.addAttribute("page", blogService.listPublishedBlogs(pageable));
        model.addAttribute("types", typeService.listTypesTop(6));
        model.addAttribute("tags", tagService.listTagsTop(10));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogsTop(8));
        return "index";
    }

    /**
     * 前端根据博客title和content进行全局搜索
     *
     * @param pageable
     * @param query
     * @param model
     * @return
     */
    @PostMapping("/search")
    public String globalSearch(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                               @RequestParam String query, Model model) {
        model.addAttribute("page", blogService.listBlogs("%" + query + "%", pageable));
        model.addAttribute("query", query);
        return "search";
    }

    /**
     * 前端展示博客详情
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/blogs/{id}")
    public String blogDetail(@PathVariable Long id, Model model) {
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    /**
     * footer底部显示最新3条博客
     *
     * @return
     */
    @GetMapping("/footer/newblogs")
    public String newblogs(Model model) {
        model.addAttribute("newblogs", blogService.listRecommendBlogsTop(3));
        return "_fragments :: newblogList";
    }


}
