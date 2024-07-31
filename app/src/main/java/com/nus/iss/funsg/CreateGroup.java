package com.nus.iss.funsg;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateGroup extends AppCompatActivity {
    private ImageButton backBtn;
    private EditText groupNameText;
    private EditText groupDescriptionText;
    private Button submitBtn;
    private Spinner spinnerCategory;

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
                /*TODO: 考虑在传输的时候将图片上传，可能要先暂存下来
                   controller中的参数：request，multipartFile（图片）
                *  createGroup的方法还没有写，因为还不确定里面的post是什么
                *  1.目前只写了Request，还没有写response,看样子不需要response*/
                //createGroup(new AuthCreateGroupRequest(groupId,groupName,groupDescription));
            }
        });

    }


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