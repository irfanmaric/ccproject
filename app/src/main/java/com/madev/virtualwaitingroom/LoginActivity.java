package com.madev.virtualwaitingroom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.madev.virtualwaitingroom.Common.Common;
import com.madev.virtualwaitingroom.Model.User;

public class LoginActivity extends AppCompatActivity {

    TextView tv_register;
    EditText et_phoneNumber,et_password;
    Button btn_signIn;
    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();




        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        database = FirebaseDatabase.getInstance();
        table_user= database.getReference("User");

        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(Common.isConnectedToInternet(getBaseContext())){
                        /*if(checkBox.isChecked()){
                            Paper.book().write(Common.USER_KEY,edtPhone.getText().toString());
                            Paper.book().write(Common.PWD_KEY,edtPassword.getText().toString());

                        }*/

                        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                        mDialog.setMessage("Molimo pričekajte...");
                        mDialog.show();

                        table_user.addValueEventListener(new ValueEventListener() {



                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(et_phoneNumber.getText().toString()).exists()) {

                                    mDialog.dismiss();

                                    User user = dataSnapshot.child(et_phoneNumber.getText().toString()).getValue(User.class);
                                    user.setPhone(et_phoneNumber.getText().toString());


                                    if(et_password.getText().toString().isEmpty())
                                        Toast.makeText(LoginActivity.this, "Niste unijeli podatke !", Toast.LENGTH_SHORT).show();

                                    else if (user.getPassword().equals(et_password.getText().toString()))
                                    {
                                        Intent homeIntent = new Intent(LoginActivity.this,Home.class);
                                        Common.currentUser=user;
                                        startActivity(homeIntent);
                                        finish();

                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this, "Pogrešna šifra !", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    mDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Korisnik ne postoji", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("TAG",databaseError.getMessage());
                            }
                        });
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Trenutno niste povezani na internet",Toast.LENGTH_SHORT).show();
                        return;
                    }



            }
        });
    }

    private void init() {
        tv_register = (TextView)findViewById(R.id.tv_register);
        et_phoneNumber = (EditText)findViewById(R.id.et_phoneNumber);
        et_password = (EditText)findViewById(R.id.et_password);
        btn_signIn = (Button)findViewById(R.id.btn_signIn);


    }
}