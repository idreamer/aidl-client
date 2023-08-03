package com.victorchoi.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.victorchoi.server.ICalc;

public class MainActivity extends AppCompatActivity {
    private ICalc svc;
    private final String TAG = "Client-Calc";
    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onService Connected");
            svc = ICalc.Stub.asInterface(service);
            try {
                final int result = getSum();
                Log.d(TAG, Integer.toString(result));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent("calcService");
        intent.setPackage("com.victorchoi.server");

        Boolean result = bindService(intent, conn, BIND_AUTO_CREATE);

        if (result) {
            Log.d(TAG, "binding on start");
            return;
        }

        Log.d(TAG, "failure on binding" + result);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(conn);
        Log.d(TAG, "unbinding on stop");
    }

    private int getSum() throws RemoteException {
        if (svc != null) {
            try {
                return svc.sum(1,2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "svc is null");
        return -1;
    }
}