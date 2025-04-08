package com.example.velogoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class History extends AppCompatActivity {

    ListView lstHistory;
    SQLiteDatabase db;
    ArrayList<Rentals> historyList = new ArrayList<Rentals>();
    MyAdapterHistory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);
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
                    Intent intent = new Intent(History.this, MainActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_station) {
                    Intent intent = new Intent(History.this, Stations.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_history) {
                    return true;
                } else if (itemId == R.id.nav_extend) {
                    Intent intent = new Intent(History.this, Extend.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        lstHistory = findViewById(R.id.lstHistory);

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("UserID", -1);

        if (userID != -1) {
            getHistoryList(userID);
        } else {
            Toast.makeText(this, "Không tìm thấy userID", Toast.LENGTH_SHORT).show();
        }

    }
    private void getHistoryList(int userID) {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

        Cursor c = db.rawQuery("SELECT Bicycles.BicycleID, Bicycles.ModelName, Rentals.RentalID, " +
                "Rentals.UserID, Rentals.StationID, Rentals.RentalStart, " +
                "Rentals.RentalEnd, Rentals.TotalCost, Rentals.Status FROM Bicycles, Rentals " +
                "WHERE Bicycles.BicycleID = Rentals.BicycleID " +
                        "AND Rentals.UserID = ?" +
                        "AND Rentals.Status = ?",
                new String[]{String.valueOf(userID), "Hoàn thành"});

        // Xử lý kết quả truy vấn
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String bicycleID = c.getString(0) != null ? c.getString(0) : "";
            String bicycleName = c.getString(1) != null ? c.getString(1) : "";
            String rentalID = c.getString(2) != null ? c.getString(2) : "";
            String stationID = c.getString(4) != null ? c.getString(4) : "";
            String rentalStart = c.getString(5) != null ? c.getString(5) : "";
            String rentalEnd = c.getString(6) != null ? c.getString(6) : "";
            String totalCost = c.getString(7) != null ? c.getString(7) : "";
            String status = c.getString(8) != null ? c.getString(8) : "";


            // Thêm thông tin vào danh sách
            historyList.add(new Rentals(bicycleID, bicycleName, rentalID, String.valueOf(userID), stationID, rentalStart, rentalEnd, totalCost, status));
            c.moveToNext();
        }

        // Cập nhật adapter với danh sách mới
        adapter = new MyAdapterHistory(this, android.R.layout.simple_list_item_1, historyList);
        lstHistory.setAdapter(adapter);
    }
}