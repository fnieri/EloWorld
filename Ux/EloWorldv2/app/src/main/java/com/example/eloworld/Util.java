package com.example.eloworld;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONObject;

import src.Model;
import src.Client;

public class Util {

    public static void changeLayout(Context caller, Class newScreen) {
        Intent intent = new Intent(caller, newScreen);
        System.out.println(caller.toString() + "changing to " + newScreen.toString());
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        caller.startActivity(intent);

    }
    public static void removeActionBar(AppCompatActivity caller) {
        if (caller.getSupportActionBar() != null) {
            caller.getSupportActionBar().hide();
        }
    }
    public static void sendThreadedMessage(Client client, JSONObject message) {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    client.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static boolean changeLayoutOnAuth(Context caller, Client client, boolean isLogIn) {
        Model model = client.getModel();
        if (model.isSetUp()) {
            if (isLogIn) Util.changeLayout(caller, MainActivity.class);
            else {System.out.println("called"); Util.changeLayout(caller, OnboardingPage.class); }
            return true;
        }
        return false;
    }
    public static void setFragmentToFullscreen(View root, Fragment fragment) {
        WindowInsetsController controller = root.getWindowInsetsController();
        if (controller != null) {
            System.out.println("Ciao");
            controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());

            controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
        }
    }

    private Util() {}
}
