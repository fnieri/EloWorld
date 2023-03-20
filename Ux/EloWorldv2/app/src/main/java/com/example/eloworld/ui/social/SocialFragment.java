package com.example.eloworld.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.example.eloworld.AddFriend;
import com.example.eloworld.App;
import com.example.eloworld.R;
import com.example.eloworld.Util;
import com.example.eloworld.databinding.FragmentSocialBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import src.Client;
import src.Enum.UserRoles;
import src.Model;

public class SocialFragment extends Fragment {
    Client client;
    private FragmentSocialBinding binding;
    List<String> friends;
    List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard = new ArrayList<>();
    List<String> displayLeaderboard = new ArrayList<>();
    Model model;

    Button viewBlokchainBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        SocialViewModel socialViewModel =
        new ViewModelProvider(this).get(SocialViewModel.class);
        client = ((App) requireActivity().getApplication()).getClient();

        binding = FragmentSocialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        model = client.getModel();
        ListView leaderboardListView = binding.leaderboardListView;
        setUpLeaderboard(leaderboardListView, model);

        ListView friendsListView = binding.friendsListView;
        setUpFriends(friendsListView, model);

        removeRefereeInterface(model, root);


        return root;
    }

    public void setUpLeaderboard(ListView leaderboardListView, Model model) {

        leaderboard = model.getLeaderboard();
        displayLeaderboard.add("EloWorld Leaderboard:");
        Collections.reverse(leaderboard);
        for (Map.Entry<Integer, Map.Entry<String, Integer>> playerEntry: leaderboard) {
            int position = playerEntry.getKey();
            String playerUsername = playerEntry.getValue().getKey();
            int playerELO = playerEntry.getValue().getValue();
            String playerAndElo = position + "\t" + playerUsername + "\t" + playerELO;
            displayLeaderboard.add(playerAndElo); //Add player at his position in the list to use in the listview
        }
        ArrayAdapter<String> leaderboardAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, displayLeaderboard);
        leaderboardListView.setAdapter(leaderboardAdapter); //Code by ChatGPT
    }

    public void setUpFriends(ListView friendsListView, Model model) {
        friends = model.getFriends();
        List<String> displayFriends = new ArrayList<>(friends);
        displayFriends.add(0, "Vos amis:");

        ArrayAdapter<String> friendsAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_activated_1, displayFriends);
        friendsListView.setAdapter(friendsAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addFriendSocial(View view) {
    }

    public void addBlockSocial(View view) {
    }

    public void viewBlockchain(View view) {
    }

    public void removeRefereeInterface(Model model, View root) {
        if (model.getRole() != UserRoles.REFEREE) {
            //Remove the button if it's not a referee

            viewBlokchainBtn = root.findViewById(R.id.view_blockchain);
            viewBlokchainBtn.setVisibility(View.GONE);
        }
    }
}