package com.example.ofir.car_imulator;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class playMedia extends AppCompatActivity {

    private ListView listView;
    private static MediaPlayer mediaPlayer = new  MediaPlayer();
    private AudioManager myAudioManager;
    public static String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/myImulatorMusic";

    ArrayList<File> allSongs;
    String[] item;
   // private SwipeRefreshLayout swipeContainer;
    private ProgressDialog dialog;

   // DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_media);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myAudioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        listView = (ListView) findViewById(R.id.listViewPlayMedia);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(231, 76, 60)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

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

        dialog = new ProgressDialog(playMedia.this);
        dialog.setMessage("Loading...");
        dialog.show();

        if(isExternalStorageReadable()) {
            final ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
            allSongs = mySongs;
            item = new String[mySongs.size()];
            for (int i = 0; i < mySongs.size(); i++) {
                item[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
            }
            listView.setAdapter(new CostumAdapter(this, item));

            listView.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mediaPlayer.reset();
                            //   String fileP = (path + "/" + String.valueOf(parent.getItemAtPosition(position)) + ".mp3.mp3");
                            mediaPlayer = new MediaPlayer();

                            try {
                                mediaPlayer.setDataSource(mySongs.get(position).toString());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );

            dialog.hide();
        }
        else
        {
            dialog.hide();
            Toast.makeText(this,"canot read from phone storage",Toast.LENGTH_LONG).show();
        }
    }


    //check if the external storage is avilable to at list read
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        mediaPlayer.reset();
        super.onBackPressed();
    }

    public void onClickPause(View view){
        mediaPlayer.pause();
     /*   Media mediaO=db.FindMediaByName(0);
        if(mediaO!=null) {
            listView.setAdapter(new CostumAdapterM(this, mediaO.getName()));
        }*/
    }
    public void onClickPlay(View view){
        mediaPlayer.start();
        /*Media mediaO=db.FindMediaByName(0);
        if(mediaO!=null) {
            listView.setAdapter(new CostumAdapterM(this, mediaO.getName()));
        }*/
    }
    public void onClickSilence(View view){
        int mod=myAudioManager.getRingerMode();
        if(mod==AudioManager.RINGER_MODE_NORMAL){
            mediaPlayer.setVolume(0, 0);
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Toast.makeText(playMedia.this, "Now in silent Mode", Toast.LENGTH_LONG).show();
        }
        else if(mod==AudioManager.RINGER_MODE_SILENT){
            mediaPlayer.setVolume(1,1);
            myAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Toast.makeText(playMedia.this,"Now in normal Mode",Toast.LENGTH_LONG).show();
        }
     /*   Media mediaO=db.FindMediaByName(0);
        if(mediaO!=null) {
            listView.setAdapter(new CostumAdapterM(this, mediaO.getName()));
        }*/
    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> al=new ArrayList<File>();
        File[] files=root.listFiles();
        for(File singleFile:files){
            if (!singleFile.getName().equalsIgnoreCase("sound") && !singleFile.getName().equalsIgnoreCase("audio") && singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3") ||singleFile.getName().endsWith(".wav") )
                    al.add(singleFile);
            }
        }
        return al;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    takeAction(result.get(0));
                }
                break;
        }
    }

    public void takeAction(String action){
        for(int i=0;i<item.length;i++){
        if((action.equalsIgnoreCase("play "+item[i])) || (((item[i]).toLowerCase()).contains((action).toLowerCase())) && !action.equalsIgnoreCase("play")){
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(allSongs.get(i).toString());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
              return;
            }
        }
        if(action.equalsIgnoreCase("play") || action.equalsIgnoreCase("play something") || action.equalsIgnoreCase("play song")){
            Random rand = new Random();
            int  n = rand.nextInt(item.length);
            mediaPlayer.reset();
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(allSongs.get(n).toString());
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
         else if((action.intern()==("make a call").intern() || action.intern()==("call").intern() || action.intern()==("call contact").intern())){
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
        else if((action.intern()==("home").intern() || action.intern()==("home page").intern() || action.intern()==("open home page").intern())){
            Intent i=new Intent(this,homePage.class);
            startActivity(i);
        }
        else Toast.makeText(this,"the action:'"+action+"' not exist try something else",Toast.LENGTH_LONG).show();
    }

}
