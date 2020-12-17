package com.example.donLuHo.bean;

public class StartupsImageBean {
    private String id;
    private String title;
    private String url;
    private int seconds;
    private String sort;
    private String created_at;
    private String updated_at;
    private String handicap_code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getHandicap_code() {
        return handicap_code;
    }

    public void setHandicap_code(String handicap_code) {
        this.handicap_code = handicap_code;
    }
}
