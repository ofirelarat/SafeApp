package com.example.ofir.car_imulator;

/**
 * Created by ofir on 21/02/2016.
 */
public class Media {
    int idMedia;
    String[] name;

    public Media(int idMedia, String name) {
        this.idMedia=idMedia;
        this.name = name.split(",");
    }

    public int getIdMedia() {
        return idMedia;
    }

    public void setIdMedia(int idMedia) {
        this.idMedia = idMedia;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }

}
