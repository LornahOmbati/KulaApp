package com.example.kulaapp.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.kulaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    CardView card_recipe1,card_recipe2,card_recipe3,card_recipe4,card_recipe5, card_recipe6,card_recipe7,card_recipe8;
    BottomNavigationView navigationView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize your CardViews and BottomNavigationView
        card_recipe1 = view.findViewById(R.id.card_recipe1);
        card_recipe2 = view.findViewById(R.id.card_recipe2);
        card_recipe3 = view.findViewById(R.id.card_recipe3);
        card_recipe4 = view.findViewById(R.id.card_recipe4);
        card_recipe5 = view.findViewById(R.id.card_recipe5);
        card_recipe6 = view.findViewById(R.id.card_recipe6);
        card_recipe7 = view.findViewById(R.id.card_recipe7);
        card_recipe8 = view.findViewById(R.id.card_recipe8);

        // Set click listeners for CardViews
        card_recipe1.setOnClickListener(v -> {
            _dialogAddParent1();
            showToast("You clicked recipe 1!");
        });

        card_recipe2.setOnClickListener(v -> {
            _dialogAddParent2();
            showToast("You clicked recipe 2!");
        });

        card_recipe3.setOnClickListener(v -> {
            _dialogAddParent3();
            showToast("You clicked recipe 3!");
        });

        card_recipe4.setOnClickListener(v -> {
            _dialogAddParent4();
            showToast("You clicked recipe 4!");
        });

        card_recipe5.setOnClickListener(v -> {
            _dialogAddParent5();
            showToast("You clicked recipe 5!");
        });

        card_recipe6.setOnClickListener(v -> {
            _dialogAddParent6();
            showToast("You clicked recipe 6!");
        });

        card_recipe7.setOnClickListener(v -> {
            _dialogAddParent7();
            showToast("You clicked recipe 7!");
        });

        card_recipe8.setOnClickListener(v -> {
            _dialogAddParent8();
            showToast("You clicked recipe 8!");
        });

        return view;
    }

    // Method to show dialogs
    private void _dialogAddParent1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe1, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe2, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void _dialogAddParent3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe3, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent4() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe4, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent5() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe5, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent6() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe6, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent7() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe7, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void _dialogAddParent8() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_recipe8, null);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // Method to show toast
    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}