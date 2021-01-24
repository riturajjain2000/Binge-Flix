package com.skf.bingeflix.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.skf.bingeflix.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfo extends AppCompatActivity {

    EditText etname, etemail, etgender, etphone, etdob;
    CircleImageView profilepic;
    ImageView close;
    TextView save, changepic;
    Uri filepath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);


        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        etname = findViewById(R.id.NAMEE);
        close = findViewById(R.id.close);
        etemail = findViewById(R.id.EMAILE);
        etgender = findViewById(R.id.etgender);
        etphone = findViewById(R.id.etphone);
        etdob = findViewById(R.id.etdob);
        save = findViewById(R.id.save);
        profilepic = findViewById(R.id.profile_image);
        changepic = findViewById(R.id.changepic);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(PersonalInfo.this, R.anim.blink_anim);
                save.startAnimation(animation);

                updateProfile();
            }


        });


        changepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(PersonalInfo.this, R.anim.text_anim);
                changepic.startAnimation(animation);

                ChooseImage();

            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Animation animation = AnimationUtils.loadAnimation(PersonalInfo.this, R.anim.blink_anim);
                close.startAnimation(animation);

            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        String name = user != null ? user.getDisplayName() : "User";
        String uid = Objects.requireNonNull(user).getUid();
        DocumentReference documentReference = db.collection("Users").document(name + " " + uid);


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String tName = documentSnapshot.getString("Name");
                    etname.setText(tName);


                    String tEmail = documentSnapshot.getString("Email");
                    etemail.setText(tEmail);


                    String tGender = documentSnapshot.getString("Gender");
                    if (!Objects.equals(tGender, "")) {
                        etgender.setText(tGender);
                    }


                    String tDOB = documentSnapshot.getString("Date of Birth");
                    if (!Objects.equals(tDOB, "")) {
                        etdob.setText(tDOB);
                    }


                    String tPhone = documentSnapshot.getString("Phone no ");
                    if (!Objects.equals(tPhone, "")) {
                        etphone.setText(tPhone);
                    }

                    String tImage = documentSnapshot.getString("Profile");
                    if (!Objects.equals(tImage, "")) {
                        Picasso.get().load(tImage).into(profilepic);
                    }
                } else {
                    Toast.makeText(PersonalInfo.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonalInfo.this, "Error", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", e.toString());

            }
        });
    }


    private void ChooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), 1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK

                && data != null) {

            filepath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                profilepic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


    private void updateProfile() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();


        String name = etname.getText().toString();
        String gender = etgender.getText().toString();
        String dob = etdob.getText().toString();
        assert user != null;
        String uid = user.getUid();
        String phone = etphone.getText().toString();

        final Map<String, Object> info = new HashMap<>();
        info.put("Name", name);
        info.put("Gender", gender);
        info.put("Date of Birth", dob);
        info.put("username", name);


        if (!Patterns.PHONE.matcher(phone).matches()) {
            etphone.requestFocus();
            etphone.setSelected(true);
            etphone.setError("Please enter a valid Phone Number");

        } else {
            info.put("Phone no ", phone);
        }


        final StorageReference reference = storageReference.child("Uploads/Profile Pic/" + name + " " + uid);

        if (filepath != null) {

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading....");
            progressDialog.show();


            reference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();


                    Toast.makeText(PersonalInfo.this, "Personal Info Updated!", Toast.LENGTH_SHORT).show();

                    finish();


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded " + (int) progress + " %");


                }
            });


        }


        Task<Uri> urlTask = reference.getDownloadUrl();
        while (!urlTask.isSuccessful()) ;
        Uri downloadUrl = urlTask.getResult();

        final String Downurl = String.valueOf(downloadUrl);

        info.put("Profile", Downurl);
        info.put("imageURL", Downurl);
        info.put("id", user.getUid());

        firebaseDatabase.child(uid).setValue(info);

        db.collection("Users").document(name + " " + uid).set(info, SetOptions.merge());


    }
}
