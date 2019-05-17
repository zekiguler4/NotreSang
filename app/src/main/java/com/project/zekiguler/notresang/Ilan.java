package com.project.zekiguler.notresang;

public class Ilan {
    public String Aciklama;
    public String Aciliyet;
    public String HastaAdi;
    public String HastaneAdi;
    public String IrtibatNo;
    public String KanGrubu;
    public String Uzaklik;

    public Ilan(String HastaAdi, String IrtibatNo, String HastaneAdi, String KanGrubu, String Aciklama, String Aciliyet, String Uzaklik) {
        this.HastaAdi = HastaAdi;
        this.IrtibatNo = IrtibatNo;
        this.HastaneAdi = HastaneAdi;
        this.KanGrubu = KanGrubu;
        this.Aciklama = Aciklama;
        this.Aciliyet = Aciliyet;
        this.Uzaklik = Uzaklik;
    }
}