package com.gelitix.backend.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
@ToString
public class Response<T> {
    private int statusCode;
    private String message;
    private boolean success;
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer pageNumber;

    public Response(int statusCode, String message,T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.success = statusCode == HttpStatus.OK.value();
        this.data = data;

    }

    public Response(int statusCode, String message, T data, int pageNumber) {
        this.statusCode = statusCode;
        this.message = message;
        this.success = statusCode == HttpStatus.OK.value();
        this.data = data;
        this.pageNumber = pageNumber;
    }

    public static <T> ResponseEntity<Response<T>> failed(String message) {
        return failed(HttpStatus.BAD_REQUEST.value(), message, null);
    }

    public static <T> ResponseEntity<Response<T>> failed(T data) {
        return failed(HttpStatus.BAD_REQUEST.value(), "Bad request", data);
    }

    public static <T> ResponseEntity<Response<T>> failed(int statusCode, String message) {
        return failed(statusCode, message, null);
    }

    public static <T> ResponseEntity<Response<T>> failed(int statusCode, String message, T data) {
        Response<T> response = new Response<>(statusCode, message, data);
        response.setSuccess(false);
        return ResponseEntity.status(statusCode).body(response);
    }

    public static <T> ResponseEntity<Response<T>> success(String message, T data) {
        return success(HttpStatus.OK.value(), message, data);
    }

    public static <T> ResponseEntity<Response<T>> success(String message) {
        return success(HttpStatus.OK.value(), message, null);
    }

    public static <T> ResponseEntity<Response<T>> success(int statusCode, String message, T data) {
        Response<T> response = new Response<>(statusCode, message, data);
        response.setSuccess(true);
        return ResponseEntity.status(statusCode).body(response);
    }

    public static <T> ResponseEntity<Response<T>> success(int statusCode, String message, T data, Integer pageNumber) {
        Response<T> response = new Response<>(statusCode, message, data, pageNumber);
        response.setSuccess(true);
        return ResponseEntity.status(statusCode).body(response);
    }
}
