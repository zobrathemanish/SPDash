package com.sajiloprint.dashboard.models;

import androidx.annotation.Keep;

import java.io.Serializable;

/**
 * Created by Manish on 19/1/18.
 */
@Keep
public class ImageUploadModel implements Serializable {

    private String uploadimageid;
    String imageLocation;
    String quantity;


    public ImageUploadModel() {
    }

    public ImageUploadModel(String uploadimageid, String imageLocation) {
        this.uploadimageid = uploadimageid;
        this.imageLocation = imageLocation;

    }







    public String getFimageLocation() {
        return imageLocation;
    }

    public void setFimageLocation(String primage) {
        this.imageLocation= imageLocation;
    }

    public String getUploadimageid() {
        return uploadimageid;
    }

    public void setUploadimageid(String uploadimageid) {
        this.uploadimageid= uploadimageid;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity= quantity;
    }


}
