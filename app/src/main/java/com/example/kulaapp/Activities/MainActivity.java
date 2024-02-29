package com.example.kulaapp.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.kulaapp.Fragments.HomeFragment;
import com.example.kulaapp.Fragments.ProfileFragment;
import com.example.kulaapp.Fragments.SettingsFragment;
import com.example.kulaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load the HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        navigationView = findViewById(R.id.navigation_view);

        navigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment fragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                fragment = new HomeFragment();
            } else if (itemId == R.id.navigation_settings) {
                fragment = new SettingsFragment();
            } else if (itemId == R.id.navigation_profile) {
                fragment = new ProfileFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
            return true;
        });
    }
}
