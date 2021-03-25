package com.ulan.timetable.activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.ulan.timetable.R;
import com.ulan.timetable.adapters.MarkAdapter;
import com.ulan.timetable.utils.DbHelper;

public class MarkActivity extends AppCompatActivity {
    private ListView listView;
    private MarkAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView =findViewById(R.id.listMark);
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        Log.d("mark", String.valueOf(dbHelper.getMark().size()));

        adapter = new MarkAdapter(this, dbHelper.getMark());
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }




}