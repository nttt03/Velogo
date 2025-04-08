
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

public class MyAdapterStations extends ArrayAdapter<RentalStations> {
    ArrayList<RentalStations> rentalStationsList = new ArrayList<RentalStations>();

    public MyAdapterStations(@NonNull Context context, int resource, @NonNull ArrayList<RentalStations> objects) {
        super(context, resource, objects);
        rentalStationsList = objects;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.my_stations, null);

        ImageView imgLocation = (ImageView) v.findViewById(R.id.imgLocation);
        TextView txtID = (TextView) v.findViewById(R.id.txtID);
        TextView txtStationName = (TextView) v.findViewById(R.id.txtStationName);
        TextView txtLocation = (TextView) v.findViewById(R.id.txtLocation);
        TextView txtCapacity = (TextView) v.findViewById(R.id.txtCapacity);

        // Lấy tên ảnh từ đối tượng Bicycles
        imgLocation.setImageResource(R.drawable.img_location);
        txtID.setText("Mã trạm: " + rentalStationsList.get(position).getStationID());
        txtStationName.setText("Tên trạm: " + rentalStationsList.get(position).getStationName());
        txtLocation.setText("Vị trí: " + rentalStationsList.get(position).getLocation());
        txtCapacity.setText("Sức chứa: " + rentalStationsList.get(position).getCapacity());

        return v;
    }
}
