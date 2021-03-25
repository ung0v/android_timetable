package com.ulan.timetable.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.ulan.timetable.adapters.FragmentsTabAdapter;
import com.ulan.timetable.fragments.FridayFragment;
import com.ulan.timetable.fragments.MondayFragment;

import com.ulan.timetable.fragments.ThursdayFragment;
import com.ulan.timetable.fragments.TuesdayFragment;
import com.ulan.timetable.fragments.WednesdayFragment;
import com.ulan.timetable.R;


import java.util.Calendar;

import static com.ulan.timetable.utils.BrowserUtil.openUrlInChromeCustomTab;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String DB_NAME = "timetabledb";
    private FragmentsTabAdapter adapter;
    private ViewPager viewPager;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initAll();

    }

    private void initAll() {

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setupUserProfile(navigationView);
        setupFragments();



    }

    private void setupUserProfile(NavigationView navigationView){
       View nav_head = navigationView.getHeaderView(0);
        String name, sClass;
        TextView studentName = (TextView) nav_head.findViewById(R.id.studentName);
        TextView studentClass = (TextView) nav_head.findViewById(R.id.studentClass);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        name = sharedPreferences.getString("studentName", "Name default");
        sClass = sharedPreferences.getString("studentClass", "class default");
        Log.d("main", name + " / "+sClass);
        studentName.setText(name);
        studentClass.setText(sClass);
    }

    private void setupFragments() {
        adapter = new FragmentsTabAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tabLayout);

        adapter.addFragment(new MondayFragment(), getResources().getString(R.string.monday));
        adapter.addFragment(new TuesdayFragment(), getResources().getString(R.string.tuesday));
        adapter.addFragment(new WednesdayFragment(), getResources().getString(R.string.wednesday));
        adapter.addFragment(new ThursdayFragment(), getResources().getString(R.string.thursday));
        adapter.addFragment(new FridayFragment(), getResources().getString(R.string.friday));
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void Logout() {
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("connect");
        editor.remove("studentName");
        editor.remove("studentClass");
        editor.commit();

        SharedPreferences.Editor defEdit =  PreferenceManager.getDefaultSharedPreferences(this).edit();
        defEdit.remove(SettingsActivity.KEY_SCHOOL_WEBSITE_SETTING);
        defEdit.commit();

        this.deleteDatabase(DB_NAME);

        Intent intent = new Intent(this, LoginActitity.class);
        startActivity(intent);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        switch (item.getItemId()) {
            case R.id.schoolwebsitemenu:
                String schoolWebsite = PreferenceManager.getDefaultSharedPreferences(this).getString(SettingsActivity.KEY_SCHOOL_WEBSITE_SETTING, null);
                if (!TextUtils.isEmpty(schoolWebsite)) {
                    openUrlInChromeCustomTab(getApplicationContext(), schoolWebsite);
                } else {
                    Snackbar.make(navigationView, R.string.school_website_snackbar, Snackbar.LENGTH_SHORT).show();
                }
                return true;

            case R.id.mark:
                Intent mark = new Intent(MainActivity.this, MarkActivity.class);
                startActivity(mark);
                return true;
            case R.id.fee:
                Intent fee = new Intent(MainActivity.this, FeeActivity.class);
                startActivity(fee);
                return true;

            case R.id.logout:
                Logout();
                return true;

            case R.id.settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;
            default:
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
        }
    }
}