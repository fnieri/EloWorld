package com.example.eloworld;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import src.Client;
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
        handleFriends(true);
    }

    public void removeFriend(View view) throws JSONException {
        handleFriends(false);
    }

    public void handleFriends(boolean isAdding) throws JSONException {
        String friendUsername = getFriendUsername();
        Model model = client.getModel();
        String sender = model.getUsername();
        if (!Objects.equals(sender, friendUsername)) {
            FriendReqActions action = isAdding ? FriendReqActions.FOLLOW_FRIEND : FriendReqActions.REMOVE_FRIEND;
            JSONObject friendMessage = messageFactory.friendMessage(sender, friendUsername, action);
            Util.sendThreadedMessage(client, friendMessage);
            finish();
        }
        else Toast.makeText(this, "Vous ne pouvez pas ajouter ou supprimer vous memes", Toast.LENGTH_SHORT).show();

    }
    public String getFriendUsername() throws JSONException {
        TextInputEditText friendToAdd = (TextInputEditText) this.findViewById(R.id.friend_input_edit);
        return  String.valueOf(friendToAdd.getText());
    }
}
