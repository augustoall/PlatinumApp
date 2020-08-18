package br.com.app.platinumapp.appvendas.Services;

/**
 * Esta classe recebe o ID de registro firebase que será exclusivo para cada aplicativo.
 * Este ID de registro é necessário quando você quer enviar uma mensagem a um único dispositivo.
 * Você pode enviar esse token para o aplicativo servidor para enviar notificação para dispositivos mais tarde.
 * <p>
 * <p>
 * <p>
 * methods
 * <p>
 * onTokenRefresh () método será chamado sempre que há uma mudança na ID de registro firebase.
 * <p>
 * storeRegIdInPref () armazena o ID de registro em shared-preferences .
 * <p>
 * LocalBroadcastManager - gerente de Broadcast transmite o ID de registro de todas as atividades aqueles estão ouvindo.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import br.com.app.platinumapp.appvendas.Importacoes.VolleySingleton;
import br.com.app.platinumapp.appvendas.Util.Util;
import br.com.app.platinumapp.appvendas.Util.TokenObject;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    private static final String URL = "https://cmdv.sistemaseapps.com.br/FCM/get_device_id_firebase.php";
    private RequestQueue rq;
    private TokenObject tokenObject;


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        rq = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);


        // sending reg id to your server
        sendTheRegisteredTokenToWebServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Util.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }


    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Util.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }


    private void sendTheRegisteredTokenToWebServer(final String token) {
        rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringPostRequest = new StringRequest(
                Request.Method.POST,
                URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                tokenObject = gson.fromJson(response, TokenObject.class);
                if (null == tokenObject) {
                    Util.Mensagem(getApplicationContext(), "Não foi possível registrar o token firebese ", Util.THEME_CMDV);
                } else {
                    Util.Mensagem(getApplicationContext(), "Token Firebase Registrado", Util.THEME_CMDV);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Util.Mensagem(getApplicationContext(), error.getMessage().toString(), Util.THEME_CMDV);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_celularkey", Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("device_id_firebase", token);
                return params;
            }
        };
        rq.add(stringPostRequest);
    }


}


