package com.wmy.blog.service;

import com.wmy.blog.pojo.Comment;

import java.util.List;

/**
 * @author wmy
 * @date 2021/5/3 15:47
 */
public interface CommentService {

    List<Comment> listCommentsByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
