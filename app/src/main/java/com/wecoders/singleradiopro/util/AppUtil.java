package com.wecoders.singleradiopro.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import com.wecoders.singleradiopro.R;
import com.wecoders.singleradiopro.databinding.DialogExitBinding;
import com.wecoders.singleradiopro.databinding.DialogReportBinding;

public class AppUtil {

    //dialog for exiting application.
    public static void showExitDialog(Context context, boolean isPlaying, AlertDialogListener dialogListener) {
        String message = "";
        String negativeText = "";
        if (isPlaying) {
            message = context.getResources().getString(R.string.exit_dialog_message);
            negativeText = context.getResources().getString(R.string.exit_negative_button);
        } else {
            message = context.getResources().getString(R.string.exit_dialog_message2);
            negativeText = context.getResources().getString(R.string.exit_negative_button2);
        }


        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        DialogExitBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_exit, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.exitDialogMessage.setText(message);
        binding.btnMinimize.setText(negativeText);

        binding.btnMinimize.setOnClickListener(view -> {
            alertDialog.dismiss();
            if (isPlaying) dialogListener.onCancel();
        });

        binding.btnExit.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onPositive();
        });

        alertDialog.show();
    }

    //dialog for reporting radio station.
    public static void showReportDialog(Context context, AlertDialogListener dialogListener) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        DialogReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(alertDialog.getContext()), R.layout.dialog_report, null, false);
        alertDialog.setView(binding.getRoot(), 30, 20, 30, 20);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        binding.btnReport.setOnClickListener(view -> {
            alertDialog.dismiss();
            dialogListener.onPositive();
        });

        binding.btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }


    public interface AlertDialogListener {
        void onPositive();

        void onCancel();

    }


}
