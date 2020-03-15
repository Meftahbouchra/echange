package com.bouchra.myapplicationechange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bouchra.myapplicationechange.models.Membre;

import java.util.Date;


public class PreferenceUtils {

    SharedPreferences pref;
    Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "store";
    public static final String MEMBER = "user";
    public static final String ID_MEMBER = "id_user";
    public static final String NOM_MEMBER = "nom_user";
    public static final String NUM_MEMBER = "num_user";
    public static final String EMAIL_MEMBER = "email_user";
    public static final String ADDR_MEMBER = "addr_user";
    public static final String FB_MEMBER = "fb_user";
    public static final String GOOGLE_MEMBER = "google_user";
    public static final String DATE_MEMBER = "date_user";

    public PreferenceUtils(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }


  public Membre getMember() {
        Membre member = new Membre();
        member.setIdMembre(pref.getString(ID_MEMBER , ""));
        member.setNomMembre(pref.getString(NOM_MEMBER , ""));
        member.setNumTel(pref.getInt(NUM_MEMBER , 0));
        member.setEmail(pref.getString(EMAIL_MEMBER , ""));
        member.setAdresseMembre(pref.getString(ADDR_MEMBER , ""));
        member.setLienCompteFb(pref.getString(FB_MEMBER , ""));
        member.setLienCompteGoogle(pref.getString(GOOGLE_MEMBER , ""));
        member.setDateInscription(new Date(pref.getLong(DATE_MEMBER , new Date().getTime())));
        member.setIdMembre(pref.getString(ID_MEMBER , ""));
        return member;
    }

    public void setMember(Membre member) {
        editor.putString(ID_MEMBER , member.getIdMembre());
        editor.putString(NOM_MEMBER , member.getNomMembre());
        editor.putInt(NUM_MEMBER , member.getNumTel());
        editor.putString(EMAIL_MEMBER , member.getEmail());
        editor.putString(ADDR_MEMBER , member.getAdresseMembre());
        editor.putString(FB_MEMBER , member.getLienCompteFb());
        editor.putString(GOOGLE_MEMBER , member.getLienCompteGoogle());
        editor.putLong(DATE_MEMBER , member.getDateInscription().getTime());
        editor.commit();
    }

    public void Clear(){
        editor.clear().commit();
    }


}