package com.example.cbook;

public class forcart {

    private String name,amount,date,time,key;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public forcart(String name, String amount, String date, String time,String key) {
        this.name = name;
        this.amount = amount;
        this.key=key;
        this.date = date;
        this.time = time;
    }

    public forcart(){
    }




}
