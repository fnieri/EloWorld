package com.example.eloworld;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eloworld.databinding.ActivityMainBinding;

import org.json.JSONException;

import java.io.File;
import java.security.Permission;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import src.Client;
import src.Model;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Client client;
    final static String PATH_TO_BLOCKCHAIN_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "Android/data/com.example.eloworld" + File.separator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, create the folder
            File file = new File(String.valueOf(this.getFilesDir()));

            if (!file.exists()) {
                boolean created = file.mkdirs();
                if (!created) {
                    Log.e("TAG", "Failed to create directory");
                }
            }

        } else {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        super.onCreate(savedInstanceState);
        client = ((App) getApplication()).getClient();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        Model model = client.getModel();
        try {
            model.setReferee(this);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_social, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }



    public void logOut(View v) {
        finish();
        Util.changeLayout(this, SignIn.class);
    }

    public void addMatch(View v) {
        Util.changeLayout(this, AddMatch.class);
    }

    public void addFriendSocial(View view) {
        Util.changeLayout(this, AddFriend.class);
    }

    public void addBlockSocial(View view) {Util.changeLayout(this, AddBlock.class);}


}