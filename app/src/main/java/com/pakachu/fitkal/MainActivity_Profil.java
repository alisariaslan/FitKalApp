package com.pakachu.fitkal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity_Profil extends AppCompatActivity {

//    private Database_Yediklerim dy;
    private Database_Gereken dtgereken;
    private Database_Bilgilerim dtpo;

    private Cursor c1;

    private Spinner spin_amac;
    private TextView tv_kalori;
    private TextView tv_protein;
    private TextView tv_karbonhidrat;
    private TextView tv_yag;

    private ImageView profil_avatar;

    private String ad;
    private Integer yas;
    private Double boy;
    private Double kilo;
    private Integer cinsiyet;
    private Double carpan;

    private Integer alinacakkalori;
    private Integer alinacakprotein;
    private Integer alinacakkarbonhidrat;
    private Integer alinacakyag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profil);

//        dy = new Database_Yediklerim(this);
        dtgereken = new Database_Gereken(this);
        dtpo = new Database_Bilgilerim(this);
        c1 = dtgereken.getData();
        Cursor c2 = dtpo.getData();
        if(c2.getCount()==0){
            finish();
            startActivity(new Intent(MainActivity_Profil.this, MainActivity_ProfilOlustur.class));
}
        c2.moveToLast();
        ad = c2.getString(1);
        yas = c2.getInt(2);
        boy = c2.getDouble(3);
        kilo = c2.getDouble(4);
        cinsiyet = c2.getInt(5);
        carpan = c2.getDouble(6);

        TextView tv_ad = findViewById(R.id.tv_profil_ad);
        tv_ad.setText(ad);

        tv_kalori = findViewById(R.id.tv_profil_kalori);
        tv_protein = findViewById(R.id.tv_profil_protein);
        tv_karbonhidrat = findViewById(R.id.tv_profil_karbonhidrat);
        tv_yag = findViewById(R.id.tv_profil_yag);

        profil_avatar = findViewById(R.id.iv_profil_avatar);
        Button baslat = findViewById(R.id.btn_profil_baslat);

        spin_amac = findViewById(R.id.spin_profil_amac);
        spin_amac.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: //boş
                        alinacakyag = 0;
                        alinacakkalori = 0;
                        alinacakkarbonhidrat = 0;
                        alinacakprotein = 0;
                        Yazdir();
                        baslat.setEnabled(false);
                        break;
                    case 1: //fit kalmak
                        Hesapla(0.8, 0.0);
                        baslat.setEnabled(true);
                        break;
                    case 2: //kilo vermek
                        Hesapla(0.7, 0.3);
                        baslat.setEnabled(true);
                        break;
                    case 3: //kilo almak
                        Hesapla(0.9, -0.3);
                        baslat.setEnabled(true);
                        break;
                    case 4: //kas yapmak
                        Hesapla(1.5, -0.75);
                        baslat.setEnabled(true);
                        break;
                    case 5: //yağ oranı azaltmak
                        Hesapla(1.25, 0.1);
                        baslat.setEnabled(true);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Button yeniden = findViewById(R.id.btn_profil_yeniden);
        yeniden.setOnClickListener(view -> CheckYeniden());

        baslat.setOnClickListener(view -> {
            if (!CheckPO()) {
                boolean gereken = dtgereken.addData(alinacakprotein, alinacakkarbonhidrat, alinacakyag, spin_amac.getSelectedItemPosition());
                if (gereken) {
                    Toast.makeText(MainActivity_Profil.this, "Sayaç başlatıldı. Ana menüye yönlendiriliyor...", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity_Profil.this, MainActivity.class));
                } else
                    Toast.makeText(MainActivity_Profil.this, "Giriş red edildi.", Toast.LENGTH_SHORT).show();
            }
        });

        if (c1.getCount() != 0) {
            c1.moveToLast();
            spin_amac.setSelection(c1.getInt(4));
        }
    }

    private void CheckYeniden() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dtpo.deleteName();
                    Database_Sepet database_sepet=new Database_Sepet(MainActivity_Profil.this);
                    database_sepet.deleteAll();
                    dtgereken.deleteHedef();
                    Toast.makeText(MainActivity_Profil.this, "Profil silindi. Profil oluşturma sayfasına yönlendiriliyor...", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(MainActivity_Profil.this, MainActivity_ProfilOlustur.class));
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Profil.this);
        builder.setMessage("Yeniden bir profil oluşturmak istediğinizden emin misiniz?").setPositiveButton("Oluştur", dialogClickListener)
                .setNegativeButton("Vazgeçtim", dialogClickListener).show();
    }

    private boolean CheckPO() {
        boolean hello = true;
        if (c1.getCount() != 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Database_Sepet database_sepet=new Database_Sepet(MainActivity_Profil.this);
                            database_sepet.deleteAll();
                            dtgereken.deleteHedef();
                            finish();
                            startActivity(new Intent(MainActivity_Profil.this, MainActivity_Profil.class));
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Profil.this);
            builder.setMessage("Aktif bir makro sayacınız bulunmakta! Yeni hedef için sayacı sıfırlamamız gerekiyor...").setPositiveButton("Sıfırla", dialogClickListener)
                    .setNegativeButton("Vazgeçtim", dialogClickListener).show();
        } else
            hello = false;
        return hello;
    }

    private void Hesapla(Double protein, Double aktiviteDuzeyindenCikar) {
        alinacakkalori = KaloriHesapla(carpan - aktiviteDuzeyindenCikar, cinsiyet);
        alinacakprotein = ProteinHesapla(kilo, protein);
        alinacakyag = YagHesapla(alinacakkalori);
        alinacakkarbonhidrat = KarboHesapla(alinacakkalori);
        Yazdir();
    }

    private void Yazdir() {
        tv_kalori.setText("Günlük kalori ihtiyacım: " + alinacakkalori + "kcal");
        tv_protein.setText("Günlük protein ihtiyacım: " + alinacakprotein + "gr");
        tv_yag.setText("Günlük yağ ihtiyacım: " + alinacakyag + "gr");
        tv_karbonhidrat.setText("Günlük karbonhidrat ihtiyacım: " + alinacakkarbonhidrat + "gr");
    }

    private Integer KaloriHesapla(Double tempo, Integer cins) {
        Double sonuc;
        if (cins == 0) { //erkek
            sonuc = (66.4730 + (13.7516 * kilo) + (5.0033 * boy) - (6.7550 * Double.valueOf(yas))) * tempo;
        } else { //kadın
            sonuc = (655.0955 + (9.5634 * kilo) + (1.8496 * boy) - (4.6756 * yas)) * tempo;
        }
        return sonuc.intValue();
    }

    private Integer ProteinHesapla(Double kg, Double miktar) {
        Double sonuc = kg * miktar;
        return sonuc.intValue();
    }

    private Integer KarboHesapla(Integer kcal) {
        Double sonuc = Double.valueOf(kcal);
        sonuc = alinacakkalori.doubleValue() - ((alinacakprotein.doubleValue() * 4) + (alinacakyag.doubleValue() * 9));
        sonuc /= 4;
        return sonuc.intValue();
    }

    private Integer YagHesapla(Integer kcal) {
        Double sonuc = Double.valueOf(kcal);
        sonuc = ((alinacakkalori / 100.0) * 25) / 9;
        return sonuc.intValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.menuitem_hesapmakinesi).setEnabled(false);
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
        } else if (id == R.id.menuitem_yemek) {
            finish();
            startActivity(new Intent(this, MainActivity_Yemeksepeti.class));
        } else if (id == R.id.menuitem_sepet) {
            finish();
            startActivity(new Intent(this, MainActivity_Sepet.class));
        } else if (id == R.id.cikis) {
            finish();
        } else if (id == R.id.hakkinda) {
            startActivity(GoToURL.GoToStore());
        }
        else if (id == R.id.beta) {
            startActivity(GoToURL.GoToInstagram());
        }
        return true;
    }

}