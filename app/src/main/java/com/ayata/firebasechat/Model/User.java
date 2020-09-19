package com.ayata.firebasechat.Model;

public class User {
    private String userId;
    private String userName;
    private String password;
    private String mobileNo;
    private String email;
    private Boolean online;
private String imgUrl;


    public String getUserId() {
        return userId;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public User(String userId, String userName, String password, String mobileNo, String email, String imgUrl,Boolean isOnline) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.mobileNo = mobileNo;
        this.email = email;
        this.imgUrl = imgUrl;
        this.online=isOnline;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
