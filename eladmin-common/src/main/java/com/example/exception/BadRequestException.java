package com.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
/**
 * Created with IntelliJ IDEA.
 * User: liaozhenglong
 * Date: 2020/1/26
 * Time: 16:29
 * Description: 统一一场处理
 */

@Getter
public class BadRequestException extends RuntimeException {

    private Integer status = BAD_REQUEST.value();

    public BadRequestException(String msg){
        super(msg);
    }

    public BadRequestException(HttpStatus status,String msg){
        super(msg);
        this.status = status.value();
    }
}
