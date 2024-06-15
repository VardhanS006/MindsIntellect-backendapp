package com.backend.backendapp.controller;

import com.backend.backendapp.model.Mentor;
import com.backend.backendapp.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
public class MentorController {

    @Autowired
    private MentorRepository mentorRepository;
 
    @GetMapping("/mentor")
    Optional<Mentor> getMentordata(@RequestParam("user") Long userid){
        return mentorRepository.findByUserId(userid);
    }

    @GetMapping("/mentors")
    List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }
    
}
