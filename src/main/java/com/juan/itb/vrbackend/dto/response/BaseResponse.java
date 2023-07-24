package com.juan.itb.vrbackend.dto.response;

import static com.juan.itb.vrbackend.dto.Constant.SUCCESS;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse<T> {
  private boolean success;
  private String message;
  private String error;
  private T data;
  public static <T> BaseResponse<T> ok(T data) {
    return BaseResponse.<T>builder()
        .success(true)
        .message(SUCCESS)
        .data(data)
        .build();
  }
  public static <T> BaseResponse<T> error(String message) {
    return BaseResponse.<T>builder()
        .success(false)
        .message(message)
        .build();
  }

  public static <T> BaseResponse<T> error(String message, String error) {
    return BaseResponse.<T>builder()
        .success(false)
        .message(message)
        .error(error)
        .build();
  }
}
