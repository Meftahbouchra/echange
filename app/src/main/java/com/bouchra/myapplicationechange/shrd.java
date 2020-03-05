package com.bouchra.myapplicationechange;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.utils.PreferenceUtils;

public class shrd extends AppCompatActivity {
  private Button save;
  private Button Show;
   private  EditText editText1;
     private  EditText editText2;
     private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shrd);
        save=findViewById(R.id.b1);
        Show=findViewById(R.id.b2);
        editText1=findViewById(R.id.e1);
        editText2=findViewById(R.id.e2);
        String mot=editText1.getText().toString();
        save.setOnClickListener(v -> {
            PreferenceUtils.saveName(mot,context);
        });
        Show.setOnClickListener(v -> {
editText2.setText(PreferenceUtils.getNAme(context));
        });
    }

}
