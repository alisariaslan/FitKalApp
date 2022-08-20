package com.pakachu.fitkal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity_Sepet extends AppCompatActivity {

    private LinearLayout linearLayout;

    private Database_Sepet database_sepet;
    //    private Database_Yediklerim mDatabaseYediklerim;
    private ListView mListView;
    public boolean silmod = false;
    int a = 0;
//    boolean birkere = false;

    private ArrayList<Integer> list_id = new ArrayList<>();
    private ArrayList<Double> list_urunid = new ArrayList<>(); //ürün no
    private ArrayList<String> list_ad1 = new ArrayList<>(); //ad
    private ArrayList<Double> list_gramaj = new ArrayList<>(); //gram
    private ArrayList<Double> list_gram = new ArrayList<>();
    private ArrayList<Double> list_protein1 = new ArrayList<>();
    private ArrayList<Double> list_karb1 = new ArrayList<>();
    private ArrayList<Double> list_yag1 = new ArrayList<>();
    private ArrayList<Integer> list_kcal = new ArrayList<>();
    private int x;



    private int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void populateListView() {
        x = -1;
        Cursor data = database_sepet.getData();
        while (data.moveToNext()) {
            x++;
            list_id.add(data.getInt(0));
            list_urunid.add(data.getDouble(1));
            list_gramaj.add(Double.valueOf(data.getString(2)));
            list_kcal.add(data.getInt(3));

            Log.e("besin","id: "+list_urunid.get(x).toString());
            Log.e("besin","gram: "+list_gramaj.get(x).toString());
            Log.e("besin","kcal: "+list_kcal.get(x).toString());

//            double pro = (list_gramaj.get(x) / list_gram.get(list_urunid.get(x).intValue())) * list_protein1.get(list_urunid.get(x).intValue());
//            double karb = (list_gramaj.get(x) / list_gram.get(list_urunid.get(x).intValue())) * list_karb1.get(list_urunid.get(x).intValue());
//            double yag = (list_gramaj.get(x) / list_gram.get(list_urunid.get(x).intValue())) * list_yag1.get(list_urunid.get(x).intValue());
//            double kcal = pro * 4 + karb * 4 + yag * 9;

//            Toast.makeText(this, pro+" "+karb+" "+yag+" "+kcal, Toast.LENGTH_SHORT).show();

            //VIEW
            LinearLayout ll_yatay = new LinearLayout(this);
            LinearLayout.LayoutParams ll_yatay_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            ll_yatay_params.setMargins(0, 0, 0, dpToPx(30, this));
            ll_yatay.setOrientation(LinearLayout.HORIZONTAL);
            ll_yatay.setLayoutParams(ll_yatay_params);
            ll_yatay.setBackgroundColor(Color.argb(10, 0, 0, 0));
            ll_yatay.setPadding(0, dpToPx(10, this), 0, 0);
            int finalA = a;
            CountDownTimer countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Toast.makeText(MainActivity_Sepet.this, "Long click", Toast.LENGTH_SHORT).show();
                }
            };
            ll_yatay.setTag(x);
            ll_yatay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        ll_yatay.setBackgroundColor(Color.argb(10, 0, 0, 0));
                        countDownTimer.cancel();
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        ll_yatay.setBackgroundColor(Color.argb(10, 0, 255, 0));
                        countDownTimer.start();

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        ll_yatay.setBackgroundColor(Color.argb(10, 0, 0, 0));
                        countDownTimer.cancel();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
//                                        Toast.makeText(MainActivity_Sepet.this, "" + ll_yatay.getTag(), Toast.LENGTH_SHORT).show();

                                        int t = (int) ll_yatay.getTag();
                                        database_sepet.deleteID(list_id.get(t));
                                        finish();
                                        startActivity(new Intent(MainActivity_Sepet.this, MainActivity_Sepet.class));
//                                        list_id.remove(t);
//                                        list_urunid.remove(t);
//                                        list_gramaj.remove(t);
//
//                                        list_gram.remove(t);
//                                        list_protein1.remove(t);
//                                        list_karb1.remove(t);
//                                        list_yag1.remove(t);
//                                        ll_yatay.setVisibility(View.GONE);

                                        break;
                                    case DialogInterface.BUTTON_NEGATIVE:

                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Sepet.this);
                        builder.setMessage("Silmek istediğinizden emin misiniz?").setPositiveButton("Sil", dialogClickListener)
                                .setNegativeButton("İptal", dialogClickListener).setTitle("Uyarı").show();
                    }

                    return true;
                }
            });

            //RESİM
            ImageView iv = new ImageView(this);
            int drawable = getResources().getIdentifier("y" + (list_urunid.get(x).intValue()), "drawable", getPackageName());
            if (drawable == 0)
                iv.setImageResource(R.drawable.y00);
            else
                iv.setImageResource(drawable);

            LinearLayout.LayoutParams ivparams = new LinearLayout.LayoutParams(dpToPx(100, this), dpToPx(100, this));
            ivparams.setMargins(dpToPx(30, this), 0, 0, dpToPx(10, this));
            iv.setLayoutParams(ivparams);
            ll_yatay.addView(iv);

            LinearLayout ll_yazilar = new LinearLayout(this);
            LinearLayout.LayoutParams ll_yazilar_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll_yazilar.setOrientation(LinearLayout.VERTICAL);

//            String idStr = "";
//            if (!list_id.get(a).toString().matches("-1")) {
//                idStr+=" [";
//                idStr+= list_id.get(a).toString();
//                idStr+="]";
//            }


            //            textView.setText(list_ad1.get(list_urunid.get(x)) + " " + list_gramaj.get(x) + "gr");


            //YAZI
            TextView textView = new TextView(this);
            Integer id = list_urunid.get(x).intValue();
            textView.setText(list_ad1.get(id));
            textView.setTextSize(dpToPx(14, this));
            textView.setPadding(dpToPx(30, this), 0, 0, 0);
            ll_yazilar.addView(textView);

            //ALTYAZI
            TextView textView2 = new TextView(this);
//            textView2.setText((list_gramaj.get(a)+idStr).trim());
            textView2.setText(list_gramaj.get(x) + "gr");
            textView2.setTextSize(dpToPx(10, this));
            textView2.setPadding(dpToPx(30, this), 0, 0, 0);
            ll_yazilar.addView(textView2);

            //MINIYAZI
            TextView textView3 = new TextView(this);
//            double kcal = (list_protein1.get(a) * 4) + (list_karb1.get(a) * 4) + (list_yag1.get(a) * 9);
//            textView3.setText((int) kcal + " Kalori");
            textView3.setText(list_kcal.get(x) + " Kalori");
//            textView3.setText(list_kcal.get(x) + " kcal");
            textView3.setTextSize(dpToPx(8, this));
            textView3.setPadding(dpToPx(30, this), 0, 0, 0);
            ll_yazilar.addView(textView3);

            ll_yatay.addView(ll_yazilar);
            linearLayout.addView(ll_yatay);


//            LinearLayout.LayoutParams lpmargin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            lpmargin.setMargins(0, 0, 0, 30);
//
//            LinearLayout ll_contentBorder = new LinearLayout(this);
//            ll_contentBorder.setLayoutParams(lpmargin);
//            ll_contentBorder.setBackgroundColor(Color.argb(255, 0, 0, 0));
//            ll_contentBorder.setPadding(2, 2, 2, 2);
//            ll_contentBorder.setTag(x);
//
//            ll_contentBorder.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    if (event.getAction() == MotionEvent.ACTION_UP) {
//                        ll_contentBorder.setBackgroundColor(Color.argb(255, 0, 0, 0));
//                        if (silmod) {
//
////                            Toast.makeText(MainActivity_Sepet.this,""+ ll_contentBorder.getTag() , Toast.LENGTH_SHORT).show();
////                            mDatabaseHelper.deleteBoth(list_urunid.get(x),list_gramaj.get(x));
//                            int t = Integer.valueOf((Integer) ll_contentBorder.getTag());
//                            database_sepet.deleteID(list_id.get(t));
//                            list_id.remove(t);
//                            list_urunid.remove(t);
//                            list_gramaj.remove(t);
//                            ll_contentBorder.setVisibility(View.GONE);
//                        }
//                        // Do what you want
//                        return true;
//                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                        ll_contentBorder.setBackgroundColor(Color.argb(255, 255, 50, 50));
//
//                        // Do what you want
//                        return true;
//                    }
//                    return false;
//                }
//            });
//
//
//            LinearLayout ll_imgBorder = new LinearLayout(this);
//            ll_imgBorder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            ll_imgBorder.setBackgroundColor(Color.argb(255, 255, 255, 255));
//
//            TextView textView = new TextView(this);
//
//            int drawable = getResources().getIdentifier("y" + (list_urunid.get(x)), "drawable", getPackageName());
//            if (drawable == 0)
//                textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.y00), null, null, null);
//            else
//                textView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(drawable), null, null, null);
//
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            textView.setText(list_ad1.get(list_urunid.get(x)) + " " + list_gramaj.get(x) + "gr");
//            textView.setTextSize(24f);
//            textView.setGravity(Gravity.CENTER_VERTICAL);
//            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            ll_imgBorder.addView(textView);
//            ll_contentBorder.addView(ll_imgBorder);
//            linearLayout.addView(ll_contentBorder);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sepet);
        linearLayout = findViewById(R.id.ll_sv_sepet);


        String[] food_ad = getResources().getStringArray(R.array.food_ad);
        String[] food_gram = getResources().getStringArray(R.array.food_gram);
        String[] food_protein = getResources().getStringArray(R.array.food_protein);
        String[] food_karbs = getResources().getStringArray(R.array.food_karb);
        String[] food_yag = getResources().getStringArray(R.array.food_yag);

        for (int x = 0; x < food_ad.length; x++) {
            list_ad1.add(food_ad[x]);
            list_gram.add(Double.valueOf(food_gram[x]));
            list_protein1.add(Double.valueOf(food_protein[x]));
            list_karb1.add(Double.valueOf(food_karbs[x]));
            list_yag1.add(Double.valueOf(food_yag[x]));
//            list_kcal.add( Double.valueOf(food_protein[x])*4+Double.valueOf(food_karbs[x])*4+Double.valueOf(food_yag[x])*9);
        }


        Database_Yemekler database_yemekler = new Database_Yemekler(this);
        Cursor cursor_yemekler = database_yemekler.getData();
        while (cursor_yemekler.moveToNext()) {
            list_ad1.add(cursor_yemekler.getString(2));
            list_gram.add(cursor_yemekler.getDouble(4));
            list_protein1.add(cursor_yemekler.getDouble(5));
            list_karb1.add(cursor_yemekler.getDouble(6));
            list_yag1.add(cursor_yemekler.getDouble(7));
//            list_kcal.add(cursor_yemekler.getDouble(5)*4+cursor_yemekler.getDouble(6)*4+cursor_yemekler.getDouble(7)*9);
        }

        database_sepet = new Database_Sepet(this);

        populateListView();

        Button btn_sepet_kaydet = findViewById(R.id.btn_sepet_kaydet);

        Button btn_sepet_sil = findViewById(R.id.btn_sepet_sil);
        ActionBar actionBar = getSupportActionBar();
        btn_sepet_sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (a) {
                    case 0:
                        Toast.makeText(MainActivity_Sepet.this, "SİLMEK İÇİN TIKLAYIN", Toast.LENGTH_SHORT).show();
                        silmod = true;
                        btn_sepet_sil.setText("iptal");
                        btn_sepet_kaydet.setEnabled(false);
                        actionBar.hide();
                        a++;
                        break;
                    case 1:
                        Toast.makeText(MainActivity_Sepet.this, "SİLME KAPATILDI", Toast.LENGTH_SHORT).show();
                        silmod = false;
                        btn_sepet_sil.setText("sil");
                        btn_sepet_kaydet.setEnabled(true);
                        actionBar.show();
                        a--;
                }

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
//        menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.app_bar_switch0).setVisibility(View.GONE);
        menu.findItem(R.id.menuitem_sepet).setEnabled(false);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setEnabled(false);
        if (id == R.id.menuitem_kullanici) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else if (id == R.id.ayarlar) {
            finish();
            startActivity(new Intent(this, MainActivity_Ayarlar.class));
        } else if (id == R.id.menuitem_hesapmakinesi) {
            finish();
            startActivity(new Intent(this, MainActivity_Profil.class));
        } else if (id == R.id.menuitem_yemek) {
            finish();
            startActivity(new Intent(this, MainActivity_Yemeksepeti.class));
        } else if (id == R.id.cikis) {
            finish();
        } else if (id == R.id.hakkinda) {
            startActivity(GoToURL.GoToStore());
        } else if (id == R.id.beta) {
            startActivity(GoToURL.GoToInstagram());
        }
        return true;
    }
}