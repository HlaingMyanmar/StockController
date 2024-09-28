package org.DAO;


import org.models.Brand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface DataAccessObject<T> {

    List<T> getAllList();

    Brand getBrandById(String id,String name);

    int getDeleteById(String id);

    @Transactional()
    int insert(T t);


    @Transactional
    int update(T t);

    int [] updateBatch(List<T>t);

    @Transactional
    int [] insertBatch(List<T> t);


}
