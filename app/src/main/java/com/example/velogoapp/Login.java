package com.example.velogoapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;


public class Login extends AppCompatActivity {
    public static final String DATABASE_NAME = "VeloGoApp.db";
    SQLiteDatabase db;
    EditText edtEmail, edtPassword;
    Button btnLogin, btnCloseLogin;
    TextView btnRigister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnCloseLogin = findViewById(R.id.btnCloseLogin);
        btnRigister = findViewById(R.id.btnRigister);
        initDB();

        btnRigister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

//        btnLogin.setOnClickListener(view -> {
//            String email = edtEmail.getText().toString();
//            String password = edtPassword.getText().toString();
//
//            if (email.isEmpty()) {
//                showCustomToast("Vui lòng nhập email!", R.drawable.baseline_warning_amber_24, R.layout.layout_toast_warning);
//                edtEmail.requestFocus();
//            } else if (password.isEmpty()) {
//                showCustomToast("Vui lòng nhập mật khẩu!", R.drawable.baseline_warning_amber_24, R.layout.layout_toast_warning);
//                edtPassword.requestFocus();
//            } else {
//                String fullName = getUserFullName(email, password);
//                if (fullName != null) {
//                    // Lấy userID từ cơ sở dữ liệu
//                    int userID = getUserID(email, password);
//
//                    // Lưu userID vào SharedPreferences
//                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putInt("userID", userID);  // Lưu userID vào SharedPreferences
//                    editor.apply(); // Lưu thay đổi
//
//                    // Chuyển đến MainActivity
//                    Intent intent = new Intent(Login.this, MainActivity.class);
//                    intent.putExtra("USER_NAME", fullName);
//                    startActivity(intent);
//                } else {
//                    showCustomToast("Email hoặc mật khẩu không đúng!", R.drawable.baseline_error_24, R.layout.layout_toast_error);
//                }
//            }
//        });
//
//
//        btnCloseLogin.setOnClickListener(view -> finish());
//    }
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();

            if (email.isEmpty()) {
                showCustomToast("Vui lòng nhập email!", R.drawable.baseline_warning_amber_24, R.layout.layout_toast_warning);
                edtEmail.requestFocus();
            } else if (password.isEmpty()) {
                showCustomToast("Vui lòng nhập mật khẩu!", R.drawable.baseline_warning_amber_24, R.layout.layout_toast_warning);
                edtPassword.requestFocus();
            } else {
                // Lấy thông tin người dùng từ database
                Cursor cursor = getUserDetails(email, password);
                if (cursor != null && cursor.moveToFirst()) {
                    String role = cursor.getString(cursor.getColumnIndex("Role"));
                    // Lưu thông tin vào SharedPreferences
                    saveUserToPreferences(cursor);

                    // Điều hướng dựa vào vai trò (Role)
                    Intent intent;
                    if ("Admin".equalsIgnoreCase(role)) {
                        intent = new Intent(Login.this, MainActivityAdmin.class); // Chuyển đến MainActivityAdmin nếu role là Admin
                    } else {
                        intent = new Intent(Login.this, MainActivity.class); // Chuyển đến MainActivity nếu role là User
                    }

                    intent.putExtra("USER_NAME", cursor.getString(cursor.getColumnIndex("FullName")));
                    startActivity(intent);

                } else {
                    showCustomToast("Email hoặc mật khẩu không đúng!", R.drawable.baseline_error_24, R.layout.layout_toast_error);
                }
            }
        });

        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(Login.this);
            }
        });
    }

    private void initDB() {
        db = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        try {
            // Tạo bảng Users
            if (!isTableExists(db, "Users")) {
                db.execSQL("CREATE TABLE Users (" +
                        "UserID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "FullName TEXT, " +
                        "Gender TEXT, " +
                        "Email TEXT NOT NULL UNIQUE, " +
                        "Password TEXT NOT NULL, " +
                        "PhoneNumber TEXT, " +
                        "Address TEXT, " +
                        "Role TEXT DEFAULT 'User', " +
                        "Avatar BLOB, " +
                        "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP);");
                db.execSQL("INSERT OR IGNORE INTO Users (FullName, Gender, Email, Password, PhoneNumber, Address, Role, Avatar) VALUES " +
                        "('Thao Nguyen', 'Nữ', 'tn03@gmail.com', '123', '0123456789', 'Hồ Chí Minh', 'User', NULL), " +
                        "('Lê Huyền Diệu', 'Nữ', 'huyendieu@gmail.com', '123', '0123456788', 'Hồ Chí Minh', 'User', NULL), " +
                        "('Bùi Văn Cảnh', 'Nam', 'vancanh@gmail.com', '123', '0123456787', 'Hồ Chí Minh', 'User', NULL), " +
                        "('Admin', 'Nam', 'admin@gmail.com', '123', '0123456786', 'Hồ Chí Minh', 'Admin', NULL), " +
                        "('Ngô Lan Anh', 'Nữ', 'lananh@gmail.com', '123', '0123456785', 'Hồ Chí Minh', 'User', NULL);");
            }

            // Tạo bảng Bicycles
            if (!isTableExists(db, "Bicycles")) {
                db.execSQL("CREATE TABLE Bicycles (" +
                        "BicycleID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Image BLOB, " +
                        "ModelName TEXT NOT NULL, " +
                        "Type TEXT CHECK (Type IN ('Nam', 'Nữ', 'Thể thao')), " +
                        "Status TEXT CHECK (Status IN ('Có sẵn', 'Đang sử dụng', 'Bảo trì')), " +
                        "RentalPricePerHour REAL, " +
                        "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP);");
                db.execSQL("INSERT OR IGNORE INTO Bicycles (ModelName, Type, Status, RentalPricePerHour) VALUES " +
                        "('Mountain Bike', 'Thể thao', 'Có sẵn', 25.000), " +
                        "('Xe đạp Vinbike', 'Nữ', 'Có sẵn', 10.000), " +
                        "('Xe đạp HITASA', 'Nam', 'Đang sử dụng', 12.000), " +
                        "('Xe đạp Thống Nhất', 'Nữ', 'Bảo trì', 20.000), " +
                        "('Xe đạp AVAcycle', 'Nam', 'Có sẵn', 8.000);");
            }

            // Tạo bảng RentalStations
            if (!isTableExists(db, "RentalStations")) {
                db.execSQL("CREATE TABLE RentalStations (" +
                        "StationID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "StationName TEXT NOT NULL, " +
                        "Location TEXT NOT NULL, " +
                        "Capacity INTEGER NOT NULL, " +
                        "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP);");
                db.execSQL("INSERT OR IGNORE INTO RentalStations (StationName, Location, Capacity) VALUES " +
                        "('Trạm Phố đêm Bạch Đằng', 'Bạch Đằng, Phú Cường, Thủ Dầu Một, Bình Dương', 30), " +
                        "('Trạm AEON MALL Bình Dương', '1 Đại lộ Bình Dương, Thuận An, Bình Dương', 25), " +
                        "('Trạm Công viên Thới Hòa', 'Thới Hòa, Bến Cát, Bình Dương', 20), " +
                        "('Trạm Công viên nước Thanh Lễ', '563 Đại lộ Bình Dương, Hiệp Thành, Thủ Dầu Một, Bình Dương', 15), " +
                        "('Sân golf Sông Bé', '77 Đại lộ Bình Dương, Lái Thiêu, Thuận An, Bình Dương', 50);");
            }

            // Tạo bảng Rentals
            if (!isTableExists(db, "Rentals")) {
                db.execSQL("CREATE TABLE Rentals (" +
                        "RentalID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "UserID INTEGER NOT NULL, " +
                        "BicycleID INTEGER NOT NULL, " +
                        "StationID INTEGER, " +
                        "RentalStart DATETIME NOT NULL, " +
                        "RentalEnd DATETIME, " +
                        "TotalCost REAL, " +
                        "Status TEXT CHECK (Status IN ('Đang thực hiện', 'Hoàn thành', 'Đã hủy')), " +
                        "FOREIGN KEY (UserID) REFERENCES Users(UserID), " +
                        "FOREIGN KEY (BicycleID) REFERENCES Bicycles(BicycleID), " +
                        "FOREIGN KEY (StationID) REFERENCES RentalStations(StationID));");
                db.execSQL("INSERT OR IGNORE INTO Rentals (UserID, BicycleID, StationID, RentalStart, RentalEnd, TotalCost, Status) VALUES " +
                        "(1, 1, 1, '2024-04-01 08:00:00', '2024-04-02 08:00:00', 50.0, 'Hoàn thành'), " +
                        "(2, 2, 2, '2024-05-01 09:00:00', '2024-05-02 08:00:00', 20.0, 'Hoàn thành'), " +
                        "(3, 3, 3, '2024-06-01 07:00:00', '2024-06-02 08:00:00', 24.0, 'Hoàn thành'), " +
                        "(4, 4, 4, '2024-07-01 10:00:00', '2024-07-02 08:00:00', 40.0, 'Hoàn thành'), " +
                        "(5, 5, 5, '2024-08-01 06:00:00', '2024-08-02 08:00:00', 16.0, 'Hoàn thành');");
            }

            // Tạo bảng Feedback
            if (!isTableExists(db, "Feedback")) {
                db.execSQL("CREATE TABLE Feedback (" +
                        "FeedbackID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "UserID INTEGER NOT NULL, " +
                        "RentalID INTEGER NOT NULL, " +
                        "FeedbackText TEXT, " +
                        "Rating INTEGER CHECK (Rating BETWEEN 1 AND 5), " +
                        "CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (UserID) REFERENCES Users(UserID), " +
                        "FOREIGN KEY (RentalID) REFERENCES Rentals(RentalID));");
                db.execSQL("INSERT OR IGNORE INTO Feedback (UserID, RentalID, FeedbackText, Rating) VALUES " +
                        "(1, 1, 'Tuyệt vời!', 5), " +
                        "(3, 3, 'Dịch vụ rất tốt, xe đẹp và mới.', 4), " +
                        "(5, 5, 'Tạm ổn.', 2), " +
                        "(2, 2, 'ok.', NULL), " +
                        "(4, 4, 'okk.', NULL);");
            }

            // Tạo bảng BicycleHistory
            if (!isTableExists(db, "BicycleHistory")) {
                db.execSQL("CREATE TABLE BicycleHistory (" +
                        "HistoryID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "BicycleID INTEGER NOT NULL, " +
                        "StationID INTEGER NOT NULL, " +
                        "Actions TEXT CHECK (Actions IN ('Checked In', 'Checked Out', 'Maintenance')) NOT NULL, " +
                        "ActionDate DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "Notes TEXT, " +
                        "FOREIGN KEY (BicycleID) REFERENCES Bicycles(BicycleID), " +
                        "FOREIGN KEY (StationID) REFERENCES RentalStations(StationID));");
                db.execSQL("INSERT OR IGNORE INTO BicycleHistory (BicycleID, StationID, Actions, Notes) VALUES " +
                        "(1, 1, 'Checked In', 'Có sẵn cho thuê'), " +
                        "(2, 2, 'Checked Out', 'Hiện đang cho thuê'), " +
                        "(3, 3, 'Maintenance', 'Đang sửa chữa'), " +
                        "(4, 4, 'Checked In', 'Có sẵn cho thuê'), " +
                        "(5, 5, 'Checked Out', 'Hiện đang cho thuê');");
            }
            // Thêm dữ liệu
            //insertSampleData();

        } catch (Exception e) {
            Log.e("DatabaseError", "Khởi tạo CSDL không thành công", e);
            Toast.makeText(this, "Khởi tạo CSDL không thành công: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private String getUserFullName(String email, String password) {
        String fullName = null;
        try {
            Cursor cursor = db.rawQuery("SELECT FullName FROM Users WHERE Email=? AND Password=?", new String[]{email, password});
            if (cursor.moveToFirst()) {
                fullName = cursor.getString(0);
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lấy tên người dùng", Toast.LENGTH_LONG).show();
        }
        return fullName;
    }

    private int getUserID(String email, String password) {
        int userID = -1;
        try {
            Cursor cursor = db.rawQuery("SELECT UserID FROM Users WHERE Email=? AND Password=?", new String[]{email, password});
            if (cursor.moveToFirst()) {
                userID = cursor.getInt(0);
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lấy userID", Toast.LENGTH_LONG).show();
        }
        return userID;
    }

    // Lấy toàn bộ thông tin người dùng từ database
    private Cursor getUserDetails(String email, String password) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM Users WHERE Email=? AND Password=?", new String[]{email, password});
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    // Lưu thông tin người dùng vào SharedPreferences
    private void saveUserToPreferences(Cursor cursor) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("UserID", cursor.getInt(cursor.getColumnIndex("UserID")));
        editor.putString("FullName", cursor.getString(cursor.getColumnIndex("FullName")));
        editor.putString("Gender", cursor.getString(cursor.getColumnIndex("Gender")));
        editor.putString("Email", cursor.getString(cursor.getColumnIndex("Email")));
        editor.putString("Password", cursor.getString(cursor.getColumnIndex("Password")));
        editor.putString("PhoneNumber", cursor.getString(cursor.getColumnIndex("PhoneNumber")));
        editor.putString("Address", cursor.getString(cursor.getColumnIndex("Address")));
        editor.putString("Role", cursor.getString(cursor.getColumnIndex("Role")));
        editor.apply(); // Lưu thay đổi

    }

    private void showCustomToast(String message, int iconResId, int typeLayout) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(typeLayout, null); // Sử dụng typeLayout để chỉ định layout cụ thể

        TextView tvCustomToast = layout.findViewById(R.id.tvCustomToast);
        tvCustomToast.setText(message);

        ImageView imgCustomToast = layout.findViewById(R.id.imgCustomToast);
        imgCustomToast.setImageResource(iconResId);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }



}
