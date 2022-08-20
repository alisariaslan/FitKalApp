package com.pakachu.fitkal;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity_YemekDetay extends AppCompatActivity {

    public static int lol = 0;
    public static ArrayList<String> list_ad = new ArrayList<>();
    public static ArrayList<String> list_fayda = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yemekdetay);

        Button btn = findViewById(R.id.btnexit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String[] food_names = getResources().getStringArray(R.array.food_ad);
        String[] food_faydalar = getResources().getStringArray(R.array.food_ayrinti);

        for (int x = 0; x < food_names.length; x++) {
            list_ad.add(food_names[x]);
            list_fayda.add(food_faydalar[x]);
        }

        Database_Yemekler database_yemekler = new Database_Yemekler(this);
        Cursor cursor_yemekler = database_yemekler.getData();
        while (cursor_yemekler.moveToNext()) {
            list_ad.add(cursor_yemekler.getString(2));
            list_fayda.add(cursor_yemekler.getString(3));
        }


        ImageView iv = findViewById(R.id.imageView);
        iv.setImageResource(getResources().getIdentifier("y" + lol, "drawable", getPackageName()));

        TextView tv = findViewById(R.id.textView2);
        tv.setText(list_ad.get(lol));

        TextView tv2 = findViewById(R.id.textView7);
        String faydasi = "";
//        faydasi+="100gr da: " + plist[anInt] + "gr Protein, " + klist[anInt] + "gr Karbonhidrat, " + ylist[anInt] + "gr Yağ bulunmaktadır.";
//        faydasi+="\n\n"+fflist[anInt];
        tv2.setText(list_fayda.get(lol));
    }


}