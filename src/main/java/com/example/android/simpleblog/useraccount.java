package com.example.android.simpleblog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.

 */
public class useraccount extends Fragment {


    private TextView setupName;
    private CircleImageView setupImage;
    private Uri mainImageURI = null;

    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private Button edit_button;
    private String user_id,current_user_id;
    // TODO: Rename parameter arguments, choose names that match

    public useraccount() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View mView = inflater.inflate(R.layout.fragment_useraccount, container, false);


        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();



        setupImage = mView.findViewById(R.id.setup_image);
        setupName = mView.findViewById(R.id.setup_name);
        edit_button = mView.findViewById(R.id.edit_profile);
        if (getArguments() != null) {
            user_id = getArguments().getString("blog_user_id");

        }
        if(current_user_id.equals(user_id)){
            edit_button.setVisibility(View.VISIBLE);
            edit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent setupIntent = new Intent(getActivity(), SetupActivity.class);
                    startActivity(setupIntent);
                }
            });

        }else{

            edit_button.setVisibility(View.INVISIBLE);
        }



        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");



                        mainImageURI = Uri.parse(image);

                        setupName.setText(name);

                        RequestOptions placeholderRequest = new RequestOptions();
                        placeholderRequest.placeholder(R.drawable.default_image);

                        Glide.with(getActivity()).setDefaultRequestOptions(placeholderRequest).load(image).into(setupImage);


                    }

                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), "(FIRESTORE Retrieve Error) : " + error, Toast.LENGTH_LONG).show();

                }

            }
        });


        return mView;


    }
}

