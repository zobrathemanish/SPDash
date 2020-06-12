package com.sajiloprint.dashboard.models;

/**
 * Created by kshitij on 16/1/18.
 */

public class DeleteCardsModel {


    public String cardname;


    public DeleteCardsModel() {
    }

    public DeleteCardsModel(String cardname) {
        this.cardname = cardname;

    }

    public String getCardname() {
        return cardname;
    }

    public void setCardname(String cardname) {
        this.cardname = cardname;
    }




}
