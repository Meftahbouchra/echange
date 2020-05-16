package com.bouchra.myapplicationechange;

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
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bouchra.myapplicationechange.activities.MyProfil;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.bouchra.myapplicationechange.fragments.editPassword;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class editMyProfil extends AppCompatActivity {
    private EditText name;
    private EditText email;
    private EditText number;
    private TextView enregister;
    private TextView retour;
    private LinearLayout passwordLayout;

    private TextView tack_Pict;
    private CircleImageView image_user;
    private DatabaseReference databaseReference;
    PreferenceUtils preferenceUtils;
    Uri imagrUri;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    //permissions arry
    String[] cameraPermission;
    String[] storagePermission;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profil);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        number = findViewById(R.id.number);
        passwordLayout = findViewById(R.id.passwordLayout);
        tack_Pict = findViewById(R.id.tack_Pict);
        enregister = findViewById(R.id.enregister);
        image_user = findViewById(R.id.image_user);
        retour = findViewById(R.id.retour);
        preferenceUtils = new PreferenceUtils(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        name.setText(preferenceUtils.getMember().getNomMembre());
        email.setText(preferenceUtils.getMember().getEmail());
        number.setText(String.valueOf(preferenceUtils.getMember().getNumTel()));
        //image_user
        Picasso.get().load(preferenceUtils.getMember().getPhotoUser()).into(image_user);
        imagrUri = Uri.parse(preferenceUtils.getMember().getPhotoUser());
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        tack_Pict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
                Bundle bundle = new Bundle();
                bundle.putString("linkProfile", "fromProfil");
                bottomsheet.setArguments(bundle);
                bottomsheet.show(getSupportFragmentManager(), "Image Dialog");


            }

        });
        if (preferenceUtils.getMember().getMotDePasse().isEmpty()) {
            passwordLayout.setVisibility(View.GONE);

        }
        enregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = name.getText().toString();
                String mail = email.getText().toString();
                String numTel = number.getText().toString();
                if (!nome.isEmpty() && !mail.isEmpty() && !numTel.isEmpty() && imagrUri != null) {
                    //imguri.toString() f object
                    databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(preferenceUtils.getMember().getIdMembre());
                    Membre user = new Membre();
                    user.setEmail(mail);
                    user.setNomMembre(nome);
                    user.setIdMembre(preferenceUtils.getMember().getIdMembre());
                    user.setPhotoUser(imagrUri.toString());
                    user.setDateInscription(preferenceUtils.getMember().getDateInscription());
                    user.setNumTel(Integer.parseInt(numTel));
                    databaseReference.setValue(user)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        preferenceUtils.Clear();
                                        preferenceUtils.setMember(user);
                                        startActivity(new Intent(editMyProfil.this, MyProfil.class));
                                        finish();
                                    }
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(editMyProfil.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                } else {
                    Toast.makeText(editMyProfil.this, "khas les d", Toast.LENGTH_SHORT).show();
                }

            }
        });


        passwordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("kayan pasword", preferenceUtils.getMember().getMotDePasse());
                getSupportFragmentManager().beginTransaction().add(R.id.fragment, new editPassword(), "editpassword").commit();

            }
        });
    }

    public void pickFromGallery() {
        //intent to pick image from gallery

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);


    }

    public void pickFromCamera() {
        //intent to pick image from camera
        //rani dayra whd prgrm sahal fi tp

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);


    }

    private boolean checkStoragePermission() {
        //check if storage permission is enabel or not
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void resuestStoragePermission() {
        //request runtime storage permission
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
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
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // this methode will be called  after picking image from camera or gallery
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                        imagrUri = Uri.parse(saveImage(bitmap));

                        Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                        //*******************
                        image_user.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                //******************
                image_user.setImageBitmap(thumbnail);
                imagrUri = Uri.parse(saveImage(thumbnail));
                Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        //both permission are garented
                        pickFromCamera();

                    } else {
                        // camera or gallery or both permission ware denied
                        Toast.makeText(this, "camera et storage permission sont necessaire !... ", Toast.LENGTH_SHORT).show();
                    }

                } else {


                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //storage permission garented
                        pickFromGallery();

                    } else {
                        // camera or gallery or both permission ware denied
                        Toast.makeText(this, " storage permission  necessaire !... ", Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
            }
            break;
        }
    }
}
