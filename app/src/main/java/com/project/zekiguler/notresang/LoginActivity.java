package com.project.zekiguler.notresang;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {

    public Button LoginCancelButton, LoginOkButton;
    public EditText LoginKullaniciAlani, LoginSifreAlani;
    public String kullanici, sifre;
    public AlertDialog.Builder alertDialogBuilder;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginOkButton = findViewById(R.id.loginokbutton);
        LoginCancelButton = findViewById(R.id.logincancelbutton);
        LoginOkButton.setOnClickListener(this);
        LoginCancelButton.setOnClickListener(this);
        LoginKullaniciAlani = findViewById(R.id.loginkullaniciAlani);
        LoginSifreAlani = findViewById(R.id.loginsifreAlani);
        alertDialogBuilder = new AlertDialog.Builder(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null) {
            Intent slideactivity = new Intent(this, MainActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
            startActivity(slideactivity, bndlanimation);
        }
    }
    public void onClick(View v) {
        if(v == LoginOkButton) {
            kullanici = LoginKullaniciAlani.getText().toString();
            sifre = LoginSifreAlani.getText().toString();
            if(kullanici.equals("") || sifre.equals("")) {
                String message = "";
                if(kullanici.equals("")) {
                    message =  "Kullanıcı Adı Eksik Girilmiştir. \n" ;
                }
                if(sifre.equals("")) {
                    message = message + "Şifre Eksik Girilmiştir. \n" ;
                }
                alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }});
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            } else {
                mAuth.signInWithEmailAndPassword(kullanici, sifre).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent slideactivity = new Intent(getApplicationContext(), MainActivity.class);
                            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation3,R.anim.animation4).toBundle();
                            startActivity(slideactivity, bndlanimation);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        alertDialogBuilder.setMessage("Giriş Başarısız Olmuştur");
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
            }
        }
        if(v == LoginCancelButton) {
            Intent slideactivity = new Intent(this, EnterActivity.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation1,R.anim.animation2).toBundle();
            startActivity(slideactivity, bndlanimation);
        }
    }
}
