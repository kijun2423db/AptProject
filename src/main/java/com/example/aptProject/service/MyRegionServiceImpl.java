package com.example.aptProject.service;

import com.example.aptProject.dao.MyRegionDao;
import com.example.aptProject.entity.LocationCode;
import com.example.aptProject.entity.MyRegion;
import com.example.aptProject.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyRegionServiceImpl implements MyRegionService{
    @Autowired private MyRegionDao mDao;
    @Autowired private LocationService lSvc;
    @Override
    public void registerUserBy2Names(Users user, String firstName, String secondName) {
        LocationCode locationCode = lSvc.getLocationCodeByFirstNameAndSecondName(firstName, secondName);
        mDao.insert(user.getUid(), locationCode.getlCode());
    }

    @Override
    public void registerUserBy3Names(Users user, String firstName, String secondName, String lastName) {
        LocationCode locationCode = lSvc.getLocationCodeByFirstNameAndSecondNameAndLastName(firstName, secondName, lastName);
        mDao.insert(user.getUid(), locationCode.getlCode());
    }

    @Override
    public void updateUser(Users user, String firstName, String secondName) {
        LocationCode locationCode = lSvc.getLocationCodeByFirstNameAndSecondName(firstName, secondName);
        MyRegion myRegion = mDao.findByUserId(user.getUid());
        System.out.println("user.getUid ======================= " + user.getUid());
        System.out.println("first ======================= " + firstName);
        System.out.println("second ======================= " + secondName);
        mDao.update(locationCode.getlCode(), myRegion.getMid());
    }

    /**
     *
     * 3월 18일 기준 작업
     */

    @Override
    public MyRegion getMyRegionByUid(String uid) {
        MyRegion myRegion = mDao.findByUserId(uid);
        return myRegion;
    }

}
