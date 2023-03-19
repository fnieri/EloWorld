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
    public int currentBlockSelected = 0;
    public App() throws IOException {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getExternalFilesDir(null);getExternalFilesDir(null);
        new Thread(() -> client.main()).start();
    }

    public Client getClient() {return client;}

    public int getCurrentBlockSelected() {
        return currentBlockSelected;
    }

    public void setCurrentBlockSelected(int newBlockSelected) {
        currentBlockSelected = newBlockSelected;
    }

    public void setClient(Client newClient) {
        client = newClient;
        new Thread(() -> client.main()).start();
    }
}
