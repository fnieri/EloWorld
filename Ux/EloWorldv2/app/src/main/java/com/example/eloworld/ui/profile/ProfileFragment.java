package com.example.eloworld.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.eloworld.AddMatch;
import com.example.eloworld.App;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.eloworld.R;
import com.example.eloworld.Util;
import com.example.eloworld.databinding.FragmentProfileBinding;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.Objects;

import src.Client;
import src.Enum.UserRoles;
import src.Model;

public class ProfileFragment extends Fragment {
    Client client;
    private FragmentProfileBinding binding;
    ProfileViewModel profileViewModel;
    //To be shown only to referee
    Button addMatchBtn;
    TextView refereeRatingText;
    TextView refereeRatingScore;
    Model model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        client = ((App) requireActivity().getApplication()).getClient();
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        model = client.getModel();

        showClientInformation();
        removeRefereeInterface(model, root);

        return root;
    }
    @Override
    public void onResume() {
        client = ((App) requireActivity().getApplication()).getClient();
        model = client.getModel();

        showClientInformation();

        super.onResume();
    }
    /**
     * Remove buttons and text from view that don't need to be shown if it's not a referee
     * @param model model
     * @param root Root view
     */
    public void removeRefereeInterface(Model model, View root) {
        if (model.getRole() != UserRoles.REFEREE) {
            //Remove the button if it's not a referee
            addMatchBtn = (Button) root.findViewById(R.id.AddMatch);
            addMatchBtn.setVisibility(View.GONE);
            refereeRatingText = (TextView) root.findViewById(R.id.profileRefereeText);
            refereeRatingScore = (TextView) root.findViewById(R.id.playerRefereeRating);
            refereeRatingText.setVisibility(View.GONE);
            refereeRatingScore.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateELO(int newELO) {
        profileViewModel.setELO(newELO);
    }

    public void showClientInformation() {
        //Bind texts to model to be dynamically updated
        final TextView username = binding.profileUsername;
        final TextView memberSince = binding.memberSince;
        final TextView ELO = binding.playerELORating;
        final TextView refereeScore = binding.playerRefereeRating;
        final TextView publicKey = binding.publicKey;

        String mUsername = model.getUsername();
        int mElo = model.getELO();
        int mRefereeScore = 0;
        try {
            mRefereeScore = model.getRefereeScore();
        } catch (JSONException e) {
        }
        String mPublicKey = model.getPublicKey();
        String mMemberSince = model.getMemberSince();


        profileViewModel.setUsername(mUsername);
        profileViewModel.setRefereeELO(mRefereeScore);
        profileViewModel.setELO(mElo);
        profileViewModel.setPublicKey(mPublicKey);
        profileViewModel.setMemberSince(mMemberSince);

        profileViewModel.getUsername().observe(getViewLifecycleOwner(), username::setText);
        profileViewModel.getELO().observe(getViewLifecycleOwner(), ELO::setText);
        profileViewModel.getMemberSince().observe(getViewLifecycleOwner(), memberSince::setText);
        profileViewModel.getRefereeELO().observe(getViewLifecycleOwner(), refereeScore::setText);
        profileViewModel.getPublicKey().observe(getViewLifecycleOwner(), publicKey::setText);


    }
}