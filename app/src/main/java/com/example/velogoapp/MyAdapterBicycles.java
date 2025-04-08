package com.example.velogoapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyAdapterBicycles extends ArrayAdapter<Bicycles>{
    ArrayList<Bicycles> bicyclesList = new ArrayList<Bicycles>();

    public MyAdapterBicycles(@NonNull Context context, int resource, @NonNull ArrayList<Bicycles> objects) {
        super(context, resource, objects);
        bicyclesList = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_bicycles, null);

        ImageView imgbicycle = (ImageView) v.findViewById(R.id.imgBicycle);
        TextView txtModelName = (TextView) v.findViewById(R.id.txtModelName);
        TextView txtType = (TextView) v.findViewById(R.id.txtType);
        TextView txtStatus = (TextView) v.findViewById(R.id.txtStatus);
        TextView txtPrice = (TextView) v.findViewById(R.id.txtPrice);

        imgbicycle.setImageResource(R.drawable.img_bike);
        txtModelName.setText(bicyclesList.get(position).getModelName());
        txtType.setText("Loại xe: " + bicyclesList.get(position).getType());
        txtStatus.setText("Trạng thái: " + bicyclesList.get(position).getStatus());
        txtPrice.setText("Giá thuê: " + bicyclesList.get(position).getRentalPricePerHour() + ".000 VNĐ");

        return v;
    }
}
