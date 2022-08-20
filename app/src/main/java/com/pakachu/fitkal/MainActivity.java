package com.pakachu.fitkal;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private String ad;

    private TextView textview_yorum,textview_puan;
    private TextView tv_ad,tv_kalori1,tv_kalori2,tv_pro,tv_karb,tv_yag;

    private ProgressBar pb1, pb2, pb3, pbKcal;
    private Double aldigim_protein = 0.0;
    private Double aldigim_karb = 0.0;
    private Double aldigim_yag = 0.0;
    private Integer akh = 0, ap = 0, ak = 0, ay = 0;

    private  ArrayList<Integer> list_gram1;
    private ArrayList<Double> list_protein1,list_karb1,list_yag1;

    private  String[] food_ad,food_gram,food_protein,food_karbs,food_yag;

    private  Database_Bilgilerim database_bilgilerim;
    private  Database_Gereken database_gereken;
    private Database_Yemekler database_yemekler;
    private Database_Beslenmem database_beslenmem;
    private Database_Sepet database_sepet;

    private Cursor cursor_yemekler,cursor_sepet,bilgilerim,cursor_gereken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_anasayfa);

        CheckPO();

        SetItems();

        Button btn_kullanici_sifirla = findViewById(R.id.btn_kullanici_sifirla);
        btn_kullanici_sifirla.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        database_sepet.deleteAll();
                        Toast.makeText(MainActivity.this, "Yediklerim sıfırlandı.", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Yediklerinizi sıfırlamak istediğinizden emin misiniz?").setPositiveButton("Sıfırla", dialogClickListener)
                    .setNegativeButton("Vazgeçtim", dialogClickListener).show();
        });

        Button btn_main_sil = findViewById(R.id.btn_main_sil);
        btn_main_sil.setOnClickListener(view -> {
            database_sepet.deleteAll();
            Toast.makeText(MainActivity.this, "Veritabanı temizlendi.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        });

        Button btn_main_gunluk = findViewById(R.id.btn_kullanici_gunlukSave);
        btn_main_gunluk.setOnClickListener(view -> {
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            boolean eger = database_beslenmem.addData(akh, ap, ak, ay, currentDate);
            if (eger) {
                database_sepet.deleteAll();
                finish();
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                Toast.makeText(MainActivity.this, "Günlüğe kayıt eklendi: " + currentDate, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Bir hata oluştu! -506", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void yorumYap() {
        String yorum_protein, yorum_karb, yorum_yag, yorum_all;
        int puan = 10;
        if (pb1.getProgress() < pb1.getMax()) {
            yorum_protein = "İyi gidiyorsun. Günlük tüm protein ihtiyacını karşıladın sayılır.";
            puan -= 2;
            if (pb1.getProgress() < pb1.getMax() / 2) {
                yorum_protein = "İhtiyacının yarısından az protein almışsın. Bu çok kötü. İyileşme hızında düşüş olabilir.";
                puan -= 3;
            }
        } else
            yorum_protein = "Çok iyi! Protein ihtiyacını tamamen karşılamışsın. Sakın hız kesme.";
        if (pb2.getProgress() < pb2.getMax()) {
            yorum_karb = "Güzel. Günlük karbonhidrat ihtiyacını tamamlamana az kaldı.";
            puan -= 1;
            if (pb2.getProgress() < pb2.getMax() / 2) {
                yorum_karb = "Aldığın karbonhidrat miktarı çok düşük! Gün içerisinde enerjin az olabilir dikkatli ol.";
                puan -= 2;
            }
        } else
            yorum_karb = "Harika! Karbonhidrat ihtiyacının tümünü gidermişsin. Enerjin adeta bomba gibi dışarı vuracak!";
        if (pb3.getProgress() < pb3.getMax()) {
            yorum_yag = "İşte bu! Günlük yağ alımın bitti sayılır.";
            puan -= 1;
            if (pb3.getProgress() < pb3.getMax() / 2) {
                yorum_yag = "Tükettiğin yağ miktarı oldukça düşük! Vücudun yapı taşı olan aminoasitleri üretmen zorlaşacak. Hormonlarında dalgalanmalar yaşayabilirsin.";
                puan -= 1;
            }
        } else yorum_yag = "Mükemmel! Yeterince yağ tüketmişsin. Önünü kimse alamaz!";
        yorum_all = "*" + yorum_protein + "\n*" + yorum_karb + "\n*" + yorum_yag;
        yorum_all += "\n\n" + ad + ", bugün ki analizim şimdilik bu kadar.";
        textview_yorum.setText(yorum_all);
        textview_puan.setText("Günlük beslenme puanın 10 üzerinden " + puan);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menuitem_kullanici).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        item.setEnabled(false);
        if (id == R.id.menuitem_yemek) {
            finish();
            startActivity(new Intent(this, MainActivity_Yemeksepeti.class));
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

    private void CheckMY() {
        Database_Bilgilerim dt = new Database_Bilgilerim(this);
        Cursor c = dt.getData();
        if (c.getCount() == 0) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity_ProfilOlustur.class));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Henüz bir profil oluşturulmamış. Lütfen yeni bir profil oluşturun.").setPositiveButton("Profil oluştur", dialogClickListener)
                    .setNegativeButton("Vazgeçtim", dialogClickListener).setTitle("Lütfen profil oluşturun").setIcon(R.drawable.ic_baseline_person_24).show();
        }
    }

    private void CheckPO() {
        Database_Gereken dt = new Database_Gereken(this);
        Cursor c = dt.getData();
        if (c.getCount() == 0) {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        startActivity(new Intent(MainActivity.this, MainActivity_Profil.class));
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Profil oluşturulmuş fakat hedef seçilmemiş. Lütfen bir hedef seçiniz...").setPositiveButton("Hedef seç", dialogClickListener)
                    .setNegativeButton("Vazgeçtim", dialogClickListener).setTitle("Hedef seçin").setIcon(R.drawable.ic_baseline_calculate_24).show();
        }
        CheckMY();
    }

    private void SetItems() {
        database_bilgilerim = new Database_Bilgilerim(this);
        database_sepet = new Database_Sepet(this);
        database_beslenmem = new Database_Beslenmem(this);
        database_gereken = new Database_Gereken(this);
        database_yemekler = new Database_Yemekler(this);

        list_gram1 = new ArrayList<>();
        list_protein1 = new ArrayList<>();
        list_karb1 = new ArrayList<>();
        list_yag1 = new ArrayList<>();

        textview_yorum = findViewById(R.id.textView10);
        textview_puan = findViewById(R.id.textView11);
        tv_ad = findViewById(R.id.textView8);
        tv_kalori1 = findViewById(R.id.tv_alinankcal2);
        tv_kalori2 = findViewById(R.id.tv_alinankcal3);
        tv_pro = findViewById(R.id.tv_alinanpro);
        tv_karb = findViewById(R.id.tv_alinankarb);
        tv_yag = findViewById(R.id.tv_alinanyag);

        pb1 = findViewById(R.id.progressBar);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar4);
        pbKcal = findViewById(R.id.progressBar3);

        cursor_gereken = database_gereken.getData();
        cursor_sepet = database_sepet.getData();
        cursor_yemekler = database_yemekler.getData();
        bilgilerim = database_bilgilerim.getData();

        food_ad = getResources().getStringArray(R.array.food_ad);
        food_gram = getResources().getStringArray(R.array.food_gram);
        food_protein = getResources().getStringArray(R.array.food_protein);
        food_karbs = getResources().getStringArray(R.array.food_karb);
        food_yag = getResources().getStringArray(R.array.food_yag);

        for (int x = 0; x < food_ad.length; x++) {
            list_gram1.add(Integer.valueOf(food_gram[x]));
            list_protein1.add(Double.valueOf(food_protein[x]));
            list_karb1.add(Double.valueOf(food_karbs[x]));
            list_yag1.add(Double.valueOf(food_yag[x]));
        }

        while (cursor_yemekler.moveToNext()) {
            list_gram1.add(Integer.valueOf(cursor_yemekler.getString(5)));
            list_protein1.add(Double.valueOf(cursor_yemekler.getString(6)));
            list_karb1.add(Double.valueOf(cursor_yemekler.getString(7)));
            list_yag1.add(Double.valueOf(cursor_yemekler.getString(8)));
        }

        if (bilgilerim.getCount() != 0) {
            bilgilerim.moveToLast();
            ad = bilgilerim.getString(1);
            tv_ad.setText(ad);
        }

        if (cursor_sepet.getCount() != 0) {
            ArrayList<Integer> grams = new ArrayList<>();
            int z = 0;
            while (cursor_sepet.moveToNext()) {
                int index = cursor_sepet.getInt(1);
                grams.add(cursor_sepet.getInt(2));
                aldigim_protein += (grams.get(z).doubleValue() / list_gram1.get(index)) * list_protein1.get(index);
                aldigim_karb += (grams.get(z).doubleValue() / list_gram1.get(index)) * list_karb1.get(index);
                aldigim_yag += (grams.get(z).doubleValue() / list_gram1.get(index)) * list_yag1.get(index);
                z++;
            }

            ap = aldigim_protein.intValue();
            ak = aldigim_karb.intValue();
            ay = aldigim_yag.intValue();
        }


        if (cursor_gereken.getCount() != 0) {
            cursor_gereken.moveToLast();
            int gereken_protein = cursor_gereken.getInt(1);
            int gereken_karb = cursor_gereken.getInt(2);
            int gereken_yag = cursor_gereken.getInt(3);
            Integer gereken_kalori = (gereken_protein * 4) + (gereken_karb * 4) + (gereken_yag * 9);
            pb1.setMax(gereken_protein);
            pb2.setMax(gereken_karb);
            pb3.setMax(gereken_yag);
            pbKcal.setMax(gereken_kalori);
            pb1.setProgress(aldigim_protein.intValue());
            pb2.setProgress(aldigim_karb.intValue());
            pb3.setProgress(aldigim_yag.intValue());
            double aldigimkalorihesap = (aldigim_protein * 4) + (aldigim_karb * 4) + (aldigim_yag * 9);
            pbKcal.setProgress((int) aldigimkalorihesap);
            tv_pro.setText("Alınan: " + ap + "/" + gereken_protein + " gr");
            tv_karb.setText("Alınan: " + ak + "/" + gereken_karb + " gr");
            tv_yag.setText("Alınan: " + ay + "/" + gereken_yag + " gr");

            akh = (int) aldigimkalorihesap;
            tv_kalori1.setText(akh.toString()); //TODO SET PROGRESS BAR
            tv_kalori2.setText("/" + gereken_kalori + " kcal");
            if (akh > gereken_kalori) {
                tv_kalori1.setTextColor(Color.argb(100, 255, 0, 0));
            }
            yorumYap();
        }
    }
}