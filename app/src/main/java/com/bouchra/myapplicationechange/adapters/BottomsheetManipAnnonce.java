package com.bouchra.myapplicationechange.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.confirmeSuppAnnonce;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomsheetManipAnnonce extends BottomSheetDialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_manip_annonce, container, false);
        Button btnModifier = v.findViewById(R.id.btn_modifier);
        Button btnSupprimer = v.findViewById(R.id.btn_supprimer);
        btnModifier.setOnClickListener(v1 -> {

        });
        btnSupprimer.setOnClickListener(v12 -> {
            openDialog();
//Toast.makeText(getContext(), "supp cette annonce", Toast.LENGTH_SHORT).show();
        });
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // intint fo fragment
   ((DetailMesannonce) getActivity()).goToFragmentModifier();

            }


        });
        return v;
    }




    private void openDialog() {
        confirmeSuppAnnonce confirmeSuppAnnonce = new confirmeSuppAnnonce();
        confirmeSuppAnnonce.show(getFragmentManager(), "confirmeSuppAnnonce");


    }

}
