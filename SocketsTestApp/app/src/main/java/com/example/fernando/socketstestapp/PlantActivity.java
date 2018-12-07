package com.example.fernando.socketstestapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class PlantActivity extends AppCompatActivity {

    Button btnWater;
    ProgressBar pgbConnect;
    Socket socket;
    PrintWriter writer;
    final int port = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        btnWater = findViewById(R.id.btn_water);
        pgbConnect = findViewById(R.id.pgb_connect);
        Intent callingIntent = getIntent();
        if (callingIntent != null){
            String address = callingIntent.getStringExtra("address");
            new clientTask().execute(address);
        }
        else{
            finish();
        }

        btnWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (socket == null || socket.isClosed()){
                    finish();
                }
                try {
                    writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    writer.println("W");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (socket != null){
            if (!socket.isClosed()) {
                try {
                    socket.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onDestroy();
    }

    public class clientTask extends AsyncTask<String, Object, Boolean> {
        @Override
        protected void onPreExecute() {
            pgbConnect.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                String address = strings[0];
                InetAddress serverAddress = InetAddress.getByName(address);
                try {
                    socket = new Socket(serverAddress, port);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean createdSocket) {
            pgbConnect.setVisibility(View.INVISIBLE);
            if (createdSocket) {
                Toast.makeText(PlantActivity.this, "Connected", Toast.LENGTH_LONG).show();


            }
            else{
                Toast.makeText(PlantActivity.this, "Could not connect", Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }
}
