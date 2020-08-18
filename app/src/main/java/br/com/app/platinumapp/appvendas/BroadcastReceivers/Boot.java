package br.com.app.platinumapp.appvendas.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by JAVA-PC on 14/05/2017.
 */

public class Boot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Script", "boot completo");
    }
}
