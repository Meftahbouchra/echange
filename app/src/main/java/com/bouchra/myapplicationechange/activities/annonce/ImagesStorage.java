package com.bouchra.myapplicationechange.activities.annonce;

import android.Manifest;
import android.content.ClipData;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.bouchra.myapplicationechange.adapters.myImage;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

public class ImagesStorage extends AppCompatActivity {


    StorageReference mStorageRef;
    public Uri imguri;

    private static final String IMAGE_DIRECTORY = "/Echangedarticle";
    private int GALLERY = 1, CAMERA = 3, CAMERA_PERMISSION = 2, WRITE_EXTERNAL_STORAGE = 4, READ_EXTERNAL_STORAGE = 5;
    // private ImageView imageview;
    private Annonce annonce;
    private String selectedCateg;
    private ArrayList<Uri> listImages;
    private com.bouchra.myapplicationechange.adapters.myImage myImage;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_storage);

        //StorageReference Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("Images Annonce");
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        selectedCateg = getIntent().getStringExtra("categorie");
        //  imageview = findViewById(R.id.image_view);
        recyclerView = findViewById(R.id.recycleImages);

        listImages = new ArrayList<>();
        myImage = new myImage(this, listImages);
        // recycle view horizontal

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myImage);
        // bottom shet
        Button buttonOpenBottomSheet = findViewById(R.id.button_sheet);
        buttonOpenBottomSheet.setOnClickListener(v -> {
            if (listImages.size() <= 5) {
               /* BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
                bottomsheet.show(getSupportFragmentManager(), "exemplBottomsheet");*/
                BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
                Bundle bundle = new Bundle();
                bundle.putString("linkAnnonce","fromAnnonce");
                bottomsheet.setArguments(bundle);
                bottomsheet.show((this).getSupportFragmentManager(),"Image Dialog");
            } else {
                Toast.makeText(this, "Vous ne pouvez pas ajouter d'autres photos ", Toast.LENGTH_SHORT).show();
            }

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

            if (listImages.size() != 0) {

                Fileuploader();
            } else {
                Toast.makeText(this, "Ajouter des images a votre annonce ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // photo gallery or camera methodes
    public void choosePhotoFromGallary() {
        if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
        } else {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);// EXTRA_ALLOW_MULTIPLE n'est disponible que pour l api >=
            startActivityForResult(intent, GALLERY);
        }

    }

    public void takePhotoFromCamera() {
        if (!checkCameraPermission()) {
            resuestCameraPermission();
        } else if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
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
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        // display your images

                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            if (listImages.size() <= 5) {
                                listImages.add(Uri.parse(saveImage(bitmap)));
                                myImage.notifyDataSetChanged();
                            } else {
                                Toast.makeText(this, "Vous ne pouvez pas ajouter d'autres photos ", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                } else if (data.getData() != null) {
                    Uri uri = data.getData();
                    // display your image

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        listImages.add(Uri.parse(saveImage(bitmap)));
                        myImage.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }


            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //******************
            // imageview.setImageBitmap(thumbnail);
            // imguri = Uri.parse(saveImage(thumbnail));

            listImages.add(Uri.parse(saveImage(thumbnail)));
            myImage.notifyDataSetChanged();


            Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == CAMERA_PERMISSION) {
            //takePhotoFromCamera();
        } else if (requestCode == WRITE_EXTERNAL_STORAGE) {
            //takePhotoFromCamera();
        } else if (requestCode == READ_EXTERNAL_STORAGE) {
            //takePhotoFromCamera();
        }
    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    throw new IOException("Cant able to create file");
                }
            }
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

        for (Uri uri : listImages) {
            imguri = uri;
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


    }


    //permission
    private boolean checkCameraPermission() {
        //check if camera permission is enabel or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private boolean checkExternalStorageWritePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private boolean checkExternalStorageREADPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void resuestCameraPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(ImagesStorage.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        Toast.makeText(this, "resuestCameraPermission", Toast.LENGTH_SHORT).show();
    }

    private void resuestExternalStorageWritePermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(ImagesStorage.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        Toast.makeText(this, "resuest_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
    }

    private void resuestExternalStorageREADPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(ImagesStorage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        Toast.makeText(this, "resuest_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
    }


}
