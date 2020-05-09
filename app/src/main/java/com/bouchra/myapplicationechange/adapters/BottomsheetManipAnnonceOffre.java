package com.bouchra.myapplicationechange.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.confirmeSuppAnnonce;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomsheetManipAnnonce extends BottomSheetDialogFragment {

    String Offre = null;
    String annonce = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_manip_annonce, container, false);
        Offre = this.getArguments().getString("fromOffre");
        annonce = this.getArguments().getString("fromAnnonce");
        Button btnModifier = v.findViewById(R.id.btn_modifier);
        Button btnSupprimer = v.findViewById(R.id.btn_supprimer);

        btnSupprimer.setOnClickListener(v12 -> {
            openDialog(Offre, annonce);

            // Toast.makeText(getContext(), "is" + Offre + " and " + annonce+".", Toast.LENGTH_SHORT).show();
        });
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Offre != null) {
// ndaigh l wi ymodifier
                    Toast.makeText(getContext(), "need view of modication !!!", Toast.LENGTH_SHORT).show();
                }
                if (annonce != null) {
                    // intint fo fragment
                    ((DetailMesannonce) getActivity()).goToFragmentModifier();
                }


            }


        });
        return v;
    }


    private void openDialog(String offre, String annone) {

        confirmeSuppAnnonce confirmeSuppAnnonce = new confirmeSuppAnnonce();
        Bundle b2 = new Bundle();
        b2.putString("Offre", offre);
        b2.putString("Annonce", annone);
        confirmeSuppAnnonce.setArguments(b2);
        confirmeSuppAnnonce.show(getFragmentManager(), "confirmeSuppAnnonce");


    }

}
