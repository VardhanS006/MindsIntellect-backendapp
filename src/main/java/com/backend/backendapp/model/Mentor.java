package com.backend.backendapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class Mentor{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mentorId;

    public Long getMentorId() {
        return mentorId;
    }

    private Long userId;
    private Long answers;
    private BigDecimal amnt_withdrawn;
    private BigDecimal amnt_pending;
    private Long rate_pending;
    private Long ac_number;
    private String ac_name;
    private String ifsc;
    private String branch_add;

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRate_pending() {
        return rate_pending;
    }
    public void setRate_pending(Long rate_pending) {
        this.rate_pending = rate_pending;
    }
    public Long getAnswers() {
        return answers;
    }
    public void setAnswers(Long answers) {
        this.answers = answers;
    }
    public BigDecimal getAmnt_withdrawn() {
        return amnt_withdrawn;
    }
    public void setAmnt_withdrawn(BigDecimal amnt_withdrawn) {
        this.amnt_withdrawn = amnt_withdrawn;
    }
    public BigDecimal getAmnt_pending() {
        return amnt_pending;
    }
    public void setAmnt_pending(BigDecimal amnt_pending) {
        this.amnt_pending = amnt_pending;
    }
    public Long getAc_number() {
        return ac_number;
    }
    public void setAc_number(Long ac_number) {
        this.ac_number = ac_number;
    }
    public String getAc_name() {
        return ac_name;
    }
    public void setAc_name(String ac_name) {
        this.ac_name = ac_name;
    }
    public String getIfsc() {
        return ifsc;
    }
    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
    public String getBranch_add() {
        return branch_add;
    }
    public void setBranch_add(String branch_add) {
        this.branch_add = branch_add;
    }

    public Mentor(){

        this.answers = (long) 0;
        this.amnt_withdrawn = new BigDecimal("0.0");
        this.amnt_pending = new BigDecimal("0.0");
        this.rate_pending = (long) 0;
        this.ac_number = (long) 0;
        this.ac_name = "";
        this.ifsc = "";
        this.branch_add = "";
        
    }

}
