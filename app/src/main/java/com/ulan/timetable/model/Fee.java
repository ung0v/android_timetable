package com.ulan.timetable.model;

public class Fee {
    private String total;
    private String paid;
    private String overage;

    public Fee() {
    }

    public Fee(String total, String paid, String overage) {
        this.total = total;
        this.paid = paid;
        this.overage = overage;
    }

    public String getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Fee{" +
                "total='" + total + '\'' +
                ", paid='" + paid + '\'' +
                ", overage='" + overage + '\'' +
                '}';
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getOverage() {
        return overage;
    }

    public void setOverage(String overage) {
        this.overage = overage;
    }
}
