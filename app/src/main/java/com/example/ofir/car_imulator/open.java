package com.example.ofir.car_imulator;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class open extends AppCompatActivity {
    Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_open);

        myHandler = new Handler();
        myHandler.postDelayed(mMyRunnable, 2000);
    }

    private Runnable mMyRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            Intent i = new Intent(getApplicationContext(),homePage.class);
            startActivity(i);
        }
    };

    public void onClickOpen(View view){
        myHandler.removeCallbacks(mMyRunnable);
        Intent i=new Intent(this,homePage.class);
        startActivity(i);
    }

}
