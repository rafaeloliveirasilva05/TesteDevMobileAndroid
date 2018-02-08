package com.example.rafael.testedevmobileandroid.domain.domainDadosRefeicao;

import java.util.List;

/**
 * Created by rafael on 28/01/18.
 */

public class ItemRefeicao {

    public List<Foods> foods;
    private String energy;
    private String carbohydrate;
    private String fat;
    private String protein;

    public List<Foods> getFoods() {
        return foods;
    }

    public String getEnergy() {
        return energy;
    }

    public String getCarbohydrate() {
        return carbohydrate;
    }

    public String getFat() {
        return fat;
    }

    public String getProtein() {
        return protein;
    }
}
