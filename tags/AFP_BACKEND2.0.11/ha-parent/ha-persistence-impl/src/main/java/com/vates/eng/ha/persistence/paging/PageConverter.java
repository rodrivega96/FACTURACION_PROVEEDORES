package com.vates.eng.ha.persistence.paging;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author
 * 
 * @param <T>
 */
@Service
public class PageConverter<T> {

    public Page<T> convertFrom(org.springframework.data.domain.Page<T> page) {
        Page<T> newPage = new Page<T>();
        BeanUtils.copyProperties(page, newPage);

        return newPage;
    }

}
