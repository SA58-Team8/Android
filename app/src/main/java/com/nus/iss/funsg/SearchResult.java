package com.nus.iss.funsg;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class SearchResult extends AppCompatActivity {

    private FrameLayout filterBtn;
    private LinearLayout filterOptionsLayout;
    String query;
    private RecyclerView recyclerView;
    private EventAdapterForSearch eventAdapter;
    private EditText searchEdit;
    private FrameLayout searchBtn;
    private List<AuthEventsResponse> eventsList = new ArrayList<>();
    private List<AuthEventsResponse> filterEventList;
    private List<AuthEventsResponse> originEventList;

    private CheckBox checkbox1;
    private CheckBox checkbox2;
    private CheckBox checkbox3;
    private Button timeConditionBtn;
    private Button distanceConditionBtn;

    //for get current location
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private boolean isDistanceBtnSelected = false;
    private boolean isTimeBtnSelected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        query = intent.getStringExtra("query");


        filterBtn=findViewById(R.id.filter_button);
        timeConditionBtn=findViewById(R.id.condition1_button);
        distanceConditionBtn=findViewById(R.id.condition2_button);

        filterOptionsLayout = findViewById(R.id.filter_options_layout);

        filterBtn.setOnClickListener(v->toggleFilterOptions());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapterForSearch(this, new ArrayList<>(eventsList));
        recyclerView.setAdapter(eventAdapter);

        searchEdit=findViewById(R.id.search_result_search_bar);
        searchBtn=findViewById(R.id.search_result_search_button_container);
        searchEdit.setHint(query);
        searchBtn.setOnClickListener(view -> {
            query = searchEdit.getText().toString().trim();
            if (!query.isEmpty()) {
                Intent intent2 = new Intent(this,SearchResult.class);
                intent2.putExtra("query",query);
                startActivity(intent2);
                finish();
            }
            else if(query.isEmpty()){
                query=searchEdit.getHint().toString();
                Intent intent2 = new Intent(this,SearchResult.class);
                intent2.putExtra("query",query);
                startActivity(intent2);
                finish();
            }
        });

        checkbox1 = findViewById(R.id.option1);
        checkbox2 = findViewById(R.id.option2);
        checkbox3 = findViewById(R.id.option3);
        checkbox1.setChecked(true);
        checkbox2.setChecked(true);
        checkbox3.setChecked(true);
        checkbox1.setOnCheckedChangeListener((buttonView, isChecked) -> filterEventsByParticipants());
        checkbox2.setOnCheckedChangeListener((buttonView, isChecked) -> filterEventsByParticipants());
        checkbox3.setOnCheckedChangeListener((buttonView, isChecked) -> filterEventsByParticipants());
        timeConditionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTimeBtnSelected=!isTimeBtnSelected;
                filterTime();
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        distanceConditionBtn.setOnClickListener(v -> filterDistance());
        fetchEvents();

    }
    private void filterTime(){
        if(isDistanceBtnSelected){
            isDistanceBtnSelected=false;
            distanceConditionBtn.setBackgroundResource(R.drawable.category_button);
            distanceConditionBtn.setTextColor(getResources().getColor(R.color.darkblue));
        }
        if(isTimeBtnSelected){
            timeConditionBtn.setBackgroundResource(R.drawable.button_pressed);
            timeConditionBtn.setTextColor(getColor(R.color.white));
            sortEventsByDate(filterEventList);
        }
        else{
            timeConditionBtn.setBackgroundResource(R.drawable.category_button);
            timeConditionBtn.setTextColor(getColor(R.color.darkblue));
            eventAdapter.updateEvents(new ArrayList<>(originEventList));
        }
    }
    private void filterDistance(){
        isDistanceBtnSelected = !isDistanceBtnSelected;
        if(isTimeBtnSelected){
            isTimeBtnSelected=false;
            timeConditionBtn.setBackgroundResource(R.drawable.category_button);
            timeConditionBtn.setTextColor(getColor(R.color.darkblue));
        }
        if (isDistanceBtnSelected) {
            distanceConditionBtn.setBackgroundResource(R.drawable.button_pressed);
            distanceConditionBtn.setTextColor(getResources().getColor(R.color.white));
        }
        else{
            distanceConditionBtn.setBackgroundResource(R.drawable.category_button);
            distanceConditionBtn.setTextColor(getResources().getColor(R.color.darkblue));
        }
    }
    private void toggleFilterOptions() {
        if (filterOptionsLayout.getVisibility() == View.GONE) {
            filterOptionsLayout.setVisibility(View.VISIBLE);
        } else {
            filterOptionsLayout.setVisibility(View.GONE);
        }
    }
    private void fetchEvents(){
        Retrofit retrofit=RetrofitClient.getClientNoToken(IPAddress.ipAddress);
        AuthService authService=retrofit.create(AuthService.class);
        authService.searchEvents(query,"android").enqueue(new Callback<List<AuthEventsResponse>>() {
            @Override
            public void onResponse(Call<List<AuthEventsResponse>> call, Response<List<AuthEventsResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventsList.addAll(response.body());
                    filterEventList=new ArrayList<>(eventsList);
                    originEventList=new ArrayList<>(eventsList);
                    eventAdapter.updateEvents(new ArrayList<>(eventsList));
                } else if (response.isSuccessful() && response.body() == null) {
                    Toast.makeText(SearchResult.this, "No Result, Please check your words", Toast.LENGTH_SHORT).show();

                } else{
                    Log.e("EventsResponseError", "Failed to load events: " + response.message());
                    Toast.makeText(SearchResult.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AuthEventsResponse>> call, Throwable t) {
                Log.e("EventsAcquireFailure", "Error fetching events: " + t.getMessage(), t);
                Toast.makeText(SearchResult.this, "Error fetching events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortEventsByDate(List<AuthEventsResponse> filterEventList){
        Collections.sort(filterEventList, dateComparator);
        eventAdapter.updateEvents(new ArrayList<>(filterEventList));
    }
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private Comparator<AuthEventsResponse> dateComparator = new Comparator<AuthEventsResponse>() {
        @Override
        public int compare(AuthEventsResponse e1, AuthEventsResponse e2) {
            Date date1 = parseDate(e1.getStart());
            Date date2 = parseDate(e2.getStart());
            if (date1 == null || date2 == null) {
                return 0;
            }
            return date1.compareTo(date2);
        }
    };
    private Date parseDate(String dateString) {
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void filterEventsByParticipants(){

        originEventList.clear();
        filterEventList.clear();
        for (AuthEventsResponse event : eventsList){
            int participants = event.getEventParticipants().size();
            boolean matchesCheckbox1 = checkbox1.isChecked() && participants < 5;
            boolean matchesCheckbox2 = checkbox2.isChecked() && participants >= 5 && participants <= 10;
            boolean matchesCheckbox3 = checkbox3.isChecked() && participants > 10;
            if (matchesCheckbox1 || matchesCheckbox2 || matchesCheckbox3) {
                originEventList.add(event);
                filterEventList.add(event);
            }
        }
        if (isTimeBtnSelected){
            filterTime();
        }
        else if(isDistanceBtnSelected){
            filterDistance();
        }
        else{
            eventAdapter.updateEvents(originEventList);
        }
    }

}