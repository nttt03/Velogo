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

public class admin_ManageBike extends AppCompatActivity {
    ListView lstBike;
    Button btnopenBike;
    ArrayList<Bicycles> bikeList = new ArrayList<Bicycles> ();
    MyAdapterBicyclesAdmin adapter;
    SQLiteDatabase db;
    int posselected = -1; // Giu Vi tri tren ListView
    // Khai báo các biến nhận kết quả trả về từ activity
    public static final int OPEN_BIKE= 113;
    public static final int EDIT_BIKE= 114;
    public static final int SAVE_BIKE= 115;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_manage_bike);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lstBike = findViewById(R.id.lstBike);
        btnopenBike = findViewById(R.id.btnOpenBike);
        getBikeList();
        registerForContextMenu(lstBike);
        btnopenBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(admin_ManageBike.this, admin_InsertBikeActivity.class);
                startActivityForResult(intent, admin_ManageBike.OPEN_BIKE);
            }
        });
        lstBike.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                posselected = position; // Giả sử posselected đã được khai báo ở nơi khác
                // Thực hiện hành động cần thiết tại đây
                return false; // Trả về true nếu sự kiện đã được xử lý
            }
        });
    }

    private void getBikeList() {
        Cursor c = null;
        try {
            db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
            c = db.query("Bicycles", null, null, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    String id = c.getString(0) != null ? c.getString(0) : "";
                    String field1 = c.getString(1) != null ? c.getString(1) : "";
                    String field2 = c.getString(2) != null ? c.getString(2) : "";
                    String field3 = c.getString(3) != null ? c.getString(3) : "";
                    String field4 = c.getString(4) != null ? c.getString(4) : "";
                    String field5 = c.getString(5) != null ? c.getString(5) : "";
                    String field6 = c.getString(6) != null ? c.getString(6) : "";

                    bikeList.add(new Bicycles(id, field1, field2, field3, field4, field5, field6));
                } while (c.moveToNext());
            }

            adapter = new MyAdapterBicyclesAdmin(this, android.R.layout.simple_list_item_1, bikeList);
            lstBike.setAdapter(adapter);
        } catch (Exception ex) {
            Log.e("admin_ManageBike", "Lỗi listBike: ", ex);
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
        alertDialogBuilder.setTitle("Xác nhận để xóa xe..!!!");
        // Icon of Alert Dialog
        alertDialogBuilder.setIcon (R.drawable.img_question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Bạn có chắc muốn xóa xe này?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton ("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Đông Activity hiện tại
                db = openOrCreateDatabase(Login.DATABASE_NAME, MODE_PRIVATE, null);
                String id_bike = bikeList.get(posselected).getBicycleID();
                if (db.delete("Bicycles", "BicycleID=?", new String[]{id_bike}) != -1) {
                    bikeList.remove(posselected);
                    adapter.notifyDataSetChanged(); // cap nhat lai adapter
                    Toast.makeText(getApplication(), "Xóa xe thành công!!!", Toast.LENGTH_LONG).show();
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
        inflater.inflate(R.menu.menu_bike, menu);
    }

    @Override
    public boolean onContextItemSelected (MenuItem item) {
        // TODO Auto-generated method stub
        if(item.getItemId() == R.id.mnueditbike){
            Bicycles bicycles = bikeList.get(posselected);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(admin_ManageBike.this, admin_EditBikeActivity.class);
            bundle.putSerializable ("bicycles", bicycles);
            intent.putExtra("data", bundle);
            startActivityForResult(intent, admin_ManageBike.EDIT_BIKE);
            return true;
        } else if (item.getItemId() == R.id.mnudeletebike) {
            comfirmDelete();
            return true;
        } else if (item.getItemId() == R.id.mnuclosebike) {
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
            case admin_ManageBike.OPEN_BIKE:
                if (resultCode == admin_ManageBike.SAVE_BIKE) {
                    Bundle bundle = data.getBundleExtra("data");
                    Bicycles bicycles = (Bicycles) bundle.getSerializable("bicycles");
                    bikeList.add(bicycles);
                    adapter.notifyDataSetChanged();
                }
                break;
            case admin_ManageBike.EDIT_BIKE:
                if (resultCode == admin_ManageBike.SAVE_BIKE) {
                    Bundle bundle = data.getBundleExtra("data");
                    Bicycles bicycles = (Bicycles) bundle.getSerializable("bicycles");
                    bikeList.set(posselected, bicycles);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }
}