package com.juan.itb.vrbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table("options")
public class Option {

  @Id
  private Long id;

  @Column("question_id")
  private Long questionId;

  @Column("option_text")
  private String optionText;

  @Column("is_correct")
  private Boolean isCorrect;
}