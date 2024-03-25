package com.example.aptProject.service;

import com.example.aptProject.entity.APIResult;

import com.example.aptProject.entity.APIResultIncludeTotalCount;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApiServiceImpl implements ApiService{
    @Autowired LocationService lSvc;
    @Override
    public StringBuilder getAPIResult(String serviceKey, String pageNo, String numOfRows, String LAWD_CD, String DEAL_YMD) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + pageNo); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + numOfRows); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("LAWD_CD","UTF-8") + "=" + LAWD_CD); /*지역코드*/
        urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD","UTF-8") + "=" + DEAL_YMD); /*계약월*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        return sb;
    }

    @Override
    public APIResultIncludeTotalCount getResultList(String numOfRows, String LAWD_CD, String period) throws IOException {
       String serviceKey = "rlpbGR9EbYg8iu0YftsAGmUeblmq9qJenXIk7WsVg0qr%2FRXALrab9zfstv0OkO5A15gR4aKR5aO%2FVFtjV6dkfA%3D%3D";     // 기준이 형
  //     String serviceKey = "m6VKwfm7ZHeugZXEw7laLFAM41rMMp3kK21%2BOF7pXCi3bA64o8KwZhVpG627gfD2Y5Bl34RCb8%2B3PR2AqL%2BGHQ%3D%3D";     // 시연누나
//      String serviceKey = "kSmmxK6j6yUg%2FzHt%2FJ3dYTNRofjKnX0qSSNSWdoqG0gmrChsZRMtUMZMJL8DxU2cwkP%2BOhexSGnPlalb43kNjw%3D%3D";  // 도현
    //  String serviceKey = "VGyeLICpqeNan%2FeK8Z%2FQZtRWwlEnqokjASqIvw6DOui%2FkKBrk2nJ%2FDLlP4Psl3X4Hm05JQmAULE9Ly5h66%2F%2BDA%3D%3D ";  // 희재
        String pageNo = "1";
        String DEAL_YMD = LocalDate.now().getYear() + String.format("%02d", LocalDate.now().getMonthValue() - 1);

       // 문자열로 나오네  System.out.println("ㄴㅇㄴㅇㄴㅇㄴㅇㄴㅇ ============" + DEAL_YMD);

        APIResult apiResult = null;
        List<APIResult> resultList = new ArrayList<>();
        int count = 0;
        String totalCount ="";
        int notExistCount = 0;
        for(int k = 0; k<Integer.parseInt(period); k++){
            System.out.println("여기부터가 한번 호출!!!!!!!!");

            int year = LocalDate.now().getYear(); // 현재 연도
            int month = LocalDate.now().getMonthValue() -1; // 현재 월

            // k 값에 따라 연도와 월을 조정
            if (k >= 12) {
                int yearsToSubtract = k / 12;
                int monthsToSubtract = k % 12;
                year -= yearsToSubtract;
                month -= monthsToSubtract;

                // 월이 음수가 되면 연도를 조정
                if (month <= 0) {
                    year -= 1;
                    month += 12;
                }
            } else {
                month -= k;
                if (month <= 0) {
                    year -= 1;
                    month += 12;
                }
            }
            DEAL_YMD = year + String.format("%02d", month);
            StringBuilder sb = getAPIResult(serviceKey, pageNo, numOfRows, LAWD_CD, DEAL_YMD);
            try {
                // XML 문자열
                String xmlString = sb.toString();
                // XML 파서 생성
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
                // XML 파싱
                Document doc = builder.parse(inputStream);
                doc.getDocumentElement().normalize();

                Node totalCountNode = doc.getElementsByTagName("totalCount").item(0);
                count += Integer.parseInt(totalCountNode.getTextContent().trim());

                // 각 항목 추출
                NodeList itemList = doc.getElementsByTagName("item");
                for (int i = 0; i < itemList.getLength(); i++) {
                    Node itemNode = itemList.item(i);
                    if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element itemElement = (Element) itemNode;
                        String 거래금액 = itemElement.getElementsByTagName("거래금액").item(0).getTextContent().trim();
                        String 건축년도 = itemElement.getElementsByTagName("건축년도").item(0).getTextContent().trim();
                        String 년 = itemElement.getElementsByTagName("년").item(0).getTextContent().trim();
                        if(itemElement.getElementsByTagName("도로명").item(0) == null){
                            notExistCount +=1;
                            continue;
                        }
                        String 도로명 = itemElement.getElementsByTagName("도로명").item(0).getTextContent().trim();
                        String 도로명건물본번호코드 = itemElement.getElementsByTagName("도로명건물본번호코드").item(0).getTextContent().trim();
                        int tmpFor도로명 = Integer.parseInt(도로명건물본번호코드);
                        도로명건물본번호코드 = String.valueOf(tmpFor도로명);
                        String 법정동 = itemElement.getElementsByTagName("법정동").item(0).getTextContent().trim();
                        String 아파트 = itemElement.getElementsByTagName("아파트").item(0).getTextContent().trim();
                        String 월 = itemElement.getElementsByTagName("월").item(0).getTextContent().trim();
                        String 일 = itemElement.getElementsByTagName("일").item(0).getTextContent().trim();
                        // 월과 일이 한 자리인 경우 앞에 0을 붙여 두 자리로 만듭니다.
                        if (월.length() == 1) {
                            월 = "0" + 월;
                        }
                        if (일.length() == 1) {
                            일 = "0" + 일;
                        }
                        // 변환 패턴 지정
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                        String tmp = 년 + 월 + 일;
                        LocalDate 년월일 = LocalDate.parse(tmp, formatter);        //여기 한번 확인


                        String 전용면적 = itemElement.getElementsByTagName("전용면적").item(0).getTextContent().trim();
                        String 지역코드 = itemElement.getElementsByTagName("지역코드").item(0).getTextContent().trim();
                        String 층 = itemElement.getElementsByTagName("층").item(0).getTextContent().trim();
                        String 해제여부 = itemElement.getElementsByTagName("해제여부").item(0).getTextContent().trim();

                        String addr = "";
                        addr = lSvc.getLocationName(Integer.parseInt(지역코드)) + " " + 도로명 + " " + 도로명건물본번호코드;


                        Map<String, Double> map = getGeoCode(addr);
                        System.out.println("map ================= " + map);

                        if (map != null && 해제여부.isEmpty()) { // 도로명 주소의 위도와 경도가 있는 경우에만 결과에 추가
                            apiResult = new APIResult(년, 월, 일, 년월일, 지역코드, 법정동, 도로명, 도로명건물본번호코드, 아파트, 층, 전용면적, 건축년도, 거래금액, totalCount, map.get("lon"), map.get("lat"));
                            resultList.add(apiResult);
                            System.out.println("년: " + 년);
                            System.out.println("월: " + 월);
                            System.out.println("일: " + 일);
                            System.out.println("년월일: " + 년월일);
                            System.out.println("지역코드: " + 지역코드);
                            System.out.println("법정동: " + 법정동);
                            System.out.println("도로명: " + 도로명);
                            System.out.println("아파트: " + 아파트);
                            System.out.println("층: " + 층);
                            System.out.println("전용면적(m^2): " + 전용면적);
                            System.out.println("건축년도: " + 건축년도);
                            System.out.println("거래금액(만): " + 거래금액);
                            System.out.println("lat: " + map.get("lat"));
                            System.out.println("lon: " + map.get("lon"));
                            System.out.println("notExistCount: " + notExistCount);
                            System.out.println("---");
                        }else {
                            notExistCount +=1;
                        }
                    }
                }
            } catch (Exception e) {
                notExistCount += 1;
                e.printStackTrace();
            }
        }
        totalCount = String.valueOf(count);
        APIResultIncludeTotalCount result = new APIResultIncludeTotalCount(resultList, totalCount, notExistCount);

        return result;

    }

    @Override
    public Map<String, Double> getGeoCode(String addr) throws IOException, ParseException {
        String kakaoKey = "53f0f1441d99a7c87f4d29410ab21e7e";
        String query = URLEncoder.encode(addr, "utf-8");
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json"
                + "?query=" + query;

        URL url = new URL(apiUrl);
        // Header setting
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "KakaoAK " + kakaoKey);

        // 응답 결과 확인
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            System.out.println("API 호출에 실패했습니다. 응답 코드: " + responseCode);
            return null; // 실패한 경우 null 반환
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while((line = br.readLine()) != null)
            sb.append(line);
        br.close();

        // JSON 데이터에서 원하는 값 추출하기
        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(sb.toString());
        JSONArray documents = (JSONArray) object.get("documents");
        if (documents.isEmpty()) {
            System.out.println("도로명 주소 검색 결과가 없습니다.");
            return null; // 검색 결과가 없는 경우 null 반환
        }

        JSONObject item = (JSONObject) documents.get(0);
        String lon_ = (String) item.get("x");
        String lat_ = (String) item.get("y");

        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lon", Double.parseDouble(lon_));
        map.put("lat", Double.parseDouble(lat_));
        return map;
    }

    @Override
    public Map<String, Double> getCenterGeoCode(List<APIResult> apiResults) throws IOException, ParseException {
        Map<String, Double> centerGeoCode = new HashMap<>();

        double lonSum = 0.0;
        double latSum = 0.0;
        for (APIResult result : apiResults) {
            lonSum += result.getLon();
            latSum += result.getLat();
        }

        double lonAverage = lonSum / apiResults.size();
        double latAverage = latSum / apiResults.size();

        centerGeoCode.put("lon", lonAverage);
        centerGeoCode.put("lat", latAverage);

        return centerGeoCode;
    }

    @Override
    public String getTotalCount(String LAWD_CD, String period) throws IOException {
        return getResultList("1", LAWD_CD, period).getTotalCount();
    }

    @Override
    public List<APIResult> getAPIResultListByLatAndLon(APIResultIncludeTotalCount apiResultIncludeTotalCount, Double lat, Double lon) {
        List<APIResult> resultList = new ArrayList<>();
        List<APIResult> list = apiResultIncludeTotalCount.getApiResults();

        String totalCount = list.get(0).getTotalCount();
        for (APIResult result : list) {
            if(Objects.equals(result.getLat(), lat) && Objects.equals(result.getLon(), lon)){
                resultList.add(result);
            }
        }
        return resultList;
    }


    //법정동 코드.txt파일 데이터 전처리 하고 insert 쿼리문으로 변경해주는 코드
    public static List<List<String>> filterAndParseDataFromFile(String filename) {
        List<List<String>> result = new ArrayList<>();
        Map<String, List<String>> codeMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "EUC-KR"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t"); // 탭으로 분리

                // "존재"를 포함하는 데이터만 처리
                if (parts.length >= 3 && parts[2].contains("존재")) {
                    String code = parts[0].substring(0, 5); // 지역 코드는 5자리로 자르기
                    // 중복된 지역 코드는 건너뜀
                    if (codeMap.containsKey(code)) {
                        continue;
                    }
                    List<String> entry = new ArrayList<>();
                    entry.add(code); // 지역 코드 추가
                    // 지역 이름을 공백 기준으로 나누어 저장
                    String[] regionNameParts = parts[1].split(" ");
                    for (String regionNamePart : regionNameParts) {
                        entry.add(regionNamePart);
                    }
                    result.add(entry); // 결과에 추가
                    codeMap.put(code, entry); // 지역 코드와 데이터 매핑
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String generateSQLQuery(List<List<String>> parsedData) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("insert into locationCode values\n");
        for (int i = 0; i < parsedData.size(); i++) {
            List<String> entry = parsedData.get(i);
            StringBuilder valuesBuilder = new StringBuilder();
            valuesBuilder.append("(");
            for (int j = 0; j < entry.size(); j++) {
                // 문자열인 경우 따옴표 추가
                if (!isNumeric(entry.get(j))) {
                    valuesBuilder.append("'");
                }
                valuesBuilder.append(entry.get(j));
                if (!isNumeric(entry.get(j))) {
                    valuesBuilder.append("'");
                }
                if (j < entry.size() - 1) {
                    valuesBuilder.append(", ");
                }
            }
            // 데이터 요소의 수를 테이블의 컬럼 수와 맞추기 위해 null 값 추가
            int numOfColumns = 4;
            if (entry.size() < numOfColumns) {
                for (int k = entry.size(); k < numOfColumns; k++) {
                    valuesBuilder.append(", null");
                }
            }
            valuesBuilder.append(")");
            queryBuilder.append(valuesBuilder);
            if (i < parsedData.size() - 1) {
                queryBuilder.append(",\n");
            }
        }
        queryBuilder.append(";");
        return queryBuilder.toString();
    }

    // 문자열이 숫자인지 확인하는 유틸리티 메서드
    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}
