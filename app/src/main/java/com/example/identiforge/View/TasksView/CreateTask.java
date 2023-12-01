package com.example.identiforge.View.TasksView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.identiforge.Model.DateHelper;
import com.example.identiforge.Model.Helper;
import com.example.identiforge.R;
import com.example.identiforge.View.HabitsView.CreateHabit;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateTask extends AppCompatActivity {
    public static final String ID_EXTRA = "id";
    public static final String EDIT_EXTRA = "edit";
    public static final String LIST_EXTRA = "list";
    public static final String TITLE_EXTRA = "title";
    public static final String DESC_EXTRA = "desc";
    public static final String POINTS_EXTRA = "points";
    public static final String IDENTITY_EXTRA = "identity";
    public static final String DAY_EXTRA = "day";
    private boolean editing;
    private int id;
    private String identitySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        editing = false;

        ArrayList<String> spinnerList = getIntent().getStringArrayListExtra(LIST_EXTRA);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);

        EditText titleET = findViewById(R.id.createTask_editTextTitle);
        EditText descET = findViewById(R.id.createTask_editTextDescription);
        EditText pointsET = findViewById(R.id.createTask_editTextPoints);
        TextView dayTV = findViewById(R.id.createTask_setViewDay);
        Spinner identitySpinner = findViewById(R.id.createTask_identitySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        identitySpinner.setAdapter(adapter);

        dayTV.setText(DateHelper.getCurrentDate());

        Bundle b = getIntent().getExtras();
        if(b.containsKey(ID_EXTRA)) {
            editing = true;
            id = b.getInt(ID_EXTRA);
            titleET.setText(b.getString(TITLE_EXTRA));
            descET.setText(b.getString(DESC_EXTRA));
            pointsET.setText(String.valueOf(b.getInt(POINTS_EXTRA)));
            identitySpinner.setSelection(b.getInt(IDENTITY_EXTRA));
            dayTV.setText(b.getString(DAY_EXTRA));
            identitySelected = spinnerList.get(b.getInt(IDENTITY_EXTRA));
        }

        dayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateTask.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // Handle the selected date
                                dayTV.setText(DateHelper.formatDate(day, (month+1), year));
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        identitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identitySelected = spinnerList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button save = findViewById(R.id.createTask_saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String title = titleET.getText().toString();
                    String desc = descET.getText().toString();
                    int points = Integer.parseInt(pointsET.getText().toString());
                    String day = dayTV.getText().toString();

                    if(Helper.isSafe(title) && Helper.isSafe(desc) && Helper.isSafe(points) &&
                        Helper.isSafe(identitySelected) && Helper.isSafe(day)){
                        sendResult(title, desc, points, day);
                    }
                    else{
                        Toast.makeText(CreateTask.this, "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(NumberFormatException exception){
                    Toast.makeText(CreateTask.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendResult(String title, String desc, int points, String day) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(TITLE_EXTRA, title);
        resultIntent.putExtra(DESC_EXTRA, desc);
        resultIntent.putExtra(POINTS_EXTRA, points);
        resultIntent.putExtra(IDENTITY_EXTRA, identitySelected);
        resultIntent.putExtra(DAY_EXTRA, day);
        if(editing) resultIntent.putExtra(ID_EXTRA, id);
        resultIntent.putExtra(EDIT_EXTRA, editing);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}