package com.example.rafael.testedevmobileandroid.domain.domainDadosRefeicao;

/**
 * Created by rafael on 28/01/18.
 */

public class Foods {

    private String description;
    private String measure;
    private String amount;
    private String weight;
    private String energy;
    private String carbohydrate;
    private String fat;
    private String protein;


    public String getDescription() {
        return description;
    }

    public String getMeasure() {
        return measure;
    }

    public String getAmount() {
        return amount;
    }

    public String getWeight() {
        return weight;
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

    @Override
    public String toString() {
        return description;
    }
}
