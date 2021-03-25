package com.ulan.timetable.model;

public class Mark {
    private String s_name,credits, diligence, mid_term, end_term, grade;
    private int id;

    public Mark() {
    }

    public Mark(String s_name, String credits, String diligence, String mid_term, String end_term, String grade) {
        this.s_name = s_name;
        this.credits = credits;
        this.diligence = diligence;
        this.mid_term = mid_term;
        this.end_term = end_term;
        this.grade = grade;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getDiligence() {
        return diligence;
    }

    public void setDiligence(String diligence) {
        this.diligence = diligence;
    }

    public String getMid_term() {
        return mid_term;
    }

    public void setMid_term(String mid_term) {
        this.mid_term = mid_term;
    }

    public String getEnd_term() {
        return end_term;
    }

    public void setEnd_term(String end_term) {
        this.end_term = end_term;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
