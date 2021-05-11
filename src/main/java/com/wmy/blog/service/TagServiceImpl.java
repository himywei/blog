package com.wmy.blog.service;

import com.wmy.blog.NotFoundException;
import com.wmy.blog.dao.BlogRepository;
import com.wmy.blog.dao.TagRepository;
import com.wmy.blog.pojo.Blog;
import com.wmy.blog.pojo.Tag;
import com.wmy.blog.pojo.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wmy
 * @date 2021/4/30 10:46
 */
@Service
public class TagServiceImpl implements TagService {


    @Autowired
    TagRepository tagRepository;
    @Autowired
    BlogRepository blogRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTags(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTags(String ids) {

        return tagRepository.findAllById(convertToList(ids));
    }


    /**
     * 前端查询top个tag
     *
     * @param size
     * @return
     */
    @Override
    public List<Tag> listTagsTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTopTags(pageable);
    }

    /**
     * 将字符串转换为数组
     * eg:"1,2,3" -> [1,2,3]
     *
     * @param ids
     * @return
     */
    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在该标签");
        }
        BeanUtils.copyProperties(tag, t); //更新t
        return tagRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.getOne(id);
        //由于tag是tag-blog多对多关系中的被维护端，删除tag时需要手动删除tag-blog中间表中的相关记录
        for (Blog blog : tag.getBlogs()) {
            blog.getTags().remove(tag);
        }
        blogRepository.saveAll(tag.getBlogs()); //更新blog到数据库
        tagRepository.deleteById(id);
    }
}
