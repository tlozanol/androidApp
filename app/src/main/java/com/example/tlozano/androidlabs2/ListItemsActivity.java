package com.example.tlozano.androidlabs2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.asus.androidlabs.R;

public class ListItemsActivity extends Activity {
    protected static final String ACTIVITY_NAME="ListItemsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Log.i(ACTIVITY_NAME, "In onCreate");

        final ImageButton imagebutton1 = findViewById(R.id.imagebutton1);
        imagebutton1.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                final int REQUEST_IMAGE_CAPTURE = 1;
                Intent takePictureIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        });
        setOnCheckedChanged();
        onCheckChanged();
    }

    private void onCheckChanged() {
        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1) ;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ListItemsActivity.this);
                builder.setMessage(R.string.dialog_message);
                builder.setTitle(R.string.dialog_title);
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("Response", "Here is my response");
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();

            }
        });
    }

    private void setOnCheckedChanged() {
        final Switch rb1 = (Switch) findViewById(R.id.switch1);
        rb1.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View view) {
                CharSequence text1 = "Switch is On";
                int duration1 = Toast.LENGTH_SHORT;
                CharSequence text2 = "Switch is Off";
                int duration2 = Toast.LENGTH_LONG;
                if(rb1.isChecked()) {
                    Toast toast = Toast.makeText(ListItemsActivity.this, text1, duration1);
                    toast.show();
                }else {
                    Toast toast = Toast.makeText(ListItemsActivity.this, text2, duration2);
                    toast.show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        final int REQUEST_IMAGE_CAPTURE = 1;
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            final ImageButton imagebutton1 = findViewById(R.id.imagebutton1);
            imagebutton1.setImageBitmap(imageBitmap);
        }
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
