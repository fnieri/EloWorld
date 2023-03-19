package com.example.eloworld.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eloworld.App;
import com.example.eloworld.SignIn;
import com.example.eloworld.Util;
import com.example.eloworld.databinding.FragmentProfileBinding;
import com.example.eloworld.databinding.FragmentSettingsBinding;

import org.json.JSONException;

import java.util.Objects;

import src.Client;
import src.Model;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    Client client;
    Model model;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        client = ((App) requireActivity().getApplication()).getClient();
        Model model = client.getModel();

        //final TextView textView = binding.textSettings;
        //settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}