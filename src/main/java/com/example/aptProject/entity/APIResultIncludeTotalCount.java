package com.example.aptProject.entity;

import java.util.List;

public class APIResultIncludeTotalCount {
    private List<APIResult> apiResults;
    private String totalCount;
    private int notExistCount;

    public APIResultIncludeTotalCount() {
    }

    @Override
    public String toString() {
        return "APIResultIncludeTotalCount{" +
                "apiResults=" + apiResults +
                ", totalCount='" + totalCount + '\'' +
                ", notExistCount=" + notExistCount +
                '}';
    }

    public List<APIResult> getApiResults() {
        return apiResults;
    }

    public void setApiResults(List<APIResult> apiResults) {
        this.apiResults = apiResults;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public int getNotExistCount() {
        return notExistCount;
    }

    public void setNotExistCount(int notExistCount) {
        this.notExistCount = notExistCount;
    }

    public APIResultIncludeTotalCount(List<APIResult> apiResults, String totalCount, int notExistCount) {
        this.apiResults = apiResults;
        this.totalCount = totalCount;
        this.notExistCount = notExistCount;
    }
}
