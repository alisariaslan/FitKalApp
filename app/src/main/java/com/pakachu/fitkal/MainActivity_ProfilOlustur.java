package com.pakachu.fitkal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity_ProfilOlustur extends AppCompatActivity {

    private EditText et_hesap_atilanadim;
    private TextView tv_hesapbilgi1;
    private EditText et_hesap_kalori;
    private TextView tv_hesap_bilgi2;
    private TextView tv_hesap_spor;
    private Spinner spin_hesap_spordallari;
    private ImageView iv_hesap_foto;

    private Database_Bilgilerim dt;

    private EditText et_ad;
    private EditText et_yas;
    private EditText et_boy;
    private EditText et_kilo;
    private Spinner spin_cinsiyet;
    private Spinner spin_aktivite;

    private String ad;
    private Integer yas;
    private Double boy;
    private Double kilo;
    private Integer cinsiyet;
    private Integer aktivite;
    private Integer spor;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedimage = data.getData();
            iv_hesap_foto.setImageURI(selectedimage);
//            MainActivity_Profil.profil_avatar.setImageURI(selectedimage);
            Toast.makeText(this, "Bu özellik deneme sürümündedir. Avatarınız geçici olarak ayarlandı.", Toast.LENGTH_SHORT).show();
        }
    }

    private void FotoYukle() {
        iv_hesap_foto.setImageURI(null);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, 3);
    }

    private void DetayEkle() {
        et_hesap_atilanadim.setVisibility(View.VISIBLE);
        tv_hesapbilgi1.setVisibility(View.VISIBLE);
        et_hesap_kalori.setVisibility(View.VISIBLE);
        tv_hesap_bilgi2.setVisibility(View.VISIBLE);
        tv_hesap_spor.setVisibility(View.VISIBLE);
        spin_hesap_spordallari.setVisibility(View.VISIBLE);
        //Toast.makeText(this, "Beta aşamasında olduğumuz için bu özellikler henüz eklenmedi.\nLütfen boş bırakınız.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_profilolustur);

        et_hesap_atilanadim = findViewById(R.id.et_hesap_atilanadim);
        tv_hesapbilgi1 = findViewById(R.id.tv_hesap_bilgi1);
        et_hesap_kalori = findViewById(R.id.et_hesap_kalori);
        tv_hesap_bilgi2 = findViewById(R.id.tv_hesap_bilgi2);
        tv_hesap_spor = findViewById(R.id.tv_hesap_spor);
        spin_hesap_spordallari = findViewById(R.id.spin_hesap_spordallari);
        iv_hesap_foto = findViewById(R.id.iv_hesap_foto);

        et_ad = findViewById(R.id.et_hesap_isim);
        et_boy = findViewById(R.id.et_hesap_boy);
        et_yas = findViewById(R.id.et_hesap_yas);
        et_kilo = findViewById(R.id.et_hesap_kilo);

        spin_cinsiyet = findViewById(R.id.spin_hesap_cinsiyet);
        spin_cinsiyet.setSelection(0);
        spin_aktivite = findViewById(R.id.spin_hesap_aktiviteduzeyi);
        spin_aktivite.setSelection(2);

        dt = new Database_Bilgilerim(this);

        Button btn_foto = findViewById(R.id.btn_hesap_foto);
        btn_foto.setOnClickListener(view -> FotoYukle());

        Button btn_olustur = findViewById(R.id.btn_hesap_olustur);
        btn_olustur.setOnClickListener(view -> {

            if (!et_ad.getText().toString().matches("")) {
                if (!et_yas.getText().toString().matches("")) {
                    if (!et_boy.getText().toString().matches("")) {
                        if (!et_kilo.getText().toString().matches("")) {
                            ad = et_ad.getText().toString();
                            yas = Integer.valueOf(et_yas.getText().toString());
                            boy = Double.valueOf(et_boy.getText().toString());
                            kilo = Double.valueOf(et_kilo.getText().toString());
                            cinsiyet = ((int) spin_cinsiyet.getSelectedItemId());
                            aktivite = ((int) spin_aktivite.getSelectedItemId());
                            double carpan = 1.0;
                            switch (aktivite) {
                                case 0:
                                    carpan = 1.0;
                                    break;
                                case 1:
                                    carpan = 1.1;
                                    break;
                                case 2:
                                    carpan = 1.2;
                                    break;
                                case 3:
                                    carpan = 1.3;
                                    break;
                                case 4:
                                    carpan = 1.4;
                                    break;
                                case 5:
                                    carpan = 1.5;
                                    break;
                            }
                            spor = ((int) spin_hesap_spordallari.getSelectedItemId());
                            double carpanPlus = 0.0;
                            switch (spor) {
                                case 0:
                                    carpanPlus = 0.0;
                                    break;
                                case 1:
                                    carpanPlus = 0.2;
                                    break;
                                case 2:
                                    carpanPlus = 0.3;
                                    break;
                                case 3:
                                    carpanPlus = 0.1;
                                    break;
                                case 4:
                                    carpanPlus = 0.25;
                                    break;
                                case 5:
                                    carpanPlus = 0.20;
                                    break;
                            }
                            dt.addData(ad, yas, boy, kilo, cinsiyet, carpan + carpanPlus + getYenicarpan(), 0, 0, 0);
                            Toast.makeText(MainActivity_ProfilOlustur.this, "Kullanıcı oluşturuldu.", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(MainActivity_ProfilOlustur.this, MainActivity_Profil.class));
                        } else
                            Toast.makeText(MainActivity_ProfilOlustur.this, "Kilonuzu doldurun lütfen.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity_ProfilOlustur.this, "Boyunuzu doldurun lütfen.", Toast.LENGTH_SHORT).show();

                } else
                    Toast.makeText(MainActivity_ProfilOlustur.this, "Yaşınızı doldurun lütfen.", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(MainActivity_ProfilOlustur.this, "İsminizi doldurun lütfen.", Toast.LENGTH_SHORT).show();


        });

        Button btn_bilgiekle = findViewById(R.id.btn_hesap_bilgiekle);
        btn_bilgiekle.setOnClickListener(view -> {
            DetayEkle();
            view.setEnabled(false);
        });

        et_hesap_atilanadim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_hesap_atilanadim.hasFocus()) {
                    String text = editable.toString();
                    if (!text.matches("")) {
                        int adim = Integer.parseInt(text);
                        double sonuc = (double) adim * 0.05;
                        adim = (int) sonuc;
                        et_hesap_kalori.setText(adim);
                    }
                }
            }
        });

        et_hesap_kalori.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_hesap_kalori.hasFocus()) {
                    String text = editable.toString();
                    if (!text.matches("")) {
                        int kalori = Integer.parseInt(text);
                        double sonuc = (double) kalori * 20.0;
                        kalori = (int) sonuc;
                        et_hesap_atilanadim.setText(kalori);
                    } else {
                        et_hesap_atilanadim.setText(null);
                    }
                }
            }
        });
    }

    private Double getYenicarpan() {
        double yenicarpan = 0.0;
        if (!et_hesap_kalori.getText().toString().matches("")) {
            double adim = Double.parseDouble(et_hesap_kalori.getText().toString());
            yenicarpan = adim / 500;
        }
        return yenicarpan;
    }
}