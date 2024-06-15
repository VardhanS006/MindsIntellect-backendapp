package com.backend.backendapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Learner{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learnerId;

    public Long getLearnerId() {
        return learnerId;
    }

    private Long userId;
    private Long questions;
    private BigDecimal amnt_paid;
    private BigDecimal amnt_pending;
    private Long ans_pending;

    public Learner(){

        this.questions = (long) 0;
        this.amnt_paid = new BigDecimal("0.0");
        this.amnt_pending = new BigDecimal("0.0");
        this.ans_pending = (long) 0;
        
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getQuestions() {
        return questions;
    }
    public void setQuestions(Long questions) {
        this.questions = questions;
    }
    public BigDecimal getAmnt_paid() {
        return amnt_paid;
    }
    public void setAmnt_paid(BigDecimal amnt_paid) {
        this.amnt_paid = amnt_paid;
    }
    public BigDecimal getAmnt_pending() {
        return amnt_pending;
    }
    public void setAmnt_pending(BigDecimal amnt_pending) {
        this.amnt_pending = amnt_pending;
    }
    public Long getAns_pending() {
        return ans_pending;
    }
    public void setAns_pending(Long ans_pending) {
        this.ans_pending = ans_pending;
    }

}
