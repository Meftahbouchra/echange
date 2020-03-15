package com.bouchra.myapplicationechange.fragments;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.models.Wilaya;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class Single_choice_classification extends DialogFragment {
    int position = 0; // default selected posotion

    public Single_choice_classification() {
    }

    public Single_choice_classification(SingleChoiceListener mListener) {
        this.mListener = mListener;
    }


    ArrayList<Wilaya> wilaya = new ArrayList<Wilaya>();

    public interface SingleChoiceListener {
      //  void onPositiveButtononCliked(String[] wilayaname, int position);
  void onPositiveButtononCliked(String wilayaname);
        void onNegativeButtononCliked();

    }

    SingleChoiceListener mListener;



    /* @NonNull
     @Override
     public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
         String[] list = getActivity().getResources().getStringArray(R.array.choice_item);
         builder.setTitle("Le classement est par :")
                 .setSingleChoiceItems(list, position, (dialog, which) -> position = which)
                 .setPositiveButton("Ok", (dialog, which) -> mListener.onPositiveButtononCliked(list, position))
                 .setNegativeButton("Annuler", (dialog, which) -> mListener.onNegativeButtononCliked());
         return builder.create();
     }*/
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.create();
        String[] wilayaname ;
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readFileFromRawDirectory(R.raw.wilayas));
            wilayaname = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    wilaya.add(new Wilaya(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")), jsonArray.getJSONObject(i).getString("nom")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wilayaname[i] = wilaya.get(i).getId() + " " + wilaya.get(i).getName();
            }
            builder.setTitle("Selectionnez une Wilaya :")
                    .setSingleChoiceItems(wilayaname, position, (dialog, which) -> position = which)
                    .setPositiveButton("Ok", (dialog, which) -> {////////////////////////////////////////////////////////////
                        mListener.onPositiveButtononCliked(wilaya.get(position).getName());
                        dismiss();
                    }).setNegativeButton("annuler",(dialog, which) ->dismiss());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return builder.create();
    }

    private String readFileFromRawDirectory(int resourceId) {
        InputStream iStream = getActivity().getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream() ;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toString();
    }
}



