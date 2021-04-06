package com.madev.virtualwaitingroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}