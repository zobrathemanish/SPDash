package com.sajiloprint.dashboard.models;

/**
 * Created by kshitij on 16/1/18.
 */

public class SubCardsmodel {


    public int cardid;
    public String cardname;
    public String cardimage;
    public String carddiscription;
    public float cardprice;
    public String title;
    public String size;
    public String bulkdescription;

    public SubCardsmodel() {
    }

    public SubCardsmodel(int cardid, String cardname, String cardimage, String carddiscription, float cardprice, String title, String size, String bulkdescription) {
        this.cardid = cardid;
        this.cardname = cardname;
        this.cardimage = cardimage;
        this.carddiscription = carddiscription;
        this.cardprice = cardprice;
        this.title = title;
        this.size = size;
        this.bulkdescription = bulkdescription;
    }

    public SubCardsmodel(int pid, String name, String image, String desc, float price) {
        this.cardid = pid;
        this.cardname = name;
        this.cardimage = image;
        this.carddiscription = desc;
        this.cardprice = price;

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
}
