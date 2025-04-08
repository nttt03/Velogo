package com.example.velogoapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BikeRental extends AppCompatActivity {
    ImageView btnBack;
    EditText edtStartDate, edtEndDate;
    Button btnStartDate, btnEndDate, btnRental;
    private int selectedYear, selectedMonth, selectedDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bike_rental);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.btnBack);
        edtStartDate = findViewById(R.id.edtStartDate);
        edtEndDate = findViewById(R.id.edtEndDate);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btnRental = findViewById(R.id.btnRental);

        // Lấy dữ liệu được truyền từ MainActivity
        String bicycleID = getIntent().getStringExtra("BicycleID");
        String modelName = getIntent().getStringExtra("ModelName");
        String type = getIntent().getStringExtra("Type");
        String rentalPricePerHour = getIntent().getStringExtra("RentalPricePerHour");

        // Ánh xạ các TextView để hiển thị thông tin
        TextView txtBicycleID = findViewById(R.id.txtBicycleID);
        TextView txtModelName = findViewById(R.id.txtModelName);
        TextView txtType = findViewById(R.id.txtType);
        TextView txtRentalPrice = findViewById(R.id.txtRentalPrice);

        // Gán dữ liệu cho các TextView
        txtBicycleID.setText("Mã xe: " + bicycleID);
        txtModelName.setText("Tên xe: " + modelName);
        txtType.setText("Loại xe: " + type);
        txtRentalPrice.setText("Giá thuê: " + rentalPricePerHour + ".000 VNĐ/ngày");


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(1);
            }
        });
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerStartDate();
            }
        });
        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerEndDate();
            }
        });
        btnRental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị custom dialog
                View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_rental, null);
                androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(BikeRental.this)
                        .setView(dialogView)
                        .setCancelable(false)
                        .create();

                Button btnDialogCancel = dialogView.findViewById(R.id.btnNo);
                Button btnDialogConfirm = dialogView.findViewById(R.id.btnYes);

                // Hủy dialog
                btnDialogCancel.setOnClickListener(v -> dialog.dismiss());

                // Xác nhận thuê xe
                btnDialogConfirm.setOnClickListener(v -> {
                    try {

                        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                        int userID = sharedPreferences.getInt("UserID", -1);

                        String startDate = edtStartDate.getText().toString();
                        String endDate = edtEndDate.getText().toString();

                        if (startDate.isEmpty() || endDate.isEmpty()) {
                            Toast.makeText(BikeRental.this, "Vui lòng chọn ngày thuê và ngày trả!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        double pricePerDay = Double.parseDouble(rentalPricePerHour);
                        long daysDiff = calculateDaysDifference(startDate, endDate);
                        double totalCost = daysDiff * pricePerDay;

                        SQLiteDatabase db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);

                        // Thêm bản ghi vào bảng Rentals
                        db.execSQL("INSERT INTO Rentals (UserID, BicycleID, RentalStart, RentalEnd, TotalCost, Status) " +
                                "VALUES (?, ?, ?, ?, ?, ?)", new Object[]{
                                userID,
                                bicycleID,
                                startDate,
                                endDate,
                                totalCost,
                                "Đang thực hiện"
                        });
                        LayoutInflater inflater = getLayoutInflater();
                        View layout = inflater.inflate(R.layout.layout_toast_success, null);
                        TextView tvCustomToast = (TextView)layout.findViewById(R.id.tvCustomToast);
                        tvCustomToast.setText("Thuê xe thành công!");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        dialog.dismiss();
                        Intent intent = new Intent(BikeRental.this, BikeRented.class);
                        startActivity(intent);
                        //finish(); // Quay lại màn hình trước
                    } catch (Exception e) {
                        Toast.makeText(BikeRental.this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                dialog.show();
            }
        });

    }

    private long calculateDaysDifference(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            long diff = endDate.getTime() - startDate.getTime();
            return (diff / (1000 * 60 * 60 * 24)) + 1; // +1 để bao gồm cả ngày thuê
        } catch (Exception e) {
            return 0;
        }
    }
    private void showDatePickerStartDate() {
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    edtStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }
    private void showDatePickerEndDate() {
        final Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    edtEndDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }
}