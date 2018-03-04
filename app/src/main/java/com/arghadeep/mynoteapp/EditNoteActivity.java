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

public class EditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";
    private int noteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        EditText title = (EditText)findViewById(R.id.title);
        EditText description = (EditText)findViewById(R.id.description);

        Intent intent = getIntent();
        String titleText = intent.getStringExtra(EXTRA_TITLE);
        String descriptionText = intent.getStringExtra(EXTRA_DESCRIPTION);
        noteId = intent.getIntExtra("noteId",1);

        title.setText(titleText);
        description.setText(descriptionText);
    }

    public void onSave(View view) {
        EditText title = (EditText)findViewById(R.id.title);
        EditText description = (EditText)findViewById(R.id.description);
        String titleText = title.getText().toString();
        String descriptionText = description.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm:ss", Locale.getDefault());
        Date date = new Date();
        String dateText = dateFormat.format(date);

        SQLiteOpenHelper myNoteAppDatabaseHelper = new MyNoteAppDatabaseHelper(this);

        try {
            SQLiteDatabase db = myNoteAppDatabaseHelper.getWritableDatabase();
            ContentValues noteValues = new ContentValues();
            noteValues.put("TITLE", titleText);
            noteValues.put("DESCRIPTION", descriptionText);
            noteValues.put("MODE","Date Note Edited");
            noteValues.put("DATE",dateText);
            db.update("NOTE",
                    noteValues,
                    "_id=?",
                    new String[] {Integer.toString(noteId)});
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
