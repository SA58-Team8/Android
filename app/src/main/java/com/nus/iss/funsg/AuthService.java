package com.nus.iss.funsg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/api/authenticate")
    Call<AuthResponse> login(@Body AuthRequest authRequest);
}
