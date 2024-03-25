package com.example.aptProject.service;

import com.example.aptProject.entity.APIResult;
import com.example.aptProject.entity.LocationCode;

import java.util.List;

public interface LocationService {
    String getLocationName(int lCode);
    List<String> getAllFirstNames();
    List<String> getSecondNamesByFirstName(String firstName);
    LocationCode getLocationCodeByFirstNameAndSecondName(String firstName, String secondName);
    LocationCode getLocationCodeByFirstNameAndSecondNameAndLastName(String firstName, String secondName, String lastName);
    List<String> getLastNamesByFirstNameAndSecondName(String firstName, String secondName);
    LocationCode getLocationCodeByLcode(int lCode);
}