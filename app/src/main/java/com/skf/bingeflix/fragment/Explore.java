package com.skf.bingeflix.fragment;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skf.bingeflix.Activity.MovieplayerActivity;
import com.skf.bingeflix.Activity.PersonalInfo;
import com.skf.bingeflix.Adapters.MovieAdapter;
import com.skf.bingeflix.Adapters.MovieItemClickListener;
import com.skf.bingeflix.Models.Movie;
import com.skf.bingeflix.Activity.MovieDetailActivity;
import com.skf.bingeflix.R;
import com.skf.bingeflix.Models.Slide;
import com.skf.bingeflix.Adapters.SlidePagerAdapter;
import com.skf.bingeflix.utils.DataSource;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class Explore extends Fragment implements MovieItemClickListener {

    private EditText searchview;
    private List<Slide> lstSlides;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private TextView tv;
    boolean e=false,h=false,o=false;
    private RecyclerView MoviesRV,popular;


    private CollectionReference firebaseFirestore = FirebaseFirestore.getInstance().collection("Hosts");

    public Explore() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lstSlides = new ArrayList<>();
        lstSlides.add(new Slide(R.drawable.kgf, "KGF: Chapter 2"));
        lstSlides.add(new Slide(R.drawable.batman, "Batman Begins"));
        lstSlides.add(new Slide(R.drawable.superman, "Man of Steel"));
        lstSlides.add(new Slide(R.drawable.wolverine, "Wolverine"));
        lstSlides.add(new Slide(R.drawable.avengers, "Avengers"));
        sliderpager = view.findViewById(R.id.slider_pager);
        sliderpager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MovieplayerActivity.class));
            }
        });

        MoviesRV = view.findViewById(R.id.Rv_movies);
        popular = view.findViewById(R.id.P_movies);
        indicator = view.findViewById(R.id.indicator);
        tv=view.findViewById(R.id.popular_tv);

        MovieAdapter movieAdapter = new MovieAdapter(getContext(), DataSource.getpopularMovies(),this);
        MoviesRV.setAdapter(movieAdapter);
        MoviesRV.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        MovieAdapter popularAdapter = new MovieAdapter(getContext(), DataSource.getlanguageMovies(),this);
        popular.setAdapter(popularAdapter);
        popular.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user= firebaseAuth.getCurrentUser();
        String name = user != null ? user.getDisplayName() : "User";
        String uid = Objects.requireNonNull(user).getUid();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(name+" "+ uid);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    try {
                        h=documentSnapshot.getBoolean("Hindi");
                        e=documentSnapshot.getBoolean("English");
                        o=documentSnapshot.getBoolean("Others");
                    }catch (Exception ignored){}

                }
                else
                {
                    Toast.makeText(getContext(), "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity",e.toString());

            }
        });

        if (e){
            tv.setText("Popular in English");
        }
        else if (h){
            tv.setText("Popular in Hindi");
        }
        else if (o){
            tv.setText("Other Popular Movies");
        }
        else
        if (e && h || e && h&& o){
            tv.setText("Popular in Hindi And English");
        }
        else
            tv.setText("Popular this week");

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),2000,4000);
        indicator.setupWithViewPager(sliderpager,true);

        searchview = view.findViewById(R.id.search_field);
        searchview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //searchUser(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        sliderpager.setAdapter(new SlidePagerAdapter(getContext(), lstSlides));


    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {

        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        // send movie information to deatilActivity
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("imgURL",movie.getThumbnail());
        intent.putExtra("imgCover",movie.getCoverPhoto());
        // lets crezte the animation
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(),
                movieImageView,"sharedName");

        startActivity(intent,options.toBundle());


    }

    class SliderTimer extends TimerTask {


        @Override
        public void run() {try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem()<lstSlides.size()-1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1);
                    }
                    else
                        sliderpager.setCurrentItem(0);
                }
            });
        }catch (Exception ignored){}




        }
    }


}
