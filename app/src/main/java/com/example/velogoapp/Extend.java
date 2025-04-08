package com.example.velogoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Extend extends AppCompatActivity {
    TableRow rowLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_extend);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Lấy thông tin từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("UserID", -1);
        String fullName = sharedPreferences.getString("FullName", "Unknown");
        TextView nameUser = findViewById(R.id.txtName);
        nameUser.setText(fullName);

        rowLogout = findViewById(R.id.rowLogout);

        rowLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo AlertDialog để xác nhận logout
                new AlertDialog.Builder(Extend.this)
                        .setMessage("Bạn có chắc muốn đăng xuất?")
                        .setCancelable(false)  // Không cho phép đóng dialog khi nhấn ngoài
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xoá thông tin người dùng trong SharedPreferences (nếu có)
                                SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();  // Xoá toàn bộ thông tin người dùng
                                editor.apply();

                                // Chuyển về màn hình Login
                                Intent intent = new Intent(Extend.this, Login.class);
                                startActivity(intent);
                                finish();  // Đảm bảo rằng activity hiện tại sẽ bị đóng
                            }
                        })
                        .setNegativeButton("No", null)  // Nếu nhấn "No", chỉ đóng dialog
                        .show();
            }
        });



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(Extend.this, MainActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_station) {
                    Intent intent = new Intent(Extend.this, Stations.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_history) {
                    Intent intent = new Intent(Extend.this, History.class);
                    startActivity(intent);
                } else if (itemId == R.id.nav_extend) {
                    return true;
                }

                return true;
            }
        });
    }
}