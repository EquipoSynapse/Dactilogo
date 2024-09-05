package com.redmatory.dactilogo.r;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.redmatory.dactilogo.R;
import com.redmatory.dactilogo.m.p2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class p3 extends AppCompatActivity {
    private Handler CARGARINTRO3;
    private ProgressBar progressBar;
    int Porcentaje = 0;
    int Tiempo = 0300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p3);
        progressBar = findViewById(R.id.progressBar);

        CARGARINTRO3 =  new Handler();
        progressBar.setProgress(0);
        while (Tiempo < 4300) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Porcentaje = Porcentaje + 5;
                    progressBar.setProgress(Porcentaje);
                }
            }, Tiempo);
            Tiempo = Tiempo + 200;
        }
        CARGARINTRO3.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(p3.this, p2.class);
                startActivity(intent);
                finish();
            }
        }, 4300);
    }

    public void  onBackPressed () {
        CARGARINTRO3.removeCallbacksAndMessages(null);
        finish();
    }

}