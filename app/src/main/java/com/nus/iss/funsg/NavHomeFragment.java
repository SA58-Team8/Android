package com.nus.iss.funsg;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NavHomeFragment extends Fragment {
    private boolean isPreview;

    private TextView categoryText;
    private TextView upcomingEventText;
    private FrameLayout searchBtn;
    private TextView suggestedText;
    private SwitchCompat suggestedToggle;
    private Button viceCheckBtn;

    public NavHomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_nav_home, container, false);


    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
        if(view!=null){
            //set underline
            categoryText=view.findViewById(R.id.main_text_category);
            categoryText.setPaintFlags(categoryText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            upcomingEventText=view.findViewById(R.id.main_text_upcoming_event);
            upcomingEventText.setPaintFlags(upcomingEventText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            if (getArguments() != null) {
                isPreview = getArguments().getBoolean("isPreview", false);
                suggestedText=view.findViewById(R.id.suggested_word);
                suggestedText.setTextColor(ContextCompat.getColor(getContext(),R.color.lightblack));
                suggestedText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(),
                                "You need to log in to get suggestion.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                suggestedToggle=view.findViewById(R.id.customSwitch);
                suggestedToggle.setVisibility(View.GONE);

                viceCheckBtn=view.findViewById(R.id.vice_check_btn);
                viceCheckBtn.setClickable(false);
                viceCheckBtn.setTextColor(ContextCompat.getColor(getContext(),R.color.lightblack));
            }



            searchBtn=view.findViewById(R.id.search_button_container);
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(requireContext(),SearchResult.class);
                    startActivity(intent);
                }
            });
        }
    }
}