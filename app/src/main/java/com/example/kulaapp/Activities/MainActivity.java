package com.example.kulaapp.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.kulaapp.Fragments.HomeFragment;
import com.example.kulaapp.Fragments.ProfileFragment;
import com.example.kulaapp.Fragments.SettingsFragment;
import com.example.kulaapp.R;
import com.example.kulaapp.Utils.AESHelper;
import com.example.kulaapp.Utils.DbHelper;
import com.example.kulaapp.Utils.GlobalVariables;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navigationView;

    public static Activity act;
    Context ctx;

    DbHelper doa;
    static AESHelper aesHelper;

    static ProgressDialog pd2;

    //permission initializations
    final String[] permissions = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA};

    public static final int MULTIPLE_PERMISSIONS = 10;

    public static void stopPD(String message) {

        if(!message.equalsIgnoreCase("unauthorized"))
        {
            Toast.makeText(act, message, Toast.LENGTH_LONG).show();
        }

        GlobalVariables.the_service_start = "stop";

        if(pd2.isShowing()) {
            pd2.dismiss();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;
        ctx = this;
        aesHelper = new AESHelper(getApplicationContext());

        createfiles();
        checkPermissions();
        checkAndroid11StoragePermision();

        /**launch db and check if needs updates*/

        //initializing the versions
        String version_code = getApplicationContext().getSharedPreferences("com.example.kulaapp", Context.MODE_PRIVATE).getString("version_code", "0");
        String current_version = aesHelper.getAppVersion(getApplicationContext());


        //check if the version_code in build gradle is the same as the current db version
        if(version_code.equals(current_version)){
            //db is up to date in the logs
            Log.v("DB",current_version+"-NO UPDATES - VERSION - "+version_code);

            //create a new instance of db helper
            doa = new DbHelper(getApplicationContext(), this);


            //check db version in database
            String version_code_db  = doa.getVersionCode();
            if(!version_code.equals(version_code_db)){
                doa = new DbHelper(getApplicationContext());
                doa.updateDBAppVersion(version_code);
            }
        } else {
            //perform db updates
            Log.v("DB",current_version+" -UPDATED - VERSION - "+version_code);
            doa = new DbHelper(getApplicationContext());
        }


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

    //create the .wc files for storage.
    private void createfiles() {
        if (ActivityCompat.checkSelfPermission(act,android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(act, permissions, 10);
        }
        else {
            try
            {
                ArrayList<String> files_needed = new ArrayList<>();

                files_needed.add(GlobalVariables.pics_file_path);
                files_needed.add(GlobalVariables.profile_pics);

                for (String file : files_needed) {
                    File file_q = new File(file);
                    if (!file_q.exists()) {
                        Log.e("Creating data dir=>", "" + String.valueOf(file_q.mkdirs()));
                    } else {
                        Log.e("data dir exists=>", "" + file_q);
                    }
                }
            }
            catch (Exception ex)
            {
                Log.e("dir not created err=>", "" + ex.getMessage());
            }

        }
    }

    //check if android11 storage access permissions are granted.
    private boolean checkAndroid11StoragePermision() {
        try {
            if(Build.VERSION.SDK_INT >= 30) {
                if (Environment.isExternalStorageManager()){
                    return true;
                }else{
                    Toast.makeText(this, "Allow App Storage Permission",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);

                    Uri uri = Uri.fromParts("package", this.getPackageName(), null);

                    intent.setData(uri);
                    startActivity(intent);
                    return false;
                }
            }
        }catch (Exception ex){

        }

        return true;
    }

    // check if permissions are granted
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

}
