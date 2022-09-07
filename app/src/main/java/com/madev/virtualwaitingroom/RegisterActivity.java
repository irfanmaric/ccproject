package com.madev.virtualwaitingroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.User;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    EditText et_name,et_surname,et_phoneNumber,et_password,et_dateBirth;
    Button btn_register;
    TextView tv_login;
    Uri saveUri;
    FirebaseDatabase database;
    DatabaseReference categories;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference table_user;
    CircleImageView userProfilePhoto,addProfilePhoto;
    Button btn_finish;
    String imageUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
         table_user = database.getReference("User");
         storage = FirebaseStorage.getInstance();
         storageReference = storage.getReference();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnectedToInternet(getBaseContext())){

                    addProfilePhoto();




            }else{
                    Toast.makeText(RegisterActivity.this,"Trenutno niste povezani na internet",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void addProfilePhoto() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
        alertDialog.setTitle("Učitajte sliku profila");



        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_profile_photo_layout,null);

        userProfilePhoto = add_menu_layout.findViewById(R.id.userProfilePhoto);
        addProfilePhoto = add_menu_layout.findViewById(R.id.addProfilePhoto);
        btn_finish = add_menu_layout.findViewById(R.id.btn_finish);

        alertDialog.setView(add_menu_layout);

        addProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();


            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });

        alertDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            saveUri = data.getData();
            Glide.with(getApplicationContext()).load(saveUri).into(userProfilePhoto);

        }
    }

    private void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Izaberite sliku"),Common.PICK_IMAGE_REQUEST);
    }

    private void uploadImage(){
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Učitavanje....");
            mDialog.show();

            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("Image/"+imageName);
            imageFolder.putFile(saveUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"Učitano!",Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                   // newCategory = new Category(edtName.getText().toString(),uri.toString());
                                    imageUrl = uri.toString();
                                    final ProgressDialog mDialog = new ProgressDialog(RegisterActivity.this);
                                    mDialog.setMessage("Molimo pričekajte...");
                                    mDialog.show();

                                    table_user.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if(dataSnapshot.child(et_phoneNumber.getText().toString()).exists())
                                            {
                                                mDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this,"",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                mDialog.dismiss();
                                                User user = new User(et_name.getText().toString(),et_surname.getText().toString(),et_password.getText().toString(),et_phoneNumber.getText().toString(),et_dateBirth.getText().toString(),imageUrl,"100");
                                                table_user.child(et_phoneNumber.getText().toString()).setValue(user);
                                                Toast.makeText(RegisterActivity.this,"Registracija uspješna, zaradile ste prvih 100 poena dobrodošlice!!",Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError)
                                        {

                                        }
                                    });

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mDialog.setMessage("Ucitano " + progress+"%");

                }
            });
        }
    }

    private void init() {
        et_name = (EditText)findViewById(R.id.et_name);
        et_surname = (EditText)findViewById(R.id.et_surname);
        et_phoneNumber = (EditText)findViewById(R.id.et_phoneNumber);
        et_password = (EditText)findViewById(R.id.et_password);
        et_dateBirth = (EditText)findViewById(R.id.et_dateBirth);

        btn_register = (Button)findViewById(R.id.btn_register);


    }
}