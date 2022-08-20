package com.pakachu.fitkal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class MainActivity_Yemeksepeti extends AppCompatActivity {

    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yemeksepeti);

        AdView mAdView = findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ArrayList<Integer> list_id1 = new ArrayList<>();
        ArrayList<Integer> list_imgid1 = new ArrayList<>();
        ArrayList<String> list_ad1 = new ArrayList<>();
        ArrayList<String> list_porsiyon1 = new ArrayList<>();
        ArrayList<Double> list_protein1 = new ArrayList<>();
        ArrayList<Double> list_karb1 = new ArrayList<>();
        ArrayList<Double> list_yag1 = new ArrayList<>();

        String[] food_imgid = getResources().getStringArray(R.array.food_imgid);
        String[] food_ad = getResources().getStringArray(R.array.food_ad);
        String[] food_porsiyon = getResources().getStringArray(R.array.food_porsiyon);
        String[] food_protein = getResources().getStringArray(R.array.food_protein);
        String[] food_karbs = getResources().getStringArray(R.array.food_karb);
        String[] food_yag = getResources().getStringArray(R.array.food_yag);

        for (int x = 0; x < food_ad.length; x++) {
            list_id1.add(-1);
            list_imgid1.add(Integer.valueOf(food_imgid[x]));
            list_ad1.add(food_ad[x]);
            list_porsiyon1.add(food_porsiyon[x]);
            list_protein1.add(Double.valueOf(food_protein[x]));
            list_karb1.add(Double.valueOf(food_karbs[x]));
            list_yag1.add(Double.valueOf(food_yag[x]));
        }

        Database_Yemekler database_yemekler = new Database_Yemekler(this);
        Cursor cursor_yemekler = database_yemekler.getData();
        while (cursor_yemekler.moveToNext()) {
            list_id1.add(cursor_yemekler.getInt(0));
            list_imgid1.add(cursor_yemekler.getInt(1));
            list_ad1.add(cursor_yemekler.getString(2));
            list_porsiyon1.add(cursor_yemekler.getString(4));
            list_protein1.add(Double.valueOf(cursor_yemekler.getString(6)));
            list_karb1.add(Double.valueOf(cursor_yemekler.getString(7)));
            list_yag1.add(Double.valueOf(cursor_yemekler.getString(8)));
        }


        LinearLayout ll_dikey = (LinearLayout) findViewById(R.id.lll);
        for (int a = 0; a < list_ad1.size(); a++) {
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
                    MainActivity_YemekDetay.lol = finalA;
                    startActivity(new Intent(MainActivity_Yemeksepeti.this, MainActivity_BunuYedim.class)); //DETAY MainActivity_YemekDetay
                }
            };
            ll_yatay.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_CANCEL) {
                        ll_yatay.setBackgroundColor(Color.argb(10, 0, 0, 0));
                        countDownTimer.cancel();
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        MainActivity_YemekDetay.lol = finalA;
                        ll_yatay.setBackgroundColor(Color.argb(10, 0, 255, 0));
                        countDownTimer.start();

                    } else if (event.getAction() == MotionEvent.ACTION_UP) {
                        MainActivity_BunuYedim.lol = finalA;
                        startActivity(new Intent(MainActivity_Yemeksepeti.this, MainActivity_BunuYedim.class)); //EKLEME
                        ll_yatay.setBackgroundColor(Color.argb(10, 0, 0, 0));
                        countDownTimer.cancel();
                    }

                    return true;
                }
            });

            //RESİM
            ImageView iv = new ImageView(this);
            iv.setImageResource(getResources().getIdentifier("y" + list_imgid1.get(a), "drawable", getPackageName()));
            LinearLayout.LayoutParams ivparams = new LinearLayout.LayoutParams(dpToPx(100, this), dpToPx(100, this));
            ivparams.setMargins(dpToPx(30, this), 0, 0, dpToPx(10, this));
            iv.setLayoutParams(ivparams);
            ll_yatay.addView(iv);

            LinearLayout ll_yazilar = new LinearLayout(this);
            LinearLayout.LayoutParams ll_yazilar_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            ll_yazilar.setOrientation(LinearLayout.VERTICAL);

            String idStr = "";
            if (!list_id1.get(a).toString().matches("-1")) {
                idStr+=" [";
                idStr+= list_id1.get(a).toString();
                idStr+="]";
            }
            //YAZI
            TextView textView = new TextView(this);
            textView.setText(list_ad1.get(a));
            textView.setTextSize(dpToPx(14, this));
            textView.setPadding(dpToPx(30, this), 0, 0, 0);
            ll_yazilar.addView(textView);

            //ALTYAZI
            TextView textView2 = new TextView(this);
            textView2.setText((list_porsiyon1.get(a)+idStr).trim());
            textView2.setTextSize(dpToPx(10, this));
            textView2.setPadding(dpToPx(30, this), 0, 0, 0);
            ll_yazilar.addView(textView2);

            //MINIYAZI
            TextView textView3 = new TextView(this);
            double kcal = (list_protein1.get(a) * 4) + (list_karb1.get(a) * 4) + (list_yag1.get(a) * 9);
            textView3.setText((int) kcal + " Kalori");
            textView3.setTextSize(dpToPx(8, this));
            textView3.setPadding(dpToPx(30, this), 0, 0, 0);
            ll_yazilar.addView(textView3);

            ll_yatay.addView(ll_yazilar);

            ll_dikey.addView(ll_yatay);
        }

        ScrollView sv = findViewById(R.id.scrollview);

        EditText et = findViewById(R.id.editTextTextPersonName);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                for (int i = 0; i < list_ad1.size(); i++) {
                    String ad = list_ad1.get(i);
                    Integer parantezIndex = 0;
                    parantezIndex = ad.indexOf("(", 0);
                    Integer adUzunluk = ad.length();
                    if (parantezIndex > 0)
                        ad = ad.substring(0, parantezIndex - 1);
//                    Toast.makeText(MainActivity2_yemek.this, ad, Toast.LENGTH_SHORT).show();

                    if (et.getText().toString().equals(ad)) {
                        i++;
                        sv.smoothScrollTo(0, (i - 1) * 400);
                    } else if (et.getText().toString().matches(""))
                        sv.scrollTo(0, 0);
                }

            }
        });

        Button yemeksil = findViewById(R.id.btn_yemeksil);
        Button ekle = findViewById(R.id.btn_yemekekle);
        ekle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0);
        yemeksil.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_delete_forever_24, 0, 0, 0);

        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (silmod != 1) {
                    finish();
                    startActivity(new Intent(MainActivity_Yemeksepeti.this, MainActivity_YemekEkle.class));
                } else {
                    if (!(et.getText().toString().matches(""))) {
                        Database_Yemekler database_yemekler = new Database_Yemekler(MainActivity_Yemeksepeti.this);
                        boolean sildinmi = database_yemekler.deleteFromIndex(Integer.valueOf(et.getText().toString()));
                        if (sildinmi) {
                            Database_Sepet database_sepet=new Database_Sepet(MainActivity_Yemeksepeti.this);
                            database_sepet.deleteAll();
                            Toast.makeText(MainActivity_Yemeksepeti.this, "Ürün silindi. Sayfa yenileniyor 3, 2, 1...", Toast.LENGTH_SHORT).show();
                            CountDownTimer countDownTimer = new CountDownTimer(3000, 1000) {
                                @Override
                                public void onTick(long l) {
                                }

                                @Override
                                public void onFinish() {
                                    finish();
                                    startActivity(new Intent(MainActivity_Yemeksepeti.this, MainActivity_Yemeksepeti.class));
                                }
                            }.start();
                        } else {
                            Toast.makeText(MainActivity_Yemeksepeti.this, "Hata! Geçersiz indis numarası", Toast.LENGTH_SHORT).show();
                            et.setText("");
                        }
                    }
                    Toast.makeText(MainActivity_Yemeksepeti.this, "Silmod kapalı", Toast.LENGTH_SHORT).show();
                    yemeksil.setVisibility(View.VISIBLE);
                    ekle.setText("yemek ekle");
                    et.setHint("Yemek ara");
                    ekle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add_24, 0, 0, 0);
                    silmod = 0;
                }
            }
        });

        yemeksil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (silmod == 0) {
                    Toast.makeText(MainActivity_Yemeksepeti.this, "Silmod açık. Arama yerine silmek istediğiniz ürünün indis numarasını yazın. '[]' içerisinde belirtilir. Dipnot: Sadece kendi eklediğiniz ürünlerin indis numaralarını görebilirsiniz.", Toast.LENGTH_SHORT).show();
                    yemeksil.setVisibility(View.GONE);
                    ekle.setText("sil");
                    et.setHint("İndis numarası yaz");
                    ekle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_close_24, 0, 0, 0);
                    silmod = 1;
                }

            }
        });

    }

    private Integer silmod = 0;

    public TextView tv(String str, boolean caps, int size, int threepadding, int bottompadding, int textalignment, int plusleftpadding) {
        TextView tv = new TextView(MainActivity_Yemeksepeti.this);
        tv.setText(str);
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        tv.setTextSize(size);
        tv.setAllCaps(caps);
        tv.setTextAlignment(textalignment);
        tv.setPadding(threepadding + plusleftpadding, threepadding, threepadding, bottompadding);
        return tv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menuitem_yemek).setEnabled(false);
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
        } else if (id == R.id.menuitem_sepet) {
            finish();
            startActivity(new Intent(this, MainActivity_Sepet.class));
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