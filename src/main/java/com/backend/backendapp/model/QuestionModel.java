package com.backend.backendapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class QuestionModel {
    @Id
    @GeneratedValue
    private Long questionId;
    private String question;
    private LocalDateTime date;
    private Long userId;
    private Long answerId;
    private String status;
    private String field;
    private Long amount;
    private Byte image; // Change type to byte[] for image data

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Byte getImage() { // Getter for image data
        return image;
    }

    public void setImage(Byte image) { // Setter for image data
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }
}
