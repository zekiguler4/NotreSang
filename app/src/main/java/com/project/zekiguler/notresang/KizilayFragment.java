package com.project.zekiguler.notresang;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;


public class KizilayFragment extends Fragment implements View.OnClickListener {
    public Button mapsBakButton;
    public static KizilayFragment newInstance() {
        KizilayFragment fragment = new KizilayFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kizilay, container, false);
        mapsBakButton = (Button) view.findViewById(R.id.mapBakbutton);
        mapsBakButton.setOnClickListener(this);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        String text;
        text = "<html><body style =\"background-color: #f7f7f7; font-size: 110%; line-height: 1.4;\";><p align=\"justify\">";
        text+= "Türk Kızılay, Bölge Kan Merkezleri, Kan Bağış Merkezleri ve mobil kan bağış araçları ile ülkemizin ihtiyacı olan kanın tamamını gönüllü ve sürekli bağışçılardan" +
                " karşılamayı amaç haline getirmiştir." +
                " Türk Kızılay, Türkiye’nin ihtiyacı olan kanın tamamını gönüllü ve sürekli bağışçılardan karşılamak" +
                " için Ulusal Kan Temini Projesi'ni yürütür. Hasta güvenliği açısından kendisine bağışlanan her kanı modern laboratuvarlarda" +
                " testlere tabii tutan Türk Kızılay, kanı ihtiyacı olan kişilere verilmek üzere hastanelere ulaştırır." +
                " Bu menüden kan bağışı yapabileceğiniz Kızılay merkezlerinizi bulabilirsiniz";
        text+= "</p></body></html>";
        webView.loadData(text, "text/html", "utf-8");
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        return view;
    }
    public void onClick(View v) {
        if (v == mapsBakButton) {
            startActivity(new Intent(getActivity(), MapsActivity.class));
        }
    }
}