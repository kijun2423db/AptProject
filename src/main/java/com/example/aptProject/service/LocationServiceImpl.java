package com.example.aptProject.service;

import com.example.aptProject.dao.LocationCodeDao;
import com.example.aptProject.entity.APIResult;
import com.example.aptProject.entity.LocationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationCodeDao locationCodeDao;

    @Override
    public List<String> getAllFirstNames() {
        return locationCodeDao.findAllFirstNames();
    }

    @Override
    public List<String> getSecondNamesByFirstName(String firstName) {
        List<String> list = locationCodeDao.findSecondNamesByFirstName(firstName);
        list.removeIf(item -> item == null);
        Collections.sort(list);


        return list;
    }

    @Override
    public LocationCode getLocationCodeByFirstNameAndSecondName(String firstName, String secondName) {
        return locationCodeDao.getLocationCodeByFirstNameAndSecondName(firstName, secondName);
    }

    @Override
    public LocationCode getLocationCodeByFirstNameAndSecondNameAndLastName(String firstName, String secondName, String lastName) {
        return locationCodeDao.getLocationCodeByFirstNameAndSecondNameAndLastName(firstName, secondName, lastName);
    }

    @Override
    public List<String> getLastNamesByFirstNameAndSecondName(String firstName, String secondName) {
        List<String> list = locationCodeDao.findLastNamesByFirstNameAndSecondName(firstName, secondName);
        list.removeIf(item -> item == null);
        Collections.sort(list);

        return list;
    }

    @Override
    public LocationCode getLocationCodeByLcode(int lCode) {
        return locationCodeDao.getLocationCode(lCode);
    }

    @Override
    public String getLocationName(int lCode) {
        String firstName, secondName, lastName = "";

        firstName = locationCodeDao.getLocationCode(lCode).getFirstName();

        secondName = locationCodeDao.getLocationCode(lCode).getSecondName();
        if(secondName == null){
            return firstName;
        }
        lastName = locationCodeDao.getLocationCode(lCode).getLastName();
        if(lastName == null){
            return firstName + " " + secondName;
        }

        return firstName + " " + secondName + " " + lastName;
    }
}
