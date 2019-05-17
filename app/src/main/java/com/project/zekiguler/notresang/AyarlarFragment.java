package com.project.zekiguler.notresang;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;


public class AyarlarFragment extends Fragment implements View.OnClickListener {
    public AlertDialog.Builder alertDialogBuilder;
    public Button logoutButton, ayarKaydetButton;
    public TextView kullaniciValue, kanValue, kullaniciMailValue;
    public EditText mesafeValue, telefonNoValue, sifreValue;
    public String userUID;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    public static AyarlarFragment newInstance() {
        AyarlarFragment fragment = new AyarlarFragment();
        return fragment;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ayarlar, container, false);

        kanValue = view.findViewById(R.id.kanGrubuValue);
        kullaniciMailValue = view.findViewById(R.id.kullaniciMailValue);
        kullaniciValue = view.findViewById(R.id.kullaniciValue);

        mesafeValue = view.findViewById(R.id.mesafeValue);
        telefonNoValue = view.findViewById(R.id.telefonValue);
        sifreValue = view.findViewById(R.id.sifreDegistir);

        logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);

        ayarKaydetButton = view.findViewById(R.id.sifreDegistirButton);
        ayarKaydetButton.setOnClickListener(this);

        alertDialogBuilder = new AlertDialog.Builder(getActivity());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  firebaseDatabase.getReference("Kullanicilar");

        myRef.orderByChild("KullaniciEmail")
                .startAt(mAuth.getCurrentUser().getEmail())
                .endAt(mAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    if(hashMap.get("KullaniciEmail").equals(mAuth.getCurrentUser().getEmail())) {
                        mesafeValue.setText(hashMap.get("KullaniciMesafe"));
                        kullaniciValue.setText(hashMap.get("KullaniciAd"));
                        telefonNoValue.setText(hashMap.get("KullaniciTelefon"));
                        kullaniciMailValue.setText(hashMap.get("KullaniciEmail"));
                        kanValue.setText(hashMap.get("KullaniciKan"));
                        userUID = hashMap.get("KullaniciUID");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
    public void onClick(View v) {
        if (v == logoutButton) {
            mAuth.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
        if (v == ayarKaydetButton) {

            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

            myRef.child(userUID).child("KullaniciMesafe").setValue(mesafeValue.getText().toString());
            myRef.child(userUID).child("KullaniciTelefon").setValue(telefonNoValue.getText().toString());
            if(!sifreValue.getText().toString().equals(""))
            {
                myRef.child(userUID).child("KullaniciSifre").setValue(sifreValue.getText().toString());
                mAuth.getCurrentUser().updatePassword(sifreValue.getText().toString());
            }
            alertDialogBuilder.setMessage("Değişiklik Yapılmıştır");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}