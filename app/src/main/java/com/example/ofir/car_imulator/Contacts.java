package com.example.ofir.car_imulator;

/**
 * Created by ofir on 15/02/2016.
 */
public class Contacts {
    int id;
    String[] names;
    String[] nums;

    public Contacts(int id, String names, String nums) {
        this.id = id;
        this.names = names.split(",");
        this.nums = nums.split(",");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public String[] getNums() {
        return nums;
    }

    public void setNums(String[] nums) {
        this.nums = nums;
    }
}
