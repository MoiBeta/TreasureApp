package com.example.mapsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mapsapp.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    private ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                if (binding.radio1.isChecked()) {
                    intent.putExtra("Treasures", 1);
                } else if (binding.radio2.isChecked()) {
                    intent.putExtra("Treasures", 2);
                } else if (binding.radio3.isChecked()) {
                    intent.putExtra("Treasures", 3);
                }
                startActivity(intent);
            }
        });
    }
}