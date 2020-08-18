package br.com.app.platinumapp.appvendas.BroadcastReceivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import br.com.app.platinumapp.appvendas.Main.Applications;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) Applications.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static String getNetworkType_WIFI_MOBILE(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        String retorno = "";
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                retorno = "wifi";
            }
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                retorno = "mobile";
            }
        }
        return retorno;
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}