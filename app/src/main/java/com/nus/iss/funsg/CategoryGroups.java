package com.nus.iss.funsg;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CategoryGroups extends AppCompatActivity {
    private FrameLayout filterBtn;
    private LinearLayout filterOptionsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_category_groups);

        filterBtn=findViewById(R.id.filter_button_in_group);
        filterOptionsLayout = findViewById(R.id.filter_options_layout_in_group);

        filterBtn.setOnClickListener(v->toggleFilterOptions());
    }
    private void toggleFilterOptions() {
        if (filterOptionsLayout.getVisibility() == View.GONE) {
            filterOptionsLayout.setVisibility(View.VISIBLE);
        } else {
            filterOptionsLayout.setVisibility(View.GONE);
        }
    }
}