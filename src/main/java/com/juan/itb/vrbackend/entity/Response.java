package com.juan.itb.vrbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juan.itb.vrbackend.dto.enums.ResponseStatus;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table("responses")
public class Response {

  @Column("user_id")
  private Long userId;

  @Column("quiz_id")
  private Long quizId;

  @Column("question_id")
  private Long questionId;

  @Column("option_id")
  private Long optionId;

  @Column("response_time")
  private Instant responseTime;

  @Default
  @Column("status")
  private ResponseStatus status = ResponseStatus.SAVED;
}