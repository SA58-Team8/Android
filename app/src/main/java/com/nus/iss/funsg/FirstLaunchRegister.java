package com.nus.iss.funsg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FirstLaunchRegister extends AppCompatActivity {
    private ImageButton backBtn;
    private Button signUpBtn;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_launch_register);

        backBtn=findViewById(R.id.back_button_register);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        usernameText=findViewById(R.id.username_text);
        emailText=findViewById(R.id.email_text);
        passwordText=findViewById(R.id.password_text);
        signUpBtn=findViewById(R.id.sign_up_submit_btn);
    }
}