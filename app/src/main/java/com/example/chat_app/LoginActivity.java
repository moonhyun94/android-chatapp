package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button loginBtn;
    private Button registerBtn;
    private Intent registerForm;
    private Intent mainPageIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);
        Intent intent = getIntent();
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerButton);
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == registerBtn) {
            registerForm = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(registerForm);
        }
        if(view == loginBtn) {
            mainPageIntent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(mainPageIntent);
        }
    }
}
