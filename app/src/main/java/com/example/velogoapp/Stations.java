package com.example.velogoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Stations extends AppCompatActivity {
    ListView lstStations;
    SQLiteDatabase db;
    ArrayList<RentalStations> rentalStationsList = new ArrayList<RentalStations>();
    MyAdapterStations adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stations);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(Stations.this, MainActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_station) {
                    return true;
                } else if (itemId == R.id.nav_history) {
                    Intent intent = new Intent(Stations.this, History.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_extend) {
                    Intent intent = new Intent(Stations.this, Extend.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        lstStations = findViewById(R.id.lstStations);
        getStationsList();
    }

    private void getStationsList() {
        try{
            db = openOrCreateDatabase (Login. DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c= db.query("RentalStations", null, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String StationID = c.getString(c.getColumnIndexOrThrow("StationID"));
                String StationName = c.getString(c.getColumnIndexOrThrow("StationName"));
                String Location = c.getString(c.getColumnIndexOrThrow("Location"));
                String Capacity = c.getString(c.getColumnIndexOrThrow("Capacity"));
                String CreatedAt = c.getString(c.getColumnIndexOrThrow("CreatedAt"));

                rentalStationsList.add(new RentalStations(StationID, StationName, Location, Capacity, CreatedAt));
                c.moveToNext();
            }

            adapter = new MyAdapterStations(this, android.R.layout.simple_list_item_1, rentalStationsList);
            lstStations.setAdapter(adapter);

        }
        catch (Exception ex) {
            Toast.makeText(getApplication (), "Loi"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}