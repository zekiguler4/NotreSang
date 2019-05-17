package com.project.zekiguler.notresang;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.ArrayList;

public class IlanAdapter extends ArrayAdapter<Ilan> {
    private Context context;
    IlanAdapter(Context context, ArrayList<Ilan> ilanlar) {
        super(context, 0, ilanlar);
        this.context = context;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        final Ilan ilan = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ilanlayout, parent, false);
        }
        TextView ilanHastaAdi = convertView.findViewById(R.id.ilanHastaAdi);
        TextView ilanIrtibatNo = convertView.findViewById(R.id.ilanIrtibatNo);
        TextView ilanHastaneAdi = convertView.findViewById(R.id.ilanHastaneAdi);
        TextView ilanMesafe = convertView.findViewById(R.id.ilanMesafe);
        Button araButton = convertView.findViewById(R.id.arabutton);
        final String no = ilan.IrtibatNo;
        final View finalConvertView = convertView;
        araButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                PopupWindow pw = new PopupWindow(inflater.inflate(R.layout.ilanpopup, null, false),finalConvertView.getWidth(),ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.showAtLocation(finalConvertView, Gravity.CENTER, 10, 0);
                pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                ((TextView)pw.getContentView().findViewById(R.id.ilanpopupname))
                        .setText(((TextView)pw.getContentView().findViewById(R.id.ilanpopupname)).getText() + ilan.HastaAdi);
                ((TextView)pw.getContentView().findViewById(R.id.ilanpopupirtibatno))
                        .setText(((TextView)pw.getContentView().findViewById(R.id.ilanpopupirtibatno)).getText() + ilan.IrtibatNo);
                ((TextView)pw.getContentView().findViewById(R.id.ilanpopuphastaneadi))
                        .setText(((TextView)pw.getContentView().findViewById(R.id.ilanpopuphastaneadi)).getText() + ilan.HastaneAdi);
                ((TextView)pw.getContentView().findViewById(R.id.ilanpopupmesafe))
                        .setText(((TextView)pw.getContentView().findViewById(R.id.ilanpopupmesafe)).getText() + ilan.Uzaklik + " Km");
                ((TextView)pw.getContentView().findViewById(R.id.ilanpopupaciklama))
                        .setText(((TextView)pw.getContentView().findViewById(R.id.ilanpopupaciklama)).getText() + ilan.Aciklama);
                ((TextView)pw.getContentView().findViewById(R.id.ilanpopupkangrubu))
                        .setText(((TextView)pw.getContentView().findViewById(R.id.ilanpopupkangrubu)).getText() + ilan.KanGrubu);
                (pw.getContentView().findViewById(R.id.ilanpopuparabutton)).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", no, null));
                        context.startActivity(intent);
                    }
                });
            }
        });
        ilanHastaAdi.setText(ilan.HastaAdi);
        ilanIrtibatNo.setText(ilan.IrtibatNo);
        ilanHastaneAdi.setText(ilan.HastaneAdi);
        ilanMesafe.setText(ilan.Uzaklik + " Km");
        return convertView;
    }
}