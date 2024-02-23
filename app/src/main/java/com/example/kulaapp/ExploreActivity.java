package com.example.kulaapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class ExploreActivity extends AppCompatActivity {

    CardView card_recipe1,card_recipe2,card_recipe3,card_recipe4,card_recipe5, card_recipe6,card_recipe7,card_recipe8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        card_recipe1 = findViewById(R.id.card_recipe1);
        card_recipe2 = findViewById(R.id.card_recipe2);
        card_recipe3 = findViewById(R.id.card_recipe3);
        card_recipe4 = findViewById(R.id.card_recipe4);
        card_recipe5 = findViewById(R.id.card_recipe5);
        card_recipe6 = findViewById(R.id.card_recipe6);
        card_recipe7 = findViewById(R.id.card_recipe7);
        card_recipe8 = findViewById(R.id.card_recipe8);
        
        card_recipe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent();
                showToast("You clicked recipe 1!");
            }
        });

        card_recipe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent2();
                showToast("You clicked recipe 2!");
            }
        });

        card_recipe3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent3();
                showToast("You clicked recipe 3!");
            }
        });

        card_recipe4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent4();
                showToast("You clicked recipe 4!");
            }
        });

        card_recipe5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent5();
                showToast("You clicked recipe 5!");
            }
        });

        card_recipe6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent6();
                showToast("You clicked recipe 6!");
            }
        });

        card_recipe7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent7();
                showToast("You clicked recipe 7!");
            }
        });

        card_recipe8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogAddParent8();
                showToast("You clicked recipe 8!");
            }
        });

    }

    private void _dialogAddParent() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe1, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent2() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe2, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent3() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe3, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent4() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe4, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent5() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe5, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent6() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe6, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent7() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe7, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent8() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ExploreActivity.this);
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view_ = layoutInflaterAndroid.inflate(R.layout.activity_recipe8, null);
        builder.setView(view_);
        builder.setCancelable(true);

        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

