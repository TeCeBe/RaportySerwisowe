package com.ctrlaltelite.raportyserwisowe;

public class Report {
    private String id;
    private String title;
    private String content;
    private String date;
    private String time;
    private String place;
    private String userId;


    public Report() {
    }

    public Report(String title, String content, String date, String time, String place, String userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
        this.place = place;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

