package com.wmy.blog.web;


import com.wmy.blog.pojo.Comment;
import com.wmy.blog.pojo.User;
import com.wmy.blog.service.BlogService;
import com.wmy.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    /**
     * 从application.yml配置文件注入游客评论对象
     */
    @Value("${comment.avatar}")
    private String avatar;

    /**
     * 前端根据博客id获取评论内容
     *
     * @param blogId
     * @param model
     * @return
     */
    @GetMapping("/comments/{blogId}")
    public String listComments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentsByBlogId(blogId));
        return "blog :: commentList";
    }

    /**
     * 前端新增评论
     *
     * @param comment
     * @param session
     * @return
     */
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session) {
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user"); //判断是否是管理员
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        } else {
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }


}
