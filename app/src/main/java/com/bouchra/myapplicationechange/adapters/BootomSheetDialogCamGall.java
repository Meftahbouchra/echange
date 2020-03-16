package com.bouchra.myapplicationechange.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.annonce.ImagesStorage;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BootomSheetDialogCamGall extends BottomSheetDialogFragment {

    private BottomSheetLIstener mListner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_camera_gallery, container, false);

        Button button1 = v.findViewById(R.id.btn_camera);
        Button button2 = v.findViewById(R.id.btn_gallery);
        button1.setOnClickListener(v1 -> {
            // Toast.makeText(getContext(), "button1", Toast.LENGTH_SHORT).show();
            //   mListner.onButtonCliked("button1 cliked");
            ((ImagesStorage) getActivity()).takePhotoFromCamera();
            dismiss();

        });
        button2.setOnClickListener(v12 -> {
            //   mListner.onButtonCliked("button2 cliked");
            //  Toast.makeText(getContext(), "button2", Toast.LENGTH_SHORT).show();
            ((ImagesStorage) getActivity()).choosePhotoFromGallary();
            dismiss();
        });


        return v;
    }


    public interface BottomSheetLIstener {
        void onButtonCliked(String text);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListner = (BottomSheetLIstener) context;
        } catch (ClassCastException e) {
            // throw  new ClassCastException(context.toString()+ "doit mettre en oeuvre BottomSheetLIstener");
        }

    }
}

