package com.vates.facpro.persistence.paging;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * It implements a conversion from Spring Data JPA ({@link org.springframework.data.domain.Page}) page of entities to a custom page of entities (See
 * {@link com.vates.facpro.persistence.paging.Page}).
 * 
 * @author Gaston Napoli
 * 
 * @param <T>
 *            Datatype of entities stored in the page.
 */
@Service
public class PageConverter<T> {

    /**
     * It converts a {@link org.springframework.data.domain.Page} instance to a {@link com.vates.facpro.persistence.paging.Page} instance.
     * 
     * @param page
     *            page to be converted.
     * @return a new converted page.
     */
    public Page<T> convertFrom(org.springframework.data.domain.Page<T> page) {

        Page<T> newPage = new Page<T>();

        BeanUtils.copyProperties(page, newPage);

        return newPage;

    }

}
