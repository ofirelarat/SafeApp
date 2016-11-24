package com.example.ofir.car_imulator;

/**
 * Created by ofir on 21/02/2016.
 */
public class Note {
    int idNote;
    String[] name;

    public Note(int idNote, String name) {
        this.idNote=idNote;
        this.name = name.split(",");
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
    }

    public String[] getName() {
        return name;
    }

    public void setName(String[] name) {
        this.name = name;
    }
}
