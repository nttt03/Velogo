package com.example.velogoapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Notify {
    public static void exit(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.customdialog_layout, null);

        ConstraintLayout dialogConstraintLayout = view.findViewById(R.id.dialogconstraintlayout);
        Button btnNo = view.findViewById(R.id.btnNo);
        Button btnYes = view.findViewById(R.id.btnYes);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(1);
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                View layout = LayoutInflater.from(context).inflate(R.layout.layout_toast_success, null);
                TextView tvCustomToast = layout.findViewById(R.id.tvCustomToast);
                tvCustomToast.setText("Bạn đã click vào nút hủy");
                Toast toast = new Toast(context);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }
}
