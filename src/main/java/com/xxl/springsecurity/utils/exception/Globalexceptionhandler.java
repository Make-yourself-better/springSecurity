package com.xxl.springsecurity.utils.exception;



import com.xxl.springsecurity.utils.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@ControllerAdvice
public class Globalexceptionhandler {

    //指定异常执行方法
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("方法执行ArithmeticException异常！");
    }

    //自定义的异常处理
    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }

    //JSR303校验统一异常出题类
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public R ValidationException(MethodArgumentNotValidException exception ){
        BindingResult bindingResult = exception.getBindingResult();
        HashMap<String, Object> map = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.stream().forEach(fieldError -> {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field,defaultMessage);
        });
        return R.error().data(map);
    }



}
