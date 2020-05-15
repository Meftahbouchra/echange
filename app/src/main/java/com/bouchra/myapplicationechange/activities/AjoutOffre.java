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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Commune;
import com.bouchra.myapplicationechange.models.Notification;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.models.Wilaya;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AjoutOffre extends AppCompatActivity {
    private Button buttonChoosePic;
    private EditText titleObjet;
    private EditText descObjet;
    private ImageView pic;
    private Button suiv;
    private String titre = "";
    private String desc = "";
    private String idAnnonce = "";
    private Spinner wilayaSpinner, villeSpinner;
    private ArrayList<Wilaya> wilaya = new ArrayList<Wilaya>();
    private ArrayList<Commune> communes = new ArrayList<Commune>();
    private String[] wilayaname;
    private DatabaseReference databaseReference, databasereference, data;
    private String selectedWilaya, selectedVille;
    private Annonce annonce;
    private Button annuler;
    private ImageButton suppImage;
    StorageReference mStorageRef;
    public Uri imguri;
    String path;

    private int GALLERY = 1, CAMERA = 3, CAMERA_PERMISSION = 2, WRITE_EXTERNAL_STORAGE = 4, READ_EXTERNAL_STORAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_offre);
        buttonChoosePic = findViewById(R.id.selectbtn);
        titleObjet = findViewById(R.id.nom_objet);
        descObjet = findViewById(R.id.desci_object);
        pic = findViewById(R.id.img_offre);
        wilayaSpinner = findViewById(R.id.spinner_wilayaobj);
        villeSpinner = findViewById(R.id.spinner_villeobj);
        annuler = findViewById(R.id.annuler);
        suppImage = findViewById(R.id.suppImage);

        //StorageReference Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("Images offre");
        annuler.setOnClickListener(v -> {
            Intent an = new Intent(AjoutOffre.this, debut.class);
            startActivity(an);
            finish();
           /* Intent annul = new Intent(AjoutOffre.this, Acceuil.class);
            startActivity(annul);
            finish();*/
        });
        suiv = findViewById(R.id.suiv);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonChoosePic.setOnClickListener(v -> {

            BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
            Bundle bundle = new Bundle();
            bundle.putString("linkOffre", "fromOffre");
            bottomsheet.setArguments(bundle);
            bottomsheet.show((this).getSupportFragmentManager(), "Image Dialog");
        });


        //njib data
        Intent ajou = getIntent();
        if (ajou != null) {

            if (ajou.hasExtra("Annonce")) {
                annonce = (Annonce) getIntent().getSerializableExtra("Annonce");
                idAnnonce = annonce.getIdAnnonce();
            }
        }

        suiv.setOnClickListener(v -> {
            if (imguri != null) {
                Fileuploader();
                titre = titleObjet.getText().toString();
                desc = descObjet.getText().toString();
                PreferenceUtils preferenceUtils = new PreferenceUtils(this);
                if (!titre.isEmpty() && !desc.isEmpty()) {
                    databaseReference = FirebaseDatabase.getInstance().getReference("Offre").child(idAnnonce);
                    Offre offre = new Offre();
                    offre.setAnnonceId(idAnnonce);
                    offre.setDateOffre(new Date());
                    offre.setDescriptionOffre(desc);
                    offre.setIdOffre(String.valueOf(offre.getDateOffre().hashCode()) + offre.getAnnonceId().hashCode());
                    offre.setNomOffre(titre);
                    offre.setWilaya(selectedWilaya.split(" ")[1]);
                    offre.setCommune(selectedVille);
                    offre.setIdUser(preferenceUtils.getMember().getIdMembre());
                    offre.setStatu("CREATED");
                    offre.setImage(imguri.toString());
                    // khasni id user li dar l offre

                    databaseReference.child(String.valueOf(offre.getDateOffre().hashCode()) + offre.getAnnonceId().hashCode()).setValue(offre).addOnCompleteListener(task2 -> {/*
                setValue () -  Cette méthode prendra un objet de classe java modèle qui contiendra toutes les variables
                 à stocker dans la référence. La même méthode sera utilisée pour mettre à jour les valeurs car elle écrase
                 les données de la référence spécifiée.

                */
                        if (task2.isSuccessful()) {
                            setStatuAnnonce();
                            addNotification(annonce, offre);
                            Toast.makeText(this, "Votre offre a été soumise auec succès ", Toast.LENGTH_LONG).show();
                            Intent an = new Intent(AjoutOffre.this, debut.class);
                            startActivity(an);
                            finish();

                        } else {
                            Toast.makeText(this, "les donnees n'ont pas crées correctement", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(this, "Vous devez remplir les champs", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, " ajouter une photo pour votre offre", Toast.LENGTH_SHORT).show();
            }

        });

        // spinners

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readFileFromRawDirectory(R.raw.wilayas));
            wilayaname = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    wilaya.add(new Wilaya(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")), jsonArray.getJSONObject(i).getString("nom")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                wilayaname[i] = wilaya.get(i).getId() + " " + wilaya.get(i).getName();
            }
            ArrayAdapter<String> wilayaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, wilayaname);
            wilayaSpinner.setAdapter(wilayaAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONArray jsonArrayCommune = new JSONArray(readFileFromRawDirectory(R.raw.communes));
            for (int i = 0; i < jsonArrayCommune.length(); i++) {
                communes.add(new Commune(Integer.parseInt(jsonArrayCommune.getJSONObject(i).getString("id")), Integer.parseInt(jsonArrayCommune.getJSONObject(i).getString("wilaya_id")), jsonArrayCommune.getJSONObject(i).getString("nom")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        wilayaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedWilaya = parent.getItemAtPosition(position).toString();
                int selectedId = Integer.parseInt(selectedWilaya.subSequence(0, 2).toString().trim());
                ArrayList<Commune> communeSelected = new ArrayList<Commune>();
                for (int i = 0; i < communes.size(); i++)
                    if (selectedId == communes.get(i).getWilaya_id()) {
                        communeSelected.add(communes.get(i));
                    }
                String[] communeName = new String[communeSelected.size()];
                for (int i = 0; i < communeSelected.size(); i++)
                    communeName[i] = communeSelected.get(i).getName();
                ArrayAdapter<String> communeAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, communeName);
                villeSpinner.setAdapter(communeAdapter);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        villeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                selectedVille = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void addNotification(Annonce annonce, Offre offre) {
        data = FirebaseDatabase.getInstance().getReference("Notification").child(annonce.getUserId());
        Notification notification = new Notification();
        notification.setIdsender(offre.getIdUser());
        notification.setIdreceiver(annonce.getUserId());
        notification.setDateNotification(new Date());
        notification.setContenuNotification("sendOffre");
        notification.setIdNotification(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode());
        data.child(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode()).setValue(notification).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {


            }
        });


    }

    private void setStatuAnnonce() {
//statu majuc avec _ w gsira
        // ndiro kima hka wla kim ta3 yselectionne
        databasereference = FirebaseDatabase.getInstance().getReference("Annonce").child(idAnnonce);//child(statu).setvaleu.set aaces lisnter ta3 mchat w kayan whdkhra ta3 mamchtch
        Annonce annonce1 = new Annonce();
        annonce1.setArticleEnRetour(annonce.getArticleEnRetour());
        annonce1.setCommune(annonce.getCommune());
        annonce1.setDateAnnonce(annonce.getDateAnnonce());
        annonce1.setDescriptionAnnonce(annonce.getDescriptionAnnonce());
        annonce1.setIdAnnonce(annonce.getIdAnnonce());
        annonce1.setImages(annonce.getImages());
        annonce1.setStatu("ATTEND_DE_CONFIRMATION_D_OFFRE");
        annonce1.setTitreAnnonce(annonce.getTitreAnnonce());
        annonce1.setUserId(annonce.getUserId());
        annonce1.setIdOffreSelected(annonce.getIdOffreSelected());
        annonce1.setWilaya(annonce.getWilaya());
        databasereference.setValue(annonce1);
    }

    private String readFileFromRawDirectory(int resourceId) {
        InputStream iStream = getApplicationContext().getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toString();
    }


    // photo gallery or camera methodes
    public void choosePhotoFromGallary() {
        if (!checkExternalStorageWritePermission()) {
            resuestExternalStorageWritePermission();
        } else if (!checkExternalStorageREADPermission()) {
            resuestExternalStorageREADPermission();
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY);
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
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    imguri = Uri.parse(saveImage(bitmap));

                    Toast.makeText(getApplicationContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    //*******************
                    pic.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            //******************
            pic.setImageBitmap(thumbnail);
            imguri = Uri.parse(saveImage(thumbnail));
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
        Log.e("img here", imguri.toString());
        try {
            InputStream stream = new FileInputStream(String.valueOf(imguri));
            //StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());
            StorageReference ref = mStorageRef.child(UUID.randomUUID().toString());
            ref.putStream(stream)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(AjoutOffre.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(task -> {
                                    // offre.getImages().add(String.valueOf(task));
                                    path = String.valueOf(task);


                                }
                        );
                    })
                    .addOnFailureListener(e -> Toast.makeText(AjoutOffre.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                .getTotalByteCount());
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


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
        ActivityCompat.requestPermissions(AjoutOffre.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);
        Toast.makeText(this, "resuestCameraPermission", Toast.LENGTH_SHORT).show();
    }


    private void resuestExternalStorageWritePermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(AjoutOffre.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
        Toast.makeText(this, "resuest_WRITE_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
    }

    private void resuestExternalStorageREADPermission() {
        //request runtime camera permission
        ActivityCompat.requestPermissions(AjoutOffre.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        Toast.makeText(this, "resuest_READ_EXTERNAL_STORAGE", Toast.LENGTH_SHORT).show();
    }


}

