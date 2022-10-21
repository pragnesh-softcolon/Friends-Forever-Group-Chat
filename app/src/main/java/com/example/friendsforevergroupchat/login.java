package com.example.friendsforevergroupchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity
{
    TextInputEditText email,password;
    Button button;
    DBHelper db=null;
    ProgressDialog pDialog;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DBHelper(login.this);
        email=findViewById(R.id.ed_mail);
        password=findViewById(R.id.ed_password);
        button=findViewById(R.id.btn_signIn);
        mAuth=FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(email.getText().toString().equals(""))
                {
                    Toast.makeText(login.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
//                    email.setError("Enter Email first");
                }
                else if(password.getText().toString().equals(""))
                {
                    Toast.makeText(login.this, "Enter Valid Password", Toast.LENGTH_SHORT).show();
//                    password.setError("Enter password first");
                }
                else
                {
                    onLogin(view);
                    pDialog = new ProgressDialog(login.this);
                    pDialog.setMessage("Logging...");
                    pDialog.setIndeterminate(false);
                    pDialog.setCancelable(true);
                    pDialog.show();
                }
            }
        });

    }
    public void onLogin(View view)
    {
        final String myEmail=email.getText().toString();
        final String myPassword=password.getText().toString();
        mAuth.signInWithEmailAndPassword(myEmail,myPassword).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            pDialog.dismiss();
                            Log.e("TAG","Sign in with Email : Success");
                            String str=email.getText().toString();
                            String[] arrOfStr = str.split("@", 2);
                            String name=arrOfStr[0];
                            Toast.makeText(login.this, "Name is "+name, Toast.LENGTH_SHORT).show();
                            Intent chat=new Intent(login.this,chat_activity.class);
                            startActivity(chat);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            finish();
                            Toast.makeText(login.this, "Aapaka swagat he....", Toast.LENGTH_SHORT).show();
                            db.insertContact(""+name);
                        }
                        else
                        {
                            pDialog.dismiss();
                            Log.e("TAG","sign in with email : failuer",task.getException());
                            Toast.makeText(login.this, "Emain or Password is invalid", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}