package br.com.app.platinumapp.appvendas.Main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;


public class Applications extends Application {
    private static Applications mInstance;

    public static synchronized Applications getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
