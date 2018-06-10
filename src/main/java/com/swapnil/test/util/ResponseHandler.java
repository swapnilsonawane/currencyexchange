package com.swapnil.test.util;

import com.swapnil.test.dto.Header;
import com.swapnil.test.dto.ResponseDto;
import com.swapnil.test.dto.Status;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class ResponseHandler {

    private static final  String SUCCESS = "success";

    private static final  String ERROR = "error";

    public ResponseDto getSuccessResponse(Object body){
        ResponseDto<Object> responseDto = new ResponseDto<Object>();
        Header header = new Header();
        header.setCurrentDate(new Date());
        header.setUuid(UUID.randomUUID().toString());
        responseDto.setHeader(header);
        responseDto.setBody(body);

        Status status = new Status();
        status.setStatus(SUCCESS);
        responseDto.setStatus(status);
        return responseDto;
    }

    public ResponseDto getErrorResponse(String errorDesc){
        ResponseDto<Object> responseDto = new ResponseDto<Object>();
        Header header = new Header();
        header.setCurrentDate(new Date());
        header.setUuid(UUID.randomUUID().toString());
        Status status = new Status();
        status.setStatus(ERROR);
        status.setErrorDesc(errorDesc);
        responseDto.setStatus(status);
        return responseDto;
    }

}
