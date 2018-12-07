package com.example.fernando.socketstestapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    Button btnSendMessage, btnConnect;
    EditText edtIPAddress, edtMessageToSend;
    Socket socket;
    int port = 3000;
    String address = "10.12.12.110";
    //String address = "192.168.0.27";
    String message;
    PrintWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize widgets
        btnSendMessage = findViewById(R.id.btn_send_message);
        btnConnect = findViewById(R.id.btnConnect);
        edtIPAddress = findViewById(R.id.edt_ip_address);
        edtMessageToSend = findViewById(R.id.edt_message_to_send);

        // Set clickListeners
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message = edtMessageToSend.getText().toString();
                if (socket == null) {
                    Toast.makeText(MainActivity.this, "Socket not created", Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        writer.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        btnConnect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlantActivity.class);
                String user_address = edtIPAddress.getText().toString();
                intent.putExtra("address", user_address);
                startActivity(intent);
                /*
                if (socket != null) {
                    if (!socket.isClosed()){
                        try{
                            socket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                    }
                }
                new clientTask().execute(user_address);*/
            }
        });


        //new Thread(new clientThread()).start();
    }



    public class clientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddress = InetAddress.getByName(address);
                try {
                    socket = new Socket(serverAddress, port);
                    if (socket == null) {
                        Toast.makeText(MainActivity.this, "Constructor failed", Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
}


