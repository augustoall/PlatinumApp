package br.com.app.platinumapp.appvendas.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Importacoes.Importa_dias_licenca;
import br.com.app.platinumapp.appvendas.Importacoes.VolleySingleton;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;

/**
 * Created by JAVA-PC on 14/05/2017.
 */

public class SyncAuto extends Service {

    private RequestQueue rq;
    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;


    @Override
    public void onCreate() {
        super.onCreate();

        empBean = new Empresa_SqliteBean();
        empDao = new Empresa_SqliteDao(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected) {
            Log.i("cmdv", "SERVICO REINICIADO");
            rq = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
            empBean = empDao.buscarEmpresa();
            Importa_dias_licenca.atualiza_dias_de_licenca(getApplicationContext(), empBean, rq);
        } else {
            Log.i("cmdv", "SEM CONEXAO COM INTERNET PARA SYNCRONIZAR");
        }

        return (super.onStartCommand(intent, flags, startId));
    }


    public IBinder onBind(Intent intent) {
        return null;
    }
}
