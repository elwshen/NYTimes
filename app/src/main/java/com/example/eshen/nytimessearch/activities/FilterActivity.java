package com.example.eshen.nytimessearch.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;

import com.example.eshen.nytimessearch.DatePickerFragment;
import com.example.eshen.nytimessearch.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private int date = -1;
    private String sortOrder;
    private ArrayList<String> newsdeskFields;

    @BindView(R.id.rbNewest) RadioButton rbNewest;
    @BindView(R.id.rbOldest) RadioButton rbOldest;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbPolitics) CheckBox cbPolitics;
    @BindView(R.id.cbUS) CheckBox cbUS;
    @BindView(R.id.cbWorld) CheckBox cbWorld;
    @BindView(R.id.cbDining) CheckBox cbDining;
    @BindView(R.id.cbEditorial) CheckBox cbEditorial;
    @BindView(R.id.cbSports) CheckBox cbSports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sortOrder = getIntent().getStringExtra("sortOrder");

        if (sortOrder.equals("newest")) {
            rbNewest.setChecked(true);
        }
        if (sortOrder.equals("oldest")) {
            rbOldest.setChecked(true);
        }
        newsdeskFields = getIntent().getStringArrayListExtra("fields");


        if (newsdeskFields.contains("Arts")) {
            cbArts.setChecked(true);
        }
        if (newsdeskFields.contains("Dining")) {
            cbDining.setChecked(true);
        }
        if (newsdeskFields.contains("Politics")) {
            cbPolitics.setChecked(true);
        }
        if (newsdeskFields.contains("Editorial")) {
            cbEditorial.setChecked(true);
        }
        if (newsdeskFields.contains("World")) {
            cbWorld.setChecked(true);
        }
        if (newsdeskFields.contains("U.S.")) {
            cbUS.setChecked(true);
        }
        if (newsdeskFields.contains("Sports")) {
            cbSports.setChecked(true);
        }
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        String day = String.valueOf(dayOfMonth);
        String month = String.valueOf(monthOfYear + 1);
        if (dayOfMonth < 10) {
            day = "0" + day;
        }
        if (monthOfYear < 10) {
            month = "0" + month;
        }
        date = Integer.parseInt(String.valueOf(year) + month + day);
    }


    public void onSubmit(View view) {
        Intent data = new Intent();
        data.putExtra("date", date);
        data.putExtra("sortOrder", sortOrder);
        data.putExtra("fields", newsdeskFields);

        setResult(RESULT_OK, data);
        this.finish();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rbNewest:
                if (checked)
                    sortOrder = "newest";
                    break;
            case R.id.rbOldest:
                if (checked)
                    sortOrder = "oldest";
                    break;
        }
    }

    public void onCheckBoxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbArts:
                if (checked)
                    newsdeskFields.add("Arts");
                else
                    newsdeskFields.remove("Arts");
                break;
            case R.id.cbEditorial:
                if (checked)
                    newsdeskFields.add("Editorial");
                else
                    newsdeskFields.remove("Editorial");
                break;
            case R.id.cbSports:
                if (checked)
                    newsdeskFields.add("Sports");
                else
                    newsdeskFields.remove("Sports");
                break;
            case R.id.cbWorld:
                if (checked)
                    newsdeskFields.add("World");
                else
                    newsdeskFields.remove("World");
                break;
            case R.id.cbUS:
                if (checked)
                    newsdeskFields.add("U.S.");
                else
                    newsdeskFields.remove("U.S.");
                break;
            case R.id.cbDining:
                if (checked)
                    newsdeskFields.add("Dining");
                else
                    newsdeskFields.remove("Dining");
                break;
            case R.id.cbPolitics:
                if (checked)
                    newsdeskFields.add("Politics");
                else
                    newsdeskFields.remove("Politics");
                break;
        }
    }


}


