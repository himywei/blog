package com.wmy.blog.service;

import com.wmy.blog.pojo.Tag;
import com.wmy.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wmy
 * @date 2021/4/30 10:41
 */
public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTags(Pageable pageable);

    List<Tag> listTags();

    List<Tag> listTags(String tagIds);

    List<Tag> listTagsTop(Integer size);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);

}
