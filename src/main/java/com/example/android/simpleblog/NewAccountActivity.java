package com.example.android.simpleblog;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class NewAccountActivity extends AppCompatActivity   {

    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private String user_id;

    private boolean isChanged = false;

    private TextView setupName;


    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private Bitmap compressedImageFile;
    private BottomNavigationView mainsideNav;

    private PostFragment PostFragment;
    private StatsFragment StatsFragment;
    private useraccount  useraccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        Toolbar setupToolbar = findViewById(R.id.accountToolbar);
        setSupportActionBar(setupToolbar);

        getSupportActionBar().setTitle("Account Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();

        try {
            user_id = getIntent().getStringExtra("userid");
        }
        catch (Exception e){
            sendToLogin();

        }


        mainsideNav = findViewById(R.id.mainsideNav);

        Bundle bundle = new Bundle();
        bundle.putString("blog_user_id", user_id);
        // FRAGMENTS
        PostFragment = new PostFragment();
        StatsFragment =new StatsFragment();
        useraccount =new  useraccount();



        initializeFragment();
        PostFragment.setArguments(bundle);
        StatsFragment.setArguments(bundle);
        useraccount.setArguments(bundle);
        mainsideNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_user_container);

                switch (item.getItemId()) {

                    case R.id.bottom_action_posts:

                        replaceFragment(PostFragment, currentFragment);
                        return true;



                    case R.id.bottom_action_stats:

                        replaceFragment(StatsFragment, currentFragment);
                        return true;

                    case R.id.bottom_action_account:

                        replaceFragment(useraccount, currentFragment);
                        return true;

                    default:
                        return false;


                }

            }
        });






    }
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    private void initializeFragment(){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(R.id.main_user_container, PostFragment);
        fragmentTransaction.add(R.id.main_user_container, StatsFragment);
        fragmentTransaction.add(R.id.main_user_container, useraccount);


        fragmentTransaction.hide(StatsFragment);
        fragmentTransaction.hide(PostFragment);


        fragmentTransaction.commit();

    }


    private void replaceFragment(Fragment fragment, Fragment currentFragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(fragment == PostFragment){


            fragmentTransaction.hide(StatsFragment);
            fragmentTransaction.hide(useraccount);

        }



        if(fragment == StatsFragment){

            fragmentTransaction.hide(PostFragment);
            fragmentTransaction.hide(useraccount);


        }
        if(fragment == useraccount){

            fragmentTransaction.hide(PostFragment);
            fragmentTransaction.hide(StatsFragment);


        }
        fragmentTransaction.show(fragment);

        //fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }



    private void sendToLogin() {

        Intent loginIntent = new Intent(NewAccountActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }

}

