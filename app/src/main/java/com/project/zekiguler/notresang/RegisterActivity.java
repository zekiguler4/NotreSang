package com.project.zekiguler.notresang;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity  implements View.OnClickListener {

    public AlertDialog.Builder alertDialogBuilder;
    public Button RegisterOkButton, RegisterCancelButton;
    public Spinner kanGrubu;
    private ArrayAdapter<String> dataAdapterforKanGrubu;
    public String[] gruplar = {"0 RH(+)", "0 RH(-)", "A RH(+)", "A RH(-)", "B RH(+)", "B RH(-)", "AB RH(+)", "AB RH(-)"};
    public EditText kullaniciAlani, sifreAlani, adAlani, telefonAlani;
    public RadioGroup cinsiyetGrup;
    public String kullanici, sifre, ad, telefon, kan, cinsiyet;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterOkButton =  findViewById(R.id.registerokbutton);
        RegisterCancelButton =  findViewById(R.id.registercancelbutton);

        RegisterOkButton.setOnClickListener(this);
        RegisterCancelButton.setOnClickListener(this);

        kanGrubu = findViewById(R.id.kanGrubu);
        dataAdapterforKanGrubu = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gruplar);
        dataAdapterforKanGrubu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kanGrubu.setAdapter(dataAdapterforKanGrubu);

        kullaniciAlani =  findViewById(R.id.kullaniciAlani);
        sifreAlani =  findViewById(R.id.sifreAlani);
        adAlani =  findViewById(R.id.adAlani);
        telefonAlani =  findViewById(R.id.telefonAlani);
        cinsiyetGrup =  findViewById(R.id.radiocinsiyetgrup);
        alertDialogBuilder = new AlertDialog.Builder(this);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  firebaseDatabase.getReference();
    }

    public void onClick(View v) {
        if (v == RegisterOkButton) {
            kullanici = kullaniciAlani.getText().toString();
            sifre = sifreAlani.getText().toString();
            ad = adAlani.getText().toString();
            telefon = telefonAlani.getText().toString();
            kan = kanGrubu.getSelectedItem().toString();
            if (cinsiyetGrup.getCheckedRadioButtonId() == -1)
            {
                cinsiyet = "";
            }
            else {
                int secilmisCinsiyetNo = cinsiyetGrup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(secilmisCinsiyetNo);
                cinsiyet = radioButton.getText().toString();
            }
            if(kullanici.equals("") || sifre.equals("") || ad.equals("") || telefon.equals("") || kan.equals("") || cinsiyet.equals("")) {
                String message = "";
                if(kullanici.equals("")) {
                    message =  "Email Adresi Eksik Girilmiştir. \n" ;
                }
                if(sifre.equals("")) {
                    message = message + "Şifre Eksik Girilmiştir. \n" ;
                }
                if(ad.equals("")) {
                    message = message + "Ad Eksik Girilmiştir. \n" ;
                }
                if(telefon.equals("")) {
                    message = message + "Telefon Eksik Girilmiştir. \n" ;
                }
                if(cinsiyet.equals("")) {
                    message = message + "Cinsiyet Eksik Girilmiştir. \n" ;
                }
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }});
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {
                mAuth.createUserWithEmailAndPassword(kullanici, sifre)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputManager.hideSoftInputFromWindow( getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                                    UUID uuid1 = UUID.randomUUID();
                                    String uuidString = uuid1.toString();
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciEmail").setValue(kullanici);
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciSifre").setValue(sifre);
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciAd").setValue(ad);
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciTelefon").setValue(telefon);
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciKan").setValue(kan);
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciCinsiyet").setValue(cinsiyet);
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciMesafe").setValue("10");
                                    myRef.child("Kullanicilar").child(uuidString).child("KullaniciUID").setValue(uuidString);
                                    alertDialogBuilder.setMessage("Kayıt Oluşturuldu\n\n" + "Kullanıcı Adı: " + kullanici + "\n" + "Ad Soyad: " + ad + "\n" + "Telefon No: " + telefon + "\n" + "Kan Grubu: " +
                                            kan + "\n" + "Cinsiyet: " + cinsiyet);
                                    alertDialogBuilder.setCancelable(false);
                                    alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent slideactivity = new Intent(alertDialogBuilder.getContext(), EnterActivity.class);
                                            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
                                            startActivity(slideactivity, bndlanimation);
                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                alertDialogBuilder.setMessage("Kullanıcı oluşturulamadı");
                                alertDialogBuilder.setCancelable(false);
                                alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }});
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                });
            }
        }
        if (v == RegisterCancelButton) {
            Intent slideactivity = new Intent(v.getContext(), EnterActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
            startActivity(slideactivity, bndlanimation);
        }
    }
}