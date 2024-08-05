package com.nus.iss.funsg;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GroupPage extends AppCompatActivity {

    private TextView groupNameTextView;
    private ImageView profileImageView;
    private TextView groupDescriptionTextView;
    private RecyclerView eventRecyclerView;
    private RecyclerView membersRecyclerView;
    private Button joinButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_page);
        groupNameTextView = findViewById(R.id.group_name);
        profileImageView = findViewById(R.id.group_image);
        groupDescriptionTextView = findViewById(R.id.group_description);
        eventRecyclerView = findViewById(R.id.event_container);
        membersRecyclerView = findViewById(R.id.members_container);
        joinButton = findViewById(R.id.join_btn);
        long groupId = getIntent().getLongExtra("groupId", -1);
        if (groupId != -1) {
            fetchGroupDetails(groupId);
        }
    }
    private void fetchGroupDetails(long groupId){
        Retrofit retrofit = RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(this));
        AuthService authService=retrofit.create(AuthService.class);
        authService.getGroupDetails(groupId).enqueue(new Callback<AuthGroupsResponse>() {
            @Override
            public void onResponse(Call<AuthGroupsResponse> call, Response<AuthGroupsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateGroupDetails(response.body());
                } else {
                /*  TODO */
                }
            }

            @Override
            public void onFailure(Call<AuthGroupsResponse> call, Throwable t) {
                /*  TODO */
            }
        });

        //get group events
        Retrofit retrofitEvent=RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(this));
        AuthService authServiceEvent=retrofitEvent.create(AuthService.class);
        authServiceEvent.getGroupEvents(groupId).enqueue(new Callback<List<AuthEventsResponse>>() {
            @Override
            public void onResponse(Call<List<AuthEventsResponse>> call, Response<List<AuthEventsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayEvents(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AuthEventsResponse>> call, Throwable t) {

            }
        });

    }
    private void populateGroupDetails(AuthGroupsResponse group){
        groupNameTextView.setText(group.getName());
        groupDescriptionTextView.setText(group.getDescription());
        Glide.with(this)
                .load(group.getProfileImagePath())
                .into(profileImageView);


//        GroupAdapterPageMember groupAdapterPageMember=new GroupAdapterPageMember(group.getMembers());
//        membersRecyclerView.setAdapter(groupAdapterPageMember);
//        membersRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void displayEvents(List<AuthEventsResponse> events){

        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        GroupAdapterPageEvent groupAdapterPageEvent=new GroupAdapterPageEvent(this,events);
        eventRecyclerView.setAdapter(groupAdapterPageEvent);
    }
}