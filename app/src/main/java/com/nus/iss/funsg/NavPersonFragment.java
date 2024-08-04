package com.nus.iss.funsg;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class NavPersonFragment extends Fragment {
    private Button logOutBtn;
    private Button createGroupBtn;
    private Button createEventBtn;
    private TextView usernameText;
    private TextView emailText;
    private String username;
    private String email;

    private ImageView userImage;
    private LinearLayout groupJoinedBtn;
    private LinearLayout eventJoinedBtn;
    private TextView groupJoinedNumberText;
    private TextView eventJoinedNumberText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_person, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();

        username = UserLoginStatus.getUsername(getContext());
        email = UserLoginStatus.getEmail(getContext());

        usernameText=view.findViewById(R.id.username);
        emailText=view.findViewById(R.id.email);
        usernameText.setText(username);
        emailText.setText(email);

        logOutBtn=view.findViewById(R.id.log_out_btn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserLoginStatus.clearUserInfo(getContext());
                Intent intent = new Intent(getActivity(), FirstLaunch.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        createGroupBtn=view.findViewById(R.id.create_new_group);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),CreateGroup.class);
                startActivity(intent);
            }
        });
        createEventBtn=view.findViewById(R.id.create_new_event);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),GroupHostSelectPage.class);
                startActivity(intent);
            }
        });

        userImage=view.findViewById(R.id.user_profile_image);
        groupJoinedBtn=view.findViewById(R.id.group_joined_btn);
        eventJoinedBtn=view.findViewById(R.id.event_joined_btn);
        groupJoinedNumberText=view.findViewById(R.id.group_joined_number);
        eventJoinedNumberText=view.findViewById(R.id.event_joined_number);
        groupJoinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), GroupList.class);
                startActivity(intent);
            }
        });
        eventJoinedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), EventList.class);
                startActivity(intent);
            }
        });

        fetchUserInfo();

    }
    private void fetchUserInfo(){
        Retrofit retrofitImage=RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(getContext()));
        AuthService authServiceImage=retrofitImage.create(AuthService.class);
        authServiceImage.getUserProfile().enqueue(new Callback<AuthUserProfileResponse>() {
            @Override
            public void onResponse(Call<AuthUserProfileResponse> call, Response<AuthUserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    Glide.with(getContext()).load(response.body().getProfileImage()).into(userImage);
                    UserLoginStatus.saveUserId(getContext(),response.body().getUserId());
                }
                else {
                    Log.e("UserResponseError", "Failed to load user image: " + response.message());
                    Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthUserProfileResponse> call, Throwable t) {
                Log.e("UserResponseFailure", "Error fetching: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Error fetching user image", Toast.LENGTH_SHORT).show();
            }
        });
        Retrofit retrofitGroup=RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(getContext()));
        AuthService authServiceGroup=retrofitGroup.create(AuthService.class);
        authServiceGroup.getGroupsJoined().enqueue(new Callback<List<AuthGroupsResponse>>() {
            @Override
            public void onResponse(Call<List<AuthGroupsResponse>> call, Response<List<AuthGroupsResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    int GroupNumber=response.body().size();
                    groupJoinedNumberText.setText(String.valueOf(GroupNumber));
                }
            }
            @Override
            public void onFailure(Call<List<AuthGroupsResponse>> call, Throwable t) {
                Log.e("ResponseGroupFailure", "Failed to load Group: " + t.getMessage(), t);
            }
        });
        Retrofit retrofitEvent=RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(getContext()));
        AuthService authServiceEvent=retrofitEvent.create(AuthService.class);
        authServiceEvent.getEventsJoined().enqueue(new Callback<List<AuthEventsResponse>>() {
            @Override
            public void onResponse(Call<List<AuthEventsResponse>> call, Response<List<AuthEventsResponse>> response) {
                if (response.isSuccessful() && response.body() != null){
                    int GroupNumber=response.body().size();
                    eventJoinedNumberText.setText(String.valueOf(GroupNumber));
                }
            }

            @Override
            public void onFailure(Call<List<AuthEventsResponse>> call, Throwable t) {
                Log.e("ResponseEventFailure", "Failed to load Event: " + t.getMessage(), t);
            }
        });
    }
}