package com.backend.backendapp.controller;

import com.backend.backendapp.config.Fileupload;
import com.backend.backendapp.model.Answer;
import com.backend.backendapp.model.Learner;
import com.backend.backendapp.model.Mentor;
import com.backend.backendapp.model.Question;
import com.backend.backendapp.model.User;
import com.backend.backendapp.repository.AnswerRepository;
import com.backend.backendapp.repository.LearnerRepository;
import com.backend.backendapp.repository.MentorRepository;
import com.backend.backendapp.repository.QuestionRepository;
import com.backend.backendapp.repository.UserRepository;
import com.backend.backendapp.security.Role;
import com.backend.backendapp.util.QAcountchck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin("http://localhost:3000")
public class AnswerController {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MentorRepository mentorRepository;

    @Autowired
    private LearnerRepository learnerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Fileupload fileupload;

    @Autowired
    private QAcountchck qAcountchck;

    @PostMapping("/rate")
    public ResponseEntity<String> rateAnswer(@RequestParam("ansId") String ansId,
            @RequestParam("rating") String ratingstr, @RequestParam("ratemessage") String ratemessage) {
        Double rating = Double.parseDouble(ratingstr);
        Long answerId = Long.parseLong(ansId);

        Answer answer = answerRepository.findByAnswerId(answerId).get();

        answer.setRating(rating);
        answer.setRatemessage(ratemessage);
        answer.setStatus("FINAL");

        Question question = questionRepository.findByQuestionId(answer.getQuesId()).get();
        question.setRating(rating);
        question.setStatus("FINAL");

        Mentor mentor = mentorRepository.findByUserId(answer.getUserId()).get();
        mentor.setRate_pending(mentor.getRate_pending()-1);
        // BigDecimal percent = new BigDecimal(((rating/5)*100)/100);
        BigDecimal percent = new BigDecimal(((double) rating / 5) * 100 / 100);
        mentor.setAmnt_pending(mentor.getAmnt_pending().add(question.getAmount().multiply(percent)));

        try {
            answerRepository.save(answer);
            questionRepository.save(question);
            mentorRepository.save(mentor);
            return new ResponseEntity<>("Answer rated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to rate answer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/postanswer", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postAnswer(@RequestParam("username") String username,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("answer") String answerText,
            @RequestParam("quesId") String questionId) {
        Long quesId = Long.parseLong(questionId);
        User user = userRepository.findByUsername(username).get();
        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        qAcountchck.checkDate(user);
        if ("MAX".equals(qAcountchck.QAcount(user))) {
            return new ResponseEntity<>("Max Answers Limit Reached", HttpStatus.UPGRADE_REQUIRED);
        } else {
            Set<Role> roles = user.getRoles();
            boolean isMentor = false;

            for (Role role : roles) {
                if (Role.ROLE_MENTOR.equals(role)) {
                    isMentor = true;
                    break;
                }
                if (Role.ROLE_LEARNER.equals(role)) {
                    return new ResponseEntity<>("Unauthorized User.", HttpStatus.UNAUTHORIZED);
                }
            }

            if (!isMentor) {
                return new ResponseEntity<>("Unknown Error.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            try {
                Answer answer = new Answer();
                answer.setAnswer(answerText);
                answer.setQuesId(quesId);

                Long userId = user.getUserId();
                answer.setUserId(userId);

                Question question = questionRepository.findByQuestionId(quesId).get();

                Mentor mentor = mentorRepository.findByUserId(userId).get();
                mentor.setAnswers(mentor.getAnswers()+1);
                mentor.setRate_pending(mentor.getRate_pending()+1);
                mentorRepository.save(mentor);

                Learner learner = learnerRepository.findByUserId(question.getUserId()).get();
                learner.setAmnt_pending(learner.getAmnt_pending().add(question.getAmount()));
                learner.setAns_pending(learner.getAns_pending()-1);
                learnerRepository.save(learner);

                user.setQna_count(user.getQna_count() + 1);
                userRepository.save(user);

                Answer savedAnswer = answerRepository.save(answer);
                Long answerId = savedAnswer.getAnswerId();

                question.setAnswerId(answerId);
                question.setStatus("PAYMENT_PENDING");
                questionRepository.save(question);

                if (file != null && !file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = StringUtils.getFilenameExtension(originalFilename);
                    String fileName = answerId + "." + fileExtension;
                    String uploadDir = "uploads" + File.separator + "answers";

                    try {
                        fileupload.uploadFile(uploadDir, fileName, file);
                        String currentPath = System.getProperty("user.dir");
                        savedAnswer.setFile(currentPath + File.separator + "backend" + File.separator + uploadDir
                                + File.separator + fileName);
                        answerRepository.save(savedAnswer);
                    } catch (Exception e) {
                        return new ResponseEntity<>("Failed to upload File" + e.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                return new ResponseEntity<>("Answer posted successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to post answer: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/answers")
    public List<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    @GetMapping("/Auserpendings")
    List<Answer> getuserpendinganswerss(@RequestParam("username") String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        qAcountchck.checkDate(user);
        return answerRepository.findByUserIdAndStatusNot(user.getUserId(), "FINAL");
    }

    @GetMapping("/useranswer")
    List<Answer> getuseranswer(@RequestParam("username") String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        qAcountchck.checkDate(user);
        return answerRepository.findByUserId(user.getUserId());
    }

    @GetMapping("/answer")
    Answer getanswer(@RequestParam("ansId") String ansId) {
        Long Id = Long.parseLong(ansId);
        return answerRepository.findByAnswerId(Id).get();
    }

}
