package com.example.ofir.car_imulator;

/**
 * Created by ofir on 19/02/2016.
 */
public class Places {
    int idPlaces;
    String[] myplaces;

    public Places(int id, String myplaces) {
        this.idPlaces = id;
        this.myplaces = myplaces.split("-");
    }

    public int getIdPlaces() {
        return idPlaces;
    }

    public void setIdPlaces(int id) {
        this.idPlaces = id;
    }

    public String[] getMyplaces() {
        return myplaces;
    }

    public void setMyplaces(String[] myplaces) {
        this.myplaces = myplaces;
    }
}
