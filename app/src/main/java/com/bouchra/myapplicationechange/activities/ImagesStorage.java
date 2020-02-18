package com.bouchra.myapplicationechange.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class ImagesStorage extends AppCompatActivity  implements AdapterView.OnItemSelectedListener , BootomSheetDialogCamGall.BottomSheetLIstener {
    @Override
    public void onButtonCliked(String text) {
        Toast.makeText(this, ""+text, Toast.LENGTH_SHORT).show();
    }

    private static int PICK_IMAGE_REQUEST = 1;
    String pathToFile;

    private Button ch;
    private Button up;

    private ImageView img;
    StorageReference mStorageRef;

    public Uri mImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_storage);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        ch = findViewById(R.id.button_choose_image);
        up = findViewById(R.id.button_upload);
        img = findViewById(R.id.image_view);
        //permission
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }
        Spinner spinner = findViewById(R.id.spinner_cat);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choix_categorie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
//camera
        ch.setOnClickListener(v -> {
            FileChooser();

        });
        // bottom shet
          Button buttonOpenBottomSheet = findViewById(R.id.button_sheet);
          buttonOpenBottomSheet.setOnClickListener(v -> {
              BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
              bottomsheet.show(getSupportFragmentManager(),"exemplBottomsheet");
          });


    }


//****************************************** deb camera
    private  void  FileChooser(){

            Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(takePic.resolveActivity(getPackageManager())!=null){
                File photoFile = null;

                    photoFile= createPhotoFile();
                    if (photoFile !=null){

                         pathToFile=photoFile.getAbsolutePath();
                        Uri photoUri = FileProvider.getUriForFile(ImagesStorage.this,"com.bouchra.myapplicationechange.fileprovider",photoFile);
                        takePic.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                        startActivityForResult(takePic,PICK_IMAGE_REQUEST);// howa dar 1
                    }



            }


    }

        private File createPhotoFile() {

            String name= new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image=null;
            try {
                image = File.createTempFile(name,".jpg",storageDir);
            } catch (IOException e) {
                Log.d("Mylog","Excep :"+toString());
            }
            return  image;
        }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if( resultCode == RESULT_OK)
            {
                if(requestCode ==PICK_IMAGE_REQUEST){
                    Bitmap bitmap= BitmapFactory.decodeFile(pathToFile);
                    img.setImageBitmap(bitmap);
                }
            }

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {

                mImageUri = data.getData();

                img.setImageURI(mImageUri);

            }
        }
    //************************************fin camera
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text =parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
