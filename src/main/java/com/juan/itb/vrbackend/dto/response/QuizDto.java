package com.juan.itb.vrbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuizDto {

  private Long id;
  private String quizName;
  private String description;
  private Instant createdAt;
  private List<QuestionDto> questions;
}
