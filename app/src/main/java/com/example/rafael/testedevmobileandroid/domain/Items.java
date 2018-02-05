package com.example.rafael.testedevmobileandroid.domain;

import java.io.Serializable;


/**
 * Created by rafael on 25/01/18.
 */

public class Items implements Serializable{

    private Profile profile;
    private String image;
    private String meal;
    private String date;
    private String energy;
    private String feedHash;
    private String id;

    public String getId() {
        return id;
    }

    private boolean curtiu =false;

    public Profile getProfile() {
        return profile;
    }
    public String getImage() {
        return image;
    }

    public String getMeal() {
        return meal;
    }

    public String getDate() {
        return date;
    }

    public String getEnergy() {
        return energy;
    }

    public String getFeedHash() {
        return feedHash;
    }

    public boolean isCurtiu() {
        return curtiu;
    }

    public void setCurtiu(boolean curtiu) {
        this.curtiu = curtiu;
    }
}
