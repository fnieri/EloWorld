package com.example.eloworld;

import android.icu.text.SymbolTable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import kotlin.jvm.internal.Intrinsics;
import src.Client;
import src.Enum.AuthActions;
import src.JsonMessageFactory;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SignIn extends AppCompatActivity {

    Client client;
    JsonMessageFactory messageFactory;
    List<Thread> runningThreads = new ArrayList<>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sign_in);

        client = ((App) getApplication()).getClient();
        messageFactory = JsonMessageFactory.getInstance();
        Thread checkLoginStatus = new Thread(this::changeLayoutOnSignIn);
        runningThreads.add(checkLoginStatus);
        checkLoginStatus.start();
    }

    public void changeLayout_SI(View v) {

        Util.changeLayout(this, SignUp.class);
        System.out.println(runningThreads.size() + "dsasd");

        finishAffinity();
    }

    public void signIn_SI(View v) throws JSONException {

        TextInputEditText usernameField = this.findViewById(R.id.sign_in_username_input_edit);
        Intrinsics.checkNotNullExpressionValue(usernameField, "usernameField");
        String username = String.valueOf(usernameField.getText());

        TextInputEditText passwordField = this.findViewById(R.id.sign_in_password_input_edit);
        Intrinsics.checkNotNullExpressionValue(passwordField, "passwordField");
        String password = String.valueOf(passwordField.getText());

        JSONObject loginMessage = messageFactory.encodeAuthMessage(username, password, AuthActions.LOGIN);
        System.out.println(loginMessage);
        Util.sendThreadedMessage(client, loginMessage);

     }

    public void changeLayoutOnSignIn() {
        for (;;) {
            if (Util.changeLayoutOnAuth(this, client, true)) {
                break;
            }
        }
    }
    @Override
    public void onDestroy() {

        super.onDestroy();

        for (Thread thread: runningThreads) {System.out.println("aa"); thread.interrupt();}
    }
}
