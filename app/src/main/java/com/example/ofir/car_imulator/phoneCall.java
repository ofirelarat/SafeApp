package com.example.ofir.car_imulator;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class phoneCall extends AppCompatActivity {

    DBHelper db;
    String num;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

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

        db = new DBHelper(this);
        final Contacts myC=db.FindContactsById(0);
        if(myC!=null) {
            listView = (ListView) findViewById(R.id.listViewPhoneCall);
            listView.setAdapter(new CostumAdapter(this, myC.getNames()));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    num = myC.getNums()[position];
                    ((EditText) findViewById(R.id.callNumEditText)).setText(num);
                    makeAcCall();
                }
            });
        }
    }

    public void onClickCall(View view) {
        num = ((EditText) findViewById(R.id.callNumEditText)).getText().toString();
        makeAcCall();
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
        Contacts myC=db.FindContactsById(0);
        for(int i=0;i<4;i++){
            if((action.equalsIgnoreCase("call "+myC.getNames()[i]))){
                num=myC.getNums()[i];
                makeAcCall();
                return;
            }
        }
        if((action.intern()==("make a call").intern() || action.intern()==("call").intern() || action.intern()==("call contact").intern())){
            Intent i=new Intent(this,phoneCall.class);
            startActivity(i);
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
}
