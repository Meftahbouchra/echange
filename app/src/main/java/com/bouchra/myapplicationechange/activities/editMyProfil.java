package com.bouchra.myapplicationechange.activities;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.bouchra.myapplicationechange.fragments.editPassword;
import com.bouchra.myapplicationechange.models.Membre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
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
    private PreferenceUtils preferenceUtils;
    private Uri imagrUri;
    //request
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST = 200;
    private static final int READ_EXTERNAL_STORAGE_REQUEST = 300;

    //image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 500;
    //permissions arry
    String[] cameraPermission;
    String[] storagePermissionWRITE;
    String[] storagePermissionREAD;


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
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissionWRITE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissionREAD = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        // show contenu
        name.setText(preferenceUtils.getMember().getNomMembre());
        email.setText(preferenceUtils.getMember().getEmail());
        number.setText(String.valueOf(preferenceUtils.getMember().getNumTel()));
        //image_user
        Picasso.get().load(preferenceUtils.getMember().getPhotoUser()).into(image_user);
        // if user don't not set img ,save last picture
        imagrUri = Uri.parse(preferenceUtils.getMember().getPhotoUser());

        tack_Pict.setOnClickListener(v -> {
            BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
            Bundle bundle = new Bundle();
            bundle.putString("linkProfile", "fromProfil");
            bottomsheet.setArguments(bundle);
            bottomsheet.show(getSupportFragmentManager(), "Image Dialog");
        });
// if user compte ne recoorepond pas a nov compte ,il n y a pas un moyen pour modifier leur mdps
        if (preferenceUtils.getMember().getMotDePasse().isEmpty()) {
            passwordLayout.setVisibility(View.GONE);
        }
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyProfil.class));
                finish();
            }
        });
        // set compte
        enregister.setOnClickListener(v -> {
            String nome = name.getText().toString();
            String mail = email.getText().toString();
            String numTel = number.getText().toString();
            if (!nome.isEmpty() && imagrUri != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference("Membre").child(preferenceUtils.getMember().getIdMembre());
                Membre user = new Membre();
                user.setEmail(mail);
                user.setNumTel(Integer.parseInt(numTel));
                user.setNomMembre(nome);
                user.setIdMembre(preferenceUtils.getMember().getIdMembre());
                user.setPhotoUser(imagrUri.toString());
                user.setDateInscription(preferenceUtils.getMember().getDateInscription());
                databaseReference.setValue(user)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                preferenceUtils.Clear();
                                preferenceUtils.setMember(user);
                                startActivity(new Intent(editMyProfil.this, MyProfil.class));
                                finish();
                            }
                        }).addOnFailureListener(e -> Toast.makeText(editMyProfil.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show());


            } else {
                Toast.makeText(editMyProfil.this, "Les données sont manquantes !!", Toast.LENGTH_SHORT).show();
            }

        });


        passwordLayout.setOnClickListener(v -> {
            Log.e("passwors exist ", preferenceUtils.getMember().getMotDePasse());
            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new editPassword(), "editpassword").commit();

        });
    }

    public void pickFromGallery() {

        if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
        } else {

            //intent to pick image from gallery
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
        }

    }

    public void pickFromCamera() {
        if (!checkCameraPermission()) {
            resuestCameraPermission();
        } else if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
        } else {
            //intent to pick image from camera
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
        }

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

    private boolean checkExternalStorageWritePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private boolean checkExternalStorageREADPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void resuestExternalStorageWritePermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, storagePermissionWRITE, WRITE_EXTERNAL_STORAGE_REQUEST);

    }

    private void resuestExternalStorageREADPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(this, storagePermissionREAD, READ_EXTERNAL_STORAGE_REQUEST);

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
                        image_user.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                }

            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                image_user.setImageBitmap(thumbnail);
                imagrUri = Uri.parse(saveImage(thumbnail));

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
            case WRITE_EXTERNAL_STORAGE_REQUEST:
            case READ_EXTERNAL_STORAGE_REQUEST: {
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
    @Override

    public void onBackPressed() {
        if (getSupportFragmentManager().findFragmentByTag("editpassword") != null) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentByTag("editpassword")).commit();
        }
    }
}
