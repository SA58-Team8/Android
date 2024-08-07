package com.nus.iss.funsg;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ModifyEvent extends AppCompatActivity {

    private ActivityResultLauncher<Intent> autocompleteLauncher;

    private EditText eventNameEditText;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private TextView eventParticipantsEditText;
    private TextView editTextLocation;
    private TextView eventDescriptionEditText;
    private ImageButton backBtn;
    private Button submitBtn;
    private Button cancelBtn;
    private LinearLayout uploadImageBtn;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private long eventId;
    private int existingParticipants;
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);
        eventId=getIntent().getLongExtra("eventId",0L);
        existingParticipants=getIntent().getIntExtra("existingParticipants",0);
        groupId=getIntent().getLongExtra("groupId",-1L);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBdgPx-6hbOIzNUno2FRgNTDwr1ALQLHs0");
        }
        editTextLocation = findViewById(R.id.edit_text_location);

        autocompleteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        editTextLocation.setText(place.getName());
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        Log.i("MainActivity", status.getStatusMessage());
                    }
                }
        );
        editTextLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(ModifyEvent.this);
                autocompleteLauncher.launch(intent);
            }
        });
        startDateEditText = findViewById(R.id.start_date);
        endDateEditText = findViewById(R.id.end_date);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        startDateEditText.setOnClickListener(v -> showDatePickerDialog(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePickerDialog(endDateEditText));

        eventNameEditText=findViewById(R.id.event_name);
        eventDescriptionEditText=findViewById(R.id.event_description);
        eventParticipantsEditText=findViewById(R.id.event_participants);
        backBtn=findViewById(R.id.back_button_modify_event);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        uploadImageBtn=findViewById(R.id.upload_image_btn);
        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUrl == null){
                    requestStoragePermission();
                }else {
                    Toast.makeText(ModifyEvent.this, "You can only upload one image", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submitBtn=findViewById(R.id.submit_modify_btn);
        cancelBtn=findViewById(R.id.cancel_event_btn);
        submitBtn.setOnClickListener(v-> submitEvent());
        cancelBtn.setOnClickListener(v-> showCancelEventDialog());
    }
    private void submitEvent(){/*  TODO wait for new a controller */
        String name = eventNameEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String description = eventDescriptionEditText.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
    }

    private void cancelEvent(){
        AuthService authService=RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(this)).create(AuthService.class);
        authService.deleteEvent(eventId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ModifyEvent.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("cancelEventFailed", "Error reading error body"+ response.message());
                    Toast.makeText(ModifyEvent.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("DeleteEventOnFailure", "delete error: " + t.getMessage(), t);
                Toast.makeText(ModifyEvent.this, "dealing onFailure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCancelEventDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Cancel Event")
                .setMessage("Do you want to cancel this event? \t Warning, this action is irreversible")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelEvent();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;
    private Uri imageUri;
    private String imageUrl;

    private void requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openFileChooser();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openFileChooser();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            String fileName = getFileName(imageUri);
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                long fileSize = inputStream.available();
                if (fileSize <= MAX_FILE_SIZE) {
                    File file = new File(getCacheDir(), fileName);
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    inputStream.close();
                    outputStream.close();
                    uploadImageToServer(file);
                } else {
                    Toast.makeText(this, "File size limited 20MB", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error reading file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FileReadError", "Error reading file", e);
            }
        }
    }
    private String getFileName(Uri uri){
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadImageToServer(File file){
        if (!file.exists()) {
            Toast.makeText(this, "File not exists", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!file.canRead()){
            Toast.makeText(this, "File cannot be read", Toast.LENGTH_SHORT).show();
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        Retrofit retrofit = RetrofitClient.getClient(IPAddress.ipAddress,UserLoginStatus.getToken(this));
        AuthService authService=retrofit.create(AuthService.class);
        if(groupId!=-1L){
            Call<ResponseBody> call= authService.uploadEventImage(groupId,body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()){
                        try{
                            imageUrl = response.body().string();
                            Toast.makeText(ModifyEvent.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e){//before is IOException
                            Log.e("UploadImageFailed", "Error reading response body,1", e);
                            Toast.makeText(ModifyEvent.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    else {
                        String errorMessage = "";
                        try {
                            errorMessage = response.errorBody().string();
                        } catch (IOException e) {
                            Log.e("UploadImageFailed", "Error reading error body,2", e);
                        }
                        Log.e("UploadImageFailed", "Upload failed: HTTP " + response.code() + " - " + errorMessage);

                        Toast.makeText(ModifyEvent.this, "Upload failed", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(ModifyEvent.this, "Upload failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("UploadImageFailed", "onFailure: ", t);
                }
            });
        }
    }

    private void showDatePickerDialog(final TextView textView) {
        new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(@NonNull DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        String selectedDate = dateFormat.format(calendar.getTime());
                        textView.setText(selectedDate);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
}