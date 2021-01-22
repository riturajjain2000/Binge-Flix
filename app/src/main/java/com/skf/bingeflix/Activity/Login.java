package com.skf.bingeflix.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.skf.bingeflix.R;

public class Login extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button login;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int RC_SIGN_IN = 1;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "Login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Email = findViewById(R.id.etName);
        Password= findViewById(R.id.etPassword);
        login = findViewById(R.id.btnLogin);
        final TextView userRegistration = findViewById(R.id.tvRegister);
        final SignInButtonImpl googlebutton = findViewById(R.id.googlebutton);





        firebaseAuth=FirebaseAuth.getInstance();



        progressDialog =new ProgressDialog(this);




        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        FirebaseUser user= firebaseAuth.getCurrentUser();
        updateUI(user);





        googlebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(Login.this,R.anim.blink_anim);
                googlebutton.startAnimation(animation);
                signIn();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check()){
                    validate(Email.getText().toString(),Password.getText().toString());}
                Animation animation = AnimationUtils.loadAnimation(Login.this,R.anim.blink_anim);
                login.startAnimation(animation);
            }
        });


        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
                Animation animation = AnimationUtils.loadAnimation(Login.this,R.anim.blink_anim);
                userRegistration.startAnimation(animation);

            }
        }); }



    private boolean check(){
        boolean result = false;

        String password= Password.getText().toString();
        String email=Email.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.requestFocus();
            Email.setSelected(true);
            Email.setError("Please enter a valid email address ");

        }
        else if(email.isEmpty() || password.isEmpty() ){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            result= true;

        }
        return result ;
    }

    private void updateUI(FirebaseUser user){

        {
            if (user==null){
                Toast.makeText(this, "Please Login ", Toast.LENGTH_SHORT).show();
            }
            else {
                startActivity(new Intent(Login.this, MainActivity.class));

            }



        }
    }







    private void validate( String userEmail, String userPassword){

        progressDialog.setMessage("Hey! By using this app you can find Great workspaces.");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    startActivity(new Intent(Login.this,MainActivity.class));
                    Toast.makeText(Login.this,"Welcome back", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(Login.this,"Invalid Email or Password", Toast.LENGTH_SHORT).show();

                }
            }
        });


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
                            Toast.makeText(Login.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
}

