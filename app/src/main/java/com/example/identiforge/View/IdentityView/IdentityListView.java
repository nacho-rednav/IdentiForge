package com.example.identiforge.View.IdentityView;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.R;
import com.example.identiforge.View.ResultContracts.CreateIdentityResultContract;

import java.util.List;

public class IdentityListView extends AppCompatActivity {

    private IdentityViewModel viewModel;
    private ActivityResultLauncher<Identity> createIdentityLauncher;
    private ActivityResultLauncher<Intent> dialogLauncher;
    private ActivityResultLauncher<Uri> activityCameraLauncher;

    private IdentityRVAdapter adapter;
    private RecyclerView recyclerView;
    AlertDialog.Builder builder;
    private Uri uri;

    private String title;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identity_list_view);

        createIdentityLauncher = registerForActivityResult(new CreateIdentityResultContract(),
                result -> {
                    if(Helper.isSafe(result)){
                        onCreateIdentityResult(result);
                    }
                });

        dialogLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(Helper.isSafe(result) && result.getResultCode() == RESULT_OK){
                        uri = Helper.getUri(this, title, level);
                        activityCameraLauncher.launch(uri);
                    }
                    else{
                        Toast.makeText(this, "Well, no photo but be proud of yourself!", Toast.LENGTH_SHORT).show();
                    }
                });

        activityCameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                result -> {

                });

        viewModel = new ViewModelProvider(this).get(IdentityViewModel.class);
        viewModel.init(getApplication());
        recyclerView = findViewById(R.id.identity_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new IdentityRVAdapter(this);
        recyclerView.setAdapter(adapter);
        initItemTouchHelper();
        viewModel.getIdentities().observe(this, new Observer<List<Identity>>() {
            @Override
            public void onChanged(List<Identity> identities) {
                adapter.submitList(identities);
            }
        });

        ImageView createButton = findViewById(R.id.identity_imgButt);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createIdentityLauncher.launch(null);
            }
        });

        builder = new AlertDialog.Builder(this);

        builder.setMessage("This will also delete all Tasks and Habits forging this identity!")
                .setTitle("Wait! Are you sure?");

    }

    private void onCreateIdentityResult(Pair<Identity, Boolean> result) {
        if(Helper.isSafe(result) && Helper.isSafe(result.first) && Helper.isSafe(result.second)){
            if(result.second){
                viewModel.updateIdentity(result.first);
            }
            else{
                viewModel.insertIdentity(result.first);
            }
        }
        else{
            Toast.makeText(this, "Something failed", Toast.LENGTH_SHORT);
        }
    }

    protected void launchEdit(Identity identity){
        createIdentityLauncher.launch(identity);
    }

    protected void levelUp(Identity identity){
        if(identity.canLevelUp()){
            viewModel.levelUp(identity);
            title = identity.getTitle();
            level = identity.getLevel() + 1;
            Intent i = new Intent(this, CelbrationDialog.class);
            i.putExtra(CelbrationDialog.TITLE_EXTRA, title);
            i.putExtra(CelbrationDialog.LEVEL_EXTRA, level);
            dialogLauncher.launch(i);
        }
    }

    public void deleteIdentity(Identity identity) {
        //Add dialog, DELETE ALL TASKS AND HABITS OF THIS IDENTITY
        viewModel.deleteIdentity(identity);
        Toast.makeText(this, "Identity deleted", Toast.LENGTH_SHORT).show();
    }

    private void initItemTouchHelper(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteIdentity(adapter.getIdentityOn(viewHolder.getAdapterPosition()));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.dismiss();
                    }
                });
                Dialog d = builder.create();
                d.show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}