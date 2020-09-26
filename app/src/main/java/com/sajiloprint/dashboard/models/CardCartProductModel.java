package com.sajiloprint.dashboard.models;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by Manish on 19/1/18.
 */
@Keep
public class CardCartProductModel implements Serializable {

    private int prid,no_of_items;
    private String useremail,usermobile,prname,prprice,primage,prdesc,message_body,uploadimageid;
    private String card_name,  card_job_title, card_company_name, card_company_address,
     card_city, card_area, card_email, card_website, card_number, card_formjob;
    private float deliveryprice;
    private String finalcolor,finalshape;
    private String shopemail;


    public CardCartProductModel() {
    }

    public CardCartProductModel(int prid, String useremail, String usermobile, String prname, String prprice, String prdesc,
                                String uploadimageid, String primage, int no_of_items, float deliveryprice) {

        this.prid = prid;
        this.useremail = useremail;
        this.usermobile = usermobile;
        this.prname = prname;
        this.prprice = prprice;
        this.primage = primage;
        this.prdesc = prdesc;
        this.uploadimageid = uploadimageid;
        this.no_of_items = no_of_items;
        this.deliveryprice=deliveryprice;

    }

    public CardCartProductModel(int prid, int no_of_items, String useremail, String usermobile, String prname, String prprice, String prdesc, String shopemail,
                                String card_name, String card_company_name, String card_company_address,
                                String card_formjob, String card_email, String card_website,
                                String card_number, String custommessage, String uploadimageid, String primage, float deliveryprice) {

        this.prid = prid;
        this.no_of_items = no_of_items;
        this.useremail = useremail;
        this.usermobile = usermobile;
        this.prname = prname;
        this.prprice = prprice;
        this.primage = primage;
        this.prdesc = prdesc;
        this.message_body = custommessage;
        this.uploadimageid = uploadimageid;
        this.card_name=card_name;
        this.card_company_address=card_company_address;
        this.card_company_name = card_company_name;
        this.card_company_address = card_company_address;
        this.card_formjob = card_formjob;
        this.card_email = card_email;
        this.card_website = card_website;
        this.card_number = card_number;
        this.deliveryprice=deliveryprice;
        this.shopemail = shopemail;

    }

    public CardCartProductModel(int prid, int no_of_items, String useremail, String usermobile, String prname, String prprice, String prdesc,
                                String custommessage, String uploadimageid, String primage, float deliveryprice, String finalcolor, String finalshape) {

        this.prid = prid;
        this.no_of_items = no_of_items;
        this.useremail = useremail;
        this.usermobile = usermobile;
        this.prname = prname;
        this.prprice = prprice;
        this.primage = primage;
        this.prdesc = prdesc;
        this.message_body = custommessage;
        this.uploadimageid = uploadimageid;
        this.deliveryprice=deliveryprice;
        this.finalcolor = finalcolor;
        this.finalshape = finalshape;
    }

    public CardCartProductModel(int prid, int no_of_items, String useremail, String usermobile, String prname, String prprice, String prdesc,
                                String custommessage, String uploadimageid, String primage, float deliveryprice) {

        this.prid = prid;
        this.no_of_items = no_of_items;
        this.useremail = useremail;
        this.usermobile = usermobile;
        this.prname = prname;
        this.prprice = prprice;
        this.primage = primage;
        this.prdesc = prdesc;
        this.message_body = custommessage;
        this.uploadimageid = uploadimageid;
        this.deliveryprice=deliveryprice;

    }

    public CardCartProductModel(int prid, int no_of_items, String useremail, String usermobile, String prname, String prprice, String prdesc, String shopemail,
                                String custommessage, String uploadimageid, String primage, float deliveryprice) {
        this.prid = prid;
        this.no_of_items = no_of_items;
        this.useremail = useremail;
        this.usermobile = usermobile;
        this.prname = prname;
        this.prprice = prprice;
        this.primage = primage;
        this.prdesc = prdesc;
        this.message_body = custommessage;
        this.uploadimageid = uploadimageid;
        this.deliveryprice = deliveryprice;
        this.shopemail = shopemail;

    }


    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public int getNo_of_items() {
        return no_of_items;
    }

    public void setNo_of_items(int no_of_items) {
        this.no_of_items = no_of_items;
    }

    public String getPrdesc() {
        return prdesc;
    }

    public void setPrdesc(String prdesc) {
        this.prdesc = prdesc;
    }

    public int getPrid() {
        return prid;
    }

    public void setPrid(int prid) {
        this.prid = prid;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getPrname() {
        return prname;
    }

    public void setPrname(String prname) {
        this.prname = prname;
    }

    public String getPrprice() {
        return prprice;
    }

    public void setPrprice(String prprice) {
        this.prprice = prprice;
    }

    public String getPrimage() {
        return primage;
    }

    public void setPrimage(String primage) {
        this.primage = primage;
    }

    public String getimageid() {
        return uploadimageid;
    }

    public void setimageid(String imageid) {
        this.uploadimageid = imageid;
    }

    public String get_job_title() {
        return card_job_title;
    }

    public void set_job_title(String job_title) {
        this.card_job_title = card_job_title;
    }

    public String getCard_company_name() {
        return card_company_name;
    }

    public void setCard_company_name(String card_company_name) {
        this.card_company_name = card_company_name;
    }

    public String getCard_company_address() {
        return card_company_address;
    }

    public void setCard_company_address(String company_address) {
        this.card_company_address = company_address;
    }

    public String getCard_formjob() {
        return card_formjob;
    }

    public void setCard_formjob(String formjob) {
        this.card_formjob = formjob;
    }

    public String getCard_area() {
        return card_area;
    }

    public void setCard_area(String card_area) {
        this.card_area = card_area;
    }

    public String getCard_email() {
        return card_email;
    }

    public void setCard_email(String email) {
        this.card_email = email;
    }

    public String getCard_website() {
        return card_website;
    }

    public void setCard_website(String website) {
        this.card_website = website;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String number) {
        this.card_number = number;
    }

    public String getCard_name() {
        return card_name;
    }

    public void setCard_name(String card_name) {
        this.card_name = card_name;
    }

    public float getDeliveryprice() {
        return deliveryprice;
    }

    public void setDeliveryprice(float deliveryprice) {
        this.deliveryprice = deliveryprice;
    }

    public String getfinalcolor() {
        return finalcolor;
    }

    public void setFinalcolor(String finalcolor) {
        this.finalcolor = finalcolor;
    }

    public String getFinalshape() {
        return finalshape;
    }

    public void setFinalshape(String finalshape) {
        this.finalshape = finalshape;
    }

    public String getShopemail() {
        return shopemail;
    }

    public void setShopemail(String shopemail) {
        this.shopemail = shopemail;
    }


}
