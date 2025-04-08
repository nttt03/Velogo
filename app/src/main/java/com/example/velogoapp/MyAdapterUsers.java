package com.example.velogoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyAdapterUsers extends ArrayAdapter<User> {
    ArrayList<User> userList = new ArrayList<User>();

    public MyAdapterUsers (@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        userList = objects;
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_user, null);

        ImageView imgUser = (ImageView) v.findViewById(R.id.imgUser);
        TextView txtNameUser = (TextView) v.findViewById(R.id.txtNameUser);
        TextView txtPhone = (TextView) v.findViewById(R.id.txtPhone);
        TextView txtGender = (TextView) v.findViewById(R.id.txtGender);
        TextView txtAddress = (TextView) v.findViewById(R.id.txtAddress);

        String gender = userList.get(position).getGender();
        if ("Nam".equals(gender)) {
            imgUser.setImageResource(R.drawable.img_male_icon);
        } else if ("Nữ".equals(gender)) {
            imgUser.setImageResource(R.drawable.img_female_icon);
        } else {
            imgUser.setImageResource(R.drawable.img_question);
        }

        txtNameUser.setText("Tên: " + userList.get(position).getFullName());
        txtPhone.setText("SĐT: " + userList.get(position).getPhoneNumber());
        txtGender.setText("Giới tính: " + userList.get(position).getGender());
        txtAddress.setText("Địa chỉ: " + userList.get(position).getAddress());

        return v;
    }
}
