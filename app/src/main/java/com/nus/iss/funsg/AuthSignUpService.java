package com.nus.iss.funsg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthSignUpService {
    @Headers({
            "Content-Type: application/json",
            "User-Agent: Android"
    })
    @POST("/auth/signup")
    Call<AuthSignUpResponse> signUp(@Body AuthSignUpRequest authSignUpRequest);

    @POST("/auth/login")
    Call<AuthLoginResponse> login(@Body AuthLoginRequest authRequest);
}
