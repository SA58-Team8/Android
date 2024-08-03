package com.nus.iss.funsg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchResult extends AppCompatActivity {

    private FrameLayout filterBtn;
    private Button condition1Btn;
    private Button condition2Btn;
    private LinearLayout filterOptionsLayout;
    String query;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private EditText searchEdit;
    private FrameLayout searchBtn;
    private List<AuthEventsResponse> eventsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Intent intent = getIntent();
        query = intent.getStringExtra("query");
        filterBtn=findViewById(R.id.filter_button);
        condition1Btn=findViewById(R.id.condition1_button);
        condition2Btn=findViewById(R.id.condition2_button);

        filterOptionsLayout = findViewById(R.id.filter_options_layout);

        filterBtn.setOnClickListener(v->toggleFilterOptions());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, eventsList,true);
        recyclerView.setAdapter(eventAdapter);

        searchEdit=findViewById(R.id.search_result_search_bar);
        searchBtn=findViewById(R.id.search_result_search_button_container);
        searchEdit.setHint(query);
        searchBtn.setOnClickListener(view -> {
            query = searchEdit.getText().toString().trim();
            if (!query.isEmpty()) {
                eventsList.clear();
                eventAdapter.notifyDataSetChanged();
                fetchEvents();
            }
        });
        fetchEvents();

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
                    eventAdapter.notifyDataSetChanged();
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
}