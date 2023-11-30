package com.example.identiforge.View.IdentityView;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.identiforge.Logic.Controller;
import com.example.identiforge.Logic.ControllerImp;
import com.example.identiforge.Model.Identity;

import java.util.List;

public class IdentityViewModel extends ViewModel {

    private  LiveData<List<Identity>> list;
    private Controller controller;
    public void init(Application app){
        controller = Controller.get(app);
        list = controller.getIdentities();
    }

    public void insertIdentity(Identity identity){ controller.insertIdentity(identity);}
    public void updateIdentity(Identity identity){controller.updateIdentity(identity);}
    public  void deleteIdentity(Identity identity){controller.deleteIdentity(identity);}
    public void levelUp(Identity identity) {controller.levelUp(identity);}
    public LiveData<List<Identity>> getIdentities() { return list;}
}
