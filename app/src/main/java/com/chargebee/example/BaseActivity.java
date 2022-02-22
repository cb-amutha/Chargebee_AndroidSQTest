package com.chargebee.example;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;

    public void showProgressDialog(){
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
                progressDialog.setCancelable(false);
            }
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
        }
        catch (Exception e){
            Log.e("BaseActivity",e.getMessage());
        }
    }
    public void hideProgressDialog(){
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (IllegalArgumentException e) {
            Log.e("BaseActivity",e.getMessage());
        }
        progressDialog = null;
    }

    public void alertSuccess(String subcriptionStatus) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Chargebee")
                .setMessage("Subscription Status :"+subcriptionStatus)
                .setPositiveButton("Ok", null).show();

    }

    protected void showDialog(String msg){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_layout);
        TextView textViewMessage =  dialog.findViewById(R.id.tv_message);
        textViewMessage.setText(msg);
        Button dialogButton =  dialog.findViewById(R.id.dialogButtonOK);
        dialogButton.setOnClickListener(view ->  {
                dialog.dismiss();
        });
        dialog.show();
    }
}
