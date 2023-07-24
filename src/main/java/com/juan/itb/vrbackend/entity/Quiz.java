package com.juan.itb.vrbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table("quizzes")
public class Quiz {

  @Id
  private Long id;

  @Column("quiz_name")
  private String quizName;

  @Column("description")
  private String description;

  @Column("created_at")
  @Default
  private Instant createdAt = Instant.now();
//
//  @MappedCollection(keyColumn = "quiz_id", idColumn = "quiz_id")
//  private List<Question> questions;
}