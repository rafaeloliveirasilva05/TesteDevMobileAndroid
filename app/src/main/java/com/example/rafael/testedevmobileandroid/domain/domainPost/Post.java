package com.example.rafael.testedevmobileandroid.domain.domainPost;


import com.example.rafael.testedevmobileandroid.domain.domainPost.Items;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rafael on 25/01/18.
 */

public class Post implements Serializable {

    private List<Items> items;
    private String t;
    private String p;

    public List<Items> getItems() {
        return items;
    }

    public String getT() {
        return t;
    }

    public String getP() {
        return p;
    }

}
