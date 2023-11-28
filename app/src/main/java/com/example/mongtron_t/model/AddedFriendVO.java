package com.example.mongtron_t.model;

import java.util.List;

public class AddedFriendVO{
    static public List<AddedFriendVO> friendsList;

    private final int friendId;
    private final String friendNickName;
    private final char friendSex;
    private boolean friendGpsState;
    private double distance;

    public AddedFriendVO(int friendId, String friendNickName, char sex, boolean friendGpsState, double distance) {
        this.friendId = friendId;
        this.friendNickName = friendNickName;
        this.friendSex = sex;
        this.friendGpsState = friendGpsState;
        this.distance = distance;
    }

    public int getFriendId() {
        return friendId;
    }

    public String getFriendNickName() {
        return friendNickName;
    }

    public char getFriendSex() {
        return friendSex;
    }

    public boolean isFriendGpsState() {
        return friendGpsState;
    }

    public double getDistance() {
        return distance;
    }

    public void setFriendGpsState() {
        this.friendGpsState = !this.friendGpsState;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
