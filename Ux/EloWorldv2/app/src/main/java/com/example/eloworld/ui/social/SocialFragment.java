package com.example.eloworld.ui.social;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.WindowCompat;
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
    Model model;


    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        SocialViewModel socialViewModel =
        new ViewModelProvider(this).get(SocialViewModel.class);
        client = ((App) requireActivity().getApplication()).getClient();

        binding = FragmentSocialBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        model = client.getModel();


        ListView friendsListView = binding.friendsListView;
        setUpFriends(friendsListView, model);

         return root;
    }


    public void setUpFriends(ListView friendsListView, Model model) {
        friends = model.getFriends();
        List<String> displayFriends = new ArrayList<>(friends);


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


}