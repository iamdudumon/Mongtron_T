package com.example.mongtron_t.model;

public class OtherVO {
    private int id;
    private String nickName;
    private int age;
    private char sex;
    private boolean gpsState;
    private double latitude;
    private double longitude;
    private double distance;

    public OtherVO(int id, String nickName, int age, char sex, boolean gpsState, float latitude, float longitude, float distance) {
        this.id = id;
        this.nickName = nickName;
        this.age = age;
        this.sex = sex;
        this.gpsState = gpsState;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public int getAge() {
        return age;
    }

    public char getSex() {
        return sex;
    }

    public boolean isGpsState() {
        return gpsState;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }
}
