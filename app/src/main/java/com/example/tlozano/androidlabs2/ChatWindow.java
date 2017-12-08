package com.example.tlozano.androidlabs2;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.asus.androidlabs.R;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    private String ACTIVITY_NAME = "ChatWindow";

    ListView listv;
    EditText chatText;
    Button sendButton;
    ArrayList<String> ChatConver = new ArrayList<>();
    ChatDatabaseHelper chatdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        Log.i(ACTIVITY_NAME, "setting listview");
        listv = findViewById(R.id.listview1);
        chatText = findViewById(R.id.ChatText);
        sendButton = findViewById(R.id.SendB);

        Log.i(ACTIVITY_NAME, "Creating data base");
        chatdb = new ChatDatabaseHelper(this);
        Log.i(ACTIVITY_NAME, "writing data base");
        SQLiteDatabase writeDB = chatdb.getWritableDatabase();
        Log.i(ACTIVITY_NAME, "saving data base");
        ChatConver = chatdb.getChatMessagesFromDB();

        final ChatAdapter messageAdapter = new ChatAdapter(this);
        listv.setAdapter(messageAdapter);



        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "opening data base");
                ChatConver.add(chatText.getText().toString());
                chatdb.insertChatDB(chatText.getText().toString());
                messageAdapter.notifyDataSetChanged();
                chatText.setText("");
            }
        });
    }

    public void onDestroy(){
        chatdb.close();
        super.onDestroy();
    }

    private class ChatAdapter extends ArrayAdapter<String>{

        public ChatAdapter(Context ctx){
            super(ctx, 0);
        }

        public int getCount(){
            return ChatConver.size();
        }

        public String getItem(int position){
            return ChatConver.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();

            View result = null;
            if(position%2==0)
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
}
