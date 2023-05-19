package com.project.ElectronicStore.exception;

import com.project.ElectronicStore.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

      private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

      //handle resource not found exception
      @ExceptionHandler(ResourceNotFoundException.class)
      public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

            logger.info("Exception Handler invoked");
            ApiResponseMessage response = ApiResponseMessage.builder()
                    .message(ex.getMessage())
                    .status(HttpStatus.NOT_FOUND)
                    .success(true).build();

            return new ResponseEntity(response, HttpStatus.NOT_FOUND);

      }

      //MethodArgumentNotValidException
      @ExceptionHandler(MethodArgumentNotValidException.class)
      public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
            List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
            Map<String, String> response = new HashMap<>();
            allErrors.stream().forEach(objectError -> {
                  String message = objectError.getDefaultMessage();
                  String field = ((FieldError)objectError).getField();
                  response.put(field, message);
            });

            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
      }

}
