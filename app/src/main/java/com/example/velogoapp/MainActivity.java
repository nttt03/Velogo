package com.example.velogoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtSearch;
    Button btnSearch;
    ListView lstBicycles;
    SQLiteDatabase db;
    ArrayList<Bicycles> bicyclesList = new ArrayList<Bicycles>();
    MyAdapterBicycles adapter;
    int posselected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("UserID", -1);
        String fullName = sharedPreferences.getString("FullName", "Unknown");
        TextView nameUser = findViewById(R.id.nameUser);
        nameUser.setText(fullName);

        LinearLayout btnBikeRented = findViewById(R.id.btnBikeRented);
        btnBikeRented.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BikeRented.class);
                startActivity(intent);
            }
        });
        lstBicycles = findViewById(R.id.lstBicycles);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        getBicyclesList();
        registerForContextMenu(lstBicycles);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
//                    Toast.makeText(MainActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (itemId == R.id.nav_station) {
                    Intent intent = new Intent(MainActivity.this, Stations.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_history) {
                    Intent intent = new Intent(MainActivity.this, History.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_extend) {
                    Intent intent = new Intent(MainActivity.this, Extend.class);
                    startActivity(intent);
                }

                return true;
            }
        });

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Stations.class);
                startActivity(intent);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Stations.class);
                startActivity(intent);
            }
        });

        lstBicycles.setOnItemClickListener((parent, view, position, id) -> {
            // Lấy đối tượng xe đạp được chọn
            Bicycles selectedBicycle = bicyclesList.get(position);

            Intent intent = new Intent(MainActivity.this, BikeRental.class);
            intent.putExtra("BicycleID", selectedBicycle.getBicycleID());
            intent.putExtra("ModelName", selectedBicycle.getModelName());
            intent.putExtra("Type", selectedBicycle.getType());
            intent.putExtra("RentalPricePerHour", selectedBicycle.getRentalPricePerHour());

            // Chuyển đến DetailActivity
            startActivity(intent);
        });

    }

    private void getBicyclesList() {
        try{
            db = openOrCreateDatabase (Login. DATABASE_NAME, MODE_PRIVATE, null);
            Cursor c = db.query("Bicycles", null, "Status = ?", new String[]{"Có sẵn"}, null, null, null);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                String bicycleID = c.getString(c.getColumnIndexOrThrow("BicycleID"));
                String image = c.getString(c.getColumnIndexOrThrow("Image"));
                String modelName = c.getString(c.getColumnIndexOrThrow("ModelName"));
                String type = c.getString(c.getColumnIndexOrThrow("Type"));
                String status = c.getString(c.getColumnIndexOrThrow("Status"));
                String rentalPricePerHour = c.getString(c.getColumnIndexOrThrow("RentalPricePerHour"));
                String createdAt = c.getString(c.getColumnIndexOrThrow("CreatedAt"));

                bicyclesList.add(new Bicycles(bicycleID, image, modelName, type != null ? type : "all", status, rentalPricePerHour, createdAt));
                c.moveToNext();
            }

            adapter = new MyAdapterBicycles(this, android.R.layout.simple_list_item_1, bicyclesList);
            lstBicycles.setAdapter(adapter);
            setListViewHeightBasedOnChildren(lstBicycles);
        }
        catch (Exception ex) {
            Toast.makeText(getApplication (), "Loi"+ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}