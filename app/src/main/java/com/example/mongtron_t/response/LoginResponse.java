package com.example.mongtron_t.response;

public class LoginResponse {
    private final int id;         //서버에서 보내주는 고유한 id
    private final String email;
    private final String nickname;
    private final int age;
    private final char sex;
    private final String nationality;
    private final String embassyNum;

    public LoginResponse(int id, String email, String nickname, int age, char sex, String nationality, String embassyNum) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.age = age;
        this.sex = sex;
        this.nationality = nationality;
        this.embassyNum = embassyNum;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickName() {
        return nickname;
    }

    public int getAge() {
        return age;
    }

    public char getSex() {
        return sex;
    }

    public String getNationality() {
        return nationality;
    }

    public String getEmbassyNum() {
        return embassyNum;
    }
}
