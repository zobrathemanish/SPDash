package com.sajiloprint.dashboard.models;

/**
 * Created by kshitij on 16/1/18.
 */

public class CardsModel {


    public String cardname;
    public String cardimage;
    public String carddiscription;

    public CardsModel() {
    }

    public CardsModel( String cardname, String cardimage, String carddiscription) {
        this.cardname = cardname;
        this.cardimage = cardimage;
        this.carddiscription = carddiscription;
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

}
