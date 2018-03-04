package com.arghadeep.mynoteapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String action = intent.getAction();
        try {
            if (action.equals(Intent.ACTION_SEND)) {
                String message = intent.getStringExtra(Intent.EXTRA_TEXT);
                EditText descriptionView = (EditText) findViewById(R.id.description);
                descriptionView.setText(message);
            }
        } catch (Exception e) {
            Toast toast = Toast.makeText(this, "Add a new note", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onSave(View view) {
        EditText titleView = (EditText)findViewById(R.id.title);
        String titleText = titleView.getText().toString();

        EditText descriptionView = (EditText)findViewById(R.id.description);
        String descriptionText = descriptionView.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm:ss", Locale.getDefault());
        Date date = new Date();
        String dateText = dateFormat.format(date);

        SQLiteOpenHelper myNoteAppDatabaseHelper = new MyNoteAppDatabaseHelper(this);

        try {
            SQLiteDatabase db = myNoteAppDatabaseHelper.getWritableDatabase();
            ContentValues noteValues = new ContentValues();
            noteValues.put("TITLE", titleText);
            noteValues.put("DESCRIPTION", descriptionText);
            noteValues.put("MODE","Date Note Added");
            noteValues.put("DATE",dateText);
            db.insert("NOTE", null, noteValues);
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
