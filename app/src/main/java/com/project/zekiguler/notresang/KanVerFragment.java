package com.project.zekiguler.notresang;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
public class KanVerFragment extends Fragment {
    public double mesafe;
    public TextView kullaniciValue, mesafeValue, bagisLabel;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference myRef;
    public FirebaseAuth mAuth;
    public SharedPreferences locationInfo;
    public String longitude = "0.0", latitude = "0.0";
    public ListView ilanList;
    public IlanAdapter adapter;
    public ArrayList<Ilan> ilanlar = new ArrayList<Ilan>();
    public static KanVerFragment newInstance() {
        KanVerFragment fragment = new KanVerFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_kanver, container, false);
        kullaniciValue = view.findViewById(R.id.kanverKullaniciValue);
        mesafeValue = view.findViewById(R.id.kanverMesafeValue);
        bagisLabel = view.findViewById(R.id.kanIlanlariLabel);
        ilanList = view.findViewById(R.id.ilanList);
        locationInfo = getActivity().getSharedPreferences("locationInfo", Context.MODE_PRIVATE);
        latitude = locationInfo.getString("latitude", "0.0");
        longitude = locationInfo.getString("longitude", "0.0");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  firebaseDatabase.getReference();
        myRef.child("Kullanicilar").orderByChild("KullaniciEmail")
                .startAt(mAuth.getCurrentUser().getEmail())
                .endAt(mAuth.getCurrentUser().getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {

                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    if(hashMap.get("KullaniciEmail").equals(mAuth.getCurrentUser().getEmail())) {
                        mesafe = Double.parseDouble(hashMap.get("KullaniciMesafe"));
                        mesafeValue.setText(hashMap.get("KullaniciMesafe") + " Km");
                        kullaniciValue.setText(hashMap.get("KullaniciAd"));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fillList();
        return view;
    }
    private void fillList() {
        ReadData(myRef.child("KanIlanlari").orderByChild("IlanDate").limitToLast(100), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) ds.getValue();
                    String ilanLatString = hashMap.get("IlanKonumLat");
                    String ilanLonString = hashMap.get("IlanKonumLon");
                    double ilanLat = Double.parseDouble(ilanLatString);
                    double ilanLon = Double.parseDouble(ilanLonString);
                    double x = mesafeHesapla(ilanLat, ilanLon, Double.parseDouble(latitude), Double.parseDouble(longitude));
                    if (x < mesafe) {
                        String IlanHastaAdi = hashMap.get("IlanHastaAdi");
                        String IlanIrtibatNo = hashMap.get("IlanIrtibatNo");
                        String IlanHastaneAdi = hashMap.get("IlanHastaneAdi");
                        String IlanKanGrubu = hashMap.get("IlanKanGrubu");
                        String IlanAciklama = hashMap.get("IlanAciklama");
                        String IlanAciliyet = hashMap.get("IlanAciliyet");
                        x = Math.floor(x * 100) / 100;
                        String IlanUzaklik = String.valueOf(x);
                        Ilan newIlan = new Ilan(IlanHastaAdi, IlanIrtibatNo, IlanHastaneAdi, IlanKanGrubu,
                                IlanAciklama, IlanAciliyet, IlanUzaklik);
                        ilanlar.add(newIlan);
                    }
                }
                Collections.reverse(ilanlar);
                adapter = new IlanAdapter(getContext(), ilanlar);
                ilanList.setAdapter(adapter);
            }
            @Override
            public void onStart() { }
            @Override
            public void onFailure() { }
        });
    }
    private static double mesafeHesapla(double lat1, double lon1, double lat2, double lon2) {
        if ((lat1 == lat2) && (lon1 == lon2)) {
            return 0;
        }
        else {
            double theta = lon1 - lon2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;
            return (dist);
        }
    }
    private void ReadData(Query query, final OnGetDataListener listener) {
        listener.onStart();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }
}