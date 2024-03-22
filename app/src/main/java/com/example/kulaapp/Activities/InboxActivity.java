package com.example.kulaapp.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kulaapp.Adapters.MessageAdapter;
import com.example.kulaapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class InboxActivity extends AppCompatActivity {

    private RecyclerView recycler_view_messages;
    private MessageAdapter adapter;

    private FloatingActionButton fab_compose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        recycler_view_messages = findViewById(R.id.recycler_view_messages);
        fab_compose = findViewById(R.id.fab_compose);

        recycler_view_messages.setLayoutManager(new LinearLayoutManager(this));

        List<String> messages = new ArrayList<>();
        // Add sample messages to the list
        messages.add("Message 1");
        messages.add("Message 2");
        // Add more messages as needed

        adapter = new MessageAdapter(messages);
        recycler_view_messages.setAdapter(adapter);


        fab_compose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TO DO: code to write new message.
            }
        });
    }
}