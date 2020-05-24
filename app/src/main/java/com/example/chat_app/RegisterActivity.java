package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    private TextInputEditText inputUserName, inputEmail, inputPassword, inputName, inputPhoneNum;

    private Button backBtn;
    private Button submissionBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent registerForm = getIntent();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        inputUserName = (TextInputEditText) findViewById(R.id.UserName);
        inputEmail = (TextInputEditText) findViewById(R.id.email);
        inputPassword = (TextInputEditText) findViewById(R.id.password);
        inputName = (TextInputEditText) findViewById(R.id.name);
        inputPhoneNum = (TextInputEditText) findViewById(R.id.phoneNum);

        submissionBtn = (Button) findViewById(R.id.submissionButton);
        backBtn = (Button) findViewById(R.id.backButton);

        submissionBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    private void createUser() {
        final String userID, userName, name, email, password, phoneNum;
        userName = inputUserName.getText().toString().trim();
        name = inputName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        phoneNum = inputPhoneNum.getText().toString().trim();
        userID = fAuth.getCurrentUser().getUid();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(RegisterActivity.this, "인증 성공 " + userID + " " + email, Toast.LENGTH_SHORT).show();

                documentReference = fStore.collection("users").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("userName", userName);
                user.put("name", name);
                user.put("email", email);
                user.put("password", password);
                user.put("phoneNum", phoneNum);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(RegisterActivity.this, "user profile created " + userID, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "user profile failed " + userID, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterActivity.this, "인증 실패" + e.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onClick(View view) {
        if(view == submissionBtn) {
            createUser();
        }
        if(view == backBtn) {
            finish();
        }
    }
}
