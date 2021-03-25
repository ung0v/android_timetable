package com.ulan.timetable.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.ulan.timetable.R;
import com.ulan.timetable.model.Fee;
import com.ulan.timetable.utils.DbHelper;

import java.util.ArrayList;

public class FeeActivity extends AppCompatActivity {
    private TextView total, paid, overage;
    ArrayList<Fee> feeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initAll();
    }

    private void initAll() {
        total = findViewById(R.id.feetotal);
        paid = findViewById(R.id.feepaid);
        overage = findViewById(R.id.feeoverage);


        DbHelper dbHelper = new DbHelper(this);
        feeList = dbHelper.getFee();
        Fee fee = feeList.get(0);
        Log.d("feeo", fee.toString());
        total.setText(fee.getTotal());
        paid.setText(fee.getPaid());
        overage.setText(fee.getOverage());

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