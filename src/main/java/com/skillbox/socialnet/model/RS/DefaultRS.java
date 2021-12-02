package com.skillbox.socialnet.model.RS;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.socialnet.model.dto.UserDTO;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Predicate;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultRS<T> {

    private String error = "string";
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("error_description")
    private String errorDesc;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer total;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer offset;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer perPage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

//    public static DefaultRS<?> of(List<?> data, Pageable pageable) {
//        DefaultRS<?> defaultRS = new DefaultRS<>();
//        defaultRS.setData(data);
//        return defaultRS;
//    }
//
//    public <E> DefaultRS<?> of(<? extends T> data) {
//        DefaultRS<?> defaultRS = new DefaultRS<>();
//        defaultRS.setData(data);
//        return defaultRS;
//    }
}
