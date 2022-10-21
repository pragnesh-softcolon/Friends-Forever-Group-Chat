package com.example.friendsforevergroupchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class chat_activity extends AppCompatActivity
{
    EditText editText;
    DBHelper db=null;
    Button sent;
    ListView lv;
    ProgressDialog pDialog;
    private DatabaseReference myDatabase;
    private String stringMessage;
    ArrayList <HashMap<String, String>> list =null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        myDatabase= FirebaseDatabase.getInstance().getReference("Message");
        db = new DBHelper(chat_activity.this);
        lv=findViewById(R.id.lv);
        sent=findViewById(R.id.sent);
        editText=findViewById(R.id.message);
        pDialog = new ProgressDialog(chat_activity.this);
        pDialog.setMessage("Starting App...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        try
        {
        myDatabase.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                stringMessage=snapshot.getValue().toString();
                stringMessage=stringMessage.substring(1,stringMessage.length()-1);
                String[] stringMessageArray = stringMessage.split(", ");
                list= new ArrayList<>();
                for(int i=0;i<stringMessageArray.length;i++)
                    {
                        String[] stringKeyvalue=stringMessageArray[i].split("=",2);
                        String date_time = stringKeyvalue[0];
                        String message = stringKeyvalue[1];
                        Log.e("Loop","date time : "+date_time);
                        Log.e("Loop","message : "+message);
                        HashMap map=new HashMap();
                        map.put("date_time",date_time);
                        map.put("message",message);
                        list.add(map);
                        Log.e("Loop","list : "+list);
                    }
                String[] from = {"message", "date_time"};
                int to[] = {R.id.msg, R.id.msg_time};
                SimpleAdapter simpleAdapter = new SimpleAdapter(chat_activity.this, list, R.layout.row, from, to);
                lv.setAdapter(simpleAdapter);
                pDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(chat_activity.this, "somethig is wrong", Toast.LENGTH_SHORT).show();
            }


        });
        }
        catch (Exception e)
        {
            Log.e("TAG","Error is "+e);
            pDialog.dismiss();
        }
        sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sentMessage(view);
            }
        });
    }
    public void sentMessage(View view)
    {
        Date dt = new Date();
        SimpleDateFormat dateFormat;
        dateFormat = new SimpleDateFormat("hh:mm a");
        Cursor c=db.getYourTableContents();
        c.moveToFirst();
        String name;
        do
        {
            name=c.getString(1);
        }while (c.moveToNext());
        String s1=dateFormat.format(dt);
        myDatabase.child(s1).setValue(name+" : "+editText.getText().toString());
        editText.setText("");
    }
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menudata, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        String str;
        str=item.getTitle().toString();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        if(str.equalsIgnoreCase("Logout"))
        {
            db.deleteContact();
            Intent log = new Intent(chat_activity.this, login.class);
            startActivity(log);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            finish();
        }
        return true;
    }
}