package com.example.leilaoautopecastech.activity.ui.meus_anuncios;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class meus_anunciosViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public meus_anunciosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("fragment de meus anuncios");
    }

    public LiveData<String> getText() {
        return mText;
    }
}