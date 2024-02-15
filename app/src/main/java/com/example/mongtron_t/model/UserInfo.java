package com.example.mongtron_t.model;

//VO class, 회원가입시 전송하고 자동 로그인을 위해 저장되는 data
public class UserInfo {
    private int id;         //서버에서 보내주는 고유한 id
    private String email;
    private String password;
    private String nickname;
    private int age;
    private char sex;
    private String nationality;
    private String embassyNum;

    private static UserInfo instance;
    public static synchronized UserInfo getInstance(){        //싱클톤 패턴을 이용해 전역변수화
        if(null == instance){
            instance = new UserInfo();
        }
        return instance;
    }

    public UserInfo(){                                        //미가입 사용자를 구분하기 위한 id
        this.id = -1;
    }

    public void initUser(){         //로그아웃시 기존의 정보를 다 삭제하기 위함
        this.id = -1;
        this.email = null;
        this.password = null;
        this.nickname = null;
        this.age = -1;
        this.sex = '0';
        this.nationality = "null";
        this.embassyNum = "null";
    }

    public void setUser(String email, String password, String nickname, int age, char sex, String nationality, String embassyNum){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        //this.phoneNo = phoneNo;
        this.sex = sex;
        this.nationality = nationality;
        this.embassyNum = embassyNum;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }


    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public char getSex() {
        return sex;
    }

    public String getNationality() { return nationality; }

    public String getPassword() { return password; }

    public String getEmbassyNum() { return embassyNum; }
}




