package com.bouchra.myapplicationechange.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.AjoutOffre;
import com.bouchra.myapplicationechange.activities.ModifierAnnonce;
import com.bouchra.myapplicationechange.activities.annonce.ImagesStorage;
import com.bouchra.myapplicationechange.activities.editMyProfil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BootomSheetDialogCamGall extends BottomSheetDialogFragment {

    private String Offre = null;
    private String Annonce = null;
    private String Profile = null;
    private String modierannonce = null;
    private Button btnCamera;
    private Button btnGallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_camera_gallery, container, false);
        Offre = this.getArguments().getString("linkOffre");
        Annonce = this.getArguments().getString("linkAnnonce");
        Profile = this.getArguments().getString("linkProfile");
        modierannonce = this.getArguments().getString("linkModifierannonce");

        btnCamera = v.findViewById(R.id.btn_camera);
        btnGallery = v.findViewById(R.id.btn_gallery);
        btnCamera.setOnClickListener(v1 -> {
            if (Offre != null) {
                ((AjoutOffre) getActivity()).takePhotoFromCamera();
            }
            if (Annonce != null) {
                ((ImagesStorage) getActivity()).takePhotoFromCamera();
            }
            if (Profile != null) {
                ((editMyProfil) getActivity()).pickFromCamera();
            }
            if (modierannonce != null) {
                ((ModifierAnnonce) getActivity()).takePhotoFromCamera();
            }
            dismiss();

        });
        btnGallery.setOnClickListener(v12 -> {

            if (Offre != null) {
                ((AjoutOffre) getActivity()).choosePhotoFromGallary();
            }
            if (Annonce != null) {
                ((ImagesStorage) getActivity()).choosePhotoFromGallary();
            }
            if (Profile != null) {
                ((editMyProfil) getActivity()).pickFromGallery();
            }
            if (modierannonce != null) {
                ((ModifierAnnonce) getActivity()).choosePhotoFromGallary();
            }

            dismiss();
        });


        return v;
    }

}

