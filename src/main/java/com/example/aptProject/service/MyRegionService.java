package com.example.aptProject.service;


import com.example.aptProject.entity.MyRegion;
import com.example.aptProject.entity.Users;

public interface MyRegionService {
    void registerUserBy2Names(Users user,String firstName,String secondName);
    void registerUserBy3Names(Users user,String firstName,String secondName, String lastName);
    void updateUser(Users user, String firstName, String secondName);


    /**
     * 3월 18일 기준 작업
     */
    MyRegion getMyRegionByUid(String uid);
}
