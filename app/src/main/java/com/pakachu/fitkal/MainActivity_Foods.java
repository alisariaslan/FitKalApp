package com.pakachu.fitkal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.pakachu.fitkal.databinding.ActivityMainFoodsBinding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity_Foods extends AppCompatActivity {

    private ActivityMainFoodsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainFoodsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.textView2.setOnClickListener(v -> {
            binding.cvCategories.setVisibility(View.VISIBLE);
            binding.cvCategories.setTranslationX(v.getWidth() * 2);
            binding.cvCategories.animate().translationX(0f).setDuration(500);
        });

        binding.textView3.setOnClickListener(v -> binding.cvCategories.animate().translationX(v.getWidth() * 2).setDuration(500));

        binding.imageView.setOnClickListener(v -> {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
            cam_uri = getBaseContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cam_uri);
            cameraActivityResultLauncher.launch(cameraIntent);
        });

        binding.imageView2.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");  //set type
            galleryActivityResultLauncher.launch(intent);
        });

        binding.button.setOnClickListener(v -> binding.imageView.performClick());

        binding.button3.setOnClickListener(v -> binding.imageView2.performClick());

        binding.textView.setOnClickListener(v -> {
            binding.cvNewFood.setVisibility(View.VISIBLE);
            binding.cvNewFood.startAnimation(AnimationUtils.loadAnimation(MainActivity_Foods.this, R.anim.fall));
        });

        binding.textView7.setOnClickListener(v -> {
            binding.editTextTextPersonName2.setText("");
            binding.editTextTextPersonName3.setText("");
            binding.editTextTextPersonName4.setText("");
            binding.editTextTextPersonName5.setText("");
            binding.editTextTextPersonName6.setText("");
            binding.editTextTextPersonName7.setText("");
            binding.cvNewFood.clearAnimation();
            binding.cvNewFood.setVisibility(View.GONE);
            binding.imageView3.setImageDrawable(null);
        });

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean passcard = true;
                String food_name = !binding.editTextTextPersonName2.getText().toString().trim().equals("") ? binding.editTextTextPersonName2.getText().toString().trim() : String.valueOf(passcard = false);
                String food_gram = !binding.editTextTextPersonName3.getText().toString().equals("") ? binding.editTextTextPersonName3.getText().toString() : String.valueOf(passcard = false);
                String food_protein = !binding.editTextTextPersonName4.getText().toString().equals("") ? binding.editTextTextPersonName4.getText().toString() : String.valueOf(passcard = false);
                String food_carb = !binding.editTextTextPersonName5.getText().toString().equals("") ? binding.editTextTextPersonName5.getText().toString() : String.valueOf(passcard = false);
                String food_fat = !binding.editTextTextPersonName6.getText().toString().equals("") ? binding.editTextTextPersonName6.getText().toString() : String.valueOf(passcard = false);
                String food_tags = binding.editTextTextPersonName7.getText().toString().trim();
                Bitmap bitmap;
                byte[] img = new byte[0];
                if (binding.imageView3.getDrawable() != null) {
                    bitmap = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                    img = stream.toByteArray();
                }
                if (passcard) {
                    Database database = new Database(getBaseContext());
                    boolean state = database.add_data_to_food_list(food_name, img, Integer.parseInt(food_gram),
                            Float.parseFloat(food_protein), Float.parseFloat(food_carb), Float.parseFloat(food_fat), food_tags);
                    if (state) {
                        binding.textView7.performClick();
                        Toast.makeText(MainActivity_Foods.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(MainActivity_Foods.this, getString(R.string.fill), Toast.LENGTH_LONG).show();
            }
        });

        binding.textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.cvFoodDetail.animate().translationY(binding.cvFoodDetail.getY() * 2).setDuration(1000);
            }
        });

        load_foods("SELECT * FROM food_list ORDER BY id ASC");
//        load_foods("SELECT * FROM food_list WHERE tags LIKE '%'");
    }

    public void show_food_detail(int id, String name, Bitmap image, int gram, float protein, float carb, float fat) {
        binding.textView15.setText(name);
        binding.imageView5.setImageBitmap(image);
        float protein_percent = (protein/gram)*100;
        float carb_percent = (carb/gram)*100;
        float fat_percent = (fat/gram)*100;
        binding.textView10.setText(getString(R.string.protein) + " " + ((int) protein_percent) + " %");
        binding.textView11.setText(getString(R.string.carb) + " " + ((int) carb_percent) + "  %");
        binding.textView12.setText(getString(R.string.fat) + " " + ((int) fat_percent) + "  %");
        binding.progressBar5.setProgress((int) protein_percent);
        binding.progressBar6.setProgress((int) carb_percent);
        binding.progressBar7.setProgress((int) fat_percent);
        binding.seekBar.setMax(gram);
        binding.seekBar.setProgress(gram);
        binding.textView14.setText(gram + " " + getString(R.string.gram));
        float calories = protein * 4 + carb * 4 + fat * 9;
        float ratio = calories/gram;
        binding.textView13.setText((int) calories + " " + getString(R.string.kcal));
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textView14.setText(progress + " " + getString(R.string.gram));
                float calories_new = progress*ratio;
                binding.textView13.setText((int) calories_new + " " + getString(R.string.kcal));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        binding.cvFoodDetail.setVisibility(View.VISIBLE);
        binding.cvFoodDetail.setTranslationY(binding.cvFoodDetail.getY() * 2);
        binding.cvFoodDetail.animate().translationY(0).setDuration(500);
    }

    private void load_foods(String select_sql) {
        Database database = new Database(getBaseContext());
        Cursor cursor = database.get_data_with_sql(select_sql);
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        while (cursor.moveToNext()) {
            Log.e("cursor", "pos: " + cursor.getPosition());
            FoodItem foodItem = new FoodItem();
            foodItem.first_id = cursor.getInt(0);
            foodItem.first_name = cursor.getString(1);
            foodItem.first_img = cursor.getBlob(2);
            foodItem.first_gram = cursor.getInt(3);
            foodItem.first_protein = cursor.getFloat(4);
            foodItem.first_carb = cursor.getFloat(5);
            foodItem.first_fat = cursor.getFloat(6);
            foodItem.first_tags = cursor.getString(7);
            if (cursor.moveToNext()) {
                Log.e("cursor", "pos: " + cursor.getPosition());
                foodItem.second_id = cursor.getInt(0);
                foodItem.second_name = cursor.getString(1);
                foodItem.second_img = cursor.getBlob(2);
                foodItem.second_gram = cursor.getInt(3);
                foodItem.second_protein = cursor.getFloat(4);
                foodItem.second_carb = cursor.getFloat(5);
                foodItem.second_fat = cursor.getFloat(6);
                foodItem.second_tags = cursor.getString(7);
                if (cursor.moveToNext()) {
                    Log.e("cursor", "pos: " + cursor.getPosition());
                    foodItem.third_id = cursor.getInt(0);
                    foodItem.third_name = cursor.getString(1);
                    foodItem.third_img = cursor.getBlob(2);
                    foodItem.third_gram = cursor.getInt(3);
                    foodItem.third_protein = cursor.getFloat(4);
                    foodItem.third_carb = cursor.getFloat(5);
                    foodItem.third_fat = cursor.getFloat(6);
                    foodItem.third_tags = cursor.getString(7);
                }
            }
            foodItems.add(foodItem);
        }
        FoodsAdapter adapterMember = new FoodsAdapter(this, foodItems, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvFoods.setLayoutManager(linearLayoutManager);
        binding.rvFoods.setAdapter(adapterMember);
    }

    private Uri cam_uri;
    private final ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        binding.imageView3.setImageURI(cam_uri);
                        binding.cvNewFood.setVisibility(View.VISIBLE);
                        binding.cvNewFood.startAnimation(AnimationUtils.loadAnimation(MainActivity_Foods.this, R.anim.fall));
                    }
                }
            });

    private final ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri imageUri = data.getData();
                        binding.imageView3.setImageURI(imageUri);
                        binding.cvNewFood.setVisibility(View.VISIBLE);
                        binding.cvNewFood.startAnimation(AnimationUtils.loadAnimation(MainActivity_Foods.this, R.anim.fall));
                    }
                }
            });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setEnabled(false);
        if (id == R.id.mi_exit) {
            finish();
        }
        return true;
    }


}