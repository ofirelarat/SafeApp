package com.example.ofir.car_imulator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class myContacts extends AppCompatActivity {

    private String names;
    private String nums;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_contacts);

        db = new DBHelper(this);

        Contacts myC=db.FindContactsById(0);
        if(myC!=null && myC.getNames().length==4 && myC.getNums().length==4) {
            ((EditText) findViewById(R.id.contactName_1_editText)).setText(myC.getNames()[0]);
            ((EditText) findViewById(R.id.contactName_2_editText3)).setText(myC.getNames()[1]);
            ((EditText) findViewById(R.id.contactName_3_editText5)).setText(myC.getNames()[2]);
            ((EditText) findViewById(R.id.contactName_4_editText7)).setText(myC.getNames()[3]);

            ((EditText) findViewById(R.id.contactNum_1_editText2)).setText(myC.getNums()[0]);
            ((EditText) findViewById(R.id.contactNum_2_editText4)).setText(myC.getNums()[1]);
            ((EditText) findViewById(R.id.contactNum_3_editText6)).setText(myC.getNums()[2]);
            ((EditText) findViewById(R.id.contactNum_4_editText8)).setText(myC.getNums()[3]);
        }

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

    }

    public void onClickSaveMyContacts(View view){

        names=((EditText)findViewById(R.id.contactName_1_editText)).getText().toString()+",";
        names+=((EditText)findViewById(R.id.contactName_2_editText3)).getText().toString()+",";
        names+=((EditText)findViewById(R.id.contactName_3_editText5)).getText().toString()+",";
        names+=((EditText)findViewById(R.id.contactName_4_editText7)).getText().toString();

        nums=((EditText)findViewById(R.id.contactNum_1_editText2)).getText().toString()+",";
        nums+=((EditText)findViewById(R.id.contactNum_2_editText4)).getText().toString()+",";
        nums+=((EditText)findViewById(R.id.contactNum_3_editText6)).getText().toString()+",";
        nums+=((EditText)findViewById(R.id.contactNum_4_editText8)).getText().toString();

        try {
            Contacts contacts = new Contacts(0, names, nums);
            if (db.AddContacts(contacts)) {
            } else {
                db.UpdateContacts(contacts);
            }
            Toast.makeText(getApplicationContext(), "your contacts saved", Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "fill all the contacts", Toast.LENGTH_LONG).show();
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

}
