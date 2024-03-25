package com.example.aptProject.service;

import com.example.aptProject.entity.APIResult;
import com.example.aptProject.entity.APIResultIncludeTotalCount;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApiService {
    StringBuilder getAPIResult(String serviceKey, String pageNo, String numOfRows, String LAWD_CD, String DEAL_YMD) throws IOException ;
    APIResultIncludeTotalCount getResultList(String numOfRows, String LAWD_CD, String period) throws IOException;
    Map<String, Double> getGeoCode(String addr) throws IOException, ParseException;
    Map<String, Double> getCenterGeoCode(List<APIResult> apiResults) throws IOException, ParseException;
    String getTotalCount(String LAWD_CD, String period) throws IOException;
    List<APIResult> getAPIResultListByLatAndLon(APIResultIncludeTotalCount apiResultIncludeTotalCount, Double lat, Double lon);

}