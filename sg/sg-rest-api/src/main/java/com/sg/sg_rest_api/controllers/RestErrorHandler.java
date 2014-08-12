/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sg.sg_rest_api.controllers;

import com.sg.dto.ErrorDto;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author tarasev
 */
@ControllerAdvice
public class RestErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestErrorHandler.class);
    
    //TODO: different http statuses for different exception classes
    //TODO: interceptor which log request totally
    //https://gist.github.com/calo81/2071634
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto processException(Exception ex) {        
        String refNumber = UUID.randomUUID().toString().toUpperCase();
        StringBuilder sb = new StringBuilder();
        sb.append("Exception refNumber: ");
        sb.append(refNumber);
        LOGGER.error(sb.toString(), ex);
        
        ErrorDto dto = new ErrorDto();
        dto.setError(ex.getMessage());
        dto.setRefNumber(refNumber);
        
//        StringWriter sw = new StringWriter();
//        PrintWriter pw = new PrintWriter(sw);
//        ex.printStackTrace(pw);
        
        return dto;
    }
}
