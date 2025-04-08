package com.example.velogoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class admin_ManageUser extends AppCompatActivity {
    ListView lstUser;
    Button btnopenUser;
    ArrayList<User> userList = new ArrayList<User> ();
    MyAdapterUsers adapter;
    SQLiteDatabase db;
    int posselected = -1; // Giu Vi tri tren ListView
    // Khai báo các biến nhận kết quả trả về từ activity
    public static final int OPEN_USER= 113;
    public static final int EDIT_USER= 114;
    public static final int SAVE_USER= 115;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_manage_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lstUser = findViewById(R.id.lstUsers);
        btnopenUser = findViewById(R.id.btnOpenUser);
        getUserList();
        registerForContextMenu(lstUser);
        btnopenUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_ManageUser.this, admin_InsertUserActivity.class);
                startActivityForResult(intent, admin_ManageUser.OPEN_USER);
            }
        });
        lstUser.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected = position;
                // Thực hiện hành động cần thiết tại đây
                return false; // Trả về true nếu sự kiện đã được xử lý
            }
        });
    }

    private void getUserList() {
        Cursor c = null;
        try {
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            c = db.query("Users", null, null, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    String id = c.getString(0) != null ? c.getString(0) : "";
                    String field1 = c.getString(1) != null ? c.getString(1) : "";
                    String field2 = c.getString(2) != null ? c.getString(2) : "";
                    String field3 = c.getString(3) != null ? c.getString(3) : "";
                    String field4 = c.getString(4) != null ? c.getString(4) : "";
                    String field5 = c.getString(5) != null ? c.getString(5) : "";
                    String field6 = c.getString(6) != null ? c.getString(6) : "";
                    String field7 = c.getString(7) != null ? c.getString(7) : "";
                    String field8 = c.getString(8) != null ? c.getString(8) : "";
                    String field9 = c.getString(9) != null ? c.getString(9) : "";

                    userList.add(new User(id, field1, field2, field3, field4, field5, field6, field7, field8, field9));
                } while (c.moveToNext());
            }

            adapter = new MyAdapterUsers(this, android.R.layout.simple_list_item_1, userList);
            lstUser.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e("admin_ManageUser", "Lỗi listuser: ", ex);
            Toast.makeText(getApplicationContext(), "Lỗi: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (c != null) {
                c.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
    public void comfirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Xác nhận để xóa người dùng..!!!");
        // Icon of Alert Dialog
        alertDialogBuilder.setIcon (R.drawable.img_question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có chắc muốn xóa người dùng?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton ("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Đông Activity hiện tại
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                String id_user = userList.get(posselected).getUserID();
                if (db.delete("Users", "UserID=?", new String[]{id_user}) != -1) {
                    userList.remove(posselected);
                    adapter.notifyDataSetChanged(); // cap nhat lai adapter
                    Toast.makeText(getApplication(), "Xóa người dùng thành công!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialogBuilder.setNegativeButton("Không đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onCreateContextMenu (ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {
        // TODO Auto-generated method stub
        if(item.getItemId() == R.id.mnuedituser){
            User user = userList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(admin_ManageUser.this, admin_EditUserActivity.class);
            bundle.putSerializable ("user", user);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, admin_ManageUser.EDIT_USER);
            return true;
        } else if (item.getItemId() == R.id.mnudeleteuser) {
            comfirmDelete();
            return true;
        } else if (item.getItemId() == R.id.mnucloseuser) {
            Notify.exit(this);
            return true;
        }
        else {
            return super.onContextItemSelected(item);
        }
    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case admin_ManageUser.OPEN_USER:
                if (resultCode == admin_ManageUser.SAVE_USER) {
                    Bundle bundle = data.getBundleExtra("data");
                    User user = (User) bundle.getSerializable("user");
                    userList.add(user);
                    adapter.notifyDataSetChanged();
                }
                break;
            case admin_ManageUser.EDIT_USER:
                if (resultCode == admin_ManageUser.SAVE_USER) {
                    Bundle bundle = data.getBundleExtra("data");
                    User user = (User) bundle.getSerializable("user");
                    userList.set(posselected, user);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}