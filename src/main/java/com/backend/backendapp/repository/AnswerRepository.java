package com.backend.backendapp.repository;

import com.backend.backendapp.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByAnswerId(Long answerId);

    List<Answer> findByUserId(Long userId);

    List<Answer> findByQuesId(Long quesId);

    List<Answer> findByStatus(String status);

    List<Answer> findByUserIdAndStatusNot(Long userId, String status);



}
