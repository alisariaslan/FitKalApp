package com.pakachu.fitkal;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.pakachu.fitkal.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MenuItem main,foods,calc;


    private float cal_req,cal_taken;
    private float pro_req,carb_req,fat_req;
    private float pro_taken,carb_taken,fat_taken;
    private boolean has_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        profile_check();
        macro_check();

        binding.fl1.startAnimation(AnimationUtils.loadAnimation(this,R.anim.comefromleft));
        binding.fl2.startAnimation(AnimationUtils.loadAnimation(this,R.anim.comefromright));
        binding.fl3.startAnimation(AnimationUtils.loadAnimation(this,R.anim.comefromleft));
    }

    private void profile_check() {
        if(!has_profile) {
            binding.textView8.startAnimation(AnimationUtils.loadAnimation(this,R.anim.blink_anim));

        }
    }

    private void macro_check() {
        binding.tvAlinankcal.setText((int)cal_taken+" "+getString(R.string.c_taken));
        binding.tvAlinanpro.setText((int)pro_taken+" "+getString(R.string.g_taken));
        binding.tvAlinankarb.setText((int)carb_taken+" "+getString(R.string.g_taken));
        binding.tvAlinanyag.setText((int)fat_taken+" "+getString(R.string.g_taken));
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
        else if (id == R.id.mi_calc)
            startActivity(new Intent(this, MainActivity_Profile.class));
         else if (id == R.id.mi_close_app)
            finish();
        return true;
    }

}