package com.example.identiforge.View.ResultContracts;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.View.IdentityView.CreateIdentity;

public class CreateIdentityResultContract extends ActivityResultContract<Identity, Pair<Identity, Boolean>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Identity input) {
        Intent intent = new Intent(context, CreateIdentity.class);
        if(Helper.isSafe(input)){
            Bundle b = new Bundle();
            b.putString(CreateIdentity.TITLE_EXTRA, input.getTitle());
            b.putString(CreateIdentity.DESC_EXTRA, input.getDescription());
            b.putInt(CreateIdentity.POINTS_EXTRA, input.getLevelUp());
            b.putInt(CreateIdentity.ID_EXTRA, input.getId());
            intent.putExtras(b);
        }
        return intent;
    }

    @Override
    public Pair<Identity, Boolean> parseResult(int i, @Nullable Intent intent) {
        Pair<Identity, Boolean> res = null;
        if(i == RESULT_OK && intent != null){
            String title = intent.getStringExtra(CreateIdentity.TITLE_EXTRA);
            String desc = intent.getStringExtra(CreateIdentity.DESC_EXTRA);
            int points = intent.getIntExtra(CreateIdentity.POINTS_EXTRA, 20);
            Boolean edit = intent.getBooleanExtra(CreateIdentity.EDIT_EXTRA, false);
            Identity identity = new Identity(title, desc, points);
            int id = intent.getIntExtra(CreateIdentity.ID_EXTRA, -1);
            if(id != -1)
                identity.setId(id);
            res = new Pair<>(identity, edit);
        }

        return res;
    }
}
