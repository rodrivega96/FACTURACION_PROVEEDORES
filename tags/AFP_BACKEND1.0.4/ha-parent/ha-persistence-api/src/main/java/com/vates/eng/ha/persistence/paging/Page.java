package com.vates.eng.ha.persistence.paging;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author 
 *
 * @param <T>
 */
@Data
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -8414337148492866764L;

    private int number;

    private int size;

    private int totalPages;

    private int numberOfElements;

    private long totalElements;

    private List<T> content;

}
