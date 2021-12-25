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
    private String sort = DEFAULT_SORT_ITEM;

    @Override
    public int getPageNumber() {
        System.out.println("offset = " + offset);
        System.out.println("itemPerPage = " + itemPerPage);
        return offset / itemPerPage;
    }

    @Override
    public int getPageSize() {
        System.out.println("getPageSize() itemPerPage = " + itemPerPage);
        return itemPerPage;
    }

    @Override
    public long getOffset() {
        System.out.println("getOffset() offset = " + offset);
        return offset;
    }

    @Override
    public Sort getSort() {
        return Sort.by(sort);
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
