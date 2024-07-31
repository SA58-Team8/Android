package com.nus.iss.funsg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstLaunchRegister extends AppCompatActivity {
    private ImageButton backBtn;
    private Button signUpBtn;
    private EditText usernameText;
    private EditText emailText;
    private EditText passwordText;
    private AuthSignUpService authSignUpService;
    private String username;
    private String email;
    private String password;


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

        Retrofit retrofit = RetrofitClient.getClientNoToken("http://192.168.0.79:8080");
        authSignUpService = retrofit.create(AuthSignUpService.class);

        signUpBtn.setOnClickListener(view -> {
            username = usernameText.getText().toString();
            email = emailText.getText().toString();
            password = passwordText.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "please fill all field", Toast.LENGTH_SHORT).show();
            } else {
                signUp(new AuthSignUpRequest(username, email, password));
            }
        });
    }
    private void signUp(AuthSignUpRequest signUpRequest){
        Call<AuthSignUpResponse> call = authSignUpService.signUp(signUpRequest);
        call.enqueue(new Callback<AuthSignUpResponse>() {
            @Override
            public void onResponse(Call<AuthSignUpResponse> call, Response<AuthSignUpResponse> response) {
                if (response.isSuccessful()) {
                    AuthSignUpResponse signUpResponse = response.body();
                    if (signUpResponse != null) {
                        String token = signUpResponse.getToken();
                        Log.d("Register", "Token: " + token);
                        Toast.makeText(FirstLaunchRegister.this,"successfully",Toast.LENGTH_SHORT).show();


                        //after register successfully, save the token,and save the user info and to the main activity
                        Executor executor = Executors.newSingleThreadExecutor();
                        executor.execute(() -> {
                            UserLoginStatus.saveLoginStatus(FirstLaunchRegister.this,true);
                            UserLoginStatus.saveUserInfo(FirstLaunchRegister.this,token,username,email);

                            runOnUiThread(() -> {
                                Intent intent = new Intent(FirstLaunchRegister.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            });
                        });

                    }
                } else {
                    Toast.makeText(FirstLaunchRegister.this, "Register failed: user has already exist. ErrorCode: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthSignUpResponse> call, Throwable t) {
                Toast.makeText(FirstLaunchRegister.this, "Please check your Internet", Toast.LENGTH_SHORT).show();
                Log.e("Register", "onFailure: ", t);
            }
        });

    }
}