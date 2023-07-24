package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.entity.Question;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends R2dbcRepository<Question, Long> {

}
