package com.backend.backendapp.model;

import com.backend.backendapp.security.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
// import jakarta.persistence.Temporal;
// import jakarta.persistence.TemporalType;
// import jakarta.persistence.JoinTable;
// import jakarta.persistence.ManyToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements UserDetails{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    public Long getUserId() {
        return userId;
    }
    private String fname;
    private String mname;
    private String lname;
    private Date dob;
    private String email;
    private Long mobile;
    private String username;
    private Integer sub_id;
    @Column(name = "image")
    private String image;
    private String password;
    private Integer qna_count;
    private Date reg_date;
    private Date today;
    
    @ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name="user_id"))
    @Enumerated(EnumType.STRING)
    // @ManyToMany(fetch = FetchType.EAGER)
    // @JoinTable(
    //     name = "user_roles",
    //     joinColumns = @JoinColumn(name = "user_id"),
    //     inverseJoinColumns = @JoinColumn(name = "role_id")
    // )
    private Set<Role> roles;

    public Integer getQna_count() {
        return qna_count;
    }
    public void setQna_count(Integer qna_count) {
        this.qna_count = qna_count;
    }
    public Date getReg_date() {
        return reg_date;
    }
    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }
    public Date getToday() {
        return today;
    }
    public void setToday(Date today) {
        this.today = today;
    }
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getMname() {
        return mname;
    }
    public void setMname(String mname) {
        this.mname = mname;
    }
    public String getLname() {
        return lname;
    }
    public void setLname(String lname) {
        this.lname = lname;
    }
    public Date getDob() {
        return dob;
    }
    public void setDob(Date dob) {
        this.dob = dob;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Long getMobile() {
        return mobile;
    }
    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Integer getSub_id() {
        return sub_id;
    }
    public void setSub_id(Integer sub_id) {
        this.sub_id = sub_id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User() {
        this.sub_id=1;
        this.qna_count=0;
        this.reg_date=Date.valueOf(LocalDate.now());
        this.today=Date.valueOf(LocalDate.now());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.name()))
            .collect(Collectors.toList());
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

}
