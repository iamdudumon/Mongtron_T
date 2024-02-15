package com.example.mongtron_t.model;

public class UserPosition {
    //private int id;
    private double latitude;
    private double longitude;
    private boolean gpsState;
    private int radiusInfo;                                 //타사용자 위치 표시 기준 (km 단위)

    private static UserPosition instance;

    public static synchronized UserPosition getInstance() {        //UserVO를 싱클톤 패턴을 이용해 전역변수화
        if (null == instance) {
            instance = new UserPosition();
        }
        return instance;
    }

    public UserPosition() {
       // this.id = UserVO.getInstance().getId();
        this.gpsState = false;
        this.radiusInfo = 5;                                //default 값은 5km
    }

    public void initUserPosition(){
        //this.id = -1;
        this.latitude= 0;
        this.longitude =0;
        this.gpsState = false;
        this.radiusInfo = 5;
    }

    //public int getId() { return id; }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean isGpsState() {
        return gpsState;
    }

    public int getRadiusInfo() {
        return radiusInfo;
    }

    public void setGpsPosition(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setGpsState() {              //현재 gps 값을 반대로 설정
        this.gpsState = !this.gpsState;
    }

    public void setRadiusInfo(int radiusInfo) {
        this.radiusInfo = radiusInfo;
    }

}
