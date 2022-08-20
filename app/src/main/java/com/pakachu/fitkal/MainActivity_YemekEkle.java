package com.pakachu.fitkal;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity_YemekEkle extends AppCompatActivity {

    Database_Yemekler mDatabaseYemekler;

    public void AddData(String ad, String ayrinti, Double gram, Double protein, Double karb, Double yag) {
//        boolean insertData = mDatabaseYemekler.addData(ad, ayrinti, gram, protein, karb, yag,sivi);
//        if (insertData)
//            Toast.makeText(this, "Data added.", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
    }

    private Boolean sivi = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yemekekle);

        EditText et_etiketler=findViewById(R.id.et_etiketler);
        for (int i = 1; i < 13; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button btn_x = (Button) findViewById(id);
            btn_x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    et_etiketler.setText(et_etiketler.getText()+""+btn_x.getText()+", ");
                    btn_x.setEnabled(false);
                }
            });
        }

        mDatabaseYemekler = new Database_Yemekler(this);

        EditText et_ad = findViewById(R.id.et_ad);
        EditText et_porsiyon = findViewById(R.id.et_porsiyon);
        EditText et_ayrinti = findViewById(R.id.et_ayrinti);
        EditText et_gram = findViewById(R.id.et_gram);
        EditText et_pro = findViewById(R.id.et_pro);
        EditText et_karb = findViewById(R.id.et_karb);
        EditText et_yag = findViewById(R.id.et_yag);
        EditText et_save = findViewById(R.id.et_admin_savelocationadress);
        et_save.setText(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath());

        Switch sw_sivi = findViewById(R.id.sw_admin_sivi);
        sw_sivi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    sivi = true;
                else
                    sivi = false;
            }
        });

        Button btn = findViewById(R.id.btn_admin_ekle);
        ScrollView sv_admin_scroll = findViewById(R.id.sv_admin_scroll);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!et_ad.getText().toString().matches("")&&!et_gram.getText().toString().matches("")&&!et_pro.getText().toString().matches("")&&!et_karb.getText().toString().matches("")&&!et_yag.getText().toString().matches(""))
                {
                    int a=0;
                    if(sivi)
                        a=1;
                    boolean insertData = mDatabaseYemekler.addData(-1,et_ad.getText().toString(),et_ayrinti.getText().toString(),et_porsiyon.getText().toString(),Integer.valueOf(et_gram.getText().toString()),Double.valueOf(et_pro.getText().toString()),Double.valueOf(et_karb.getText().toString()),Double.valueOf(et_yag.getText().toString()),a,et_etiketler.getText().toString());
                    if (insertData) {
                        Toast.makeText(MainActivity_YemekEkle.this, "Veri girişi eklendi.", Toast.LENGTH_SHORT).show();
                       startActivity(new Intent(MainActivity_YemekEkle.this,MainActivity_YemekEkle.class));
                    } else
                        Toast.makeText(MainActivity_YemekEkle.this, "Hata! Ters giden bir şeyler var.", Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(MainActivity_YemekEkle.this, "Eksik bilgiler mevcut! Lütfen tam doldurun.\n* ile belirtilen alanlar zorunludur.", Toast.LENGTH_SHORT).show();
            }
        });

        Button btn_save_database = findViewById(R.id.btn_admin_savedatabase);
        btn_save_database.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File Db = new File("/data/data/com.pakachu.fitkal/databases/yemeksepeti");
                File file = new File(et_save.getText().toString());
                file.setWritable(true);
                try {
                    copyFile(new FileInputStream(Db), new FileOutputStream(file));
                    Toast.makeText(MainActivity_YemekEkle.this, "Database başarıyla indirildi.\nDosya yolu: " + file.getPath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity_YemekEkle.this, "Yazdırma başarısız!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn_cikis = findViewById(R.id.btn_admin_cikis);
        btn_cikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(MainActivity_YemekEkle.this, MainActivity_Yemeksepeti.class));
            }
        });
    }

    public static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }
}

