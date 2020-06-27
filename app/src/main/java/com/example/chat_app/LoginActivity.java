package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn, registerBtn;
    private FirebaseAuth fAuth;
    private TextInputEditText userEmail, userPassword;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null) {
            // user signed in
            startActivity(new Intent(getApplicationContext(), MainPageNavigation.class));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_page);
        Intent intent = getIntent();
        initContent();
    }

    private void loginUser(String email, String password) {
        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login task Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainPageNavigation.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "가입되지 않은 회원입니다!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initContent() {
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn = (Button) findViewById(R.id.registerButton);

        userEmail = (TextInputEditText) findViewById(R.id.userEmail);
        userPassword = (TextInputEditText) findViewById(R.id.userPassword);

        fAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = userEmail.getText().toString().trim();
                String password = userPassword.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    userEmail.setError("이메일 형식이 아닙니다.");
                    userEmail.setFocusable(true);
                } else {
                    loginUser(email, password);
                }
            }
        });
    }
}


