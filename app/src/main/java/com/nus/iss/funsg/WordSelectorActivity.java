package com.nus.iss.funsg;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordSelectorActivity extends AppCompatActivity {

    private ChipGroup chipGroup;
    private Button submitButton;
    private List<String> wordList;
    private List<String> selectedWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selector);

        chipGroup = findViewById(R.id.chipGroup);
        submitButton = findViewById(R.id.submitButton);
        selectedWords = new ArrayList<>();

        // TODO: Temp Hardcode word list, should this be stored in a DB ?
        wordList = Arrays.asList("book", "music", "art", "science", "travel", "food", "family",
                "friend", "work", "play", "learn", "teach", "create", "explore",
                "imagine", "build");

        setupChips();
        setupSubmitButton();
    }

    private void setupChips() {
        for (String word : wordList) {
            Chip chip = new Chip(this);
            chip.setText(word);
            chip.setCheckable(true);
            chip.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (selectedWords.size() < 3) {
                        selectedWords.add(word);
                    } else {
                        chip.setChecked(false);
                        Toast.makeText(WordSelectorActivity.this, "You can only select 3 words", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    selectedWords.remove(word);
                }
            });
            chipGroup.addView(chip);
        }
    }

    private void setupSubmitButton() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedWords.size() == 3) {
                    // TODO: Send Selected words to backend --> send to mbti webapp for prediction

                    Toast.makeText(WordSelectorActivity.this, "Selected words: " + selectedWords.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(WordSelectorActivity.this, "Please select exactly 3 words", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}