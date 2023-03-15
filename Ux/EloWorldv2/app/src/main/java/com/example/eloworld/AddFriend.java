package com.example.eloworld;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.eloworld.R;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import src.Client;
import src.Enum.*;
import src.Enum.FriendReqActions;
import src.JsonMessageFactory;
import src.Model;

public class AddFriend extends AppCompatActivity {

    Client client;
    JsonMessageFactory messageFactory;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.add_friend);
        client = ((App) getApplication()).getClient();
        messageFactory = JsonMessageFactory.getInstance();
    }

    public void addFriend(View view) throws JSONException {
        String friendUsername = getFriendUsername(view);
        Model model = client.getModel();
        String sender = model.getUsername();
        JSONObject friendMessage = messageFactory.friendMessage(sender, friendUsername, FriendReqActions.FOLLOW_FRIEND);

        Util.sendThreadedmessage(client, friendMessage);
        finish();
    }

    public void removeFriend(View view) throws JSONException {
        String friendUsername = getFriendUsername(view);
        Model model = client.getModel();
        String sender = model.getUsername();
        JSONObject friendMessage = messageFactory.friendMessage(sender, friendUsername, FriendReqActions.REMOVE_FRIEND);
        Util.sendThreadedmessage(client, friendMessage);
        finish();
    }

    public String getFriendUsername(View view) throws JSONException {
        TextInputEditText friendToAdd = (TextInputEditText) this.findViewById(R.id.friend_input_edit);
        return  String.valueOf(friendToAdd.getText());
    }
}