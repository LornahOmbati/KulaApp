package com.example.kulaapp.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kulaapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        // Set action bar title (optional)
        getSupportActionBar().setTitle("Privacy Policy");
    }

    public void openPrivacyPolicyWebpage(View view) {
        // Replace "https://example.com/privacy_policy" with your actual privacy policy URL
        String privacyPolicyUrl = "https://example.com/privacy_policy";

        // Create intent to open web browser with privacy policy URL
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl));
        startActivity(intent);
    }
}
