package com.example.rafael.testedevmobileandroid.domain.domainPost;

import java.io.Serializable;

/**
 * Created by rafael on 25/01/18.
 */

public class Profile implements Serializable{

    private String id;
    private String image;
    private String name;
    private String general_goal;

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getGeneral_goal() {
        return general_goal;
    }
}
