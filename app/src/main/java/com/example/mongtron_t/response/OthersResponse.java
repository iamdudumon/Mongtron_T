package com.example.mongtron_t.response;

import com.example.mongtron_t.model.OtherVO;

import java.util.List;

public class OthersResponse {
    private int code;
    private int length;
    private List<OtherVO> users;

    private static OthersResponse instance;

    public static synchronized OthersResponse getInstance(){        //싱클톤 패턴을 이용해 전역변수화
        if(null == instance){
            instance = new OthersResponse();
        }
        return instance;
    }

    public OthersResponse(){
        this.length = 0;
    }

    public void initListPosition(){
        int code = 0;
        this.length = 0;
        if(users != null)
            users.clear();
    }

    public int getCode() {
        return code;
    }

    public int getLength() {
        return length;
    }

    public List<OtherVO> getUsers() {
        if(users == null)
            return null;
        return users;
    }

    public void setUsers(List<OtherVO> users){
        this.users = users;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
