package com.bouchra.myapplicationechange.activities;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bouchra.myapplicationechange.R;
import com.bouchra.myapplicationechange.adapters.BootomSheetDialogCamGall;
import com.bouchra.myapplicationechange.adapters.RecycleViewArticleRetour;
import com.bouchra.myapplicationechange.adapters.myImage;
import com.bouchra.myapplicationechange.models.Annonce;
import com.bouchra.myapplicationechange.models.Notification;
import com.bouchra.myapplicationechange.models.Offre;
import com.bouchra.myapplicationechange.utils.PreferenceUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import java.util.Date;
import java.util.UUID;

public class ModifierAnnonce extends AppCompatActivity {
    private Annonce annonce;

    private EditText nomAnnonce;
    private ImageView imgAnnonc;
    private EditText desciAnnonce;
    private EditText editText;
    private TextView enregister;
    private TextView textView;
    private RecyclerView recyclerView;
    private RecycleViewArticleRetour postAdapter;
    ArrayList<String> posts = new ArrayList<>();
    private boolean notify = false;

    private DatabaseReference data;

    StorageReference mStorageRef;
    private ArrayList<String> list = new ArrayList<>();
    PreferenceUtils preferenceUtils;
    private int GALLERY = 1, CAMERA = 3, CAMERA_PERMISSION = 2, WRITE_EXTERNAL_STORAGE = 4, READ_EXTERNAL_STORAGE = 5;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private ArrayList<Uri> listImages;
    private ArrayList<Uri> Images;
    private com.bouchra.myapplicationechange.adapters.myImage myImage;
    RecyclerView recyclerViewImages;
    public Uri imguri;
    private ArrayList<String> paths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_annonce);
        annonce = (Annonce) getIntent().getSerializableExtra("annonce");
        nomAnnonce = findViewById(R.id.nom_annonce);
        imgAnnonc = findViewById(R.id.img_annonc);
        desciAnnonce = findViewById(R.id.desci_annonce);
        editText = findViewById(R.id.edittxt_article);
        textView = findViewById(R.id.ajout_article);
        enregister = findViewById(R.id.enregister);
        recyclerView = findViewById(R.id.rec_retour);
        preferenceUtils = new PreferenceUtils(this);
        recyclerViewImages = findViewById(R.id.recycleImages);
        //StorageReference Firebase
        mStorageRef = FirebaseStorage.getInstance().getReference("Images Annonce");

        listImages = new ArrayList<>();
        Images=new ArrayList<>();

        Button buttonOpenBottomSheet = findViewById(R.id.button_sheet);
        buttonOpenBottomSheet.setOnClickListener(v -> {
            if (listImages.size() <= 5) {
               /* BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
                bottomsheet.show(getSupportFragmentManager(), "exemplBottomsheet");*/
                BootomSheetDialogCamGall bottomsheet = new BootomSheetDialogCamGall();
                Bundle bundle = new Bundle();
                bundle.putString("linkModifierannonce", "fromModifierannonce");
                bottomsheet.setArguments(bundle);
                bottomsheet.show((this).getSupportFragmentManager(), "Image Dialog");
            } else {
                Toast.makeText(this, "Vous ne pouvez pas ajouter d'autres photos ", Toast.LENGTH_SHORT).show();
            }

        });


        textView.setOnClickListener(v -> {
            ajoutArticle();
        });

        //UNPACK OUR DATA FROM OUR BUNDLE
      /* Bundle b3 = getArguments();
        String name = b3.getString("name");
       String titre = this.getArguments().getString("nomannonce");*/

        nomAnnonce.setText(annonce.getTitreAnnonce());
        desciAnnonce.setText(annonce.getDescriptionAnnonce());

        int j;
        for (j = 0; j < annonce.getArticleEnRetour().size(); j++) {
            String article = annonce.getArticleEnRetour().get(j);
            Log.e("annoce une is", article);
            posts.add(article);
            editText.setText("");
            postAdapter = new RecycleViewArticleRetour(this, posts);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(postAdapter);
        }
        for (int i = 0; i < annonce.getImages().size(); i++) {
            listImages.add(Uri.parse(annonce.getImages().get(i)));
            myImage = new myImage(this, listImages);
            // recycle view horizontal
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerViewImages.setLayoutManager(linearLayoutManager);
            recyclerViewImages.setAdapter(myImage);
        }

        //myImage.notifyDataSetChanged();
        /*Glide.with(this)
                .asBitmap()
                .load(annonce.getImages().get(0))
                .into(imgAnnonc);*/
     /*   if (annonce.getImages().size()<= 5) {
            listImages.add(Uri.parse(saveImage(bitmap)));
            myImage.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Vous ne pouvez pas ajouter d'autres photos ", Toast.LENGTH_SHORT).show();
        }*/
        enregister.setOnClickListener(v -> {

            if (listImages.size() != 0) {

                Fileuploader();
                updateAnnonce();

            } else {
                Toast.makeText(this, "Ajouter des images a votre annonce ", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updateAnnonce() {
        String titre_Annonce = nomAnnonce.getText().toString();
        String desc_Annonce = desciAnnonce.getText().toString();

        if (!titre_Annonce.isEmpty() && !desc_Annonce.isEmpty()) {
            ////getting the specified artist reference
            DatabaseReference refannonce = FirebaseDatabase.getInstance().getReference("Annonce").child(annonce.getIdAnnonce());
            Annonce ann = new Annonce();
            //updating annonce
            ann.setDescriptionAnnonce(desc_Annonce);//c bn
            ann.setTitreAnnonce(titre_Annonce);// c bn
            ann.setDateAnnonce(annonce.getDateAnnonce());
            ann.setStatu(annonce.getStatu());
            ann.setUserId(annonce.getUserId());
            ann.setIdAnnonce(annonce.getIdAnnonce());
            ann.setIdOffreSelected(annonce.getIdOffreSelected());
            // ann.setImages(annonce.getImages());
            ann.setImages(paths);
            ann.setCommune(annonce.getCommune());////////methode static
            ann.setWilaya(annonce.getWilaya());////////
            ann.setArticleEnRetour(posts);
            refannonce.setValue(ann)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (!annonce.getStatu().equals("NEED_To_Be_CONFIRM") && !annonce.getStatu().equals("NEED_REVIEW") && !annonce.getStatu().equals("COMPLETED")) {
                                    getOffres(annonce);

                                }

                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(ModifierAnnonce.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


        }
    }

    private void getOffres(Annonce annonce) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Offre").child(annonce.getIdAnnonce());
       /* PreferenceUtils preferenceUtils;
        preferenceUtils = new PreferenceUtils(getContext());*/
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Offre offre = postSnapshot.getValue(Offre.class);

                    addNotification(annonce, offre);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Getting model failed, log a message
            }
        });
    }

    // nrsl l mol l offre balo badal f annonce
    private void addNotification(Annonce annonce, Offre offre) {
        data = FirebaseDatabase.getInstance().getReference("Notification").child(offre.getIdUser());
        Notification notification = new Notification();
        notification.setIdsender(annonce.getUserId());
        notification.setIdreceiver(offre.getIdUser());
        notification.setDateNotification(new Date());
        notification.setContenuNotification("updateAnnonce");
        notification.setIdNotification(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode());
        data.child(String.valueOf(offre.getIdOffre().hashCode()) + annonce.getIdAnnonce().hashCode()).setValue(notification).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {


            }
        });


    }


    public void ajoutArticle() {
        String x = editText.getText().toString();
        if (posts.size() < 10) {
            if (!x.isEmpty()) {
                posts.add(x);
                editText.setText("");
                postAdapter = new RecycleViewArticleRetour(this, posts);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(postAdapter);


            } else {
                Toast.makeText(this, "remplir le champs", Toast.LENGTH_SHORT).show();
            }


        } else {
            editText.setEnabled(false);
            editText.setText("");
            Toast.makeText(this, "Dèsolè, il n'ya pas pour ajouter plus d'article", Toast.LENGTH_SHORT).show();
        }// vous avez atteint la limite des postes possible

    }

    // photo gallery or camera methodes
    public void choosePhotoFromGallary() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);


    }

    public void takePhotoFromCamera() {

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);

    }





    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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


    private void Fileuploader() {
        paths.clear();
        for (Uri uri : listImages) {
            imguri = uri;
            paths.add(String.valueOf(imguri));
            Log.e("img here", imguri.toString());
            try {
                InputStream stream = new FileInputStream(String.valueOf(imguri));
                //StorageReference ref = mStorageRef.child("images/" + UUID.randomUUID().toString());
                StorageReference ref = mStorageRef.child(UUID.randomUUID().toString());
                ref.putStream(stream)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show();
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(task -> {




                                    }
                            );
                        })
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show())
                        .addOnProgressListener(taskSnapshot -> {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
                        takePhotoFromCamera();

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
                        choosePhotoFromGallary();

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






