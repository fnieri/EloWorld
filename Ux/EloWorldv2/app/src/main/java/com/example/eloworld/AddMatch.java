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
import src.Model;
import src.Referee;

public class AddMatch extends AppCompatActivity {

    Client client;
    JsonMessageFactory messageFactory;
    Model model;
    Referee referee;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_match);
        client = ((App) getApplication()).getClient();

        messageFactory = JsonMessageFactory.getInstance();
        model = client.getModel();
        try {
            referee = model.getReferee();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void addMatchRefereeScreen(View view) throws JSONException {
        TextInputEditText player1edit = (TextInputEditText)this.findViewById(R.id.winner_input_edit);
        TextInputEditText player2edit = (TextInputEditText)this.findViewById(R.id.loser_input_edit);
        String winner = String.valueOf(player1edit.getText());
        String loser = String.valueOf(player2edit.getText());
        String refereeKey = client.getModel().getPublicKey();
        referee.createEntry(winner, loser, refereeKey);
        finish();
    }
}
