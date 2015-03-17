package com.rwssistent.LARA.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.rwssistent.LARA.R;

/**
 * Created by Samme on 8-3-2015.
 */
public class UI {

    public static void displayExitDialog(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setTitle(activity.getString(R.string.dialog_exit_title));
        adb.setMessage(activity.getString(R.string.dialog_exit_text));
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setNegativeButton(activity.getString(R.string.dialog_btn_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.setPositiveButton(activity.getString(R.string.dialog_btn_exit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        adb.show();
    }
}