package com.example.identiforge.View.IdentityView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.identiforge.Model.Helper;
import com.example.identiforge.R;

public class CreateIdentity extends AppCompatActivity {

    public static final String TITLE_EXTRA = "title";
    public static final String DESC_EXTRA = "desc";
    public static final String POINTS_EXTRA = "points";
    public static final String ID_EXTRA = "id";
    public static final String EDIT_EXTRA = "edit";

    private Boolean editing;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_identity);
        editing = false;

        EditText titleET = findViewById(R.id.createIdentity_editTextTitle);
        EditText descET = findViewById(R.id.createIdentity_editTextDescription);
        EditText pointsET = findViewById(R.id.createIdentity_editTextLevelUpPoints);

        Bundle b = getIntent().getExtras();
        if(Helper.isSafe(b)){
            titleET.setText(b.getString(TITLE_EXTRA));
            descET.setText(b.getString(DESC_EXTRA));
            pointsET.setText(String.valueOf(b.getInt(POINTS_EXTRA)));
            id = b.getInt(ID_EXTRA);
            editing = true;
        }

        Button saveButt = findViewById(R.id.createIdentity_saveButton);
        saveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String title = titleET.getText().toString();
                    String desc = descET.getText().toString();
                    int points = Integer.parseInt(pointsET.getText().toString());
                    if(Helper.isSafe(title) && Helper.isSafe(desc) && Helper.isSafe(points)){
                        sendResult(title, desc, points);
                    }
                    else{
                        Toast.makeText(CreateIdentity.this, "Wrong input", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException ex){
                    Toast.makeText(CreateIdentity.this, "Wrong input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendResult(String title, String desc, int points) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TITLE_EXTRA, title);
        resultIntent.putExtra(DESC_EXTRA, desc);
        resultIntent.putExtra(POINTS_EXTRA, points);
        resultIntent.putExtra(EDIT_EXTRA, editing);
        if(editing) resultIntent.putExtra(ID_EXTRA, id);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}