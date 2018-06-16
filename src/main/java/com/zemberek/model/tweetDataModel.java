package com.zemberek.model;

import java.util.Date;


public class tweetDataModel {

    private Date createDate;
    private String user;
    private String city;
    private String country;
    private String countryCode;
    private String tweet;

    public tweetDataModel() {
    }

    public tweetDataModel(Date createDate, String user, String city, String country, String countryCode, String tweet) {
        this.createDate = createDate;
        this.user = user;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        this.tweet = tweet;
    }
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }
}
