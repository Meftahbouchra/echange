package com.bouchra.myapplicationechange.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bouchra.myapplicationechange.R;

public class Single_choice_classification extends DialogFragment {
    int position = 0; // default selected posotion

    public interface SingleChoiceListener {
        void onPositiveButtononCliked(String[] list, int position);

        void onNegativeButtononCliked();
    }

    SingleChoiceListener mListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (SingleChoiceListener) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() + "\n" +
                    "SingleChoiceListener doit être implémenté");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] list = getActivity().getResources().getStringArray(R.array.choice_item);
        builder.setTitle("Le classement est par :")
                .setSingleChoiceItems(list, position, (dialog, which) -> position = which)
                .setPositiveButton("Ok", (dialog, which) -> mListener.onPositiveButtononCliked(list, position))
                .setNegativeButton("Annuler", (dialog, which) -> mListener.onNegativeButtononCliked());
        return builder.create();
    }
}
