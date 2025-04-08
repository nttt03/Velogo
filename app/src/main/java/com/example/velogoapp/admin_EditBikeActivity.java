package com.example.velogoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class admin_EditBikeActivity extends AppCompatActivity {
    Button btnSaveEdit, btnClearEdit, btnCloseEdit;
    EditText edtModelName, edtPrice;
    RadioGroup rdigroupType, rdigroupStatus;
    RadioButton rdiMale, rdiFemale, rdiSport, rdiCosan, rdiDangsudung, rdiBaotri;
    SQLiteDatabase db;

    int idCheckedType, idCheckedStatus;
    String type = "Nữ", status = "Có sẵn";
    String id_bike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_bike);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();
        getData();

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveBike()) {
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
                    String statusString = "";
                    if(status == "Có sẵn"){
                        statusString = "Có sẵn";
                    } else if (status == "Đang sử dụng") {
                        statusString = "Đang sử dụng";
                    } else {
                        statusString = "Bảo trì";
                    }

                    Bicycles bicycles = new Bicycles (id_bike, "",
                            edtModelName.getText().toString(), typeString, statusString,
                            edtPrice.getText().toString(), "");
                    bundle.putSerializable ("bicycles", bicycles);
                    intent.putExtra("data", bundle);
                    setResult(admin_ManageBike.SAVE_BIKE, intent);
                    Toast.makeText(getApplication(), "Cập nhật xe thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        btnCloseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(admin_EditBikeActivity.this);
            }
        });

        btnClearEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });
    }

    //Show data to Activtiy
    private void getData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra ("data");
        Bicycles bicycles = (Bicycles) bundle.getSerializable("bicycles");
        edtModelName.setText(bicycles.getModelName());
        edtPrice.setText(bicycles.getRentalPricePerHour());
        id_bike = bicycles.getBicycleID();
        if (bicycles.getType().contains("Nam")) {
            rdiMale.setChecked(true);
        }
        else if (bicycles.getType().contains("Nữ")){
            rdiFemale.setChecked(true);
        }
        else {
            rdiSport.setChecked(true);
        }

        if (bicycles.getType().contains("Có sẵn")) {
            rdiCosan.setChecked(true);
        }
        else if (bicycles.getType().contains("Đang sử dụng")){
            rdiDangsudung.setChecked(true);
        }
        else {
            rdiBaotri.setChecked(true);
        }

    }

    private boolean saveBike () {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idCheckedType = rdigroupType.getCheckedRadioButtonId();
        if (idCheckedType == R.id.rdiMale)
            type = "Nam";
        if (idCheckedType == R.id.rdiSport)
            type = "Thể thao";
        if (idCheckedStatus == R.id.rdiDangsudung)
            status = "Đang sử dụng";
        if (idCheckedStatus == R.id.rdiBaotri)
            status = "Bảo trì";
        try {
            values.put("ModelName", edtModelName.getText().toString());
            values.put("Type", type);
            values.put("Status", status);
            values.put("RentalPricePerHour", edtPrice.getText().toString());

            if (db.update("Bicycles", values, "BicycleID=?", new String[]{id_bike}) != -1)
                return true;
        } catch (Exception ex) {
            Toast.makeText(getApplication(), "Loi" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false; // Thêm không thành công
    }

    private void initWidget(){
        btnSaveEdit = findViewById(R.id.btnSaveEdit);
        btnClearEdit = findViewById(R.id.btnClearEdit);
        btnCloseEdit = findViewById(R.id.btnCloseEdit);
        edtModelName = findViewById(R.id.edtModelName);
        edtPrice = findViewById(R.id.edtPrice);
        rdigroupType = findViewById(R.id.rdigroupType);
        rdigroupStatus = findViewById(R.id.rdigroupStatus);
        rdiMale = findViewById(R.id.rdiMale);
        rdiFemale = findViewById(R.id.rdiFemale);
        rdiSport = findViewById(R.id.rdiSport);
        rdiCosan = findViewById(R.id.rdiCosan);
        rdiDangsudung = findViewById(R.id.rdiDangsudung);
        rdiBaotri = findViewById(R.id.rdiBaotri);
    }
    private void clear(){
        edtModelName.setText("");
        edtPrice.setText("");
    }
}