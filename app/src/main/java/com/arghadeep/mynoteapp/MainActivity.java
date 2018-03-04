package com.arghadeep.mynoteapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        ListView listView = (ListView)findViewById(R.id.list_note);

        try {
            SQLiteOpenHelper myNoteAppDatabaseHelper = new MyNoteAppDatabaseHelper(this);
            db = myNoteAppDatabaseHelper.getReadableDatabase();

            cursor = db.query("NOTE",
                    new String[] {"_id", "TITLE"},
                    null, null, null, null, null);

            CursorAdapter listAdapter = new SimpleCursorAdapter(MainActivity.this,
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
                Intent intent = new Intent(MainActivity.this,ViewNoteActivity.class);
                intent.putExtra(ViewNoteActivity.EXTRA_ID,(int)id);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            SQLiteOpenHelper myNoteAppDatabaseHelper = new MyNoteAppDatabaseHelper(this);
            db = myNoteAppDatabaseHelper.getReadableDatabase();

            Cursor newCursor = db.query("NOTE",
                    new String[] {"_id", "TITLE"},
                    null, null, null, null, null);

            ListView listView = (ListView)findViewById(R.id.list_note);
            CursorAdapter adapter = (CursorAdapter)listView.getAdapter();
            adapter.changeCursor(newCursor);
            cursor=newCursor;
        } catch(SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            Intent intent = new Intent(this, DeleteNoteActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.action_exit) {
            this.finishAffinity();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
