package com.example.tlozano.androidlabs2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.asus.androidlabs.R;

public class LoginActivity extends Activity {
    protected static final String ACTIVITY_NAME="LoginActivity";
    EditText userInput;
    EditText passwordInput;

    public Button button2;

    public void init() {
        button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v){

                saveInfo();
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate");

       userInput = findViewById(R.id.userInput);
       passwordInput = findViewById(R.id.passwordInput);
        init();
        displayData();
    }

    //Saves the users login info
    public void saveInfo(){
        SharedPreferences sharepref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        editor.putString("userEmail", userInput.getText().toString());
        editor.commit();
    }

    public void displayData(){
        SharedPreferences sharepref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String email = sharepref.getString("userEmail", "email@domain.com");
        userInput.setText(email);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart");
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy");
    }





}
