package com.example.eloworld;

import android.app.Application;
import android.content.Intent;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;

import src.Controller;
import src.Model;
import src.Client;

public class App extends Application {
    Client client = new Client();

    public App() throws IOException {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(() -> client.main()).start();
    }

    public Client getClient() {return client;}
}
