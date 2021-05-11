package com.wmy.blog.web.admin;

import com.wmy.blog.pojo.Type;
import com.wmy.blog.service.TypeService;
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
public class AdminTypeController {

    @Autowired
    private TypeService typeService;

    /**
     * 后端查找所有分类
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String listTypes(@PageableDefault(size = 6, sort = {"id"}, direction = Sort.Direction.DESC)
                                    Pageable pageable, Model model) {
        model.addAttribute("page", typeService.listTypes(pageable));
        return "admin/types";
    }


    /**
     * 后端新增分类界面
     *
     * @param model
     * @return
     */
    @GetMapping("/types/insert")
    public String insertTypePage(Model model) {
        model.addAttribute("type", new Type());
        return "admin/types-insert";
    }

    /**
     * 后端修改分类界面
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/update")
    public String updateTypePage(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getType(id));
        return "admin/types-insert";
    }

    /**
     * 后端新增分类
     *
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String updateType(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "该分类已存在，请重新输入！");
        }
        if (result.hasErrors()) {
            return "admin/types-insert";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增分类失败");
        } else {
            attributes.addFlashAttribute("message", "新增分类成功");
        }
        return "redirect:/admin/types";
    }

    /**
     * 后端修改分类
     *
     * @param type
     * @param result
     * @param id
     * @param attributes
     * @return
     */

    @PostMapping("/types/{id}")
    public String updateType(@Valid Type type, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Type type1 = typeService.getTypeByName(type.getName());
        if (type1 != null) {
            result.rejectValue("name", "nameError", "该分类已存在，请重新输入！");
        }
        if (result.hasErrors()) {
            return "admin/types-insert";
        }
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "更新分类失败");
        } else {
            attributes.addFlashAttribute("message", "更新分类成功");
        }
        return "redirect:/admin/types";
    }

    /**
     * 后端删除分类
     *
     * @param id
     * @param attributes
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "删除分类成功");
        return "redirect:/admin/types";
    }

}
