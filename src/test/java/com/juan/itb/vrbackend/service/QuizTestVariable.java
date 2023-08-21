package com.juan.itb.vrbackend.service;

import com.juan.itb.vrbackend.dto.enums.ResponseStatus;
import com.juan.itb.vrbackend.dto.request.CreateQuizRequest;
import com.juan.itb.vrbackend.dto.request.CreateResponseRequest;
import com.juan.itb.vrbackend.dto.request.FinalizeResponseRequest;
import com.juan.itb.vrbackend.dto.request.OptionRequest;
import com.juan.itb.vrbackend.dto.request.QuestionRequest;
import com.juan.itb.vrbackend.dto.response.OptionResponse;
import com.juan.itb.vrbackend.dto.response.QuestionDto;
import com.juan.itb.vrbackend.dto.response.QuizDto;
import com.juan.itb.vrbackend.entity.Option;
import com.juan.itb.vrbackend.entity.Question;
import com.juan.itb.vrbackend.entity.Quiz;
import com.juan.itb.vrbackend.entity.Response;
import java.time.Instant;
import java.util.Arrays;

public class QuizTestVariable {

  public static final Instant NOW = Instant.now();

  public static final Quiz QUIZ = Quiz.builder()
      .id(1L)
      .quizName("Sample Quiz")
      .description("desc")
      .createdAt(NOW)
      .build();

  public static final Question QUESTION_1 = Question.builder()
      .id(1L)
      .quizId(1L)
      .questionText("2 + 2 =")
      .build();

  public static final Question QUESTION_2 = Question.builder()
      .id(2L)
      .quizId(1L)
      .questionText("Capital of france")
      .build();

  public static final Option OPTION_1_1 = Option.builder()
      .questionId(1L)
      .id(1L)
      .optionText("1")
      .isCorrect(false)
      .build();

  public static final Option OPTION_1_2 = Option.builder()
      .questionId(1L)
      .id(2L)
      .optionText("4")
      .isCorrect(true)
      .build();

  public static final Option OPTION_1_3 = Option.builder()
      .questionId(1L)
      .id(3L)
      .optionText("8")
      .isCorrect(false)
      .build();

  public static final Option OPTION_2_1 = Option.builder()
      .questionId(2L)
      .id(4L)
      .optionText("jakarta")
      .isCorrect(false)
      .build();

  public static final Option OPTION_2_2 = Option.builder()
      .questionId(2L)
      .id(5L)
      .optionText("beijing")
      .isCorrect(false)
      .build();

  public static final Option OPTION_2_3 = Option.builder()
      .questionId(2L)
      .id(6L)
      .optionText("paris")
      .isCorrect(true)
      .build();

  public static final CreateQuizRequest CREATE_QUIZ_REQUEST = CreateQuizRequest.builder()
      .name("Sample Quiz")
      .description("desc")
      .questions(Arrays.asList(
          QuestionRequest.builder()
              .questionText("2 + 2 =")
              .options(
                  Arrays.asList(
                      OptionRequest.builder()
                          .optionText("1")
                          .isCorrect(false)
                          .build(),
                      OptionRequest.builder()
                          .optionText("4")
                          .isCorrect(true)
                          .build(),
                      OptionRequest.builder()
                          .optionText("8")
                          .isCorrect(false)
                          .build()
                  )
              )
              .build(),
          QuestionRequest.builder()
              .questionText("Capital of france")
              .options(
                  Arrays.asList(
                      OptionRequest.builder()
                          .optionText("jakarta")
                          .isCorrect(false)
                          .build(),
                      OptionRequest.builder()
                          .optionText("beijing")
                          .isCorrect(false)
                          .build(),
                      OptionRequest.builder()
                          .optionText("paris")
                          .isCorrect(true)
                          .build()
                  )
              )
              .build()
      ))
      .build();

  public static final QuizDto QUIZ_RESPONSE = QuizDto.builder()
      .id(1L)
      .quizName("Sample Quiz")
      .description("desc")
      .createdAt(NOW)
      .questions(Arrays.asList(
          QuestionDto.builder()
              .id(1L)
              .quizId(1L)
              .questionText("2 + 2 =")
              .options(
                  Arrays.asList(
                      OptionResponse.builder()
                          .id(1L)
                          .questionId(1L)
                          .optionText("1")
                          .build(),
                      OptionResponse.builder()
                          .id(2L)
                          .questionId(1L)
                          .optionText("4")
                          .build(),
                      OptionResponse.builder()
                          .id(3L)
                          .questionId(1L)
                          .optionText("8")
                          .build()
                  )
              )
              .build(),
          QuestionDto.builder()
              .id(2L)
              .quizId(1L)
              .questionText("Capital of france")
              .options(
                  Arrays.asList(
                      OptionResponse.builder()
                          .id(4L)
                          .questionId(2L)
                          .optionText("jakarta")
                          .build(),
                      OptionResponse.builder()
                          .id(5L)
                          .questionId(2L)
                          .optionText("beijing")
                          .build(),
                      OptionResponse.builder()
                          .id(6L)
                          .questionId(2L)
                          .optionText("paris")
                          .build()
                  )
              )
              .build()
      ))
      .build();

  public static final CreateResponseRequest CREATE_RESPONSE_REQUEST = CreateResponseRequest.builder()
      .userId(1L)
      .quizId(1L)
      .questionId(1L)
      .optionId(2L)
      .build();

  public static final Response RESPONSE_OLD = Response.builder()
      .userId(1L)
      .quizId(1L)
      .questionId(1L)
      .optionId(1L)
      .build();

  public static final Response RESPONSE_NEW = Response.builder()
      .userId(1L)
      .quizId(1L)
      .questionId(1L)
      .optionId(2L)
      .build();

  public static final Response RESPONSE_FINAL = Response.builder()
      .userId(1L)
      .quizId(1L)
      .questionId(1L)
      .optionId(2L)
      .status(ResponseStatus.FINAL)
      .build();

  public static final FinalizeResponseRequest FINALIZE_RESPONSE_REQUEST = FinalizeResponseRequest.builder()
      .userId(1L)
      .quizId(1L)
      .build();

}
