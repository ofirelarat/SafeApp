package com.example.ofir.car_imulator;

/**
 * Created by ofir on 15/02/2016.
 */
import android.provider.BaseColumns;


public final class DBContract {
    public DBContract() {}

    public static abstract class DBContacts {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_ID= "id";
        public static final String COLUMN_NAME_NAMES = "names";
        public static final String COLUMN_NAME_NUMS = "nums";
    }

    public static abstract class DBPlaces {
        public static final String TABLE_NAME = "places";
        public static final String COLUMN_NAME_IDP= "idP";
        public static final String COLUMN_NAME_NAMESP = "myPlaces";
    }

    public static abstract class DBMedia {
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_NAME_ID= "idM";
        public static final String COLUMN_NAME_NAME = "nameM";
    }

    public static abstract class DBNote {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_ID= "idN";
        public static final String COLUMN_NAME_NAME = "nameN";
    }

}
