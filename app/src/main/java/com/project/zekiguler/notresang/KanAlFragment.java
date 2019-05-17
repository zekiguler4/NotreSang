package com.project.zekiguler.notresang;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;
import java.util.UUID;

public class KanAlFragment extends Fragment implements View.OnClickListener {
    public Spinner kanGrubu, aciliyetGrubu;
    public String latitude = "0.0", longitude = "0.0";
    public String[] gruplar = {"0 RH(+)", "0 RH(-)", "A RH(+)", "A RH(-)", "B RH(+)", "B RH(-)", "AB RH(+)", "AB RH(-)"};
    public String[] aciliyet = {"Kritik", "Çok Acil", "Acil", "Normal"};
    public EditText hastaAdi, irtibatNo, hastaneAdi, aciklama;
    public String hastaadi, irtibatno, hastaneadi, aciklamaa, kan, aciliyett;
    public Button kanAraButton;
    public AlertDialog.Builder alertDialogBuilder;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    public SharedPreferences locationInfo;
    public static KanAlFragment newInstance() {
        KanAlFragment fragment = new KanAlFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanal, container, false);

        ArrayAdapter<String> dataAdapterforKanGrubu, dataAdapterforAciliyet;

        kanGrubu = view.findViewById(R.id.kanalGrubu);
        dataAdapterforKanGrubu = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, gruplar);
        dataAdapterforKanGrubu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kanGrubu.setAdapter(dataAdapterforKanGrubu);

        aciliyetGrubu = view.findViewById(R.id.aciliyetGrubu);
        dataAdapterforAciliyet = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, aciliyet);
        dataAdapterforAciliyet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aciliyetGrubu.setAdapter(dataAdapterforAciliyet);
        hastaAdi = view.findViewById(R.id.kanalisimAlani);
        irtibatNo = view.findViewById(R.id.irtibatNo);
        hastaneAdi = view.findViewById(R.id.hastaneAdi);
        aciklama = view.findViewById(R.id.aciklamaAlani);

        kanAraButton = view.findViewById(R.id.kanarabutton);
        kanAraButton.setOnClickListener(this);
        alertDialogBuilder = new AlertDialog.Builder(getActivity());
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        locationInfo = getActivity().getSharedPreferences("locationInfo", Context.MODE_PRIVATE);

        return view;
    }
    public void onClick(View v) {
        if (v == kanAraButton) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            hastaadi = hastaAdi.getText().toString();
            irtibatno = irtibatNo.getText().toString();
            hastaneadi = hastaneAdi.getText().toString();
            aciklamaa = aciklama.getText().toString();
            kan = kanGrubu.getSelectedItem().toString();
            aciliyett = aciliyetGrubu.getSelectedItem().toString();
            latitude = locationInfo.getString("latitude", "0.0");
            longitude = locationInfo.getString("longitude", "0.0");
            if (hastaadi.equals("") || irtibatno.equals("") || aciklamaa.equals("") || hastaneadi.equals("") ||
                    latitude.equals("0.0") || longitude.equals("0.0")) {
                String message = "";
                if (hastaadi.equals("")) {
                    message = "Hasta Adı Eksik Girilmiştir. \n";
                }
                if (irtibatno.equals("")) {
                    message = message + "İrtibat No Eksik Girilmiştir. \n";
                }
                if (hastaneadi.equals("")) {
                    message = message + "Hastane Adı Eksik Girilmiştir. \n";
                }
                if (aciklamaa.equals("")) {
                    message = message + "Açıklama Eksik Girilmiştir. \n";
                }
                if (latitude.equals("0.0") || longitude.equals("0.0")){
                    message = message + "Konum Bilgisi Eksik, Konum Hizmetlerini Açınız";
                }
                alertDialogBuilder.setMessage(message);
            } else {

                UUID uuid1 = UUID.randomUUID();
                Date date = new Date();
                long dateByMilliSeconds = date.getTime();
                String uuidString = uuid1.toString();
                myRef.child("KanIlanlari").child(uuidString).child("IlanHastaAdi").setValue(hastaadi);
                myRef.child("KanIlanlari").child(uuidString).child("IlanIrtibatNo").setValue(irtibatno);
                myRef.child("KanIlanlari").child(uuidString).child("IlanHastaneAdi").setValue(hastaneadi);
                myRef.child("KanIlanlari").child(uuidString).child("IlanKanGrubu").setValue(kan);
                myRef.child("KanIlanlari").child(uuidString).child("IlanAciliyet").setValue(aciliyett);
                myRef.child("KanIlanlari").child(uuidString).child("IlanAciklama").setValue(aciklamaa);
                myRef.child("KanIlanlari").child(uuidString).child("IlanKonumLat").setValue(latitude);
                myRef.child("KanIlanlari").child(uuidString).child("IlanKonumLon").setValue(longitude);
                myRef.child("KanIlanlari").child(uuidString).child("IlanDate").setValue(Long.toString(dateByMilliSeconds));
                alertDialogBuilder.setMessage("Kayıt Oluşturuldu\n\n" + "Hasta Adı: " + hastaadi + "\n" + "İrtibat No: " + irtibatno + "\n" +
                        "Hastane Adı: " + hastaneadi + "\n" + "Kan Grubu: " + kan + "\n" + "Aciliyet: " + aciliyett +
                        "\n" + "Açıklama: " + aciklamaa);
            }
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    hastaAdi.setText("");
                    irtibatNo.setText("");
                    hastaneAdi.setText("");
                    aciklama.setText("");
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}