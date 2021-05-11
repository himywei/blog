package com.wmy.blog.web;

import com.wmy.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wmy
 * @date 2021/5/3 19:47
 */
@Controller
public class ArchiveController {

    @Autowired
    private BlogService blogService;

    /**
     * 前端归档界面
     *
     * @param model
     * @return
     */
    @GetMapping("/archives")
    public String archive(Model model) {
        model.addAttribute("archiveMap", blogService.archiveBlog());
        model.addAttribute("blogCount", blogService.countPublishedBlogs());
        return "archives";
    }
}
