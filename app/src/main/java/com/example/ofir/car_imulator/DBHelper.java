package com.example.ofir.car_imulator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ofir on 15/02/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    SQLiteDatabase db;

    private static final String SQL_CREATE_CONTACTS =
            "CREATE TABLE " + DBContract.DBContacts.TABLE_NAME + "(" +
                    DBContract.DBContacts.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DBContract.DBContacts.COLUMN_NAME_NAMES + " TEXT," +
                    DBContract.DBContacts.COLUMN_NAME_NUMS + " TEXT)";

    private static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + DBContract.DBContacts.TABLE_NAME;

    private static final String SQL_CREATE_PLACES =
            "CREATE TABLE " + DBContract.DBPlaces.TABLE_NAME + "(" +
                    DBContract.DBPlaces.COLUMN_NAME_IDP + " INTEGER PRIMARY KEY," +
                    DBContract.DBPlaces.COLUMN_NAME_NAMESP + " TEXT)";

    private static final String SQL_DELETE_PLACES =
            "DROP TABLE IF EXISTS " + DBContract.DBPlaces.TABLE_NAME;

    private static final String SQL_CREATE_MEDIA =
            "CREATE TABLE " + DBContract.DBMedia.TABLE_NAME + "(" +
                    DBContract.DBMedia.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DBContract.DBMedia.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_MEDIA =
            "DROP TABLE IF EXISTS " + DBContract.DBMedia.TABLE_NAME;

    private static final String SQL_CREATE_NOTE =
            "CREATE TABLE " + DBContract.DBNote.TABLE_NAME + "(" +
                    DBContract.DBNote.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    DBContract.DBNote.COLUMN_NAME_NAME + " TEXT)";

    private static final String SQL_DELETE_Note =
            "DROP TABLE IF EXISTS " + DBContract.DBNote.TABLE_NAME;


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ImulatorC.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS);
        db.execSQL(SQL_CREATE_PLACES);
        db.execSQL(SQL_CREATE_MEDIA);
        db.execSQL(SQL_CREATE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_CONTACTS);
        db.execSQL(SQL_DELETE_PLACES);
        db.execSQL(SQL_DELETE_MEDIA);
        db.execSQL(SQL_DELETE_Note);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //add contacts
    public boolean AddContacts(Contacts contacts) {
        String namesS=contacts.getNames()[0]+",";
        String numsS=contacts.getNums()[0]+",";
        for(int i=1;i<4;i++){
            namesS+=contacts.getNames()[i]+",";
            numsS+=contacts.getNums()[i]+",";
        }
        if(FindContactsById(contacts.id) == null) {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBContract.DBContacts.COLUMN_NAME_ID, contacts.getId());
            values.put(DBContract.DBContacts.COLUMN_NAME_NAMES, namesS);
            values.put(DBContract.DBContacts.COLUMN_NAME_NUMS, numsS);

            db.insert(DBContract.DBContacts.TABLE_NAME, null, values);
            db.close();

            return true;
        }

        db.close();

        return false;
    }


    //find contacts by id
    public Contacts FindContactsById(int id){
        db = getReadableDatabase();

        String[] projection = {
                DBContract.DBContacts.COLUMN_NAME_ID,
                DBContract.DBContacts.COLUMN_NAME_NAMES,
                DBContract.DBContacts.COLUMN_NAME_NUMS
        };

        String selection = DBContract.DBContacts.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                DBContract.DBContacts.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        try{
            if (c != null && c.moveToNext()){
                Contacts t = new Contacts(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2)
                );

                db.close();

                return t;
            }
        } catch(Exception e){
            return null;
        } finally {
            if (c != null)
                c.close();
        }

        return null;
    }

    //update contacts
    public void UpdateContacts(Contacts contacts){
        String namesS=contacts.getNames()[0]+",";
        String numsS=contacts.getNums()[0]+",";
        for(int i=1;i<4;i++){
            namesS+=contacts.getNames()[i]+",";
            numsS+=contacts.getNums()[i]+",";
        }
        ContentValues values = new ContentValues();
        values.put(DBContract.DBContacts.COLUMN_NAME_ID, contacts.getId());
        values.put(DBContract.DBContacts.COLUMN_NAME_NAMES, namesS);
        values.put(DBContract.DBContacts.COLUMN_NAME_NUMS, numsS);

        db = getWritableDatabase();
        db.update(DBContract.DBContacts.TABLE_NAME, values, DBContract.DBContacts.COLUMN_NAME_ID + "=" + contacts.getId(), null);
        db.close();
    }

    //add places
    public boolean AddPlaces(Places places) {
        String namesP=places.getMyplaces()[0]+"-";
        for(int i=1;i<4;i++){
            namesP+=places.getMyplaces()[i]+"-";
        }
        if(FindPlacesById(places.idPlaces) == null) {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DBContract.DBPlaces.COLUMN_NAME_IDP, places.getIdPlaces());
            values.put(DBContract.DBPlaces.COLUMN_NAME_NAMESP, namesP);

            db.insert(DBContract.DBPlaces.TABLE_NAME, null, values);
            db.close();

            return true;
        }

        db.close();

        return false;
    }

    //find Places by id
    public Places FindPlacesById(int id){
        db = getReadableDatabase();

        String[] projection = {
                DBContract.DBPlaces.COLUMN_NAME_IDP,
                DBContract.DBPlaces.COLUMN_NAME_NAMESP,
        };

        String selection = DBContract.DBPlaces.COLUMN_NAME_IDP + "=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                DBContract.DBPlaces.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        try{
            if (c != null && c.moveToNext()){
                Places t = new Places(
                        c.getInt(0),
                        c.getString(1)
                );

                db.close();

                return t;
            }
        } catch(Exception e){
            return null;
        } finally {
            if (c != null)
                c.close();
        }

        return null;
    }

    //update Places
    public void UpdatePlaces(Places places){
        String namesP=places.getMyplaces()[0]+"-";
        for(int i=1;i<4;i++){
            namesP+=places.getMyplaces()[i]+"-";
        }
        ContentValues values = new ContentValues();
        values.put(DBContract.DBPlaces.COLUMN_NAME_IDP, places.getIdPlaces());
        values.put(DBContract.DBPlaces.COLUMN_NAME_NAMESP, namesP);


        db = getWritableDatabase();
        db.update(DBContract.DBPlaces.TABLE_NAME, values, DBContract.DBPlaces.COLUMN_NAME_IDP + "=" + places.getIdPlaces(), null);
        db.close();
    }

    //add media
    public boolean AddMedia(Media media) {
        String namesM=media.getName()[0]+",";
        for(int i=1;i<media.getName().length;i++){
            namesM+=media.getName()[i]+",";
        }

        if(FindMediaByName(media.getIdMedia()) == null) {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.DBMedia.COLUMN_NAME_ID, media.getIdMedia());
            values.put(DBContract.DBMedia.COLUMN_NAME_NAME, namesM);

            db.insert(DBContract.DBMedia.TABLE_NAME, null, values);
            db.close();

            return true;
        }

        db.close();

        return false;
    }

    //find Media by id
    public Media FindMediaByName(int id){
        db = getReadableDatabase();

        String[] projection = {
                DBContract.DBMedia.COLUMN_NAME_ID,
                DBContract.DBMedia.COLUMN_NAME_NAME,
        };

        String selection = DBContract.DBMedia.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                DBContract.DBMedia.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        try{
            if (c != null && c.moveToNext()){
                Media t = new Media(
                        c.getInt(0),
                        c.getString(1)
                );

                db.close();

                return t;
            }
        } catch(Exception e){
            return null;
        } finally {
            if (c != null)
                c.close();
        }

        return null;
    }


    //update Media
    public void UpdateMedia(Media media){
        String namesN=media.getName()[0]+",";
        if(media.getName().length>1) {
            for (int i = 1; i < media.getName().length - 1; i++) {
                namesN += media.getName()[i] + ",";
            }
            namesN += media.getName()[media.getName().length - 1];
        }
        ContentValues values = new ContentValues();
        values.put(DBContract.DBMedia.COLUMN_NAME_ID, media.getIdMedia());
        values.put(DBContract.DBMedia.COLUMN_NAME_NAME, namesN);

        db = getWritableDatabase();
        db.update(DBContract.DBMedia.TABLE_NAME, values, DBContract.DBMedia.COLUMN_NAME_ID + "=" + media.getIdMedia(), null);
        db.close();
    }

    //delete media from the database
    public void DeleteMedia(int id){
        String selection = DBContract.DBMedia.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(DBContract.DBMedia.TABLE_NAME, selection, selectionArgs);
        db.close();
    }

    //add note
    public boolean AddNote(Note note) {
        String namesN=note.getName()[0]+",";
        for(int i=1;i<note.getName().length;i++){
            namesN+=note.getName()[i]+",";
        }
        if(FindNoteByName(note.getIdNote()) == null) {
            db = getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DBContract.DBNote.COLUMN_NAME_ID, note.getIdNote());
            values.put(DBContract.DBNote.COLUMN_NAME_NAME, namesN);

            db.insert(DBContract.DBNote.TABLE_NAME, null, values);
            db.close();

            return true;
        }

        db.close();

        return false;
    }

    //find note by id
    public Note FindNoteByName(int id){
        db = getReadableDatabase();

        String[] projection = {
                DBContract.DBNote.COLUMN_NAME_ID,
                DBContract.DBNote.COLUMN_NAME_NAME,
        };

        String selection = DBContract.DBNote.COLUMN_NAME_ID + "=?";
        String[] selectionArgs = { String.valueOf(id) };

        Cursor c = db.query(
                DBContract.DBNote.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        try{
            if (c != null && c.moveToNext()){
                Note t = new Note(
                        c.getInt(0),
                        c.getString(1)
                );

                db.close();

                return t;
            }
        } catch(Exception e){
            return null;
        } finally {
            if (c != null)
                c.close();
        }

        return null;
    }

    public Note[] GetAll(){
        db = getReadableDatabase();

        String[] projection = {
                DBContract.DBNote.COLUMN_NAME_ID,
                DBContract.DBNote.COLUMN_NAME_NAME
        };

        Cursor c = db.query(
                DBContract.DBNote.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        try{
            if (c != null && c.moveToNext()){
                List<Note> notes = new ArrayList<>();

                do {
                    Note t = new Note(
                            c.getInt(0),
                            c.getString(1)
                    );

                    notes.add(t);
                } while(c.moveToNext());

                db.close();
                Note[] ret = new Note[notes.size()];
                notes.toArray(ret);

                return ret;
            }

            return  null;
        } catch(Exception e){
            return null;
        } finally{
            if (c != null)
                c.close();
        }
    }

    //update Note
    public void UpdateNote(Note note){
        String namesN=note.getName()[0]+",";
        if(note.getName().length>1) {
            for (int i = 1; i < note.getName().length - 1; i++) {
                namesN += note.getName()[i] + ",";
            }
            namesN += note.getName()[note.getName().length - 1];
        }
        ContentValues values = new ContentValues();
        values.put(DBContract.DBNote.COLUMN_NAME_ID, note.getIdNote());
        values.put(DBContract.DBNote.COLUMN_NAME_NAME, namesN);


        db = getWritableDatabase();
        db.update(DBContract.DBNote.TABLE_NAME, values, DBContract.DBNote.COLUMN_NAME_ID + "=" + note.getIdNote(), null);
        db.close();
    }

    //delete note from the database
    public void DeleteNote(int note_id){
        String selection = DBContract.DBNote.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(note_id) };
        db.delete(DBContract.DBNote.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
