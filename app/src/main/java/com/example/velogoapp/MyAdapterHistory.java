package com.example.velogoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterHistory extends ArrayAdapter<Rentals>{
    ArrayList<Rentals> historyList = new ArrayList<Rentals>();

    public MyAdapterHistory(@NonNull Context context, int resource, @NonNull ArrayList<Rentals> objects) {
        super(context, resource, objects);
        historyList = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_history, null);

        ImageView imgBike = (ImageView) v.findViewById(R.id.imgBike);
        TextView txtBikeName = (TextView) v.findViewById(R.id.txtBikeName);
        TextView txtIdUser = (TextView) v.findViewById(R.id.txtIdUser);
        TextView txtPrice = (TextView) v.findViewById(R.id.txtPrice);
        TextView txtDate = (TextView) v.findViewById(R.id.txtDate);
        TextView txtEndDate = (TextView) v.findViewById(R.id.txtEndDate);

        // Lấy tên ảnh từ đối tượng Bicycles
        imgBike.setImageResource(R.drawable.img_bike);
        txtBikeName.setText("Tên xe: " + historyList.get(position).getBicycleName());
        txtIdUser.setText("ID user: " + historyList.get(position).getUserID());
        txtPrice.setText("Giá thuê: " + historyList.get(position).getTotalCost() + ".000 VNĐ");
        txtDate.setText("Ngày thuê: " + historyList.get(position).getRentalStart());
        txtEndDate.setText("Ngày trả: " + historyList.get(position).getRentalEnd());
        return v;
    }
}
