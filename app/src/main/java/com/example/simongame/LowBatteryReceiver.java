package com.example.simongame;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LowBatteryReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_BATTERY_LOW))
        {
            new AlertDialog.Builder(context)
                    .setTitle("Low Battery Alert!")
                    .setMessage("Please connect charger.")
                    .setPositiveButton(R.string.posButton, null)
                    .show();
        }
    }//onReceive
}
