package com.juan.itb.vrbackend.repository;

import com.juan.itb.vrbackend.entity.Quiz;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends R2dbcRepository<Quiz, Long> {

}
