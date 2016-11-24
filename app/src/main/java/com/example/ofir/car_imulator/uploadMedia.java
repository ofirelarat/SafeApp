package com.example.ofir.car_imulator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

public class uploadMedia extends AppCompatActivity {

    public static String path= Environment.getExternalStorageDirectory().getAbsolutePath()+"/myImulatorMusic";
    public static File Dir=new File(path);
    DBHelper db;
    String FilePath;
    private static final int PICKFILE_RESULT_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_media);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(231, 76, 60)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
                try {
                    startActivityForResult(i, 101);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "sorry,your device doesn't support speech language", Toast.LENGTH_LONG).show();
                }
            }
        });


        AndroidAuthSession session = buildSession();
        dropboxAPI = new DropboxAPI<AndroidAuthSession>(session);

        Dir.mkdirs();

        db = new DBHelper(this);
    }


    public void onClickUpload(View view){

        EditText songE=(EditText)findViewById(R.id.mediaUploadEditText);
        String song=songE.getText().toString();
        try {

            DownloadFromDropboxFromPath(path + "/" + song + ".mp3", "myMusic/" + song + ".mp3");

          /*  Media mediaO=db.FindMediaByName(0);
            Media media;
            if(mediaO!=null) {
                String namesN=mediaO.getName()[0]+",";
                for(int i=1;i<mediaO.getName().length;i++) {
                    namesN += mediaO.getName()[i] + ",";
                }

                media = new Media(0, namesN + song);
            }
            else {
                media = new Media(0,song);
            }
            if(db.AddMedia(media)){}
            else {
                db.UpdateMedia(media);
            }*/


        }catch (Throwable t){}
    }


    public void onClickStorege(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/.mp3");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);

    }



    public void takeAction(String action){
        if((action.intern()==("make a call").intern() || action.intern()==("call").intern() || action.intern()==("call contact").intern())){
            Intent i=new Intent(this,phoneCall.class);
            startActivity(i);
            return;
        }
        else if((action.intern()==("send SMS").intern() || action.intern()==("SMS").intern() || action.intern()==("send a text message").intern() || action.intern()==("send a message").intern())){
            Intent i=new Intent(this,SMS.class);
            startActivity(i);
            return;
        }
        else if((action.intern()==("my playlist").intern() || action.intern()==("my media").intern() || action.intern()==("open media").intern() || action.intern()==("my podcast").intern())){
            Intent i=new Intent(this,playMedia.class);
            startActivity(i);
        }
        else if((action.intern()==("notes").intern() || action.intern()==("my notes").intern() || action.intern()==("open note").intern())){
            Intent i=new Intent(this,notes.class);
            startActivity(i);
        }
        else if((action.intern()==("what's the weather").intern() || action.intern()==("weather").intern() || action.intern()==("what is the weather").intern())){
            Intent i=new Intent(this,weather.class);
            startActivity(i);
        }
        else if((action.intern()==("home").intern() || action.intern()==("homepage").intern() || action.intern()==("open home page").intern())){
            Intent i=new Intent(this,homePage.class);
            startActivity(i);
        }
        else Toast.makeText(this,"the action:'"+action+"' not exist try something else",Toast.LENGTH_LONG).show();
    }




    static DropboxAPI<AndroidAuthSession> dropboxAPI;
    private static final String APP_KEY = "4hjayo6tiu6nfhi";
    private static final String APP_SECRET = "f1sw215pvkz6kna";
    private static final String ACCESSTOKEN = "1JwFBQd-43AAAAAAAAAARrgzdiqPu4Hkl-W4jbMiLkC2C9X18q19aW-NhfvfW6VQ";
    private DropboxAPI.UploadRequest request;
    private AndroidAuthSession buildSession()
    {
        AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeyPair);
        session.setOAuth2AccessToken(ACCESSTOKEN);
        return session;
    }
    static final int UploadFromSelectApp = 9501;
    static final int UploadFromFilemanager = 9502;
    public static String DropboxUploadPathFrom = "";
    public static String DropboxUploadName = "";
    public static String DropboxDownloadPathFrom = "";
    public static String DropboxDownloadPathTo = "";

    private void UploadToDropboxFromPath (String uploadPathFrom, String uploadPathTo)
    {
        Toast.makeText(getApplicationContext(), "Upload file ...", Toast.LENGTH_SHORT).show();
        final String uploadPathF = uploadPathFrom;
        final String uploadPathT = uploadPathTo;
        Thread th = new Thread(new Runnable()
        {
            public void run()
            {
                File tmpFile = null;
                try
                {
                    tmpFile = new File(uploadPathF);
                }
                catch (Exception e) {e.printStackTrace();}
                FileInputStream fis = null;
                try
                {
                    fis = new FileInputStream(tmpFile);
                }
                catch (FileNotFoundException e) {e.printStackTrace();}
                try
                {
                    dropboxAPI.putFileOverwrite(uploadPathT, fis, tmpFile.length(), null);
                }
                catch (Exception e) {}
                getMain().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "File successfully uploaded.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        th.start();
    }

    private void UploadToDropboxFromSelectedApp (String uploadName)
    {
        DropboxUploadName = uploadName;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Upload from ..."), UploadFromSelectApp);
    }

    private void UploadToDropboxFromFilemanager (String uploadName)
    {
        DropboxUploadName = uploadName;
        Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        intent.putExtra("CONTENT_TYPE", "*/*");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(intent, UploadFromFilemanager);
    }

    private void DownloadFromDropboxFromPath (String downloadPathTo, String downloadPathFrom)
    {
        DropboxDownloadPathTo = downloadPathTo;
        DropboxDownloadPathFrom = downloadPathFrom;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Download file ...", Toast.LENGTH_SHORT).show();
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        File file = new File(DropboxDownloadPathTo + DropboxDownloadPathFrom.substring(DropboxDownloadPathFrom.lastIndexOf('.')));
                        if (file.exists()) file.delete();
                        try {
                            FileOutputStream outputStream = new FileOutputStream(file);
                            uploadMedia.dropboxAPI.getFile(DropboxDownloadPathFrom, null, outputStream, null);
                            getMain().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "File successfully downloaded.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == UploadFromFilemanager)
        {
            final Uri currFileURI = intent.getData();
            final String pathFrom = currFileURI.getPath();
            Toast.makeText(getApplicationContext(), "Upload file ...", Toast.LENGTH_SHORT).show();
            Thread th = new Thread(new Runnable()
            {
                public void run()
                {
                    getMain().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            UploadToDropboxFromPath(pathFrom, "/db-test/" + DropboxUploadName + pathFrom.substring(pathFrom.lastIndexOf('.')));
                            Toast.makeText(getApplicationContext(), "File successfully uploaded.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            th.start();
        }
        if (requestCode == UploadFromSelectApp)
        {
            Toast.makeText(getApplicationContext(), "Upload file ...", Toast.LENGTH_SHORT).show();
            final Uri uri = intent.getData();

            DropboxUploadPathFrom = getPath(getApplicationContext(), uri);
            if(DropboxUploadPathFrom == null) {
                DropboxUploadPathFrom = uri.getPath();
            }
            Thread th = new Thread(new Runnable(){
                public void run() {
                    try
                    {
                        final File file = new File(DropboxUploadPathFrom);
                        InputStream inputStream = getContentResolver().openInputStream(uri);

                        dropboxAPI.putFile("/db-test/" + DropboxUploadName + file.getName().substring(file.getName().lastIndexOf("."),
                                file.getName().length()), inputStream, file.length(), null, new ProgressListener(){
                            @Override
                            public long progressInterval() {return 100;}
                            @Override
                            public void onProgress(long arg0, long arg1){}
                        });
                        getMain().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Toast.makeText(getApplicationContext(), "File successfully uploaded.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {e.printStackTrace();}
                }
            });
            th.start();
        }

        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK && intent != null) {
                    ArrayList<String> result = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    takeAction(result.get(0));
                }
                break;
            case PICKFILE_RESULT_CODE:
                if(resultCode ==RESULT_OK){
                    FilePath = intent.getData().getPath();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);

    }

    public String getPath(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA, MediaStore.Video.Media.DATA, MediaStore.Audio.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            if(s!=null) {
                cursor.close();
                return s;
            }
        }
        catch(Exception e){}
        try {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            if(s!=null) {
                cursor.close();
                return s;
            }
        }
        catch(Exception e){}
        try {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            String s = cursor.getString(column_index);
            cursor.close();
            return s;
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public uploadMedia getMain()
    {
        return this;
    }


}
