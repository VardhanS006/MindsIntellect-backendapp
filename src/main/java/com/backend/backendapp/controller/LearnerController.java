package com.backend.backendapp.controller;

import com.backend.backendapp.model.Learner;
import com.backend.backendapp.repository.LearnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class LearnerController {

    @Autowired
    private LearnerRepository learnerRepository;

    @GetMapping("/learner")
    Optional<Learner> getLearnerdata(@RequestParam("user") Long userid){
        return learnerRepository.findByUserId(userid);
    }

    @GetMapping("/learners")
    List<Learner> getAllLearners() {
        return learnerRepository.findAll();
    }
    
}
