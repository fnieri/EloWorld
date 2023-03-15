package com.example.eloworld;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import kotlin.jvm.internal.Intrinsics;
import src.Client;
import src.Enum.*;
import src.JsonMessageFactory;
import src.Model;

public class SignUp extends AppCompatActivity {
    Client client;
    JsonMessageFactory messageFactory;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sign_up);
        client = ((App) getApplication()).getClient();
        messageFactory = JsonMessageFactory.getInstance();
        new Thread(this::changeLayoutOnSignUp).start();


    }

    public void changeLayout_SU(View view) {
        Util.changeLayout(this, SignIn.class);
    }

    public void signUp_SU(View view) throws JSONException {
        TextInputEditText usernameField = this.findViewById(R.id.username_input_edit);
        Intrinsics.checkNotNullExpressionValue(usernameField, "usernameField");
        String username = String.valueOf(usernameField.getText());

        TextInputEditText passwordField = this.findViewById(R.id.password_input_edit);
        Intrinsics.checkNotNullExpressionValue(passwordField, "passwordField");
        String password = String.valueOf(passwordField.getText());

        TextInputEditText confirmPasswordField = this.findViewById(R.id.password_confirm_edit);
        Intrinsics.checkNotNullExpressionValue(confirmPasswordField, "confirmPasswordField");
        String confirmPassword = String.valueOf(confirmPasswordField.getText());

        if (password.equals(confirmPassword)) {

            JSONObject registerMessage = messageFactory.encodeAuthMessage(username, password, AuthActions.REGISTER);
            Util.sendThreadedmessage(client, registerMessage);
            System.out.println(registerMessage);
        }
    }

    public void changeLayoutOnSignUp() {
        for (;;) {
            if (Util.changeLayoutOnLogIn(this, client)) {
                break;
            }
        }
    }
}
