package com.backend.backendapp.controller;

import com.backend.backendapp.config.Fileupload;
import com.backend.backendapp.model.*;
import com.backend.backendapp.repository.LearnerRepository;
import com.backend.backendapp.repository.MentorRepository;
import com.backend.backendapp.repository.UserRepository;
import com.backend.backendapp.security.JwtHelper;
import com.backend.backendapp.security.Role;
import com.backend.backendapp.util.QAcountchck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin("http://localhost:3000")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MentorRepository mentorRepository;
    @Autowired
    private LearnerRepository learnerRepository;
    @Autowired
    private QAcountchck qAcountchck;

    @Autowired
    private Fileupload fileUpload;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/upgradesub")
    public ResponseEntity<String> upgradesubscription(@RequestParam("username") String username,@RequestParam("subId") String sub_id) {
        Integer subId = Integer.parseInt(sub_id);
        User user = userRepository.findByUsername(username).get();
        user.setSub_id(subId);
        try {
            userRepository.save(user);
            return new ResponseEntity<>("Successfully Subscribed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to Upgrade Subscription " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/qacount")
    public String qaCount(@RequestParam("username") String username) {
        User user = userRepository.findByUsername(username).get();
        qAcountchck.checkDate(user);
        return qAcountchck.QAcount(user);
    }

    @GetMapping("/userdata")
    Optional<User> getUserData(@RequestParam("username") String username) {
        User user1 = userRepository.findByUsername(username).get();
        qAcountchck.checkDate(user1);
        return userRepository.findByUsername(username);
    }

    @PostMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerUser(@RequestParam("images") MultipartFile file, @ModelAttribute User user) {

        // Save the file
        String useRName = user.getUsername();
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = useRName + "." + fileExtension;
        String uploadDir = "uploads" + File.separator + "user-photos";
        // String role = user.getRoles().toString();
        Set<Role> roles = user.getRoles();

        try {
            fileUpload.uploadFile(uploadDir, fileName, file);
            String currentPath = System.getProperty("user.dir");
            user.setImage(
                    currentPath + File.separator + "backend" + File.separator + uploadDir + File.separator + fileName);

            String encpass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encpass);

            // Save user to the database
            userRepository.save(user);
            Long userID = user.getUserId();
            System.out.println(userID);
            for (Role role : roles) {
                if (Role.ROLE_MENTOR.equals(role)) {
                    Mentor mentor = new Mentor();
                    mentor.setUserId(userID);
                    mentorRepository.save(mentor);
                    break;
                }
                if (Role.ROLE_LEARNER.equals(role)) {
                    Learner learner = new Learner();
                    learner.setUserId(userID);
                    learnerRepository.save(learner);
                    break;
                }
            }

            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to register user " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/username={username}")
    public Integer getUserByUsername(@PathVariable String username) {
        if (userRepository.findByUsername(username).isPresent()) {
            return 1;
        } else {
            return 0;
        }
    }

    @GetMapping("/users/email={email}")
    public Integer getUserByEmail(@PathVariable String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            return 1;
        } else {
            return 0;
        }
    }

    @GetMapping("/users/mobile={mobile}")
    public Integer getUserByMobile(@PathVariable Long mobile) {
        if (userRepository.findByMobile(mobile).isPresent()) {
            return 1;
        } else {
            return 0;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtHelper.generateToken(userDetails);

        Set<Role> roles = ((User) userDetails).getRoles();
        String role = roles.isEmpty() ? "" : roles.iterator().next().name();

        JwtResponse response = JwtResponse.builder()
                .JwtToken(token)
                .role(role)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            System.out.println(authentication);
            manager.authenticate(authentication);

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }

    }

    // @ExceptionHandler(BadCredentialsException.class)
    // public String exceptionHandler() {
    // return "Credentials Invalid !!";
    // }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        // logger.error("Authentication failed: {}", ex.getMessage());

        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
