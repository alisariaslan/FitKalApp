package com.pakachu.fitkal;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity_Foods extends AppCompatActivity {

    private ActivityMainFoodsBinding binding;
    private String currentSQL = "SELECT * FROM food_list ORDER BY id ASC";
    private boolean searchByName = false;
    private boolean firstLoad = true;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainFoodsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.topbar)));

        database = new Database(getBaseContext());

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
            ((InputMethodManager) MainActivity_Foods.this.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean passcard = true;
                String food_name = !binding.editTextTextPersonName2.getText().toString().trim().equals("") ? binding.editTextTextPersonName2.getText().toString().trim() : String.valueOf(passcard = false);
                String food_gram = !binding.editTextTextPersonName3.getText().toString().equals("") ? binding.editTextTextPersonName3.getText().toString().trim() : String.valueOf(passcard = false);
                String food_protein = !binding.editTextTextPersonName4.getText().toString().equals("") ? binding.editTextTextPersonName4.getText().toString().trim() : String.valueOf(passcard = false);
                String food_carb = !binding.editTextTextPersonName5.getText().toString().equals("") ? binding.editTextTextPersonName5.getText().toString().trim() : String.valueOf(passcard = false);
                String food_fat = !binding.editTextTextPersonName6.getText().toString().equals("") ? binding.editTextTextPersonName6.getText().toString().trim() : String.valueOf(passcard = false);
                String food_tags = binding.editTextTextPersonName7.getText().toString().trim();
                String food_ids = "";
                if (Float.parseFloat(food_protein) > Float.parseFloat(food_gram) |
                        Float.parseFloat(food_carb) > Float.parseFloat(food_gram) |
                        Float.parseFloat(food_fat) > Float.parseFloat(food_gram) |
                        Float.parseFloat(food_protein) + Float.parseFloat(food_carb) + Float.parseFloat(food_fat) > Float.parseFloat(food_gram))
                    passcard = false;
                Bitmap bitmap;
                byte[] img = new byte[0];
                if (binding.imageView3.getDrawable() != null) {
                    bitmap = ((BitmapDrawable) binding.imageView3.getDrawable()).getBitmap();
                    float width = bitmap.getWidth();
                    float height = bitmap.getHeight();

                    float new_width = width, new_height = height;
                    float ratio = width / height;

                    while (new_width > 200) {
                        new_width /= 2;
                        new_height /= 2;
                    }
                    new_width*=ratio;
                    new_height*=ratio;

                    Log.e("bitmap", "x: " + new_width + ", y: " + new_height);
                    Matrix matrix = new Matrix();

                    float scaleWidth = (new_width) / width;
                    float scaleHeight = (new_height) / height;

                    matrix.postScale(scaleWidth, scaleHeight);
                    Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) width, (int) height, matrix, true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                    img = stream.toByteArray();
                }
                if (passcard) {

                    String[] taglist = food_tags.split(",");
                    for (String tag : taglist
                    ) {
                        tag = tag.trim();
                        if (!tag.equals("")) {
                            if (!database.get_tags_array("SELECT * FROM tag_list").contains(tag)) {
                                database.add_data_to_tag_list(tag);
                            }
                            food_ids += "," + database.get_tag_id(tag);
                        }
                    }
                    Log.e("tags", "id: " + food_ids);

                    boolean state = database.add_data_to_food_list(food_name, img, Integer.parseInt(food_gram),
                            Float.parseFloat(food_protein), Float.parseFloat(food_carb), Float.parseFloat(food_fat), food_ids);
                    if (state) {
                        binding.textView7.performClick();
                        Toast.makeText(MainActivity_Foods.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                        load_foods(currentSQL);
                        ((InputMethodManager) MainActivity_Foods.this.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } else
                        Toast.makeText(MainActivity_Foods.this, getString(R.string.error) + "376", Toast.LENGTH_SHORT).show();
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

        binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execute_sql("DELETE FROM food_list WHERE id=" + v.getTag());
                Toast.makeText(MainActivity_Foods.this, getString(R.string.delete), Toast.LENGTH_SHORT).show();
                binding.textView9.performClick();
                load_foods(currentSQL);
            }
        });

        binding.editTextTextPersonName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (searchByName)
                    load_foods("SELECT * FROM food_list WHERE name LIKE '" + s + "%'");
            }
        });

        binding.editTextTextPersonName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    searchByName = true;
                    binding.editTextTextPersonName.setText("");
                }
                return false;
            }
        });

        binding.lvCheckes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = binding.lvCheckes.getItemAtPosition(position).toString();
                searchByName = false;
                binding.editTextTextPersonName.setText(getString(R.string.find_food_by_c) + ": " + selected);
                int tag_id = database.get_tag_id(selected);
                load_foods("SELECT * FROM food_list WHERE tags LIKE '%" + tag_id + "%'");
            }
        });

        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekMax = binding.seekBar.getMax();
                if(seekMax<9999)
                {
                    seekMax*=2;
                    binding.seekBar.setMax(seekMax);
                    binding.seekBar.setProgress(seekMax);
                }else Toast.makeText(MainActivity_Foods.this, ""+getString(R.string.max), Toast.LENGTH_SHORT).show();
            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekProgress = binding.seekBar.getProgress();
                if(database.check_profile()==0)
                {

                    Toast.makeText(MainActivity_Foods.this, ""+getString(R.string.teddy_warning), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(seekProgress>0)
                {
                    binding.textView9.performClick();
                    Toast.makeText(MainActivity_Foods.this, ""+getString(R.string.daily_add), Toast.LENGTH_SHORT).show();
                    database.add_data_to_eaten_list(database.get_food_id(binding.textView15.getText().toString()),seekProgress);
                } else Toast.makeText(MainActivity_Foods.this, ""+getString(R.string.please), Toast.LENGTH_SHORT).show();
            }
        });


        load_foods(currentSQL);
    }


    public void show_food_detail(int id, String name, Bitmap image, int gram, float protein, float carb, float fat) {
        binding.imageView4.setTag(id);
        binding.textView15.setText(name);
        binding.imageView5.setImageBitmap(image);
        float protein_percent = (protein / gram) * 100;
        float carb_percent = (carb / gram) * 100;
        float fat_percent = (fat / gram) * 100;
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
        float ratio = calories / gram;
        binding.textView13.setText((int) calories + " " + getString(R.string.kcal));
        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.textView14.setText(progress + " " + getString(R.string.gram));
                float calories_new = progress * ratio;
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

    private ArrayList<String> tag_barrel(ArrayList<String> array, String tags) {
        String[] id_list = tags.split(",");
        for (String id : id_list
        ) {
            id = id.trim();
            if (!id.equals("")) {
                String tag = database.get_tag_from_id(Integer.parseInt(id));
                if (!array.contains(tag)) {
                    array.add(tag);
                    Log.e("taglist", "Tag: " + tag);
                }
            }
        }
        return array;
    }

    private void load_foods(String select_sql) {
        currentSQL = select_sql;
        Cursor cursor = database.get_data_with_sql(select_sql);
        ArrayList<FoodItem> foodItems = new ArrayList<>();
        ArrayList<String> arrayList = new ArrayList<>();
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
            if (firstLoad)
                tag_barrel(arrayList, foodItem.first_tags);
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
                if (firstLoad)
                    tag_barrel(arrayList, foodItem.second_tags);
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
                    if (firstLoad)
                        tag_barrel(arrayList, foodItem.third_tags);
                }
            }
            foodItems.add(foodItem);
        }
        FoodsAdapter adapterMember = new FoodsAdapter(this, foodItems, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvFoods.setLayoutManager(linearLayoutManager);
        binding.rvFoods.setAdapter(adapterMember);
        ArrayAdapter<String> categories = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        if (firstLoad)
            binding.lvCheckes.setAdapter(categories);
        firstLoad = false;
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