package com.juan.itb.vrbackend.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateResponseRequest {

  @NotNull
  private Long userId;
  @NotNull
  private Long quizId;
  @NotNull
  private Long questionId;
  @NotNull
  private Long optionId;
}
