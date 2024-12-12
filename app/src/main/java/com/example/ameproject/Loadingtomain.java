package com.example.ameproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Loadingtomain extends AppCompatActivity {
Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loadingtomain);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(Loadingtomain.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                ,300
        );
        }
    }
