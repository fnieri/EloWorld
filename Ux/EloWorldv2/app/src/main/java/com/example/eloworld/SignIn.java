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

public class SignIn extends AppCompatActivity {

    Client client;
    JsonMessageFactory messageFactory;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sign_in);

        client = ((App) getApplication()).getClient();
        messageFactory = JsonMessageFactory.getInstance();
        new Thread(this::changeLayoutOnSignIn).start();

        Log.e("TAG", "crtea");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("TAG", "resyme");
        client = ((App) getApplication()).getClient();
        messageFactory = JsonMessageFactory.getInstance();
        new Thread(this::changeLayoutOnSignIn).start();
    }

    public void changeLayout_SI(View v) {

        Util.changeLayout(this, SignUp.class);
    }

    public void signIn_SI(View v) throws JSONException {

        TextInputEditText usernameField = (TextInputEditText)this.findViewById(R.id.username_input_edit);
        Intrinsics.checkNotNullExpressionValue(usernameField, "usernameField");
        String username = String.valueOf(usernameField.getText());

        TextInputEditText passwordField = (TextInputEditText)this.findViewById(R.id.password_input_edit);
        Intrinsics.checkNotNullExpressionValue(passwordField, "passwordField");
        String password = String.valueOf(passwordField.getText());

        JSONObject loginMessage = messageFactory.encodeAuthMessage(username, password, AuthActions.LOGIN);
        System.out.println(loginMessage);
        Util.sendThreadedMessage(client, loginMessage);

     }

     public void changeLayoutOnSignIn() {
        for (;;) {
            if (Util.changeLayoutOnLogIn(this, client)) {
                System.out.println("ASDA");
                break;
            }
        }
     }
}
