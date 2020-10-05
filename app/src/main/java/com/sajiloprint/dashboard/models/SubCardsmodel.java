package com.sajiloprint.dashboard.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kshitij on 16/1/18.
 */

public class SubCardsmodel implements Serializable {


    public int cardid;
    public String cardname;
    public String cardimage;
    public String carddiscription;
    public float cardprice;
    public String title;
    public String size;
    public float height;
    public float width;
    public String bulkdescription;
    public List<String> productimages;
    public String shopemail;
    public String uploadimageid;
    private String shopmobile,shopname;

    public SubCardsmodel() {
    }

    public SubCardsmodel(int cardid, String cardname, String cardimage, String carddiscription, float cardprice, String title, String size,float height, float width, String bulkdescription, String shopemail, String shopname,String shopmobile, List<String> productimages) {
        this.cardid = cardid;
        this.cardname = cardname;
        this.cardimage = cardimage;
        this.carddiscription = carddiscription;
        this.cardprice = cardprice;
        this.title = title;
        this.size = size;
        this.bulkdescription = bulkdescription;
        this.shopemail = shopemail;
        this.productimages = productimages;
        this.height = height;
        this.width = width;
        this.shopmobile = shopmobile;
        this.shopname = shopname;
    }

    public SubCardsmodel(int pid, String name, String image, String desc, float price, String shopemail, String shopname,String shopmobile, String uploadimageid) {
        this.cardid = pid;
        this.cardname = name;
        this.cardimage = image;
        this.carddiscription = desc;
        this.cardprice = price;
        this.shopemail = shopemail;
        this.uploadimageid = uploadimageid;
        this.shopmobile = shopmobile;
        this.shopname = shopname;

    }

    public SubCardsmodel(int pid, String name, String image, String desc, float price,String bulkdescription, String shopemail, String shopname,String shopmobile, List<String> productimages) {
        this.cardid = pid;
        this.cardname = name;
        this.cardimage = image;
        this.carddiscription = desc;
        this.cardprice = price;
        this.shopemail = shopemail;
        this.productimages = productimages;
        this.bulkdescription = bulkdescription;
        this.shopmobile = shopmobile;
        this.shopname = shopname;
    }


    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }

    public String getCardimage() {
        return cardimage;
    }

    public void setCardimage(String cardimage) {
        this.cardimage = cardimage;
    }

    public String getCarddiscription() {
        return carddiscription;
    }

    public void setCarddiscription(String carddiscription) {
        this.carddiscription = carddiscription;
    }

    public float getCardprice() {
        return cardprice;
    }

    public void setCardprice(float cardprice) {
        this.cardprice = cardprice;
    }

    public String getShopemail() {
        return shopemail;
    }

    public void setShopemail(String shopemail) {
        this.shopemail = shopemail;
    }

    public String getUploadimageid() {
        return uploadimageid;
    }

    public void setUploadimageid(String uploadimageid) {
        this.uploadimageid = uploadimageid;
    }

    public List<String> getProductimages() {
        return productimages;
    }

    public void setProductimages(List<String> productimages) {
        this.productimages = productimages;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getBulkdescription() {
        return bulkdescription;
    }

    public void setBulkdescription(String bulkdescription) {
        this.bulkdescription = bulkdescription;
    }

    public int getCardid() {return cardid;}
    public void setCardid(int cardid) {this.cardid = cardid;}

    public String getShopmobile() {
        return shopmobile;
    }

    public void setShopmobile(String shopmobile) {
        this.shopmobile = shopmobile;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }







}
