package com.example.identiforge.View.IdentityView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private IdentityRVAdapter adapter;
    private RecyclerView recyclerView;

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
    }

    private void onCreateIdentityResult(Pair<Identity, Boolean> result) {
        Log.d("Message", "OHO:" + result.second);
        Log.d("Message", "OHOOO: " + result.first.getTitle());
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
                deleteIdentity(adapter.getIdentityOn(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
    }
}