package com.example.eloworld;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.TokenWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
        TextInputEditText player1edit = this.findViewById(R.id.winner_input_edit);
        TextInputEditText player2edit = this.findViewById(R.id.loser_input_edit);
        String winner = String.valueOf(player1edit.getText());
        String loser = String.valueOf(player2edit.getText());
        String refereeUsername = model.getUsername();
        if (!winner.equals(loser) && (!winner.equals(refereeUsername) && !loser.equals(refereeUsername))) {
            JSONObject entryMessage = messageFactory.encodeEntryMessage(winner, loser);
            Util.sendThreadedMessage(client, entryMessage);
            finish();
        }
        else Toast.makeText(getApplicationContext(), "Il y a eu un erreur lors de l'ajout!", Toast.LENGTH_LONG).show();
    }


}
