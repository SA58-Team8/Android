package com.nus.iss.funsg;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    //this class is use for login validation

    @POST("/api/authenticate")
    Call<AuthResponse> login(@Body AuthRequest authRequest);
}
