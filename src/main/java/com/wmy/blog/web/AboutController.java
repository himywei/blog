package com.wmy.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wmy
 * @date 2021/5/3 20:14
 */
@Controller
public class AboutController {
    /**
     * 前端about界面
     *
     * @return
     */
    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }
}
