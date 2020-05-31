package com.bouchra.myapplicationechange.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.activities.profilUser;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetPhoneNumber extends BottomSheetDialogFragment {
    private Button call;
    private  Button sms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_phone_number, container, false);
        call=v.findViewById(R.id.btn_call);
        sms=v.findViewById(R.id.btn_sms);

        call.setOnClickListener(v1 -> {
            ((profilUser) getActivity()).Call();
            dismiss();

        });
        sms.setOnClickListener(v12 -> {
            ((profilUser) getActivity()).sendSms();
            dismiss();
        });


        return v;
    }

}

