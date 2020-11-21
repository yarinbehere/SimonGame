package com.example.simongame;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class NotifyIdleService extends Service {
    public NotifyIdleService() {
    }

    private final long SERVICE_TIMER_VALUE = 60000;
    private CountDownTimer timer;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler
    {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                10);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        setTimer();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        timer.cancel();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    private void setTimer()
    {
        timer = new CountDownTimer(SERVICE_TIMER_VALUE, SERVICE_TIMER_VALUE)
        {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                makeToast("You left the game for more than 5 minutes. Come back and play!");
            }
        }.start();
    }

    private void makeToast(String strMessage_to_Display)
    {
            Toast.makeText(this, strMessage_to_Display, Toast.LENGTH_SHORT).show();
    }
}
