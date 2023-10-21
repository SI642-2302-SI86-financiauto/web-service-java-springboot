package com.financiauto.webservice.shared.domain.service.communication.communication;

import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.util.Strings;

@Getter
@Setter
public class BaseResponse<T>{
    private boolean success;
    private String message;
    private T resource;

    public BaseResponse(String message){
        this.success = false;
        this.message = message;
        this.resource = null;
    }

    public BaseResponse(T resource){
        this.success = true;
        this.message = Strings.EMPTY;
        this.resource = resource;
    }
}
