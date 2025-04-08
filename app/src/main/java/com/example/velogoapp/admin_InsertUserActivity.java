package com.example.velogoapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class admin_InsertUserActivity extends AppCompatActivity {
    Button btnSaveInsertUser, btnClearInsertUser, btnCloseInsertUser;
    EditText edtIdUser, edtNameUser, edtEmail, edtPhone, edtAddress;
    RadioGroup rdigroupGender, rdigroupRole;
    RadioButton rdiMale, rdiFemale, rdiUser, rdiAdmin;
    SQLiteDatabase db;

    int posSpinner = -1;
    int idCheckedGender, idCheckedRole;
    String gender = "Nữ", role = "Admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_insert_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();

        btnCloseInsertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(admin_InsertUserActivity.this);
            }
        });

        btnClearInsertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
            }
        });

        btnSaveInsertUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidInput()) {
                    return;
                }
                long id = saveUser();
                if (id!=-1){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    String genderString = (gender == "Nữ") ? "Nữ" : "Nam";
                    String roleString = (role == "User") ? "User" : "Admin";
                    String password = "123";
                    User user = new User(id+"", edtNameUser.getText().toString(),
                            genderString, edtEmail.getText().toString(), password, edtPhone.getText().toString(),
                            edtAddress.getText().toString(), roleString, "", "");
                    bundle.putSerializable ("user", user);
                    intent.putExtra("data", bundle);
                    setResult(admin_ManageUser.SAVE_USER, intent);
                    Toast.makeText(getApplication(), "Thêm người dùng thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private long saveUser () {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idCheckedGender = rdigroupGender.getCheckedRadioButtonId();
        if (idCheckedGender == R.id.rdiMale)
            gender = "Nam";
        idCheckedRole = rdigroupRole.getCheckedRadioButtonId();
        if (idCheckedRole == R.id.rdiUser)
            role = "User";
        try {
//            values.put("UserID", edtIdUser.getText().toString());
            values.put("FullName", edtNameUser.getText().toString());
            values.put("Gender", gender);
            values.put("Email", edtEmail.getText().toString());
            values.put("Password", "123");
            values.put("PhoneNumber", edtPhone.getText().toString());
            values.put("Address", edtAddress.getText().toString());
            values.put("Role", role);
            return (db.insert("Users", null, values));
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
        btnSaveInsertUser = findViewById(R.id.btnSaveInsertUser);
        btnClearInsertUser = findViewById(R.id.btnClearInsertUser);
        btnCloseInsertUser = findViewById(R.id.btnCloseInsertUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        rdigroupGender = findViewById(R.id.rdigroupGender);
        rdigroupRole = findViewById(R.id.rdigroupRole);
    }
    private void clear(){
        edtNameUser.setText("");
        edtEmail.setText("");
        edtAddress.setText("");
        edtPhone.setText("");
    }

    private boolean isValidInput() {
        if (edtNameUser.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập Họ và Tên!");
            return false;
        }
        if (edtEmail.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập Email!");
            return false;
        }
        if (edtPhone.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập Số điện thoại!");
            return false;
        }
        if (edtAddress.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập Địa chỉ!");
            return false;
        }
        if (rdigroupGender.getCheckedRadioButtonId() == -1) {
            showToast("Vui lòng chọn Giới tính!");
            return false;
        }
        if (rdigroupRole.getCheckedRadioButtonId() == -1) {
            showToast("Vui lòng chọn Vai trò!");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}