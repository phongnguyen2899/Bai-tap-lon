package com.example.noteapp;

import java.util.Date;

public class Note {
    private int userid;
    private String title;
    private String content;
    private String gps;
    private String createdate;

    public Note(int userid, String title, String content, String gps, String createdate) {
        this.userid = userid;
        this.title = title;
        this.content = content;
        this.gps = gps;
        this.createdate = createdate;
    }
    public Note(String title,String content,String createdate){
        this.title=title;
        this.content=content;
        this.createdate=createdate;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }
}
