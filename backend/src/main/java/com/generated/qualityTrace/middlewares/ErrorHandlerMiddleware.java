package com.generated.qualityTrace.middlewares;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.generated.qualityTrace.utils.ServiceException;

@RestControllerAdvice
public class ErrorHandlerMiddleware {
  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<Map<String, Object>> handleService(ServiceException e) {
    HttpStatus status = e.getCode().endsWith("NOT_FOUND") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(status).body(Map.of("code", e.getCode(), "message", e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleOther(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(Map.of("code", "INTERNAL_ERROR", "message", String.valueOf(e.getMessage())));
  }
}
