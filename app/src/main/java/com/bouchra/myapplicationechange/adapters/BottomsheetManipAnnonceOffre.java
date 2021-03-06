package com.bouchra.myapplicationechange.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.DetailMesannonce;
import com.bouchra.myapplicationechange.activities.ModiferOffre;
import com.bouchra.myapplicationechange.models.Annonce;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomsheetManipAnnonceOffre extends BottomSheetDialogFragment {


    Annonce annonce;
    com.bouchra.myapplicationechange.models.Offre offreObject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_manip_annonce, container, false);

        annonce = (Annonce) this.getArguments().getSerializable("fromAnnonce");
        offreObject = (com.bouchra.myapplicationechange.models.Offre) this.getArguments().getSerializable("objectOffre");
        Button btnModifier = v.findViewById(R.id.btn_modifier);
        Button btnSupprimer = v.findViewById(R.id.btn_supprimer);

        btnSupprimer.setOnClickListener(v12 -> {
            openDialog(annonce, offreObject);
            dismiss();
        });
        btnModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (offreObject != null) {
                    goToFragmentModifier();
                    dismiss();


                }
                if (annonce != null) {
                    ((DetailMesannonce) getActivity()).goToFragmentModifier();
                    dismiss();
                }
            }

        });
        return v;
    }


    private void openDialog(Annonce annonce, com.bouchra.myapplicationechange.models.Offre offreObject) {

        confirmeSuppAnnonceOffre confirmeSuppAnnonce = new confirmeSuppAnnonceOffre();
        Bundle b2 = new Bundle();
        b2.putSerializable("Annonce", annonce);
        b2.putSerializable("offreObject", offreObject);
        confirmeSuppAnnonce.setArguments(b2);
        confirmeSuppAnnonce.show(getFragmentManager(), "confirmeSuppAnnonceOffre");


    }

    public void goToFragmentModifier() {


        Intent an = new Intent(getContext(), ModiferOffre.class);
        an.putExtra("offre", offreObject);
        startActivity(an);
        getActivity().finish();

    }

}
