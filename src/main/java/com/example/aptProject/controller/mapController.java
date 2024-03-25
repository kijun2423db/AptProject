package com.example.aptProject.controller;

import com.example.aptProject.entity.APIResult;
import com.example.aptProject.entity.APIResultIncludeTotalCount;
import com.example.aptProject.service.ApiService;
import com.example.aptProject.service.LocationService;
import jakarta.servlet.http.HttpSession;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class mapController {
    /**
     * 쥰내바꿈 0318 기준
     */
    @Autowired
    private ApiService aSvc;

    @Autowired
    private LocationService lSvc;

    @GetMapping("getResult/{LAWD_CD}/{period}")
    public String getResult(@PathVariable String LAWD_CD, @PathVariable String period, HttpSession session, Model model) throws IOException, ParseException {
//        String totalCount = "100000";
        String totalCount = aSvc.getTotalCount(LAWD_CD, period);
        Map<String, Double> center = new HashMap<>();

        APIResultIncludeTotalCount result = aSvc.getResultList(totalCount, LAWD_CD, period);
        List<APIResult> resultList = result.getApiResults();
        center = aSvc.getCenterGeoCode(resultList);
        Double lon, lat;
        lon = center.get("lon");
        lat = center.get("lat");
        session.setAttribute("centerLon", lon);
        session.setAttribute("centerLat", lat);


        session.setAttribute("totalCount", resultList.size());
//        System.out.println("totalCount ======= " + totalCount);
//        System.out.println("notExistCount -------- " + result.getNotExistCount());
//        System.out.println("resultlist==============" + resultList.size());
        model.addAttribute("resultList", resultList);

        List<String> firstNames = lSvc.getAllFirstNames();
        model.addAttribute("firstNames", firstNames);



        return "map/detail4";
    }

    @GetMapping("/graph/{lat}/{lon}/{list}")
    public String getSecondNamesByFirstName(@PathVariable String lat, @PathVariable String lon, @PathVariable APIResultIncludeTotalCount list, HttpSession session, Model model) {
        List<APIResult> resultList = aSvc.getAPIResultListByLatAndLon(list, Double.parseDouble(lat), Double.parseDouble(lon));
        model.addAttribute("resultList", resultList);

        return "map/detail5";
    }


    @GetMapping("/search/secondNames")
    @ResponseBody
    public List<String> getSecondNamesByFirstName(@RequestParam("firstName") String firstName) {
        List<String> secondNames = lSvc.getSecondNamesByFirstName(firstName);
        return secondNames;
    }


    @GetMapping("/search/lastNames")
    @ResponseBody
    public List<String> getLastNamesByFirstNameAndSecondName(@RequestParam("firstName") String firstName,@RequestParam("secondName") String secondName) {
        List<String> lastNames = lSvc.getLastNamesByFirstNameAndSecondName(firstName, secondName);
        return lastNames;
    }

    @GetMapping("/search/firstNames")
    public List<String> getAllFirstNames() {
        return lSvc.getAllFirstNames();
    }

    @PostMapping("/search")
    public String searchProc(String firstName, String secondName, String lastName, String period) {

        int lCode = 0;
        if(lastName.isEmpty()){
            lCode = lSvc.getLocationCodeByFirstNameAndSecondName(firstName, secondName).getlCode();
        }else{
            lCode = lSvc.getLocationCodeByFirstNameAndSecondNameAndLastName(firstName, secondName, lastName).getlCode();
        }

        return "redirect:/api/getResult/" + lCode + "/" + period;

    }
}
