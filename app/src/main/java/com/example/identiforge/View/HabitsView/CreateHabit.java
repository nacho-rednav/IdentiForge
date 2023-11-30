package com.example.identiforge.View.HabitsView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.identiforge.Model.Helper;
import com.example.identiforge.Model.Identity;
import com.example.identiforge.R;

import java.util.ArrayList;

public class CreateHabit extends AppCompatActivity {

    public static final String ID_EXTRA = "id";
    public static final String EDIT_EXTRA = "edit";
    public static final String LIST_EXTRA = "list";
    public static final String TITLE_EXTRA = "title";
    public static final String DESC_EXTRA = "desc";
    public static final String POINTS_EXTRA = "points";
    public static final String IDENTITY_EXTRA = "identity";
    public static final String MON_EXTRA = "mon";
    public static final String TUES_EXTRA = "tues";
    public static final String WEDNES_EXTRA = "wednes";
    public static final String THURS_EXTRA = "thurs";
    public static final String FRI_EXTRA = "fri";
    public static final String SATUR_EXTRA = "satur";
    public static final String SUN_EXTRA = "sun";

    boolean editing;
    private int id;

    String identitySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_habit);
        editing = false;

        ArrayList<String> spinnerList = getIntent().getStringArrayListExtra(LIST_EXTRA);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);


        EditText titleET = findViewById(R.id.createHabit_editTextTitle);
        EditText descET = findViewById(R.id.createHabit_editTextDescription);
        EditText pointsET = findViewById(R.id.createHabit_editTextPoints);
        CheckBox mondayCB = findViewById(R.id.checkboxMonday);
        CheckBox tuesdayCB = findViewById(R.id.checkboxTuesday);
        CheckBox wednesCB = findViewById(R.id.checkboxWednesday);
        CheckBox thursCB = findViewById(R.id.checkboxThursday);
        CheckBox fridayCB = findViewById(R.id.checkboxFriday);
        CheckBox saturCB = findViewById(R.id.checkboxSaturday);
        CheckBox sunCB = findViewById(R.id.checkboxSunday);
        Spinner identitySpinner = findViewById(R.id.createHabit_identitySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        identitySpinner.setAdapter(adapter);

        Bundle b = getIntent().getExtras();
        if(b.containsKey(ID_EXTRA)){
            editing = true;
            id = b.getInt(ID_EXTRA);
            titleET.setText(b.getString(TITLE_EXTRA));
            descET.setText(b.getString(DESC_EXTRA));
            pointsET.setText(String.valueOf(b.getInt(POINTS_EXTRA)));
            identitySpinner.setSelection(b.getInt(IDENTITY_EXTRA));
            identitySelected = spinnerList.get(b.getInt(IDENTITY_EXTRA));
            mondayCB.setChecked(b.getBoolean(MON_EXTRA));
            tuesdayCB.setChecked(b.getBoolean(TUES_EXTRA));
            wednesCB.setChecked(b.getBoolean(WEDNES_EXTRA));
            thursCB.setChecked(b.getBoolean(THURS_EXTRA));
            fridayCB.setChecked(b.getBoolean(FRI_EXTRA));
            saturCB.setChecked(b.getBoolean(SATUR_EXTRA));
            sunCB.setChecked(b.getBoolean(SUN_EXTRA));
        }

        identitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identitySelected = spinnerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button save = findViewById(R.id.createHabit_saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String title = titleET.getText().toString();
                    String desc = descET.getText().toString();
                    int points = Integer.parseInt(pointsET.getText().toString());
                    boolean monday = mondayCB.isChecked();
                    boolean tuesday = tuesdayCB.isChecked();
                    boolean wednes = wednesCB.isChecked();
                    boolean thurs = thursCB.isChecked();
                    boolean friday = fridayCB.isChecked();
                    boolean satur = saturCB.isChecked();
                    boolean sun = sunCB.isChecked();

                    if(checkRequirements(title, desc, points, monday, tuesday, wednes, thurs,
                            friday, satur, sun)){
                        sendResult(title, desc, points, monday, tuesday, wednes, thurs,
                                friday, satur, sun);
                    }
                    else{
                        Toast.makeText(CreateHabit.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(NumberFormatException exception){
                    Toast.makeText(CreateHabit.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendResult(String title, String desc, int points,
                            boolean monday, boolean tuesday, boolean wednes, boolean thurs,
                            boolean friday, boolean satur, boolean sun) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(TITLE_EXTRA, title);
        resultIntent.putExtra(DESC_EXTRA, desc);
        resultIntent.putExtra(POINTS_EXTRA, points);
        resultIntent.putExtra(IDENTITY_EXTRA, identitySelected);
        resultIntent.putExtra(MON_EXTRA, monday);
        resultIntent.putExtra(TUES_EXTRA, tuesday);
        resultIntent.putExtra(WEDNES_EXTRA, wednes);
        resultIntent.putExtra(THURS_EXTRA, thurs);
        resultIntent.putExtra(FRI_EXTRA, friday);
        resultIntent.putExtra(SATUR_EXTRA, satur);
        resultIntent.putExtra(SUN_EXTRA, sun);
        if(editing) resultIntent.putExtra(ID_EXTRA, id);
        resultIntent.putExtra(EDIT_EXTRA, editing);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private boolean checkRequirements(String title, String desc, int points, boolean monday,
                                      boolean tuesday, boolean wednes, boolean thurs, boolean friday,
                                      boolean satur, boolean sun) {

        boolean res = Helper.isSafe(title) && Helper.isSafe(desc)
                && Helper.isSafe(points) && Helper.isSafe(identitySelected);
        if(res)
            res = monday || tuesday || wednes || thurs || friday || satur || sun;
        return res;
    }
}