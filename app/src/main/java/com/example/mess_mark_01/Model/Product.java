package com.example.mess_mark_01.Model;

import java.util.ArrayList;
import java.util.Stack;

public class Product {

    private String proID;
    private String proTag;
    private String proDescription;
    private String proStartingPrice;
    private String ownerID;
    private String proPicUrl;
    String city;

    public Product(String proTag, String proDescription, String proStartingPrice, String ownerID) {
        this.proTag = proTag;
        this.proDescription = proDescription;
        this.proStartingPrice = proStartingPrice;
        this.ownerID = ownerID;
    }

    public Product(){}
    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProTag(String proTag) {
        this.proTag = proTag;
    }

    public void setProDescription(String proDescription) {
        this.proDescription = proDescription;
    }

    public void setProStartingPrice(String proStartingPrice) {
        this.proStartingPrice = proStartingPrice;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public void setProPicUrl(String proPicUrl) {
        this.proPicUrl = proPicUrl;
    }

    public String getProID() {
        return proID;
    }

    public String getProTag() {
        return proTag;
    }

    public String getProDescription() {
        return proDescription;
    }

    public String getProStartingPrice() {
        return proStartingPrice;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getProPicUrl() {
        return proPicUrl;
    }

    public Product(String proTag, String proDescription, String proStartingPrice, String ownerID, String proPicUrl) {
        this.proID = proID;
        this.proTag = proTag;
        this.proDescription = proDescription;
        this.proStartingPrice = proStartingPrice;
        this.ownerID = ownerID;
        this.proPicUrl = proPicUrl;
    }

    public Product(String proTag, String proStartingPrice) {
        this.proTag = proTag;
        this.proStartingPrice = proStartingPrice;
    }
}