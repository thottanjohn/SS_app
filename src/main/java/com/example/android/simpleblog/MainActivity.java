package com.example.android.simpleblog;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    private String current_user_id;
    public Handler handler;
    public Runnable refresh;
    private DrawerLayout mDrawerLayout;

    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();


        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();





        Toolbar toolbar = findViewById(R.id.mtoolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.mipmap.action_navbar);



     if(mAuth.getCurrentUser() == null ){

         sendToLogin();
                }
         else {
try {
    current_user_id = mAuth.getCurrentUser().getUid();
}
catch (Exception e){
    sendToLogin();
}
            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();

                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();


                    }

                }
            });

            mDrawerLayout = findViewById(R.id.drawer_layout);
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


            if (savedInstanceState == null) {
                Class fragmentClass = main_fragment.class;
                try {
                    Fragment fragment = (Fragment) fragmentClass.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(
                    new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(MenuItem menuItem) {

                            Fragment fragment = null;
                            Class fragmentClass = null;
                            switch(menuItem.getItemId()){
                                case R.id.nav_home:
                                    fragmentClass = main_fragment.class;
                                    break;

                                case R.id.nav_about:
                                    fragmentClass = AboutFragment.class;
                                    break;
                                case R.id.nav_profile:
                                    Intent commentIntent = new Intent(MainActivity.this, NewAccountActivity.class);
                                    commentIntent.putExtra("userid", current_user_id);
                                    startActivity(commentIntent);

                                    break;
                                case R.id.nav_events:
                                    fragmentClass = Eventsfragment.class;
                                    break;
                                case R.id.nav_log_out:
                                    logOut();
                                    break;
                                 default:
                                     fragmentClass = main_fragment.class;


                            }
                            if (fragmentClass!=null) {
                                                              try {
                                    fragment = (Fragment) fragmentClass.newInstance();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                FragmentManager fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                            }
                            // Highlight the selected item has been done by NavigationView
                            menuItem.setChecked(true);
                            // Set action bar title

                            if(menuItem.getTitle().equals("My Profile")){

                            }else {
                                setTitle(menuItem.getTitle());
                            }
                            // set item as selected to persist highlight

                            // close drawer when item is tapped
                            mDrawerLayout.closeDrawers();



                            // Add code here to update the UI based on the item selected
                            // For example, swap UI fragments here

                            return true;
                        }
                    });

        }


    }



        //FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();







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
            Toast.makeText(MainActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    private void sendToLogin() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();

    }
}

