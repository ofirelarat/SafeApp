package com.example.ofir.car_imulator;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.System.*;

public class notes extends AppCompatActivity {

    TextView note;
    private File root;
    private File file;
    private ListView listView;
    DBHelper db;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        note=(TextView)findViewById(R.id.noteTextView);
        root=new File(Environment.getExternalStorageDirectory(),"Notes");
        listView=(ListView)findViewById(R.id.listViewNotePage);
        db = new DBHelper(this);
        Note notes=db.FindNoteByName(0);

        if(notes!=null) {
            listView.setAdapter(new CostumAdapterN(this, notes.getName()));
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


        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        FileReader fr = null;
                        String buf = null;
                        String fileP = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/Notes" + "/"+String.valueOf(parent.getItemAtPosition(position))) + ".txt";
                        try {
                            fr = new FileReader(fileP);
                            BufferedReader br = new BufferedReader(fr);
                            String str = br.readLine();
                            buf = str;
                            while ((str = br.readLine()) != null) {
                                buf = str;
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        note.setText(buf.toString());

                    }
                }
        ) ;

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRL);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                Note noteO = db.FindNoteByName(0);
                if (noteO != null) {
                    listView.setAdapter(new CostumAdapterM(getApplicationContext(), noteO.getName()));
                }
                swipeContainer.setRefreshing(false);
                }
                 }, 3000);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }



    public void onClickRecord(View view) {
        promptSpeechInput();
    }

    public void promptSpeechInput() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something...");
        try {
            startActivityForResult(i, 100);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "sorry,your device doesn't support speech language", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    note.setText(result.get(0));
                }
                break;
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



    public void onClickSave(View view){
        String T;
        try {
            T = android.text.format.DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
            // this will create a new name everytime and unique

            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, T + ".txt");  // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(note.getText().toString());
            writer.flush();
            writer.close();

            Note notesO=db.FindNoteByName(0);
            Note note;
            if(notesO!=null) {
                String namesN=notesO.getName()[0]+",";
                for(int i=1;i<notesO.getName().length;i++){
                    namesN+=notesO.getName()[i]+",";
                }
               note = new Note(0, namesN + T.toString());
            }
            else {
                note = new Note(0, T.toString());
            }
            if(db.AddNote(note)){}
            else {
                db.UpdateNote(note);
            }
            Toast.makeText(getApplicationContext(),"File generated with name " + T + ".txt",Toast.LENGTH_LONG).show();
            Note notes=db.FindNoteByName(0);
            if(notes!=null) {
                listView.setAdapter(new CostumAdapterN(this, notes.getName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
