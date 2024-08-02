package com.nus.iss.funsg;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EventPage extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private LatLng defaultLatLng = new LatLng(1.28679, 103.85379);
    private ExecutorService executorService;
    private Handler handler;

    private ImageView eventImage;
    private TextView eventName;
    private TextView eventDate;
    private TextView eventLocation;
    private TextView eventDescription;
    private TextView groupName;
    private TextView goingNumber;

    private ImageView hostImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_page);
        long eventId = getIntent().getLongExtra("eventId",-1L);

        eventName = findViewById(R.id.event_name);
        eventDate = findViewById(R.id.event_date);
        eventLocation = findViewById(R.id.event_location);
        groupName = findViewById(R.id.group_name);
        eventDescription = findViewById(R.id.event_description);
        eventImage = findViewById(R.id.event_image);
        hostImage = findViewById(R.id.host_image);
        goingNumber=findViewById(R.id.going);

        fetchEventDetails(eventId);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initMap();
        }
        executorService = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    private void fetchEventDetails(long eventId){
        Retrofit retrofit=RetrofitClient.getClientNoToken(IPAddress.ipAddress);
        AuthService authService=retrofit.create(AuthService.class);
        Call<AuthEventsResponse> call = authService.getEventDetails(eventId);
        call.enqueue(new Callback<AuthEventsResponse>() {
            @Override
            public void onResponse(Call<AuthEventsResponse> call, Response<AuthEventsResponse> response) {
                if (response.isSuccessful() && response.body() != null){
                    AuthEventsResponse event=response.body();
                    updateUI(event);
                    showEventLocationOnMap(event.getLocation());
                }
                else {
                    Log.e("EventResponseError", "Failed to load event: " + response.message());
                    Toast.makeText(EventPage.this, "Failed to load events", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthEventsResponse> call, Throwable t) {
                Log.e("EventAcquireFailure", "Failed to load event: " + t.getMessage(), t);
                Toast.makeText(EventPage.this, "Error fetching event", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUI(AuthEventsResponse event){
        eventName.setText(event.getName());
        eventDate.setText(DateUtils.formatDateString(event.getStart()) + " - " + DateUtils.formatDateString(event.getEnd()));
        eventLocation.setText(event.getLocation());
        groupName.setText(event.getGroupName());
        eventDescription.setText(event.getDescription());
        goingNumber.setText("Going("+event.getEventParticipants().size()+")");

        Glide.with(this).load(event.getProfileImagePath()).into(eventImage);
        //set host photo
        Glide.with(this).load(event.getCreatedBy().getProfileImage()).into(hostImage);

        //set participants photo;

    }
    private void showEventLocationOnMap(String locationName){
        //locationName="Merlion Park";
        executeGeocodeTask(locationName, 0);
    }


    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void executeGeocodeTask(String locationName, int retryCount){
        executorService.submit(() -> {
            Geocoder geocoder = new Geocoder(EventPage.this, Locale.getDefault());
            LatLng latLng = null;
            try {
                List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            LatLng finalLatLng = latLng;
            handler.post(() -> {
                if (finalLatLng != null && mMap != null) {
                    handler.postDelayed(() -> {
                        mMap.addMarker(new MarkerOptions().position(finalLatLng).title(locationName));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(finalLatLng, 15));
                        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
                    }, 30);
                }
                else {
                    if (retryCount < 3) {
                        try{
                            //handler.postDelayed(() -> executeGeocodeTask(locationName, retryCount + 1), 3000);
                        }
                        catch (Exception e){}

                    } else {
                        if (mMap != null) {
                            mMap.addMarker(new MarkerOptions().position(defaultLatLng).title("Default Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 15));
                        }
                    }
                }
            });
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}