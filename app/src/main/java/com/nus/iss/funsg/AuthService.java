package com.nus.iss.funsg;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @Headers({
            "Content-Type: application/json",
            "User-Agent: Android"
    })
    @POST("groups")
    Call<Void> createGroup(@Body AuthCreateGroupRequest request);

    @Multipart
    @Headers({
            "User-Agent: Android"
    })
    @POST("groups/groupImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);


    @Headers({
            "User-Agent: Android"
    })
    @GET("categories/{categoryId}/groups")
    Call<List<AuthGroupsResponse>> getGroups(@Path("categoryId") Long categoryId);

    @Headers({
            "User-Agent: Android"
    })
    @GET("events")
    Call<List<AuthEventsResponse>> getEvents();

    @Headers({
            "User-Agent: Android"
    })
    @GET("events/{eventId}")
    Call<AuthEventsResponse> getEventDetails(@Path("eventId") long eventId);
}
