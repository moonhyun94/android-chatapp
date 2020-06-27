package com.example.chat_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chat_app.models.User;
import com.example.chat_app.utils.ValidationUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private DocumentReference documentReference;
    private TextInputEditText inputEmail, inputPassword, inputName, inputPhoneNum;
    private TextInputLayout emailLayout, passwordLayout, nameLayout, phoneNumLayout;
    private Button backBtn, submissionBtn;
    private ValidationUtils valUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        Intent registerForm = getIntent();
        initContent();
        inputListener();
    }

    private void createUser() {
        final String name, email, password, phoneNum;
        name = inputName.getText().toString().trim();
        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();
        phoneNum = inputPhoneNum.getText().toString().trim();

        valUtil = new ValidationUtils(name, email, password, phoneNum);

        if (valUtil.isOk()) {
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    User user = new User("", name, name, email, password, phoneNum, "");
                    documentReference = fStore.collection("users").document(email);
                    documentReference.set(user);
                    Toast.makeText(RegisterActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == submissionBtn) { createUser(); }
        if (view == backBtn) { finish(); }
    }

    private void inputListener() {
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (inputEmail.getText().toString().trim().isEmpty()) {
                    emailLayout.setError("이메일을 입력해주세요.");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.getText().toString().trim()).matches()) {
                    emailLayout.setError("이메일 형식에 맞지 않습니다.");
                    return;
                }
                emailLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        inputPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputPassword.getText().toString().trim().isEmpty()) {
                    passwordLayout.setError("비밀번호를 입력해주세요.");
                    return;
                }
                if (!valUtil.getPasswordPattern().matcher(inputPassword.getText().toString().trim()).matches()) {
                    passwordLayout.setError("8 ~ 15 자리, 숫자, 영대소문자, 특수문자를 포함해야합니다.");
                    return;
                }
                passwordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        inputPhoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputPhoneNum.getText().toString().trim().isEmpty()) {
                    phoneNumLayout.setError("핸드폰 번호를 입력해주세요.");
                    return;
                }
                if (!valUtil.getPhonePattern().matcher(inputPhoneNum.getText().toString().trim()).matches()) {
                    phoneNumLayout.setError("핸드폰 번호 형식이 아닙니다.");
                    return;
                }
                phoneNumLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (inputName.getText().toString().trim().isEmpty()) {
                    nameLayout.setError("이름을 입력해주세요");
                    return;
                }
                nameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void initContent() {
        inputEmail = (TextInputEditText) findViewById(R.id.email);
        inputPassword = (TextInputEditText) findViewById(R.id.userPassword);
        inputName = (TextInputEditText) findViewById(R.id.name);
        inputPhoneNum = (TextInputEditText) findViewById(R.id.phoneNum);

        emailLayout = (TextInputLayout) findViewById(R.id.til_email_register);
        passwordLayout = (TextInputLayout) findViewById(R.id.til_password_register);
        nameLayout = (TextInputLayout) findViewById(R.id.til_name_register);
        phoneNumLayout = (TextInputLayout) findViewById(R.id.til_phoneNum_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        valUtil = new ValidationUtils();

        submissionBtn = (Button) findViewById(R.id.submissionButton);
        backBtn = (Button) findViewById(R.id.backButton);
        submissionBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }


}
