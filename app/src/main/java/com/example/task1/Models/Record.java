package com.example.task1.Models;

import com.google.gson.annotations.SerializedName;

public class Record {

    @SerializedName("score")
    private int score;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private  double lon;
    @SerializedName("city")
    private String city;
    @SerializedName("country")
    private String country;
    @SerializedName("difficulty")
    private String difficulty;
    @SerializedName("type")
    private String type;

    public Record(int score)
    {
        this.score=score;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public Record setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Record setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Record{" +
                "score=" + score +
                ", lat=" + lat +
                ", lon=" + lon +
                ", city=" + city +
                ", country=" + country +
                ", difficulty=" + difficulty +
                ", type=" + type +
                '}';
    }
}
