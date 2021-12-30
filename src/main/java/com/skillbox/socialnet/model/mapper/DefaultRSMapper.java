package com.skillbox.socialnet.model.mapper;

import com.skillbox.socialnet.model.RS.DefaultRS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Calendar;
import java.util.List;

public class DefaultRSMapper {

    public static <T> DefaultRS<T> of(T data) {
        DefaultRS<T> defaultRS = new DefaultRS<>();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(data);
        return defaultRS;
    }

    public static <T> DefaultRS<T> of(T data, Pageable pageable) {
        DefaultRS<T> defaultRS = new DefaultRS<>();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(data);
        return defaultRS;
    }

    public static DefaultRS<List<?>> of(List<?> data) {
        DefaultRS<List<?>> defaultRS = new DefaultRS<>();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setData(data);
        return defaultRS;
    }

    public static DefaultRS<List<?>> of(List<?> data, Page<?> page) {
        DefaultRS<List<?>> defaultRS = new DefaultRS<>();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setTotal(page.getTotalElements()); // TODO или page.getTotalElements()
        defaultRS.setOffset(page.getNumber());
        defaultRS.setPerPage(page.getSize());
        defaultRS.setData(data);
        return defaultRS;
    }

    public static DefaultRS<List<?>> of(List<?> data, Pageable pageable) {
        DefaultRS<List<?>> defaultRS = new DefaultRS<>();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setOffset(pageable.getPageNumber());
        defaultRS.setPerPage(pageable.getPageSize());
        defaultRS.setData(data);
        return defaultRS;
    }

    public static DefaultRS<?> error(String errorMessage) {
        DefaultRS<?> defaultRS = new DefaultRS<>();
        defaultRS.setError(errorMessage);
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        return defaultRS;
    }

    public static DefaultRS<List<?>> of(Page<?> page) {
        DefaultRS<List<?>> defaultRS = new DefaultRS<>();
        defaultRS.setError("string");
        defaultRS.setTimestamp(Calendar.getInstance().getTimeInMillis());
        defaultRS.setTotal(page.getTotalElements()); // TODO или page.getTotalElements()
        defaultRS.setOffset(page.getNumber());
        defaultRS.setPerPage(page.getSize());
        defaultRS.setData(page.getContent());
        return defaultRS;
    }

}
