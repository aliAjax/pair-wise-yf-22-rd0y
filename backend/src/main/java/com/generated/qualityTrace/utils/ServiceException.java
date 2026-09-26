package com.generated.qualityTrace.utils;

public class ServiceException extends RuntimeException {
  private final String code;

  public ServiceException(String code, String message) {
    super(message);
    this.code = code;
  }

  public String getCode() { return code; }
}
