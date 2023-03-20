package com.example.eloworld;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import src.Block;
import src.BlockEntry;
import src.Client;
import src.Model;
import src.Referee;

public class BlockSelectionView extends AppCompatActivity {

    Client client;
    Model model;
    Referee referee;
    int blockSelected;
    ListView entriesListView;
    List<String> displayEntries;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.singular_block_view);
        App thisApp = (App) getApplication();
        blockSelected = thisApp.getCurrentBlockSelected();
        client = thisApp.getClient();
        model = client.getModel();
        try {
            referee = model.getReferee();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Block block;
        try {
            block = referee.getBlock(blockSelected);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        entriesListView = this.findViewById(R.id.entries_list_view);

        List<BlockEntry> entries = block.getEntries();
        for (BlockEntry entry: entries) {
            System.out.println(entry.toString());

            System.out.println("---");
        }
        setUpBlock(entries, entriesListView);
    }

    public void setUpBlock(List<BlockEntry> entries, ListView entriesView) {
        displayEntries = new ArrayList<>();
        displayEntries.add("Vos entrées:" );
        for (BlockEntry entry: entries) {
            displayEntries.add(entry.toString());
        }
        ArrayAdapter<String> entriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, displayEntries);
        entriesView.setAdapter(entriesAdapter);
    }
}
