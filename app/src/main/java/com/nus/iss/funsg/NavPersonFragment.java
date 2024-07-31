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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class NavPersonFragment extends Fragment {
    Button logOutBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nav_person, container, false);
    }

    @Override
    public void onStart(){
        super.onStart();
        View view = getView();
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
        }
    }
}