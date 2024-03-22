package com.example.kulaapp.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.kulaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //final String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    //ContexCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATION)==PackageManager.PERMISSION_GRANTED { sendNotification(this) }

    TextView sign_out,delete_txt,privacy_txt;

    Switch ads_option, notify_option, dark_mode;

    TextView diet_txt, done_txt;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        sign_out = view.findViewById(R.id.sign_out);
        delete_txt = view.findViewById(R.id.delete_txt);
        privacy_txt = view.findViewById(R.id.privacy_txt);
        ads_option = view.findViewById(R.id.ads_option);
        notify_option = view.findViewById(R.id.notify_option);
        dark_mode = view.findViewById(R.id.dark_mode);
        diet_txt = view.findViewById(R.id.diet_txt);

        diet_txt.setOnClickListener(v -> {
            _dialogAddParent();
            showToast("clicked!");
        });


        privacy_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent1();
                showToast("You clicked recipe 1!");
            }
        });

        ads_option.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Check if the switch is checked (enabled) or unchecked (disabled)
            if (isChecked) {
                // Enable ads and tracking
                enableAdsAndTracking();
            } else {
                // Disable ads and tracking
                disableAdsAndTracking();
            }
        });

        notify_option.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                enableNotifications();
            }else{
                disableNotifications();
            }
        });

        dark_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked){

                // checking if the switch is turned on
                if (isChecked) {

                    // setting theme to night mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    buttonView.setText("Night Mode");
                }

                else {

                    // setting theme to light theme
                    AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO);
                    buttonView.setText("Light Mode");
                }
            }
        });


        return view;
    }

    private void enableNotifications() {

    }

    private void disableNotifications() {
    }

    private void enableAdsAndTracking() {
        // Implement logic to enable ads and tracking
        // For example, you can enable ad networks and tracking services here
        // You might use a library like Google Mobile Ads SDK for ads
        // and Firebase Analytics for tracking
        // Here's a sample implementation using Firebase Analytics:
        // FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(true);
    }

    private void disableAdsAndTracking() {
        // Implement logic to disable ads and tracking
        // For example, you can disable ad networks and tracking services here
        // You might use a library like Google Mobile Ads SDK for ads
        // and Firebase Analytics for tracking
        // Here's a sample implementation using Firebase Analytics:
        // FirebaseAnalytics.getInstance(this).setAnalyticsCollectionEnabled(false)
    }

    private void _dialogAddParent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dietary_options, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        done_txt = dialogView.findViewById(R.id.done_txt);

        done_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void _dialogAddParent1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe8, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}