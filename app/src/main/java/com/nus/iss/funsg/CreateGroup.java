package com.nus.iss.funsg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.pm.PackageManager;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CreateGroup extends AppCompatActivity {
    private ImageButton backBtn;
    private EditText groupNameText;
    private EditText groupDescriptionText;
    private Button submitBtn;
    private Spinner spinnerCategory;

    private LinearLayout uploadImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_group);
        spinnerCategory = findViewById(R.id.spinner_category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, R.layout.custom_spinner_item);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        backBtn=findViewById(R.id.back_button_create_group);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        groupNameText=findViewById(R.id.group_name);
        groupDescriptionText=findViewById(R.id.group_description);
        uploadImageBtn=findViewById(R.id.upload_image);

        submitBtn=findViewById(R.id.create_group_submit_btn);
        submitBtn.setOnClickListener(view ->{
            String groupName =groupNameText.getText().toString();
            String groupDescription=groupDescriptionText.getText().toString();
            String category = spinnerCategory.getSelectedItem().toString();
            Long groupId=getCategoryId(category);

            if(groupName.isEmpty()||groupDescription.isEmpty()){
                Toast.makeText(this, "please fill all field", Toast.LENGTH_SHORT).show();
            }
            else{
            /*  TODO*/
                //createGroup(new AuthCreateGroupRequest(groupId,groupName,groupDescription));
            }
        });

    }

/*
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 2;
    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;
    private Uri imageUri;
    private String imageUrl;
    private void requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openFileChooser();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
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
            uploadImageToServer();
        }
    }
    private void uploadImageToServer(){
        File file = new File(getRealPathFromURI(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

*/
    public static Long getCategoryId(String categoryName){
        long id = 1;
        switch (categoryName){
            case "Creative Arts":
                id=1;
                break;
            case "Business Tech":
                id=2;
                break;
            case "Community Causes":
                id=3;
                break;
            case "Health Lifestyle":
                id=4;
                break;
            case "Lifelong Learning":
                id=5;
                break;
            case "Outdoor Hobbies":
                id=6;
                break;
        }
        return id;
    }
}