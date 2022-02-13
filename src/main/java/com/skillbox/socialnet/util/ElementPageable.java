package com.skillbox.socialnet.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElementPageable implements Pageable {

    private static final int DEFAULT_ITEM_PER_PAGE = 20;
    private static final String DEFAULT_SORT_ITEM = "id";

    private int itemPerPage = DEFAULT_ITEM_PER_PAGE;
    private int offset;
    private Sort sort = Sort.by(DEFAULT_SORT_ITEM);

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    @Override
    public int getPageNumber() {
        return offset / itemPerPage;
    }

    @Override
    public int getPageSize() {
        return itemPerPage;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return null;
    }

    @Override
    public Pageable previousOrFirst() {
        return null;
    }

    @Override
    public Pageable first() {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

}
