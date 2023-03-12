package com.example.eloworld;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import src.Client;
import src.JsonMessageFactory;

public class AddMatch extends AppCompatActivity {

    Client client;
    JsonMessageFactory messageFactory;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_match);
        client = ((App) getApplication()).getClient();

        messageFactory = JsonMessageFactory.getInstance();
    }

    public void addMatchRefereeScreen(View view) throws JSONException {
        TextInputEditText player1edit = (TextInputEditText)this.findViewById(R.id.winner_input_edit);
        TextInputEditText player2edit = (TextInputEditText)this.findViewById(R.id.loser_input_edit);
        String player1Username = String.valueOf(player1edit.getText());
        String player2Username = String.valueOf(player2edit.getText());
        String refereeUsername = client.getModel().getUsername();
        JSONObject entryMessage = messageFactory.encodeEntryMessage(refereeUsername, player1Username, player2Username);
        Util.sendThreadedmessage(client, entryMessage);
        finish();
    }
}
