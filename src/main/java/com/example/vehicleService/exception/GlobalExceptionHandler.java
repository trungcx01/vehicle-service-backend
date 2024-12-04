package com.example.vehicleService.exception;

import com.example.vehicleService.dto.ErrorDetail;
import com.example.vehicleService.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetail> handleBlogAPIException(BlogAPIException exception, WebRequest webRequest){
        ErrorDetail errorDetail = new ErrorDetail(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    /*Dùng @valid trước các đối tượng. TH đối tượng ko hợp lệ (valid), sẽ lưu chi tiết các lỗi vào BindingResult

    */

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
        ErrorDetail errorDetail = new ErrorDetail(new Date(), message, request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

//        @ExceptionHandler(MethodArgumentNotValidException.class)  // xử lý các TH validate ko thỏa mãn
//    public ResponseEntity<ErrorDetail> handleValidException(MethodArgumentNotValidException exception, WebRequest webRequest){
//        String message = exception.getBindingResult().getAllErrors().stream()
//                .map(e -> e.getDefaultMessage()).collect(Collectors.joining(", "));
//        ErrorDetail errorDetail = new ErrorDetail(new Date(), message, webRequest.getDescription(false));
//        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
//    }
}
