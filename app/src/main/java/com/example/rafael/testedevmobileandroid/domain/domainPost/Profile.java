package com.example.rafael.testedevmobileandroid.domain.domainPost;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;

/**
 * Created by rafael on 25/01/18.
 */

public class Profile extends RealmObject implements Serializable, RealmModel {

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
