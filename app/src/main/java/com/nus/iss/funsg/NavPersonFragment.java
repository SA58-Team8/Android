package com.nus.iss.funsg;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NavPersonFragment extends Fragment {
    private Button logOutBtn;
    private Button createGroupBtn;
    private Button createEventBtn;
    private TextView usernameText;
    private TextView emailText;
    private String username;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_person, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();

        username = UserLoginStatus.getUsername(getContext());
        email = UserLoginStatus.getEmail(getContext());

        usernameText=view.findViewById(R.id.username);
        emailText=view.findViewById(R.id.email);
        usernameText.setText(username);
        emailText.setText(email);
        if(view!=null){
            logOutBtn=view.findViewById(R.id.log_out_btn);
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        UserLoginStatus.clearUserInfo(getContext());
                        getActivity().runOnUiThread(() -> {
                            Intent intent = new Intent(getActivity(), FirstLaunch.class);
                            startActivity(intent);
                            getActivity().finish();
                        });
                    });


                }
            });

            createGroupBtn=view.findViewById(R.id.create_new_group);
            createGroupBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(),CreateGroup.class);
                    startActivity(intent);
                }
            });
            createEventBtn=view.findViewById(R.id.create_new_event);
            createEventBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getContext(),CreateEvent.class);
                    startActivity(intent);
                }
            });


        }
    }
}