package com.bouchra.myapplicationechange.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bouchra.myapplicationechange.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ImagesStorage extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

private static int PICK_IMAGE_REQUEST=1;

    private Button ch;
    private Button up;
    private ImageView img;
    StorageReference mStorageRef;

    public Uri mImageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_storage);
        mStorageRef= FirebaseStorage.getInstance().getReference("Images");
        ch = findViewById(R.id.button_choose_image);
        up = findViewById(R.id.button_upload);
        img = findViewById(R.id.image_view);
        Spinner spinner=findViewById(R.id.spinner_cat);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.choix_categorie,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        ch.setOnClickListener(v -> {
         FileChooser();
        });

        up.setOnClickListener(v -> {
         Fileuploader();
        });

    }
    private String getExtension( Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private  void  Fileuploader(){

StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(mImageUri));
        Ref.putFile(mImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get a URL to the uploaded content****************************
                   // Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    Toast.makeText(ImagesStorage.this, "Image uploaded succeFul", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

    }


    private  void  FileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            img.setImageURI(mImageUri);
           // Picasso.get().load(mImageUri).into(mImageView);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text =parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
