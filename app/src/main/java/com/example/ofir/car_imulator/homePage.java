package com.example.ofir.car_imulator;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class homePage extends AppCompatActivity {

    private TextView Tspeed;
    TextToSpeech tts;
    DBHelper db;
    String num;
    public ProgressDialog dialog;


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location.getSpeed() != 0) {
                Tspeed.setText("" + (location.getSpeed() * 3600 / 1000) + " km/h");
            } else {
                Tspeed.setText(0 + " km/h");
            }
            if(location.getSpeed()<100*3600/1000){
                Tspeed.setTextColor(Color.parseColor("#2ecc71"));
            }
            if(location.getSpeed()>=100*3600/1000){
                Tspeed.setTextColor(Color.parseColor("#c0392b"));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(),"turn on GPS",Toast.LENGTH_LONG).show();
          //  Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          //  startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.rgb(231, 76, 60)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //          .setAction("Action", null).show();
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
                try {
                    startActivityForResult(i, 101);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "sorry,your device doesn't support speech language", Toast.LENGTH_LONG).show();
                }
            }
        });


        Location location = new Location(LocationManager.GPS_PROVIDER);
        Tspeed = (TextView) findViewById(R.id.speedTextView);
        locationListener.onLocationChanged(location);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 10, this.locationListener);

        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    tts.setLanguage(Locale.getDefault());
                }
            }
        });

        com.github.clans.fab.FloatingActionButton floatingActionButtonContact = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        com.github.clans.fab.FloatingActionButton floatingActionButtonPlaces = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);
        com.github.clans.fab.FloatingActionButton floatingActionButtonWeather = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButtonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),myContacts.class);
                startActivity(i);
            }
        });

        floatingActionButtonPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),myPlaces.class);
                startActivity(i);
            }
        });

        floatingActionButtonWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),weather.class);
                startActivity(i);
            }
        });
    }

    public void onClickClock(View view){
        Calendar calendar=Calendar.getInstance();
        String toSpeak=String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp",calendar);
        Snackbar.make(view, toSpeak, Snackbar.LENGTH_LONG).setAction("Action", null).show();
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause(){
        if(tts!=null){
            tts.stop();
         //   tts.shutdown();
        }
        super.onPause();
    }

    public void onClickCall(View view){
        Intent i=new Intent(this,phoneCall.class);
        startActivity(i);
    }
    public void onClickSMS(View view){
        Intent i=new Intent(this,SMS.class);
        startActivity(i);
    }
    public void onClickNote(View view){
        Intent i=new Intent(this,notes.class);
        startActivity(i);
    }
    public void onClickPlayMedia(View view){
        Intent i=new Intent(this,playMedia.class);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
        new Thread(){
            public void run(){
                try{
                    sleep(500);
                }catch (Exception e){}
                dialog.dismiss();
            }
        }.start();
        startActivity(i);
    }
    public void onClickUpload(View view){
        Intent i=new Intent(this,uploadMedia.class);
        startActivity(i);
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
        db = new DBHelper(this);
        Contacts myC=db.FindContactsById(0);
        if(myC!=null) {
            for (int i = 0; i < 4; i++) {
                if ((action.equalsIgnoreCase ("call " + myC.getNames()[i]))) {
                    num = myC.getNums()[i];
                    makeAcCall();
                    return;
                }
            }
        }
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
       else if((action.intern()==("what is the time").intern() || action.intern()==("time").intern()|| action.intern()==("what's the time").intern())){
            Calendar calendar=Calendar.getInstance();
            String toSpeak=String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp",calendar);
            Toast.makeText(getApplicationContext(),toSpeak,Toast.LENGTH_LONG).show();
            tts.speak(toSpeak,TextToSpeech.QUEUE_FLUSH,null);
        }
        else Toast.makeText(this,"the action:'"+action+"' not exist try something else",Toast.LENGTH_LONG).show();
    }

    public void makeAcCall() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + num));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            startActivity(callIntent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this,"call failed",Toast.LENGTH_LONG).show();
        }
    }
}
