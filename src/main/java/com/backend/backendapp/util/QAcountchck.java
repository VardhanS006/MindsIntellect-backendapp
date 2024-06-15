package com.backend.backendapp.util;

import com.backend.backendapp.model.User;
import com.backend.backendapp.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class QAcountchck {
    
    private final UserRepository userRepository;

    public QAcountchck(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkDate(User user){
        Date currentDate = Date.valueOf(LocalDate.now());
        if(!currentDate.equals(user.getToday())){
            user.setToday(currentDate);
            user.setQna_count(0);
            userRepository.save(user);
        }
    }

    public String QAcount(User user){
        if ((user.getSub_id()==1 && user.getQna_count()>=3)||(user.getSub_id()==2 && user.getQna_count()>=10)||(user.getSub_id()==3 && user.getQna_count()>=25)) {
            return "MAX";
        }
        else {
            return "OK";
        }
    }

    
}
