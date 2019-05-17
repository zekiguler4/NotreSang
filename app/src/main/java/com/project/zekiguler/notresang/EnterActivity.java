package com.project.zekiguler.notresang;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnterActivity extends AppCompatActivity  implements View.OnClickListener {
    public Button RegisterButton, LoginButton;
    public LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        LoginButton = findViewById(R.id.loginbutton);
        RegisterButton = findViewById(R.id.registerbutton);
        LoginButton.setOnClickListener(this);
        RegisterButton.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    }
    public void onClick(View v) {
        if(v == LoginButton){
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
                    !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                showAlert();
            } else {
                if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
                    showAlert2();
                }
                else {
                    Intent slideactivity = new Intent(EnterActivity.this, LoginActivity.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
                    startActivity(slideactivity, bndlanimation);
                }
            }
        }
        if(v == RegisterButton){
            if (!AppStatus.getInstance(getApplicationContext()).isOnline()) {
                showAlert2();
            }
            else {
                Intent slideactivity = new Intent(v.getContext(), RegisterActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
                startActivity(slideactivity, bndlanimation);
            }
        }
    }
    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Konum Paylaşımı Kapalı")
                .setMessage("Konum Bilgileri Kapalıdır.\nGiriş Yapabilmek İçin Lütfen Açınız ")
                .setPositiveButton("Konum Ayarları", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
    private void showAlert2() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("İnternet Kapalı")
                .setMessage("İnternet Kapalıdır.\nUygulamayı Kullanabilmek İçin İnterneti Açınız")
                .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(myIntent);
                    }
                });
        dialog.show();
    }
}
