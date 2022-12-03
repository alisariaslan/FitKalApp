package com.pakachu.fitkal;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.pakachu.fitkal.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MenuItem main, foods, calc;
    private Database database;

    private float cal_req, cal_taken;
    private float pro_req, carb_req, fat_req;
    private float pro_taken, carb_taken, fat_taken;
    private boolean has_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        database = new Database(getBaseContext());
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.topbar)));

        binding.fl1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.comefromleft));
        binding.fl2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.comefromright));
        binding.fl3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.comefromleft));

        new CountDownTimer(1000 ,10000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                binding.tedyCons.setVisibility(View.VISIBLE);
                binding.tedyCons.startAnimation(AnimationUtils.loadAnimation(MainActivity.this,R.anim.fadeinslow));
            }
        }.start();




//        List<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(0f, 30f));
//        entries.add(new BarEntry(1f, 50f));
//        entries.add(new BarEntry(2f, 20f));
//        BarDataSet set = new BarDataSet(entries, null);
//        BarData data = new BarData(set);
//        data.setBarWidth(0.9f); // set custom bar width
//        binding.columnChart.setData(data);
//        binding.columnChart.setFitBars(true); // make the x-axis fit exactly all bars
//        binding.columnChart.invalidate(); // refresh
//        binding.columnChart.setDescription(null);
//
//        List<PieEntry> pie_entries = new ArrayList<>();
//        pie_entries.add(new PieEntry(30.0f, getString(R.string.protein)));
//        pie_entries.add(new PieEntry(50.0f, getString(R.string.carb)));
//        pie_entries.add(new PieEntry(20.0f, getString(R.string.fat)));
//        PieDataSet pie_data_set = new PieDataSet(pie_entries, null);
//        pie_data_set.setColors(new int[]{R.color.protein, R.color.carb, R.color.fat}, getBaseContext());
//        pie_data_set.setValueTextColor(R.color.black);
//        PieData pie_data = new PieData(pie_data_set);
//        binding.pieChart.setData(pie_data);
//        binding.pieChart.invalidate(); // refresh

}


    public void profile_check() {
        database = new Database(getBaseContext());

        Cursor cursor = database.get_data_with_sql("SELECT * FROM profile_list");
        if (cursor.moveToNext()) {

            binding.textView8.setText(cursor.getString(1) + getString(R.string.sprofile));
            binding.textView8.clearAnimation();
        } else {
            binding.textView8.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink_anim));
            binding.textView8.setText(getString(R.string.teddy_warning));
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            profile_check();
            macro_check();
        }
    }

    private void macro_check() {
        binding.tvAlinankcal.setText((int) cal_taken + " " + getString(R.string.c_taken));
        binding.tvAlinanpro.setText((int) pro_taken + " " + getString(R.string.g_taken));
        binding.tvAlinankarb.setText((int) carb_taken + " " + getString(R.string.g_taken));
        binding.tvAlinanyag.setText((int) fat_taken + " " + getString(R.string.g_taken));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.mi_home).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mi_foods)
            startActivity(new Intent(this, MainActivity_Foods.class));
        else if (id == R.id.mi_calc) {
            Intent intent = new Intent(this, MainActivity_Profile.class);
            intent.setAction("main_ac");
            startActivity(intent);
        } else if (id == R.id.mi_close_app)
            finish();
        return true;
    }
}