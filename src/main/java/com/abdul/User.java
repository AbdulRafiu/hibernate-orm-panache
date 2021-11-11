package com.abdul;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;

@Entity
@Cacheable
public class User {
    @Id @GeneratedValue private Long id;

    @Column(length = 10, unique = true, updatable = false)
    private String username;

    @Column(length = 32)
    private String password;

    @Column(length = 40, unique = true)
    private String email;

    @Column(length = 3)
    private Integer age;

    public User(Long id, String username, String password, String email, Integer age) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "com.abdul.User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

}
