package com.dogusumit.faktoriyelveardisiksayilar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class activity1 extends AppCompatActivity {

    TextView tv_islem;
    EditText et_islem1, et_islem2, et_islem3;
    RadioButton rd_topla, rd_carp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1);

        tv_islem = (TextView) findViewById(R.id.textView5);
        et_islem1 = (EditText) findViewById(R.id.editText8);
        et_islem2 = (EditText) findViewById(R.id.editText9);
        et_islem3 = (EditText) findViewById(R.id.editText10);
        rd_topla = (RadioButton) findViewById(R.id.radioButton);
        rd_carp = (RadioButton) findViewById(R.id.radioButton2);

        et_islem1.addTextChangedListener(textWatcher);
        et_islem2.addTextChangedListener(textWatcher);
        et_islem3.addTextChangedListener(textWatcher);

        rd_topla.setOnClickListener(onClickListener);
        rd_carp.setOnClickListener(onClickListener);


        tv_islem.requestFocus();


        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hesapla();
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            hesapla();
        }
    };

    private void uygulamayiOyla() {
        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            } catch (Exception ane) {
            }
        }
    }

    private void marketiAc() {
        try {
            Uri uri = Uri.parse("market://developer?id=" + getString(R.string.play_store_id));
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.play_store_id))));
            } catch (Exception ane) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.oyla:
                uygulamayiOyla();
                return true;
            case R.id.market:
                marketiAc();
                return true;
            case R.id.cikis:
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hesapla() {
        try {
            tv_islem.setText("hesaplanıyor...");
            String s1, s2, s3;
            s1 = et_islem1.getText().toString();
            s2 = et_islem2.getText().toString();
            s3 = et_islem3.getText().toString();
            //str = str.replaceAll("[^0-9]", "");

            if (s1.length() > 0 && s2.length() > 0 && s3.length() > 0) {
                double basla = Double.parseDouble(s1);
                double bitir = Double.parseDouble(s2);
                double artis = Double.parseDouble(s3);
                if (artis == 0) {
                    tv_islem.setText("artış miktarı 0 olamaz!");
                    return;
                }
                if (basla == bitir) {
                    tv_islem.setText("başlangıç ve bitiş sayıları aynı olamaz!");
                    return;
                }
                if (basla > bitir) {
                    if (artis < 0) {
                        double tmp = basla;
                        basla = bitir;
                        bitir = tmp;
                        artis *= -1;
                    } else {
                        String tmp = s1 + "'den " + s2 + "'ye " + s3 + "artarak ulaşılamaz!";
                        tv_islem.setText(tmp);
                        return;
                    }
                }
                if (basla < bitir && (artis < 0)) {
                    String tmp = s1 + "'den " + s2 + "'ye " + s3 + "artarak ulaşılamaz!";
                    tv_islem.setText(tmp);
                    return;
                }
                if (rd_topla.isChecked()) {

                    new hesaplaTask().execute(basla,bitir,artis,0.0);
                } else if (rd_carp.isChecked()) {

                    new hesaplaTask().execute(basla,bitir,artis,1.0);
                }

            } else {
                tv_islem.setText(getString(R.string.text6));
            }
        } catch (Exception e) {
            //tv1.setText(e.getMessage().toString());
            tv_islem.setText(getResources().getString(R.string.hata) + "\n" + e.getMessage());
        }
        return ;

    }

    private class hesaplaTask extends AsyncTask<Double, Void, Void> {

         double sonuc = 0;


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Locale locale = new Locale("tr", "TR");
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator(' ');
            String pattern = "###,##0.################################";
            DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
            String number = decimalFormat.format(sonuc);
            ((TextView)findViewById(R.id.textView5)).setText(number);
        }

        @Override
        protected Void doInBackground(Double... params) {
            double basla = params[0];
            double bitir = params[1];
            double artis = params[2];

            if (params[3]==0.0) {
                sonuc = 0;
                for (double i = basla; i <= bitir; i += artis) {
                    sonuc += i;
                }
            } else if (params[3]==1.0) {
                sonuc = 1;
                for (double i = basla; i <= bitir; i += artis) {
                    sonuc *= i;
                }
            }

            return null;
        }
    }
}