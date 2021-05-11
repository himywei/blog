package com.wmy.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//Aop增强拦截
@ControllerAdvice
public class ControllerExceptionHandler {
    //导入日志信息
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //加上这个注解处理异常信息
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
        logger.error("Request URL : {}, Exception : {}", request.getRequestURL(), e); //记录异常信息的URL和info
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) { //不拦截指定状态的异常
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url", request.getRequestURL()); //前端页面获取URL
        mv.addObject("exception", e);//前端页面获取异常信息
        mv.setViewName("error/error"); //设置返回页面
        return mv;
    }
}
