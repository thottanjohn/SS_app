package com.example.android.simpleblog;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class Chat extends AppCompatActivity {


        private FirebaseAuth mAuth;
        private Toolbar mToolbar;

        private ViewPager mViewPager;
    private FirebaseFirestore firebaseFirestore;

        private DatabaseReference mUserRef;

        private TabLayout mTabLayout;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            mAuth = FirebaseAuth.getInstance();

            firebaseFirestore = FirebaseFirestore.getInstance();
            mToolbar =findViewById(R.id.main_page_toolbar);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle("Lapit Chat");

            if (mAuth.getCurrentUser() != null) {


                mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

            }


            //Tabs
            mViewPager =  findViewById(R.id.main_tabPager);
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            mViewPager.setAdapter(mSectionsPagerAdapter);

            mTabLayout =  findViewById(R.id.main_tabs);
            mTabLayout.setupWithViewPager(mViewPager);




        }

        @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser == null){

                sendToStart();

            } else {

                mUserRef.child("online").setValue("true");

            }

        }


        @Override
        protected void onStop() {
            super.onStop();

            FirebaseUser currentUser = mAuth.getCurrentUser();

            if(currentUser != null) {

                mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

            }

        }

        private void sendToStart() {

            Intent startIntent = new Intent(Chat.this, MainActivity.class);
            startActivity(startIntent);
            finish();

        }


        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            super.onCreateOptionsMenu(menu);

            getMenuInflater().inflate(R.menu.main_menu, menu);


            return true;
        }




}
