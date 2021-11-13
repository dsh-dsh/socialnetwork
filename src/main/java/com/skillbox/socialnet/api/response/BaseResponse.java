package com.skillbox.socialnet.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<DataStr> {

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

    public void setError() {
        this.error = "invalid_request";
    }
}
