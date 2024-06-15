package com.backend.backendapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.backendapp.model.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Long> {

    // @Query("SELECT fname,mname,lname FROM User WHERE username = :username")
    // Object findSubsetByUsername(@Param("username") String username);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    // List<User> findByMobile(Long mobile);
    Optional<User> findByMobile(Long mobile);

    //update today date
    // void updateAttributeByUsername(String username, String attributeName, Date attributeValue);
    
}
