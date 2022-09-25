package com.pakachu.fitkal;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pakachu.fitkal.databinding.ActivityMainFoodsBinding;
import com.pakachu.fitkal.databinding.FoodItemBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {
    FoodItemBinding binding;
    MainActivity_Foods mainActivity_foods;
    ArrayList<FoodItem> foodItems;
    Activity activity;

    int animation_duration = 1000;
    int animation_duration_implementer = 100;

    public FoodsAdapter(Activity activity, ArrayList<FoodItem> foodItems, MainActivity_Foods mainActivity_foods) {
        this.activity = activity;
        this.foodItems = foodItems;
        this.mainActivity_foods = mainActivity_foods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodsAdapter.ViewHolder(FoodItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        binding.cardView31.animate().rotationY(360f).setDuration(animation_duration);
        animation_duration += animation_duration_implementer;
        FoodItem foodItem = foodItems.get(position);
        binding.textView912.setText(foodItem.first_name);
        Bitmap first_image = null;
        if (Arrays.toString(foodItem.first_img).length() > 2) {
            first_image = BitmapFactory.decodeByteArray(foodItem.first_img, 0, foodItem.first_img.length);
            binding.imageView44.setImageBitmap(first_image);
        }
        Bitmap finalFirst_image = first_image;
        binding.button51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity_foods.show_food_detail(foodItem.first_id, foodItem.first_name, finalFirst_image, foodItem.first_gram, foodItem.first_protein,
                        foodItem.first_carb, foodItem.first_fat);
            }
        });
        if (foodItem.second_id != 0) {
            binding.cardView32.animate().rotationY(360f).setDuration(animation_duration);
            animation_duration += animation_duration_implementer;
            binding.cardView32.setVisibility(View.VISIBLE);
            binding.textView911.setText(foodItem.second_name);
            Bitmap second_image = null;
            if (Arrays.toString(foodItem.second_img).length() > 2) {
                second_image = BitmapFactory.decodeByteArray(foodItem.second_img, 0, foodItem.second_img.length);
                binding.imageView41.setImageBitmap(second_image);
            }
            Bitmap finalSecond_image = second_image;
            binding.button52.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity_foods.show_food_detail(foodItem.second_id, foodItem.second_name, finalSecond_image, foodItem.second_gram, foodItem.second_protein,
                            foodItem.second_carb, foodItem.second_fat);
                }
            });
        }
        if (foodItem.third_id != 0) {
            binding.cardView33.animate().rotationY(360f).setDuration(animation_duration);
            animation_duration += animation_duration_implementer;
            binding.cardView33.setVisibility(View.VISIBLE);
            binding.textView91.setText(foodItem.third_name);
            Bitmap third_image = null;
            if (Arrays.toString(foodItem.third_img).length() > 2) {
                third_image = BitmapFactory.decodeByteArray(foodItem.third_img, 0, foodItem.third_img.length);
                binding.imageView46.setImageBitmap(third_image);
            }
            Bitmap finalThird_image = third_image;
            binding.button53.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity_foods.show_food_detail(foodItem.third_id, foodItem.third_name, finalThird_image, foodItem.third_gram, foodItem.third_protein,
                            foodItem.third_carb, foodItem.third_fat);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull FoodItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}





