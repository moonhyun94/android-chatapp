package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainPage extends AppCompatActivity {
    private TextView showText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        Intent mainPageIntent = getIntent();
        showText = (TextView) findViewById(R.id.showText);

    }
}
