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

public class admin_EditUserActivity extends AppCompatActivity {
    Button btnSaveEditUser, btnClearEditUser, btnCloseEditUser;
    EditText edtNameUser, edtEmail, edtPhone, edtAddress;
    RadioGroup rdigroupGender, rdigroupRole;
    RadioButton rdiMale, rdiFemale, rdiUser, rdiAdmin;
    SQLiteDatabase db;

    int posSpinner = -1;
    int idCheckedGender, idCheckedRole;
    String gender = "Nữ", role = "Admin";
    String id_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_edit_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();
        getData();

        btnSaveEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (saveUser()) {
                    Intent intent = getIntent();
                    Bundle bundle = new Bundle();
                    String genderString = (gender == "Nữ") ? "Nữ" : "Nam";
                    String roleString = (gender == "User") ? "User" : "Admin";
                    User user = new User (id_user, edtNameUser.getText().toString(), genderString,
                            edtEmail.getText().toString(), "123", edtPhone.getText().toString(),
                            edtAddress.getText().toString(), roleString, "", "");
                    bundle.putSerializable ("user", user);
                    intent.putExtra("data", bundle);
                    setResult(admin_ManageUser.SAVE_USER, intent);
                    Toast.makeText(getApplication(), "Cập nhật người dùng thành công!!!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        btnCloseEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notify.exit(admin_EditUserActivity.this);
            }
        });

        btnClearEditUser.setOnClickListener(new View.OnClickListener() {
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
        User user = (User) bundle.getSerializable("user");
        edtNameUser.setText(user.getFullName());
        edtEmail.setText(user.getEmail());
        edtPhone.setText(user.getPhoneNumber());
        edtAddress.setText(user.getAddress());
        id_user = user.getUserID();
        if (user.getGender().contains("Nam")) {
            rdiMale.setChecked(true);
        }
        else {
            rdiFemale.setChecked(true);
        }
        if (user.getRole().contains("User")) {
            rdiUser.setChecked(true);
        }
        else {
            rdiAdmin.setChecked(true);
        }

    }

    private boolean saveUser () {
        db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        idCheckedGender = rdigroupGender.getCheckedRadioButtonId();
        if (idCheckedGender == R.id.rdiMale)
            gender = "Nam";
        idCheckedRole = rdigroupRole.getCheckedRadioButtonId();
        if (idCheckedRole == R.id.rdiUser)
            role = "User";
        try {
            values.put("FullName", edtNameUser.getText().toString());
            values.put("Gender", gender);
            values.put("Email", edtEmail.getText().toString());
            values.put("PhoneNumber", edtPhone.getText().toString());
            values.put("Address", edtAddress.getText().toString());
            values.put("Role", role);
            if (db.update("Users", values, "UserID=?", new String[]{id_user}) != -1)
                return true;
        } catch (Exception ex) {
            Toast.makeText(getApplication(), "Loi" + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return false; // Thêm không thành công
    }

    private void initWidget(){
        btnSaveEditUser = findViewById(R.id.btnSaveEditUser);
        btnClearEditUser = findViewById(R.id.btnClearEditUser);
        btnCloseEditUser = findViewById(R.id.btnCloseEditUser);
        edtNameUser = findViewById(R.id.edtNameUser);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        rdigroupGender = findViewById(R.id.rdigroupGender);
        rdigroupRole = findViewById(R.id.rdigroupRole);
        rdiMale = findViewById(R.id.rdiMale);
        rdiFemale = findViewById(R.id.rdiFemale);
        rdiUser = findViewById(R.id.rdiUser);
        rdiAdmin = findViewById(R.id.rdiAdmin);
    }
    private void clear(){
        edtNameUser.setText("");
        edtEmail.setText("");
        edtAddress.setText("");
        edtPhone.setText("");
    }
}