package br.com.app.platinumapp.appvendas.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import br.com.app.platinumapp.appvendas.Services.SyncAuto;

public class ExportAutoReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        intent = new Intent(context, SyncAuto.class);
        intent.setAction("INICIAR_SERVICO");
        context.startService(intent);

    }
}
