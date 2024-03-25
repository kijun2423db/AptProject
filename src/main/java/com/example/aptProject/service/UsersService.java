package com.example.aptProject.service;

import com.example.aptProject.entity.Users;

import java.util.List;

public interface UsersService {
    public static final int CORRECT_LOGIN = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int USER_NOT_EXIST = 2;
    public static final int COUNT_PER_PAGE = 10;

    Users getUserByUid(String uid);

    List<Users> getUserList(int page);

//    void registerUser(Users user,String firstName,String secondName);

    void updateUser(Users user);

    void deleteUser(String uid);

    int login(String uid, String pwd);

    void registerUser(Users user);
}
