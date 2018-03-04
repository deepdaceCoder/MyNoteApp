package com.arghadeep.mynoteapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DeleteNoteActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ListView listView = (ListView)findViewById(R.id.list_note);

        try {
            SQLiteOpenHelper myNoteAppDatabaseHelper = new MyNoteAppDatabaseHelper(this);
            db = myNoteAppDatabaseHelper.getReadableDatabase();

            cursor = db.query("NOTE",
                    new String[] {"_id", "TITLE"},
                    null, null, null, null, null);

            CursorAdapter listAdapter = new SimpleCursorAdapter(DeleteNoteActivity.this,
                    android.R.layout.simple_expandable_list_item_1,
                    cursor,
                    new String[] {"TITLE"},
                    new int[] {android.R.id.text1}, 0 );

            listView.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView,
                                    View view,
                                    int position,
                                    long id) {

                try {
                    db.delete("NOTE", "_id=?", new String[] {Integer.toString((int)id)});
                    Cursor newCursor = db.query("NOTE",
                            new String[] {"_id", "TITLE"},
                            null, null, null, null, null);

                    CursorAdapter adapter = (CursorAdapter)listView.getAdapter();
                    adapter.changeCursor(newCursor);
                    cursor=newCursor;
                } catch(SQLiteException e) {
                    Toast toast = Toast.makeText(DeleteNoteActivity.this, "Database Unavailable", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        };

        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
