package com.pakachu.fitkal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.pakachu.fitkal.databinding.ActivityMainProfileBinding;

public class MainActivity_Profile extends AppCompatActivity {

    private ActivityMainProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mi_exit)
            finish();
        return true;
    }

}