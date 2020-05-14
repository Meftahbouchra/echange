package com.bouchra.myapplicationechange.activities.annonce;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.bouchra.myapplicationechange.models.Annonce;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

public class ImagesStorage extends AppCompatActivity {


    StorageReference mStorageRef;
    public Uri imguri;

    private static final String IMAGE_DIRECTORY = "/Echangedarticle";
    private int GALLERY = 1, CAMERA = 2;
    private ImageView imageview;
    private Annonce annonce;
    private String selectedCateg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_storage);

        //StorageReference Fire base
        mStorageRef = FirebaseStorage.getInstance().getReference("Images Annonce");
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        selectedCateg = getIntent().getStringExtra("categorie");
        imageview = findViewById(R.id.image_view);


        // bottom shet
        Button buttonOpenBottomSheet = findViewById(R.id.button_sheet);
        buttonOpenBottomSheet.setOnClickListener(v -> {
            BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
            bottomsheet.show(getSupportFragmentManager(), "exemplBottomsheet");
            //1
            //kindir ch3al mn activity troh l had fragment ta3 la tof aya ndi m3ah intent 3la hsab min rah jay
            //In your Activity
            /*
            ImageViewDialogFragment dialogFragment = new ImageViewDialogFragment ();
                        Bundle bundle = new Bundle();
                        bundle.putString("link",moviesList.get(position).getImage());
                        dialogFragment.setArguments(bundle);
                        dialogFragment.show((GalleryReviewActivity.this).getSupportFragmentManager(),"Image Dialog");
             */
            //in your DialogFragment
            /* Bundle bundle = getArguments();
 String imageLink = bundle.getString("link","");

             */
        });

        findViewById(R.id.next).setOnClickListener(v -> {
            if (imguri != null) {
                Fileuploader();
            }
        });
    }

    // photo gallery or camera methodes
    public void choosePhotoFromGallary() {
        if (!checkStoragePermission()) {
            resuestStoragePermission();
        } else {

            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(galleryIntent, GALLERY);

        }

    }

    public void takePhotoFromCamera() {
        if (!checkCameraPermission()) {
            resuestCameraPermission();
        } else {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    imguri = Uri.parse(saveImage(bitmap));

                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    //*******************
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //******************
            imageview.setImageBitmap(thumbnail);
            imguri = Uri.parse(saveImage(thumbnail));



            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getApplicationContext(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
    private void Fileuploader() {
        Log.e("img here", imguri.toString());
        try {
            InputStream stream = new FileInputStream(String.valueOf(imguri));
            //StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());
            StorageReference ref = mStorageRef.child(annonce.getIdAnnonce() + UUID.randomUUID().toString());
            ref.putStream(stream)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(ImagesStorage.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(task -> {
                                    annonce.getImages().add(String.valueOf(task));
                                    Log.e("Image link", String.valueOf(task));
                                    Intent ajou = new Intent(ImagesStorage.this, Article_en_retour.class);
                                    ajou.putExtra("annonce", annonce); //key* value
                                    ajou.putExtra("Categ", selectedCateg);
                                    startActivity(ajou);
                                    finish();
                                }
                        );
                    })
                    .addOnFailureListener(e -> Toast.makeText(ImagesStorage.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }




    private boolean checkStoragePermission() {
        //check if storage permission is enabel or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void resuestStoragePermission() {
        //request runtime storage permission
       // ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
        Toast.makeText(this, "resuestStoragePermission", Toast.LENGTH_SHORT).show();
    }


    private boolean checkCameraPermission() {
        //check if camera permission is enabel or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean resultl = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && resultl;
    }

    private void resuestCameraPermission() {
        //request runtime camera permission
      //  ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
        Toast.makeText(this, "resuestCameraPermission", Toast.LENGTH_SHORT).show();

    }

}
