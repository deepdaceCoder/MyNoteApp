package com.arghadeep.mynoteapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import java.util.Date;

public class ViewNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    private int noteId;
    private Cursor cursor;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        try {
            SQLiteOpenHelper myNoteAppDatabaseHelper = new MyNoteAppDatabaseHelper(this);
            db = myNoteAppDatabaseHelper.getReadableDatabase();

            noteId = (Integer)getIntent().getExtras().get(EXTRA_ID);

            cursor = db.query("NOTE",
                    new String[] {"_id", "TITLE", "DESCRIPTION", "MODE", "DATE"},
                    "_id=?",
                    new String[] {Integer.toString(noteId)},
                    null, null, null);

            if(cursor.moveToFirst()) {
                String titleText = cursor.getString(1);
                String descriptionText = cursor.getString(2);
                String modeText = cursor.getString(3);
                String dateText = cursor.getString(4);

                TextView title = (TextView)findViewById(R.id.title);
                TextView description = (TextView)findViewById(R.id.description);
                TextView mode = (TextView)findViewById(R.id.mode);
                TextView dateOb = (TextView)findViewById(R.id.dateText);

                title.setText(titleText);
                description.setText(descriptionText);
                mode.setText(modeText);
                dateOb.setText(dateText);
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            try {
                db.delete("NOTE", "_id=?", new String[] {Integer.toString(noteId)});

                startActivity(new Intent(this,MainActivity.class));
            } catch (SQLiteException e) {
                Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        else if (id == R.id.action_share) {
            TextView description = (TextView)findViewById(R.id.description);
            String descriptionText = description.getText().toString();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/*");
            intent.putExtra(Intent.EXTRA_TEXT,descriptionText);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEdit(View view) {
        TextView title = (TextView)findViewById(R.id.title);
        TextView description = (TextView)findViewById(R.id.description);

        String titleText = title.getText().toString();
        String descriptionText = description.getText().toString();

        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.EXTRA_TITLE,titleText);
        intent.putExtra(EditNoteActivity.EXTRA_DESCRIPTION,descriptionText);
        intent.putExtra("noteId",noteId);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
