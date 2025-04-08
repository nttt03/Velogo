package com.example.velogoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin_InsertBikeActivity extends AppCompatActivity {
    Button btnSaveInsert, btnClearInsert, btnCloseInsert;
    EditText edtModelName, edtPrice;
    RadioGroup rdigroupType;
    RadioButton rdiMale, rdiFemale, rdiSport;
    SQLiteDatabase db;

    int idCheckedType;
    String type = "Nữ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_insert_bike);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();

        btnCloseInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(admin_InsertBikeActivity.this);
            }
        });

        btnClearInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        btnSaveInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidInput()) {
                    return;
                }
                long id = saveBike();
                if (id!=-1){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    String typeString = "";
                    if(type == "Nam"){
                        typeString = "Nam";
                    } else if (type == "Nữ") {
                        typeString = "Nữ";
                    } else {
                        typeString = "Thể thao";
                    }
                    String status = "Có sẵn";
                    Bicycles bicycles = new Bicycles(id+"", "",
                            edtModelName.getText().toString(), typeString, status,
                            edtPrice.getText().toString(), "");
                    bundle.putSerializable ("bicycles", bicycles);
                    intent.putExtra("data", bundle);
                    setResult(admin_ManageBike.SAVE_BIKE, intent);
                    Toast.makeText(getApplication(), "Thêm xe mới thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private long saveBike () {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idCheckedType = rdigroupType.getCheckedRadioButtonId();
        if (idCheckedType == R.id.rdiMale)
            type = "Nam";
        if (idCheckedType == R.id.rdiSport)
            type = "Thể thao";
        try {
            values.put("Image", "");
            values.put("ModelName", edtModelName.getText().toString());
            values.put("Type", type);
            values.put("Status", "Có sẵn");
            values.put("RentalPricePerHour", edtPrice.getText().toString());
            return (db.insert("Bicycles", null, values));
        } catch (Exception ex) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.layout_toast_error, null);
            TextView tvCustomToast = (TextView)layout.findViewById(R.id.tvCustomToast);
            tvCustomToast.setText("Lỗi...!!!" + ex.getMessage());
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(layout);
            toast.show();
        }
        return -1; // Thêm không thành công
    }

    private void initWidget(){
        btnSaveInsert = findViewById(R.id.btnSaveInsert);
        btnClearInsert = findViewById(R.id.btnClearInsert);
        btnCloseInsert = findViewById(R.id.btnCloseInsert);
        edtModelName = findViewById(R.id.edtModelName);
        edtPrice = findViewById(R.id.edtPrice);
        rdigroupType = findViewById(R.id.rdigroupType);
    }
    private void clear(){
        edtModelName.setText("");
        edtPrice.setText("");
    }

    private boolean isValidInput() {
        if (edtModelName.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập Tên xe!");
            return false;
        }
        if (edtPrice.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập giá thuê theo ngày!");
            return false;
        }
        if (rdigroupType.getCheckedRadioButtonId() == -1) {
            showToast("Vui lòng chọn Loại xe!");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}