package com.wmy.blog.web.admin;

import com.wmy.blog.pojo.User;
import com.wmy.blog.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author wmy
 * @date 2021/4/30 9:08
 */
@Controller
@RequestMapping("/admin")
public class AdminLoginController {

    @Autowired
    private UserService userService;

    /**
     * 后端跳转登录界面
     *
     * @return
     */
    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 后端登录
     *
     * @param username
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(username, password);
        if (user != null) {
            User sessionUser = new User(); //为了防止清空密码的时候修改数据库的值，这里新建一个user对象，将密码置空再存进session
            BeanUtils.copyProperties(user, sessionUser);
            sessionUser.setPassword(null); //
            session.setAttribute("user", sessionUser);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误!");
            return "redirect:/admin";
        }
    }

    /**
     * 后端登出
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "admin/login";
    }
}
