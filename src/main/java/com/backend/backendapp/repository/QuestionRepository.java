package com.backend.backendapp.repository;

import com.backend.backendapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question,Long> {

    Optional<Question> findByQuestionId(Long questionId);

    List<Question> findByUserId(Long userId);

    List<Question> findByAnswerId(Long answerId);

    List<Question> findByStatus(String status);

    List<Question> findByUserIdAndStatusNot(Long userId, String status);

}