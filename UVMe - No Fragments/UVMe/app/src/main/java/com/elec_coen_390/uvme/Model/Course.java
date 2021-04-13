package com.elec_coen_390.uvme.Model;

public class Course {

    private long id;
    private String title;
    private String code;

    @Override
    public String toString() {
        return "title:" + title + '\'' +
                "code:" + code;
    }

    public Course(long id, String title, String code) {
        this.id = id;
        this.title = title;
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}