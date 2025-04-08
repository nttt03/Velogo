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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {
    TextView login;
    EditText edtName, edtEmail, edtPhone, edtAddress, edtPassword, edtConfirmPassword;
    Button btnRegister, btnClose;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isValidInput()) {
                    return;
                }
                long id = saveUser();
                if (id!=-1){
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();

                    User user = new User(id+"", edtName.getText().toString(),
                            "", edtEmail.getText().toString(), edtPassword.getText().toString(),
                            edtPhone.getText().toString(), edtAddress.getText().toString(), "User", "", "");
                    bundle.putSerializable ("user", user);
                    intent.putExtra("data", bundle);
                    setResult(admin_ManageUser.SAVE_USER, intent);
                    Toast.makeText(getApplication(), "Đăng ký thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }

    private long saveUser () {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();

        try {
            values.put("FullName", edtName.getText().toString());
            values.put("Gender", "");
            values.put("Email", edtEmail.getText().toString());
            values.put("Password", edtPassword.getText().toString());
            values.put("PhoneNumber", edtPhone.getText().toString());
            values.put("Address", edtAddress.getText().toString());
            values.put("Role", "User");
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
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnClose = findViewById(R.id.btnClose);
        login = findViewById(R.id.login);
    }
    private boolean isValidInput() {
        if (edtName.getText().toString().trim().isEmpty()) {
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
        if (edtPassword.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập password!");
            return false;
        }
        if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Vui lòng nhập lại password!");
            return false;
        }
        if (!edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
            showToast("Mật khẩu nhập lại không khớp!");
            return false;
        }
        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}