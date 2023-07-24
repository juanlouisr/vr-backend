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
@Table("questions")
public class Question {

  @Id
  private Long id;

  @Column("quiz_id")
  private Long quizId;

  @Column("question_text")
  private String questionText;

//  @MappedCollection(keyColumn = "question_id", idColumn = "question_id")
//  private List<Option> options;
}
