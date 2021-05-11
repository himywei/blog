package com.wmy.blog.dao;

import com.wmy.blog.pojo.Tag;
import com.wmy.blog.pojo.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author wmy
 * @date 2021/4/30 10:47
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Tag findByName(String name);

    /**
     * 前端查询 top 个 tag
     */
    @Query("select t from Tag t ")
    List<Tag> findTopTags(Pageable pageable);

}


