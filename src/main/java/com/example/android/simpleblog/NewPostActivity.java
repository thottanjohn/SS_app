package com.example.android.simpleblog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import id.zelory.compressor.Compressor;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar newPostToolbar;

    private ImageView newPostImage;
    private EditText newPostDesc;
    private Button newPostBtn;

    private Uri postImageUri = null;

    private ProgressBar newPostProgress;


    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String current_user_id;
    private String event_name;
    private boolean over;
    private Bitmap compressedImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();

        newPostToolbar = findViewById(R.id.new_post_toolbar);
        setSupportActionBar(newPostToolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newPostImage = findViewById(R.id.new_post_image);
        newPostDesc = findViewById(R.id.new_post_desc);
        newPostBtn = findViewById(R.id.post_btn);
        newPostProgress = findViewById(R.id.mprogress);
        event_name = getIntent().getStringExtra("event_name");
        over = getIntent().getBooleanExtra("over",false);
        if (over) {
            Intent newIntent = new Intent(NewPostActivity.this, MainActivity.class);

            startActivity(newIntent);
        } else {

            newPostImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.OFF)
                            .setMinCropResultSize(512, 512)
                            .setAspectRatio(1, 1)
                            .start(NewPostActivity.this);

                }
            });

            newPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    final String desc = newPostDesc.getText().toString();

                    if (!TextUtils.isEmpty(desc) && postImageUri != null) {


                        newPostProgress.setVisibility(View.VISIBLE);
                        newPostProgress.setClickable(false);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        final String randomName = UUID.randomUUID().toString();

                        // PHOTO UPLOAD
                        File newImageFile = new File(postImageUri.getPath());
                        try {

                            compressedImageFile = new Compressor(NewPostActivity.this)
                                    .setMaxHeight(720)
                                    .setMaxWidth(720)
                                    .setQuality(50)
                                    .compressToBitmap(newImageFile);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageData = baos.toByteArray();

                        // PHOTO UPLOAD

                        UploadTask filePath = storageReference.child(event_name + "post_images").child(randomName + ".jpg").putBytes(imageData);
                        filePath.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {

                                final String downloadUri = task.getResult().getDownloadUrl().toString();

                                if (task.isSuccessful()) {

                                    File newThumbFile = new File(postImageUri.getPath());
                                    try {

                                        compressedImageFile = new Compressor(NewPostActivity.this)
                                                .setMaxHeight(100)
                                                .setMaxWidth(100)
                                                .setQuality(1)
                                                .compressToBitmap(newThumbFile);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                    byte[] thumbData = baos.toByteArray();

                                    UploadTask uploadTask = storageReference.child(event_name + "post_images/thumbs")
                                            .child(randomName + ".jpg").putBytes(thumbData);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            final String downloadthumbUri = taskSnapshot.getDownloadUrl().toString();

                                            final Map<String, Object> postMap = new HashMap<>();
                                            // Map<String, Object> userPostMap = new HashMap<>();

                                            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                    if (task.isSuccessful()) {

                                                        String username = task.getResult().getString("name");
                                                        String userimage = task.getResult().getString("image");
                                                        postMap.put("image_url", downloadUri);
                                                        postMap.put("image_thumb", downloadthumbUri);
                                                        postMap.put("desc", desc);
                                                        postMap.put("user_id", current_user_id);
                                                        postMap.put("timestamp", FieldValue.serverTimestamp());
                                                        postMap.put("likes", 0);
                                                        postMap.put("total", 0);
                                                        postMap.put("user_name", username);
                                                        postMap.put("user_image", userimage);
                                                        postMap.put("event_name", event_name);
                                                        firebaseFirestore.collection(event_name + "Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                                                if (task.isSuccessful()) {

                                                                    Toast.makeText(NewPostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                                                    Intent mainIntent = new Intent(NewPostActivity.this, MainActivity.class);
                                                                    startActivity(mainIntent);
                                                                    finish();

                                                                } else {


                                                                }

                                                                newPostProgress.setVisibility(View.INVISIBLE);
                                                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                            }
                                                        });
                                                    } else {

                                                        //Firebase Exception

                                                    }

                                                }
                                            });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            //Error handling

                                        }
                                    });


                                } else {

                                    newPostProgress.setVisibility(View.INVISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                }

                            }
                        });


                    } else {

                        Toast.makeText(NewPostActivity.this, "Please enter a valid image description", Toast.LENGTH_LONG).show();

                    }

                }
            });


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                postImageUri = result.getUri();
                newPostImage.setImageURI(postImageUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }

    }

}
