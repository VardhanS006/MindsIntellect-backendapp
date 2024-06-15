package com.backend.backendapp.controller;

import com.backend.backendapp.config.Fileupload;
import com.backend.backendapp.model.Learner;
import com.backend.backendapp.model.Question;
import com.backend.backendapp.model.User;
import com.backend.backendapp.repository.LearnerRepository;
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

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


@RestController
@CrossOrigin("http://localhost:3000")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LearnerRepository learnerRepository;
    @Autowired
    private Fileupload fileupload;
    @Autowired
    private QAcountchck qAcountchck;

    @PostMapping("/payment")
    public ResponseEntity<String> payment(@RequestParam("quesId") String queId,@RequestParam("username") String username) {
        
        Long quesId = Long.parseLong(queId);
        Question question = questionRepository.findByQuestionId(quesId).get();
        question.setStatus("RATE_PENDING");

        User user = userRepository.findByUsername(username).get();
        
        Learner learner = learnerRepository.findByUserId(user.getUserId()).get();
        learner.setAmnt_paid(learner.getAmnt_paid().add(question.getAmount()));
        learner.setAmnt_pending(learner.getAmnt_pending().subtract(question.getAmount()));

        try{
            
            questionRepository.save(question);
            learnerRepository.save(learner);
            return new ResponseEntity<>("Payment Successful", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<String>("Payment Unsuccessful", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @PostMapping(value = "/askquestion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> askQuestion(@RequestParam("username") String username,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestParam("question") String questiontext,
            @RequestParam("field") String field,
            @RequestParam("amount") String stramount) {

        BigDecimal amount = new BigDecimal(stramount);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new ResponseEntity<>("User not found.", HttpStatus.NOT_FOUND);
        }

        qAcountchck.checkDate(user);
        if (qAcountchck.QAcount(user) == "MAX") {
            return new ResponseEntity<>("Max Answers Limit Reached", HttpStatus.UPGRADE_REQUIRED);
        } else {
            Set<Role> roles = user.getRoles();
            boolean isLearner = false;

            for (Role role : roles) {
                if (Role.ROLE_MENTOR.equals(role)) {
                    return new ResponseEntity<>("Unauthorized User.", HttpStatus.UNAUTHORIZED);
                }
                if (Role.ROLE_LEARNER.equals(role)) {
                    isLearner = true;
                    break;
                }
            }

            if (!isLearner) {
                return new ResponseEntity<>("Unknown Error.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Learner learner = learnerRepository.findByUserId(user.getUserId()).get();
            learner.setQuestions(learner.getQuestions()+1);
            learner.setAns_pending(learner.getAns_pending()+1);

            try {
                Question question = new Question();

                question.setAmount(amount);
                question.setField(field);
                question.setQuestion(questiontext);

                Long userId = user.getUserId();
                question.setUserId(userId);

                user.setQna_count(user.getQna_count() + 1);
                userRepository.save(user);

                learnerRepository.save(learner);

                Question savedQuestion = questionRepository.save(question);
                Long questionId = savedQuestion.getQuestionId();

                if (file != null && !file.isEmpty()) {
                    String fileExtension = getFileExtension(file.getOriginalFilename());
                    String fileName = questionId + "." + fileExtension;
                    String uploadDir = "uploads" + File.separator + "questions";

                    try {
                        fileupload.uploadFile(uploadDir, fileName, file);
                        String currentPath = System.getProperty("user.dir");
                        savedQuestion.setFile(currentPath + File.separator + "backend" + File.separator + uploadDir
                                + File.separator + fileName);
                        questionRepository.save(savedQuestion);
                    } catch (Exception e) {
                        return new ResponseEntity<>("Failed to upload File" + e.getMessage(),
                                HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                return new ResponseEntity<>("Question Posted Successfully", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(
                        "Failed to Post Question, Please Try Again After Sometime." + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @GetMapping("/questions")
    List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/finalquestions")
    List<Question> getAllfinalQuestions() {
        return questionRepository.findByStatus("FINAL");
    }

    @GetMapping("/question")
    Question getquestion(@RequestParam("quesId") String quesId) {
        Long Id = Long.parseLong(quesId);
        return questionRepository.findByQuestionId(Id).get();
    }

    @GetMapping("/userquestion")
    List<Question> getuserquestion(@RequestParam("username") String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        qAcountchck.checkDate(user);
        return questionRepository.findByUserId(user.getUserId());
    }

    @GetMapping("/Quserpendings")
    List<Question> getuserpendingquestions(@RequestParam("username") String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        qAcountchck.checkDate(user);
        return questionRepository.findByUserIdAndStatusNot(user.getUserId(), "FINAL");
    }

    @GetMapping("/unanswered")
    List<Question> UnansweredQuestions() {
        return questionRepository.findByAnswerId((long) 0);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
