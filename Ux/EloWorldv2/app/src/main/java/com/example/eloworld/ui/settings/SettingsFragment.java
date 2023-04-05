package com.example.eloworld.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eloworld.App;
import com.example.eloworld.R;
import com.example.eloworld.SignIn;
import com.example.eloworld.Util;
import com.example.eloworld.databinding.FragmentProfileBinding;
import com.example.eloworld.databinding.FragmentSettingsBinding;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import src.Client;
import src.Enum.UserRoles;
import src.Model;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    Client client;
    Model model;

    List<Map.Entry<Integer, Map.Entry<String, Integer>>> leaderboard = new ArrayList<>();
    List<String> displayLeaderboard = new ArrayList<>();

    Button viewBlokchainBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        client = ((App) requireActivity().getApplication()).getClient();
        Model model = client.getModel();
        ListView leaderboardListView = binding.leaderboardListView;
        setUpLeaderboard(leaderboardListView, model);
        Util.setFragmentToFullscreen(root, this);
        removeRefereeInterface(model, root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void setUpLeaderboard(ListView leaderboardListView, Model model) {
        displayLeaderboard = new ArrayList<>();
        leaderboard = model.getLeaderboard();
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

    public void removeRefereeInterface(Model model, View root) {
        if (model.getRole() != UserRoles.REFEREE) {
            //Remove the button if it's not a referee

            viewBlokchainBtn = root.findViewById(R.id.view_blockchain);
            viewBlokchainBtn.setVisibility(View.GONE);
        }
    }


}