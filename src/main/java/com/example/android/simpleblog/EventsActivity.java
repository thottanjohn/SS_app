package com.example.android.simpleblog;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class EventsActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;


    public Handler handler;
    public Runnable refresh;
    private DrawerLayout mDrawerLayout;
    private Button enterbtn,explorebtn;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        handler = new Handler();
        enterbtn  = findViewById(R.id.btn_enter);
        explorebtn =  findViewById(R.id.btn_explore);




        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_home_black_24dp);



        if (mAuth.getCurrentUser() == null) {

            sendToLogin();

        } else {

       enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventsActivity.this, NewPostActivity.class));
            }
        });

            explorebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(EventsActivity.this, PageActivity.class));
                }
            });
            mDrawerLayout = findViewById(R.id.drawer_layout1);
            mDrawerLayout.addDrawerListener(
                    new DrawerLayout.DrawerListener() {
                        @Override
                        public void onDrawerSlide(View drawerView, float slideOffset) {
                            // Respond when the drawer's position changes
                        }

                        @Override
                        public void onDrawerOpened(View drawerView) {
                            // Respond when the drawer is opened
                        }

                        @Override
                        public void onDrawerClosed(View drawerView) {
                            // Respond when the drawer is closed
                        }

                        @Override
                        public void onDrawerStateChanged(int newState) {
                            // Respond when the drawer motion state changes
                        }
                    }
            );

            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {
                            menuItem.setChecked(true);
                            switch(menuItem.getItemId()){
                                case R.id.nav_home:
                                    startActivity(new Intent(EventsActivity.this, MainActivity.class));
                                    break;
                                case R.id.nav_about:
                                    startActivity(new Intent(EventsActivity.this, AboutActivity.class));
                                    break;
                                case R.id.nav_profile:
                                    startActivity(new Intent(EventsActivity.this, SetupActivity.class));
                                    break;
                                case R.id.nav_events:
                                    startActivity(new Intent(EventsActivity.this, EventsActivity.class));
                                    break;
                                case R.id.nav_log_out:
                                    logOut();
                                    break;
                                default:
                                    startActivity(new Intent(EventsActivity.this, EventsActivity.class));

                            }
                            // set item as selected to persist highlight
                            menuItem.setChecked(true);
                            // close drawer when item is tapped
                            mDrawerLayout.closeDrawers();



                            // Add code here to update the UI based on the item selected
                            // For example, swap UI fragments here

                            return true;
                        }
                    });

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh = new Runnable() {
            public void run() {
                // Do something
                handler.postDelayed(refresh, 5000);
            }
        };
        handler.post(refresh);


        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    }




    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut() {

        try {
            mAuth.signOut();

            sendToLogin();

        } catch (Exception e) {
            Toast.makeText(EventsActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(EventsActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }
}