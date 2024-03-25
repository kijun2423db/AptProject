package com.example.aptProject.entity;

import java.time.LocalDate;

public class Users {
    private String uid;
    private String pwd;
    private String uname;
    private String email;
    private LocalDate regDate;
    private int isDeleted;

    @Override
    public String toString() {
        return "Users{" +
                "uid='" + uid + '\'' +
                ", pwd='" + pwd + '\'' +
                ", uname='" + uname + '\'' +
                ", email='" + email + '\'' +
                ", regDate=" + regDate +
                ", isDeleted=" + isDeleted +
                '}';
    }

    public Users(String uid, String pwd, String uname, String email) {
        this.uid = uid;
        this.pwd = pwd;
        this.uname = uname;
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Users() {
    }

    public Users(String uid, String pwd, String uname, String email, LocalDate regDate, int isDeleted) {
        this.uid = uid;
        this.pwd = pwd;
        this.uname = uname;
        this.email = email;
        this.regDate = regDate;
        this.isDeleted = isDeleted;
    }
}
