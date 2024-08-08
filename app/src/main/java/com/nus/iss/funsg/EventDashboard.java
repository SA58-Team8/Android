package com.nus.iss.funsg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventDashboard extends AppCompatActivity {
    private long eventId;
    private TextView eventNameText, eventTimeStartText,eventTimeEndText,eventLocationText,eventParticipantsText;
    private Button modifyBtn;
    private int limitParticipants;
    private String eventName,eventLocation,eventEndDate,eventStartDate,eventDescription,eventImageUrl;
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_dashboard);
        eventId=getIntent().getLongExtra("eventId",0L);
        eventNameText=findViewById(R.id.event_name);
        eventTimeStartText=findViewById(R.id.event_time_start);
        eventTimeEndText=findViewById(R.id.event_time_end);
        eventLocationText=findViewById(R.id.event_location);
        eventParticipantsText=findViewById(R.id.event_participants);
        modifyBtn=findViewById(R.id.event_modify_btn);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EventDashboard.this,ModifyEvent.class);
                intent.putExtra("eventId",eventId);
                intent.putExtra("existingParticipants",limitParticipants);
                intent.putExtra("groupId",groupId);
                intent.putExtra("eventName",eventName);
                intent.putExtra("eventLocation",eventLocation);
                intent.putExtra("eventEndDate",eventEndDate);
                intent.putExtra("eventStartDate",eventStartDate);
                intent.putExtra("eventDescription",eventDescription);
                intent.putExtra("eventImageUrl",eventImageUrl);
                startActivity(intent);
            }
        });
        fetchEventDetails();
        /* TODO set attendees image */
    }
    private void fetchEventDetails(){
        Retrofit retrofit=RetrofitClient.getClientNoToken(IPAddress.ipAddress);
        AuthService authService=retrofit.create(AuthService.class);
        Call<AuthEventsResponse> call = authService.getEventDetails(eventId);
        call.enqueue(new Callback<AuthEventsResponse>() {
            @Override
            public void onResponse(Call<AuthEventsResponse> call, Response<AuthEventsResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    AuthEventsResponse event=response.body();
                    updateUI(event);
                }
                else {
                    Log.e("EventResponseError", "Failed to load event: " + response.message());
                    Toast.makeText(EventDashboard.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthEventsResponse> call, Throwable t) {
                Log.e("EventAcquireFailure", "Failed to load event: " + t.getMessage(), t);
                Toast.makeText(EventDashboard.this, "Error fetching event", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUI(AuthEventsResponse event){
        eventNameText.setText(event.getName());
        eventTimeStartText.setText(DateUtils.formatDateString(event.getStart()));
        eventTimeStartText.setText(DateUtils.formatDateString(event.getEnd()));
        eventLocationText.setText(event.getLocation());
        eventParticipantsText.setText(event.getEventParticipants().size()+"/"+event.getMaxParticipants());

        eventName=event.getName();
        limitParticipants=event.getEventParticipants().size();
        groupId=event.getGroupId();
        eventImageUrl=event.getProfileImagePath();
        eventStartDate=event.getStart();
        eventEndDate=event.getEnd();
        eventLocation=event.getLocation();
        eventDescription=event.getDescription();
    }
}