package com.bouchra.myapplicationechange.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.bouchra.myapplicationechange.models.Membre;


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












  /* public static boolean saveEmail(String email, Context context) {
        //   SharedPreferences prefs = getsharedpreferances("nomfile",context.modeprivate)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_EMAIL, email);
        prefsEditor.apply();// prefsEditor.commit();
        return true;
    }

    public static String getEmail(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_EMAIL, null);
    }

    public static boolean savePassword(String password, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PASSWORD, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPassword(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PASSWORD, null);
    }
    ////////////////////////////////
    public static boolean saveName(String password, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_NAME, password);
        prefsEditor.apply();
        return true;
    }

    public static String getName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_NAME, null);
    }
    public static boolean savePhoto(String password, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString(Constants.KEY_PHOTO, password);
        prefsEditor.apply();
        return true;
    }

    public static String getPhoto(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(Constants.KEY_PHOTO, null);
    }*/
}