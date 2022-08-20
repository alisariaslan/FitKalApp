package com.pakachu.fitkal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity_Ayarlar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ayarlar);

        Button btn_login_tema = findViewById(R.id.btn_login_tema);
        btn_login_tema.setOnClickListener(view -> {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        });

        Button btn_login_yenile = findViewById(R.id.btn_login_yenile);
        btn_login_yenile.setOnClickListener(view -> {
            Toast.makeText(MainActivity_Ayarlar.this, "Yenilendi.", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity_Ayarlar.this, MainActivity.class));
        });

        Button btn_login_sepet = findViewById(R.id.btn_login_sepettemizle);
        btn_login_sepet.setOnClickListener(view -> Check(4));

        Button btn_login_hedef = findViewById(R.id.btn_login_hedefimisil);
        btn_login_hedef.setOnClickListener(view -> Check(2));

        Button btn_login_bilgilerim = findViewById(R.id.btn_login_bilgilerimisil);
        btn_login_bilgilerim.setOnClickListener(view -> Check(1));

        Button btn_login_yemek = findViewById(R.id.btn_login_yemeklerimisil);
        btn_login_yemek.setOnClickListener(view -> Check(0));
    }

    private void Check(Integer selection) {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Database_Sepet database_sepet = new Database_Sepet(MainActivity_Ayarlar.this);
                    Database_Yemekler database_yemekler = new Database_Yemekler(MainActivity_Ayarlar.this);
                    Database_Gereken database_gereken = new Database_Gereken(MainActivity_Ayarlar.this);
                    Database_Bilgilerim database_bilgilerim = new Database_Bilgilerim(MainActivity_Ayarlar.this);

                    switch (selection) {
                        case 0:
                            database_yemekler.deleteAllData();
                            Toast.makeText(MainActivity_Ayarlar.this, "Yemeklerim sıfırlandı. Yenileme gerekiyor!", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            database_gereken.deleteHedef();
                            database_bilgilerim.deleteName();
                            Toast.makeText(MainActivity_Ayarlar.this, "Bilgilerim ve Hedef silindi. Yenileme gerekiyor!", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            database_gereken.deleteHedef();
                            database_sepet.deleteAll();
                            Toast.makeText(MainActivity_Ayarlar.this, "Hedef ve Yediklerim silindi. Yenileme gerekiyor!", Toast.LENGTH_SHORT).show();
                            break;
                        case 3:
//                                database_sepet.deleteAll();
//                                Toast.makeText(MainActivity_Ayarlar.this, "Yediklerim silindi. Yenileme gerekiyor!", Toast.LENGTH_SHORT).show();
                            break;
                        case 4:
                            database_sepet.deleteAll();
                            Toast.makeText(MainActivity_Ayarlar.this, "Sepet temizlendi. Yenileme gerekiyor!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    finish();
                    break;
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_Ayarlar.this);
        builder.setMessage("İşleme devam etmek istediğinizden emin misiniz?").setPositiveButton("İlerle", dialogClickListener)
                .setNegativeButton("Vazgeçtim", dialogClickListener).setTitle("Dikkat").setIcon(R.drawable.ic_baseline_warning_24).show();

    }


}