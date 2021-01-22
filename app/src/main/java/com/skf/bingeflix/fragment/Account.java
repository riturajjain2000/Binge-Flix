package com.skf.bingeflix.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.skf.bingeflix.Activity.Login;
import com.skf.bingeflix.Activity.MainActivity;
import com.skf.bingeflix.Activity.PersonalInfo;
import com.skf.bingeflix.R;
import com.skf.bingeflix.Activity.TermsActivity;
import com.squareup.picasso.Picasso;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class Account extends Fragment {
    private TextView username;
    private CircleImageView profilepic;
    private Button language, done;
    private CheckBox c1, c2, c3;
    private Boolean h = false, e = false, o = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        TextView tv = view.findViewById(R.id.logout);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        username = view.findViewById(R.id.profileName);

        language = view.findViewById(R.id.language_button);
        Button persInfo = view.findViewById(R.id.persInfo);
        Button terms = view.findViewById(R.id.terms);
        profilepic = view.findViewById(R.id.profile_image);

        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


        persInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PersonalInfo.class));
            }
        });
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TermsActivity.class));
            }
        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), Login.class));

            }
        });
        String name = user != null ? user.getDisplayName() : "User";
        String uid = Objects.requireNonNull(user).getUid();
        DocumentReference documentReference = db.collection("Users").document(name + " " + uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String Name = documentSnapshot.getString("Name");
                    username.setText("Hi, " + Name);

                    String tImage = documentSnapshot.getString("Profile");
                    if (!Objects.equals(tImage, "")) {
                        Picasso.get().load(tImage).into(profilepic);
                    }

                } else {
                    Toast.makeText(getActivity(), "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", e.toString());

            }
        });
        return view;
    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.language_1:
                h= checked;

                break;
            case R.id.language_2:
                e = checked;

                break;

            case R.id.language_3:
                o= checked;

                break;

        }
    }
    public void showDialog() {


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String name = user != null ? user.getDisplayName() : "User";
        String uid = Objects.requireNonNull(user).getUid();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(name + " " + uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    h = documentSnapshot.getBoolean("Hindi");
                    e = documentSnapshot.getBoolean("English");
                    o = documentSnapshot.getBoolean("Others");
                } else {
                    Toast.makeText(getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", e.toString());

            }
        });


        final Dialog builder = new Dialog(Objects.requireNonNull(getContext()));
        builder.setCancelable(true);
        builder.setContentView(R.layout.dialog_choose_language);
        builder.show();
        done = builder.findViewById(R.id.button_done);
        c1 = builder.findViewById(R.id.language_1);
        c2 = builder.findViewById(R.id.language_2);
        c3 = builder.findViewById(R.id.language_3);

        if (h) {
            c1.setChecked(true);
        }
        if (e) {
            c2.setChecked(true);
        }
        if (o) {
            c3.setChecked(true);
        }



        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showStartDialog();
                builder.dismiss();

            }
        });
    }

    private void showStartDialog() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String name = user != null ? user.getDisplayName() : "User";
        assert user != null;
        String uid = user.getUid();
        Map<String, Object> info = new HashMap<>();
        info.put("Hindi", h);
        info.put("English", e);
        info.put("Others", o);
        String userid = user.getUid();
        DatabaseReference ref;
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        ref.setValue(info);

        db.collection("Users").document(name + " " + uid).set(info, SetOptions.merge());


    }


}
