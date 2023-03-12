package com.example.eloworld;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.eloworld.databinding.ActivityMainBinding;

import src.Client;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    Client client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = ((App) getApplication()).getClient();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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