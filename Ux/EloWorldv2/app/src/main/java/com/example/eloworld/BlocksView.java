package com.example.eloworld;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import src.BlockChain;
import src.Client;
import src.Model;
import src.Referee;

public class BlocksView extends AppCompatActivity {

    Client client;
    Model model;
    Referee referee;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.blocks_view);
        client = ((App) getApplication()).getClient();
        model = client.getModel();
        try {
            referee = model.getReferee();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        try {
            createBlockchainView();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressLint("SetTextI18n")
    public void createBlockchainView() throws JSONException {
        //  https://stackoverflow.com/questions/14920535/how-to-add-more-than-one-button-to-scrollview-in-android
        // https://stackoverflow.com/questions/4401028/dynamically-creating-buttons-and-setting-onclicklistener
        ScrollView blocksScrollView = this.findViewById(R.id.blocks_scroll_view);
        BlockChain blockChain = referee.getBlockchainObject();

        LinearLayout linearLayout = this.findViewById(R.id.blocks_linear_layout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Button[] blocksButtons = new Button[blockChain.size()];
        for (int i = blockChain.size() - 1; i >= 0; i--) {
            blocksButtons[i] = new Button(getApplicationContext());
            blocksButtons[i].setId(i);
            blocksButtons[i].setTag(i);
            blocksButtons[i].setText("Bloc " + i);
            blocksButtons[i].setTextColor(Color.BLUE);
            blocksButtons[i].setTextSize(20);
            blocksButtons[i].setHeight(100);
            blocksButtons[i].setWidth(100);
            linearLayout.addView(blocksButtons[i]);
            blocksButtons[i].setOnClickListener(handleOnClick(blocksButtons[i], this));
        }
    }

    View.OnClickListener handleOnClick(final Button button, Context context) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                int blockSelected = button.getId();
                ((App) getApplication()).setCurrentBlockSelected(blockSelected);
                Util.changeLayout(context, BlockSelectionView.class);
            }
        };
    }

}
