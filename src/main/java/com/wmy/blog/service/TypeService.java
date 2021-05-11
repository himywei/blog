package com.wmy.blog.service;

import com.wmy.blog.pojo.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author wmy
 * @date 2021/4/30 10:41
 */
public interface TypeService {
    Type saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    Page<Type> listTypes(Pageable pageable);

    List<Type> listTypes();

    List<Type> listTypesTop(Integer size);

    Type updateType(Long id, Type type);

    void deleteType(Long id);


}
