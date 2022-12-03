package com.pakachu.fitkal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.pakachu.fitkal.databinding.ActivityMainProfileBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity_Profile extends AppCompatActivity {

    private ActivityMainProfileBinding binding;
    private Database database;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.topbar)));
        database = new Database(getBaseContext());
        LoadProfile();

        mainActivity = (MainActivity) getIntent().getCharSequenceExtra("main_activity");

        binding.button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.profileView.setVisibility(View.VISIBLE);
                binding.profileView.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fall));
            }
        });

        binding.button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.editTextTextPersonName8.getText().toString();
                if (database.delete_data_from_profile_list(name)) {
                    Toast.makeText(MainActivity_Profile.this, getString(R.string.congrats), Toast.LENGTH_SHORT).show();
                    Clear();
                    LoadProfile();
                }
                else
                    Toast.makeText(MainActivity_Profile.this, getString(R.string.err), Toast.LENGTH_SHORT).show();
            }
        });

        binding.button10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = "";
                int age = 0, height = 0;
                float weight = 0f;
                if (!binding.editTextTextPersonName8.getText().toString().trim().equals(""))
                    name = binding.editTextTextPersonName8.getText().toString().trim();
                if (!binding.editTextTextPersonName9.getText().toString().equals(""))
                    age = Integer.parseInt(binding.editTextTextPersonName9.getText().toString());
                if (!binding.editTextTextPersonName10.getText().toString().equals(""))
                    weight = Float.parseFloat(binding.editTextTextPersonName10.getText().toString());
                if (!binding.editTextTextPersonName11.getText().toString().equals(""))
                    height = Integer.parseInt(binding.editTextTextPersonName11.getText().toString());
                if ((!name.equals("")) && age > 0 && height > 0 && weight > 0f) {
                    if (database.get_data_with_sql("SELECT * FROM profile_list").getCount() > 0) //UPDATE
                    {
                        if (database.update_data_for_profile_list(name, age, weight, height)) {
                            Toast.makeText(MainActivity_Profile.this, getString(R.string.congrats), Toast.LENGTH_SHORT).show();
                            LoadProfile();
                        } else
                            Toast.makeText(MainActivity_Profile.this, getString(R.string.err), Toast.LENGTH_SHORT).show();
                    } else { //INSERT
                        if (database.add_data_to_profile_list(name, age, weight, height, 1)
                        ) {
                            Toast.makeText(MainActivity_Profile.this, getString(R.string.congrats), Toast.LENGTH_SHORT).show();
                            LoadProfile();
                        }
                    }
                } else {
                    Toast.makeText(MainActivity_Profile.this, getString(R.string.err), Toast.LENGTH_SHORT).show();
               }
            }
        });
    }

    private void LoadProfile() {
        Cursor cursor = database.get_data_with_sql("SELECT * FROM profile_list");
        if (cursor.moveToNext()) {
            binding.editTextTextPersonName8.setText(cursor.getString(1));
            binding.editTextTextPersonName9.setText(cursor.getString(2));
            binding.editTextTextPersonName10.setText(cursor.getString(3));
            binding.editTextTextPersonName11.setText(cursor.getString(4));
            binding.textView16.setText(cursor.getString(1) + getString(R.string.sprofile));
        } else {
            binding.textView16.setText( getString(R.string.my_profile));
        }
    }

    private void Clear() {
        binding.editTextTextPersonName8.setText("");
        binding.editTextTextPersonName9.setText("");
        binding.editTextTextPersonName10.setText("");
        binding.editTextTextPersonName11.setText("");
    }

//    private void load_foods(String select_sql) {
//        Cursor cursor = database.get_data_with_sql(select_sql);
//        ArrayList<FoodItem> foodItems = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            Log.e("cursor", "pos: " + cursor.getPosition());
//            FoodItem foodItem = new FoodItem();
//            foodItem.first_id = cursor.getInt(0);
//            foodItem.first_name = cursor.getString(1);
//            foodItem.first_img = cursor.getBlob(2);
//            foodItem.first_gram = cursor.getInt(3);
//            foodItem.first_protein = cursor.getFloat(4);
//            foodItem.first_carb = cursor.getFloat(5);
//            foodItem.first_fat = cursor.getFloat(6);
//            foodItem.first_tags = cursor.getString(7);
//
//            foodItems.add(foodItem);
//        }
//        FoodsAdapter adapterMember = new FoodsAdapter(this, foodItems,);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
//        binding.rvAteList.setLayoutManager(linearLayoutManager);
//        binding.rvAteList.setAdapter(adapterMember);
//
//
//    }


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