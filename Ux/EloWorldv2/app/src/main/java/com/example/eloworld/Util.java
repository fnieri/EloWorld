package com.example.eloworld;

import android.content.Context;
import android.content.Intent;

import org.json.JSONObject;

import java.security.Permission;

import src.Model;
import kotlin.jvm.internal.Intrinsics;
import src.Client;

public class Util {
    public static void changeLayout(Context caller, Class newScreen) {
        Intent intent = new Intent(caller, newScreen);
        caller.startActivity(intent);
    }

    public static void sendThreadedmessage(Client client, JSONObject message) {
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

    public static boolean changeLayoutOnLogIn(Context caller, Client client) {
        Model model = client.getModel();
        if (model.isLoggedIn()) {

            Util.changeLayout(caller, MainActivity.class);
            return true;
        }
        return false;
    }

    private Util() {}
}
