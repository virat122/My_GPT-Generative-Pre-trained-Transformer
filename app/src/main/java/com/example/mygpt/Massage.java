package com.example.mygpt;

public class Massage {
    public static  String SENT_BY_ME="me";
    public static  String SENT_BY_bot="bot";
    String massage;
    String sendby;
    public Massage(String massage, String sendby) {
        this.massage = massage;
        this.sendby = sendby;
    }


    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getSendby() {
        return sendby;
    }

    public void setSendby(String sendby) {
        this.sendby = sendby;
    }


}
