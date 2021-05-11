package com.wmy.blog.web.admin;

import com.wmy.blog.pojo.Tag;
import com.wmy.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;


/**
 * @author wmy
 * @date 2021/4/30 10:55
 */
@Controller
@RequestMapping("/admin")
public class AdminTagController {


    @Autowired
    private TagService tagService;

    /**
     * 后端查询所有标签
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC)
                               Pageable pageable, Model model) {
        model.addAttribute("page", tagService.listTags(pageable));
        return "admin/tags";
    }

    /**
     * 后端新增标签页面
     *
     * @param model
     * @return
     */
    @GetMapping("/tags/insert")
    public String insertTagPage(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tags-insert";
    }

    /**
     * 后端修改标签页面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/update")
    public String insertTagPage(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-insert";
    }


    /**
     * 后端新增标签
     *
     * @param tag
     * @param result
     * @param attributes
     * @return
     */

    @PostMapping("/tags")
    public String insertTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        System.out.println(tag.toString());
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "该标签已存在，请重新输入!");
        }
        if (result.hasErrors()) {
            return "admin/tags-insert";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增标签失败");
        } else {
            attributes.addFlashAttribute("message", "新增标签成功");
        }
        return "redirect:/admin/tags";
    }


    /**
     * 后端修改标签
     *
     * @param tag
     * @param result
     * @param id
     * @param attributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String updateTag(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Tag tag1 = tagService.getTagByName(tag.getName());
        if (tag1 != null) {
            result.rejectValue("name", "nameError", "该标签已存在，请重新输入!");
        }
        if (result.hasErrors()) {
            return "admin/tags-insert";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新标签失败");
        } else {
            attributes.addFlashAttribute("message", "更新标签成功");
        }
        return "redirect:/admin/tags";
    }

    /**
     * 后端删除标签
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "删除标签成功");
        return "redirect:/admin/tags";
    }

}
