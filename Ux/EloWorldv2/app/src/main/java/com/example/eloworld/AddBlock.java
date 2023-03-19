package com.example.eloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import src.BlockEntry;
import src.Client;
import src.JsonMessageFactory;
import src.Model;
import src.Referee;

public class AddBlock extends AppCompatActivity {

    Client client;
    JsonMessageFactory messageFactory;
    List<BlockEntry> entries;
    List<String> displayEntries;
    Referee referee;
    Model model;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_block);

        client = ((App) getApplication()).getClient();
        messageFactory = JsonMessageFactory.getInstance();
        model = client.getModel();

        try {
            referee = model.getReferee();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ListView entriesListView = this.findViewById(R.id.entries);
        setUpDisplayEntries(entriesListView);

    }

    public void addAllEntriesToBlock(View view) throws JSONException {
        if (referee.getEntries().size() > 0) {
            referee.addBlock();
            finish();
        }
        else Toast.makeText(getApplicationContext(), "Vous ne pouvez pas ajouter un bloc vide", Toast.LENGTH_SHORT).show();
    }

    public void setUpDisplayEntries(ListView entriesListView) {
        entries = referee.getEntries();
        if (entries.size() > 0) {
            displayEntries = new ArrayList<>();
            displayEntries.add("Your entries:");
            for (BlockEntry entry : entries) {
                displayEntries.add(entry.toString());
            }
            ArrayAdapter<String> entriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, displayEntries);
            entriesListView.setAdapter(entriesAdapter); //Line by ChatGPT
        }
    }
}
