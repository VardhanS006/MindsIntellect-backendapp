package com.backend.backendapp.repository;

import com.backend.backendapp.model.Learner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LearnerRepository extends JpaRepository<Learner,Long>{

    Optional<Learner> findByUserId(Long userId);
    
}