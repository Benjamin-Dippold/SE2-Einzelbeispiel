package com.example.se2_einzelbeispiel;

import android.os.Bundle;

import com.example.se2_einzelbeispiel.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    final String IP = "se2-isys.aau.at";
    final int PORT = 53212;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.se2_einzelbeispiel.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(() -> {
                    try {
                        Socket soc = new Socket(IP, PORT);
                        OutputStream out = soc.getOutputStream();
                        BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));

                        EditText txtInput = findViewById(R.id.txtInput);
                        String message = txtInput.getText().toString();
                        out.write(message.getBytes());
                        out.write("\n".getBytes());

                        response = in.readLine();
                        TextView txtResponse = findViewById(R.id.txtServerAnswer);
                        txtResponse.post(() -> txtResponse.setText(response));

                        in.close();
                        out.close();
                        soc.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        });
    }
}