package com.example.friendsforevergroupchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity
{
    DBHelper db=null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHelper(MainActivity.this);
        Cursor yourCursor = db.getYourTableContents();
        int row = yourCursor.getCount();
        if (row > 0)
        {
            Intent profile = new Intent(MainActivity.this, chat_activity.class);
            startActivity(profile);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
        else
        {
            Intent log = new Intent(MainActivity.this, login.class);
            startActivity(log);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
    }
}