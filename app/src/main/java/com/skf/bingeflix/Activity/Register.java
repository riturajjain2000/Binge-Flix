package com.skf.bingeflix.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skf.bingeflix.R;

import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText userPassword,userEmail;
    private FirebaseAuth firebaseAuth;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseFirestore db;
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_register);
        userPassword=findViewById(R.id.etPassword);
        userEmail=findViewById(R.id.etName);
        final Button regButton = findViewById(R.id.btnRegister);
        TextView userLogin = findViewById(R.id.tvUserLogin);
        final SignInButtonImpl googlebutton = findViewById(R.id.googlebutton);


        firebaseAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        final FirebaseUser user= firebaseAuth.getCurrentUser();
        updateUI(user);

        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Register.this,R.anim.blink_anim);
                googlebutton.startAnimation(animation);
                signIn();
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Animation animation = AnimationUtils.loadAnimation(Register.this,R.anim.blink_anim);
                regButton.startAnimation(animation);




                if( validate() ){
                    final String user_email=userEmail.getText().toString().trim();
                    String user_password=userPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password ).addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){






                                        Objects.requireNonNull(firebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Register.this, "Verification Link Sent!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(Register.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register.this,MainActivity.class));

                                    }else{
                                        Toast.makeText(Register.this,"Already a user",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        } );

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Register.this,Login.class));


            }
        });
    }

    private void updateUI(FirebaseUser user){

        {
            if (user==null){
                Toast.makeText(this, "Please Register ", Toast.LENGTH_SHORT).show();
            }
            else {
                startActivity(new Intent(Register.this, MainActivity.class));

            }



        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                assert account != null;
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private Boolean validate(){
        boolean result = false;

        String password=userPassword.getText().toString();
        String email=userEmail.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userEmail.requestFocus();
            userEmail.setSelected(true);
            userEmail.setError("Please enter a valid email address ");

        }
        else if(email.isEmpty() || password.isEmpty() ){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }
        else{
            result= true;

        }
        return result ;


    }



}
