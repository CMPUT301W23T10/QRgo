package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.example.qrgo.utilities.StaggeredAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
/**
 * This class is where the user will view the images in a staggered grid layout.
 * {@link StaggeredAdapter} is used to display the images.
 */
public class StaggeredGallery extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String qr_id;
    private StaggeredAdapter adapter;


    private ArrayList<String> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }
        // Hide the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        }

        setContentView(R.layout.activity_staggered_gallery);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        );
//      dataList is an ArrayList of Strings that comes from intent
        dataList = getIntent().getStringArrayListExtra("dataList");
        qr_id = getIntent().getStringExtra("qr_code");
        adapter = new StaggeredAdapter(dataList, this);

        FloatingActionButton closeButton = findViewById(R.id.close_button_gallary);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the topmost fragment from the back stack
                StaggeredGallery.this.getSupportFragmentManager().popBackStack();
                // Create a new intent to navigate to the HomeActivity
                Intent intent = new Intent(StaggeredGallery.this, QrProfileActivity.class);
                intent.putExtra("qr_code", qr_id);
                // Clear the activity stack so that becomes the new root activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the HomeActivity and finish the current activity
                startActivity(intent);
                StaggeredGallery.this.finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        // Create a new intent to navigate to the QrProfileActivity
        Intent intent = new Intent(this, QrProfileActivity.class);
        intent.putExtra("qr_code", qr_id);
        // Clear the activity stack so that becomes the new root activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        // Start the QrProfileActivity and finish the current activity
        startActivity(intent);
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}