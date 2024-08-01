package com.nus.iss.funsg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AuthService {
    @Headers({
            "Content-Type: application/json",
            "User-Agent: Android"
    })
    @POST("auth/signup")
    Call<AuthSignUpResponse> signUp(@Body AuthSignUpRequest authSignUpRequest);

    @Headers({
            "Content-Type: application/json",
            "User-Agent: Android"
    })
    @POST("auth/login")
    Call<AuthLoginResponse> login(@Body AuthLoginRequest authRequest);
}
