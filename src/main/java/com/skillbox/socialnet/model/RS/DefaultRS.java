package com.skillbox.socialnet.model.RS;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultRS<DataStr> {

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
    private DataStr data;


}
