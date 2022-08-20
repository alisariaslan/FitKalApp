package com.pakachu.fitkal;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity_BunuYedim extends AppCompatActivity {
    public static int lol = 0;
    private Database_Sepet database_sepet;

//    private ArrayList<String> list_ad = new ArrayList<>();
//    private ArrayList<Integer> list_gram = new ArrayList<>();
//    private ArrayList<Double> list_pro = new ArrayList<>();
//    private ArrayList<Double> list_karb = new ArrayList<>();
//    private ArrayList<Double> list_yag = new ArrayList<>();
//    private ArrayList<Double> list_sivi = new ArrayList<>();

    private Integer gramaj = 100;
    private Double protein = 0.0;
    private Double karbonhidrat = 0.0;
    private Double yag = 0.0;

//    public void AddData(String ad,String ayrinti,String gram, String protein,String karb, String yag) {
//        boolean insertData = mDatabaseHelper.addData(ad,ayrinti,gram,protein,karb,yag);
//        if(insertData)
//            Toast.makeText(this, "Data added.", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bunuyedim);

        Button btn = findViewById(R.id.btn_cikis);
        btn.setOnClickListener(view -> finish());

        Database_Yemekler database_yemekler = new Database_Yemekler(this);
        database_sepet = new Database_Sepet(this);

        ArrayList<Integer> list_id1 = new ArrayList<>();
        ArrayList<String> list_ad1 = new ArrayList<>();
        ArrayList<String> list_porsiyon1 = new ArrayList<>();
        ArrayList<Integer> list_gram1 = new ArrayList<>();
        ArrayList<Double> list_protein1 = new ArrayList<>();
        ArrayList<Double> list_karb1 = new ArrayList<>();
        ArrayList<Double> list_yag1 = new ArrayList<>();

        String[] food_ad = getResources().getStringArray(R.array.food_ad);
        String[] food_porsiyon = getResources().getStringArray(R.array.food_porsiyon);
        String[] food_gram = getResources().getStringArray(R.array.food_gram);
        String[] food_protein = getResources().getStringArray(R.array.food_protein);
        String[] food_karbs = getResources().getStringArray(R.array.food_karb);
        String[] food_yag = getResources().getStringArray(R.array.food_yag);

        for (int x = 0; x < food_ad.length; x++) {
            list_id1.add(x);
            list_ad1.add(food_ad[x]);
            list_porsiyon1.add(food_porsiyon[x]);
            list_gram1.add(Integer.valueOf(food_gram[x]));
            list_protein1.add(Double.valueOf(food_protein[x]));
            list_karb1.add(Double.valueOf(food_karbs[x]));
            list_yag1.add(Double.valueOf(food_yag[x]));
        }

        Cursor cursor_yemekler = database_yemekler.getData();
        while (cursor_yemekler.moveToNext()) {
            list_id1.add(cursor_yemekler.getInt(0));
            list_ad1.add(cursor_yemekler.getString(2));
            list_porsiyon1.add(cursor_yemekler.getString(4));
            list_gram1.add(Integer.valueOf(cursor_yemekler.getString(5)));
            list_protein1.add(Double.valueOf(cursor_yemekler.getString(6)));
            list_karb1.add(Double.valueOf(cursor_yemekler.getString(7)));
            list_yag1.add(Double.valueOf(cursor_yemekler.getString(8)));
        }


        ImageView iv = findViewById(R.id.imageView2);
        iv.setImageResource(getResources().getIdentifier("y" + lol, "drawable", getPackageName()));
        if(iv.getDrawable()==null) {
            iv.setVisibility(View.GONE);
        }

        TextView tv = findViewById(R.id.textView9);
        tv.setText( list_ad1.get(lol));

        TextView tv3 = findViewById(R.id.textView13);
        tv3.setText( list_protein1.get(lol) + "gr protein");
        protein =  list_protein1.get(lol);

        TextView tv4 = findViewById(R.id.textView14);
        tv4.setText( list_karb1.get(lol) + "gr karbonhidrat");
        karbonhidrat =  list_karb1.get(lol);

        TextView tv5 = findViewById(R.id.textView15);
        tv5.setText( list_yag1.get(lol) + "gr yağ");
        yag =  list_yag1.get(lol);

        SeekBar sb = findViewById(R.id.seekBar);

//        Toast.makeText(this, ""+lol, Toast.LENGTH_SHORT).show();

        TextView tv2 = findViewById(R.id.textView12);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                gramaj = i;
                protein = ((double) i /  list_gram1.get(lol)) *  list_protein1.get(lol);
                karbonhidrat = ((double) i /  list_gram1.get(lol)) *  list_karb1.get(lol);
                yag = ((double) i /  list_gram1.get(lol)) *  list_yag1.get(lol);
                kcal=(int)((protein*4)+(karbonhidrat*4)+(yag*9));
                tv2.setText(i + "gr, "+kcal+"kcal");

                if ((String.valueOf(protein).length() > 4))
                    tv3.setText((String.valueOf(protein).substring(0, 4)) + "gr protein");
                else
                    tv3.setText(protein + "gr protein");
                if ((String.valueOf(karbonhidrat).length() > 4))
                    tv4.setText((String.valueOf(karbonhidrat).substring(0, 4)) + "gr karbonhidrat");
                else
                    tv4.setText(karbonhidrat + "gr karbonhidrat");
                if ((String.valueOf(yag).length() > 4))
                    tv5.setText((String.valueOf(yag).substring(0, 4)) + "gr yağ");
                else
                    tv5.setText(yag + "gr yağ");

                if (i == 0)
                    Toast.makeText(MainActivity_BunuYedim.this, "Gramaj 0 olamaz!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb.setMax(list_gram1.get(lol) * 10);
        sb.setProgress(list_gram1.get(lol));

        tv2.setText(tv2.getText() + " (varsayılan: "+list_porsiyon1.get(lol)+")");

        Button btn3 = findViewById(R.id.btn_artir);
        btn3.setOnClickListener(view -> sb.setProgress(sb.getProgress() + 1));
        Button btn4 = findViewById(R.id.btn_cikart);
        btn4.setOnClickListener(view -> sb.setProgress(sb.getProgress() - 1));

        int sabit=sb.getProgress();

        Button artir1=findViewById(R.id.btn_artir1);
        artir1.setOnClickListener(view -> sb.setProgress(sb.getProgress() + sabit));

        Button cikart1=findViewById(R.id.btn_cikart1);
        cikart1.setOnClickListener(view -> sb.setProgress(sb.getProgress() - sabit));

        EditText et2 = findViewById(R.id.editTextTextPersonName2);
        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    sb.setProgress(Integer.parseInt(editable.toString()));
                }
            }
        });

        ScrollView sv_bunuyedim = findViewById(R.id.sv_bunuyedim);


        Button btn_ekle = findViewById(R.id.btn_ekle);
        btn_ekle.setOnClickListener(view -> {
            //TODO BURAYA SEPETE EKLE EVENT I YAPILACAK
if(gramaj!=0) {
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    boolean hello = database_sepet.addData(lol, sb.getProgress(),kcal,currentDate);
    if (hello) {
        Toast.makeText(MainActivity_BunuYedim.this, "Yediklerim listesine " + gramaj + "gr " +  list_ad1.get(lol) + " eklendi.", Toast.LENGTH_SHORT).show();
        finish();

    } else
        Toast.makeText(MainActivity_BunuYedim.this, "Ekleme başarısız.", Toast.LENGTH_SHORT).show();

} else
    Toast.makeText(MainActivity_BunuYedim.this, "Gramaj 0 olamaz!", Toast.LENGTH_SHORT).show();


        });

        Button btn_caykasik = findViewById(R.id.btn_caykasik);
        btn_caykasik.setOnClickListener(view -> {
            sb.setProgress(2);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });
        Button btn_yemekkasik = findViewById(R.id.btn_yemekkasik);
        btn_yemekkasik.setOnClickListener(view -> {
            sb.setProgress(12);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });
        Button btn_fincan = findViewById(R.id.btn_fincan);
        btn_fincan.setOnClickListener(view -> {
            sb.setProgress(70);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });
        Button btn_caybardak = findViewById(R.id.btn_caybardak);
        btn_caybardak.setOnClickListener(view -> {
            sb.setProgress(100);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });
        Button btn_subardak = findViewById(R.id.btn_subardak);
        btn_subardak.setOnClickListener(view -> {
            sb.setProgress(200);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });
        Button btn_kase = findViewById(R.id.btn_kase);
        btn_kase.setOnClickListener(view -> {
            sb.setProgress(180);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });
        Button btn_tabak = findViewById(R.id.btn_tabak);
        btn_tabak.setOnClickListener(view -> {
            sb.setProgress(300);
            sv_bunuyedim.smoothScrollTo(0, 0);
        });

    }

    int kcal=0;

}