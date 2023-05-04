package com.example.eloworld.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> ELO;
    private final MutableLiveData<String> refereeELO;
    private final MutableLiveData<String> username;
    private final MutableLiveData<String> memberSince;
    private final MutableLiveData<String> publicKey;
    private final MutableLiveData<String> follows;

    public ProfileViewModel() {

        ELO = new MutableLiveData<>();
        refereeELO = new MutableLiveData<>();
        username = new MutableLiveData<>();
        memberSince = new MutableLiveData<>();
        publicKey = new MutableLiveData<>();
        follows = new MutableLiveData<>();
    }

    public LiveData<String> getELO() {
        return ELO;
    }
    public LiveData<String> getRefereeELO() {return refereeELO;}
    public LiveData<String> getUsername() {return username;}
    public LiveData<String> getMemberSince() {return memberSince;}
    public LiveData<String> getPublicKey() {return publicKey;}
    public LiveData<String> getFollows() {return follows;}

    public void setUsername(String newUsername) {username.setValue(newUsername);}
    public void setELO(int newELO) {ELO.setValue(String.valueOf(newELO));}
    public void setRefereeELO(int newELO) {refereeELO.setValue(String.valueOf(newELO));}
    public void setMemberSince(String newMemberSince) {memberSince.setValue("Membre depuis " + newMemberSince);}
    public void setPublicKey(String newPublicKey) {publicKey.setValue(newPublicKey);}
    public void setFollows(int newFollowsCount) {follows.setValue(String.valueOf(newFollowsCount));}
}