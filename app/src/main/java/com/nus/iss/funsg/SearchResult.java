package com.nus.iss.funsg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SearchResult extends AppCompatActivity {

    private FrameLayout filterBtn;
    private Button condition1Btn;
    private Button condition2Btn;
    private LinearLayout filterOptionsLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result);
        filterBtn=findViewById(R.id.filter_button);
        condition1Btn=findViewById(R.id.condition1_button);
        condition2Btn=findViewById(R.id.condition2_button);

        filterOptionsLayout = findViewById(R.id.filter_options_layout);

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