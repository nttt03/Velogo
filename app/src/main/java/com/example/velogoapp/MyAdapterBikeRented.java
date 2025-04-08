package com.example.velogoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterBikeRented extends ArrayAdapter<Rentals>{
    ArrayList<Rentals> bikeRentedList = new ArrayList<Rentals>();

    public MyAdapterBikeRented(@NonNull Context context, int resource, @NonNull ArrayList<Rentals> objects) {
        super(context, resource, objects);
        bikeRentedList = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        // Nếu view là null, tạo mới và ánh xạ các view
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.my_bike_rented, parent, false);

            holder = new ViewHolder();
            holder.imgBike = v.findViewById(R.id.imgBike);
            holder.txtBikeName = v.findViewById(R.id.txtBikeName);
            holder.txtIdUser = v.findViewById(R.id.txtIdUser);
            holder.txtPrice = v.findViewById(R.id.txtPrice);
            holder.txtDate = v.findViewById(R.id.txtDate);
            holder.txtEndDate = v.findViewById(R.id.txtEndDate);
            holder.btnHuy = v.findViewById(R.id.btnHuy);
            holder.btnTra = v.findViewById(R.id.btnTra);

            // Gắn ViewHolder vào view để tái sử dụng
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        // Cập nhật dữ liệu
        Rentals rental = bikeRentedList.get(position);
        holder.imgBike.setImageResource(R.drawable.img_bike);
        holder.txtBikeName.setText(rental.getBicycleName());
        holder.txtIdUser.setText("ID user: " + rental.getUserID());
        holder.txtPrice.setText("Giá thuê: " + rental.getTotalCost() + ".000 VNĐ");
        holder.txtDate.setText("Ngày thuê: " + rental.getRentalStart());
        holder.txtEndDate.setText("Ngày trả: " + rental.getRentalEnd());

        // Thiết lập sự kiện cho button "Hủy"
        holder.btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hiển thị dialog xác nhận hủy
                showCancelDialog(rental.getRentalID(), position);
            }
        });

        holder.btnTra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCompleteDialog(rental.getRentalID(), position);
            }
        });

        return v;
    }

    private void showCancelDialog(final String rentalID, final int position) {
        // Tạo dialog xác nhận hủy
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận hủy")
                .setMessage("Bạn có chắc chắn muốn hủy thuê xe này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cập nhật trạng thái trong cơ sở dữ liệu khi người dùng đồng ý
                        updateRentalStatusToCancelled(rentalID, position);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateRentalStatusToCancelled(String rentalID, int position) {
        SQLiteDatabase db = getContext().openOrCreateDatabase(Login.DATABASE_NAME, Context.MODE_PRIVATE, null);

        // Cập nhật trạng thái "Đã hủy" trong bảng Rentals
        String updateQuery = "UPDATE Rentals SET Status = 'Đã hủy' WHERE RentalID = ?";
        db.execSQL(updateQuery, new Object[]{rentalID});

        // Thông báo cho người dùng
        Toast.makeText(getContext(), "Đã hủy thuê xe", Toast.LENGTH_SHORT).show();

        // Loại bỏ mục đã hủy khỏi danh sách
        bikeRentedList.remove(position);

        // Cập nhật lại adapter và giao diện
        notifyDataSetChanged();
    }

    private void showCompleteDialog(final String rentalID, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Xác nhận trả xe")
                .setMessage("Bạn có chắc chắn muốn trả xe này không?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateRentalStatusToCompleted(rentalID, position);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void updateRentalStatusToCompleted(String rentalID, int position) {
        SQLiteDatabase db = getContext().openOrCreateDatabase(Login.DATABASE_NAME, Context.MODE_PRIVATE, null);

        // Cập nhật trạng thái "Hoàn thành" trong bảng Rentals
        String updateQuery = "UPDATE Rentals SET Status = 'Hoàn thành' WHERE RentalID = ?";
        db.execSQL(updateQuery, new Object[]{rentalID});

        // Thông báo cho người dùng
        Toast.makeText(getContext(), "Đã trả xe", Toast.LENGTH_SHORT).show();

        // Loại bỏ mục đã trả khỏi danh sách
        bikeRentedList.remove(position);

        // Cập nhật lại adapter và giao diện
        notifyDataSetChanged();
    }
    // Tạo lớp ViewHolder để tối ưu hóa việc ánh xạ view
    static class ViewHolder {
        ImageView imgBike;
        TextView txtBikeName;
        TextView txtIdUser;
        TextView txtPrice;
        TextView txtDate;
        TextView txtEndDate;
        Button btnHuy;
        Button btnTra;
    }
}
