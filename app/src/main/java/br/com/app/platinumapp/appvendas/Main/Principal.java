package br.com.app.platinumapp.appvendas.Main;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Controller.Configuracoes;
import br.com.app.platinumapp.appvendas.Controller.CatalogoProdutosRecyclerView;
import br.com.app.platinumapp.appvendas.Controller.ListaClientesEditar;
import br.com.app.platinumapp.appvendas.Controller.ListaClientesReceber;
import br.com.app.platinumapp.appvendas.Controller.ListaClientesVenda;
import br.com.app.platinumapp.appvendas.Controller.ListaParcelasVencidas;
import br.com.app.platinumapp.appvendas.Controller.ListaVendasRealizadas;
import br.com.app.platinumapp.appvendas.Exportacoes.aExec_exports;
import br.com.app.platinumapp.appvendas.Importacoes.AtualizaDadosVendedor;
import br.com.app.platinumapp.appvendas.Importacoes.CadastraCliente;
import br.com.app.platinumapp.appvendas.Importacoes.Pedir_chave_de_licenca;
import br.com.app.platinumapp.appvendas.Importacoes.VolleySingleton;
import br.com.app.platinumapp.appvendas.Importacoes.aExec_imports;
import br.com.app.platinumapp.appvendas.Model.Cheque_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.VendaDAO;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class Principal extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private ImageView imgwifi;
    private ImageView imgmobile;
    private ImageView imgdisconected;
    private TextView txvValidadedias;
    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private boolean isConect;
    private Button btn_import;
    private Button btn_export;
    private Button btn_licenca;
    private Button btn_loadimages;
    private Button btn_atualiza_clienteoffline;
    private RequestQueue rq;
    private TextView txvcreditos;
    private static final String TAG = Principal.class.getSimpleName();
    private int contador_parcelas_vencidas = 0;
    private int contador_dias_acesso = 0;
    private SharedPreferences.Editor editorSharedPref;

    private SharedPreferences mSharedPreferences;
    private List<Receber_SqliteBean> mReceber_sqliteBeenList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_dashboard);

        empBean = new Empresa_SqliteBean();
        empDao = new Empresa_SqliteDao(this);
        empBean = empDao.buscarEmpresa();
        sendbroadcast();
        rq = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        declaraObjetos();
        initialize();
        updateNetwork();
        displayFirebaseRegId();

        display_parcelas_vencidas();

        txvValidadedias = (TextView) findViewById(R.id.princ_txv000);

        Intent mensagem = getIntent();
        String msg = mensagem.getStringExtra("mensagem");
        if (msg != null) {
            txvValidadedias.setText(msg.toUpperCase());
        } else {

            if (empBean != null) {
                txvValidadedias.setText("faltam  (" + empBean.getEmp_totalemdias().toString() + ")  dias para terminar sua licenca");
            }
        }

        if (empBean != null) {
            int dias = Integer.parseInt(empBean.getEmp_totalemdias());
            if (dias <= 3) {
                if (dias == 0) {
                    finish();
                    Util.Mensagem(Principal.this, "licenca expirada", Util.THEME_CMDV);
                    Intent Pedir_chave_de_licenca = new Intent(this, Pedir_chave_de_licenca.class);
                    startActivity(Pedir_chave_de_licenca);
                } else {


                    mSharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
                    contador_dias_acesso = mSharedPreferences.getInt("notificar_dias_acesso", 0);

                    SharedPreferences.Editor editorSharedPref = mSharedPreferences.edit();
                    editorSharedPref.putInt("notificar_dias_acesso", contador_dias_acesso + 1);
                    editorSharedPref.commit();

                    contador_dias_acesso = mSharedPreferences.getInt("notificar_dias_acesso", 0);
                    Log.i("script", "mSharedPreferences : " + contador_dias_acesso);

                    if (contador_dias_acesso <= 3) {
                        gerarNotificacao(dias);
                    }

                    if (contador_dias_acesso > 8) {
                        SharedPreferences.Editor edit = mSharedPreferences.edit();
                        edit.putInt("notificar_dias_acesso", 0);
                        edit.commit();
                    }


                }
            }
        }
        // FirebaseMessaging.getInstance().subscribeToTopic(Util.TOPIC_GLOBAL);
    }


    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Util.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        Log.e(TAG, "Firebase reg id: " + regId);
    }

    private void display_parcelas_vencidas() {

        Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
        Date data = calendario.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.i("script", "data de hoje :: " + dateFormat.format(data));

        mReceber_sqliteBeenList = new Receber_SqliteDao(getApplicationContext()).buscar_parcelas_vencidas_hoje(dateFormat.format(data));

        if (mReceber_sqliteBeenList.size() > 0) {

            mSharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
            contador_parcelas_vencidas = mSharedPreferences.getInt("notificar_parcelas_vencidas", 0);
            SharedPreferences.Editor editorSharedPref = mSharedPreferences.edit();
            editorSharedPref.putInt("notificar_parcelas_vencidas", contador_parcelas_vencidas + 1);
            editorSharedPref.commit();

            contador_parcelas_vencidas = mSharedPreferences.getInt("notificar_parcelas_vencidas", 0);
            Log.i("script", "mSharedPreferences : " + contador_parcelas_vencidas);

            if (contador_parcelas_vencidas <= 3) {
                sendNotification(mReceber_sqliteBeenList.size());
            }

            if (contador_parcelas_vencidas > 8) {
                mSharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putInt("notificar_parcelas_vencidas", 0);
                edit.commit();
            }


        } else {
            Log.i("script", "nao exitem parcelas com vencimento pra hoje ");
        }
    }


    private void updateNetwork() {
        String networktype = ConnectivityReceiver.getNetworkType_WIFI_MOBILE(this);

        if (networktype.equals("wifi")) {
            imgwifi.setVisibility(View.VISIBLE);
            imgmobile.setVisibility(View.GONE);
            imgdisconected.setVisibility(View.GONE);
        }
        if (networktype.equals("mobile")) {
            imgwifi.setVisibility(View.GONE);
            imgmobile.setVisibility(View.VISIBLE);
            imgdisconected.setVisibility(View.GONE);
        }
        if (networktype.equals("")) {
            imgwifi.setVisibility(View.GONE);
            imgmobile.setVisibility(View.GONE);
            imgdisconected.setVisibility(View.VISIBLE);
        }
    }

    private void sendbroadcast() {
        boolean alarmeAtivo = (PendingIntent.getBroadcast(this, 0, new Intent("SEND_BROADCAST_EXPORT"), PendingIntent.FLAG_NO_CREATE) == null);

        if (alarmeAtivo) {
            Log.i("script", "novo broadcast export");
            Intent intent = new Intent("SEND_BROADCAST_EXPORT");
            PendingIntent p = PendingIntent.getBroadcast(this, 0, intent, 0);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(System.currentTimeMillis());
            c.add(Calendar.SECOND, 2);
            AlarmManager alarme = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarme.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 5000, p);
        } else {
            Log.i("Script", "broadcast export ativo");
        }
    }


    public void gerarNotificacao(int dias) {

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent p = PendingIntent.getActivity(this, 0, new Intent(this, Pedir_chave_de_licenca.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Central Meta Vendas");
        builder.setContentTitle("central de vendas");
        builder.setSmallIcon(R.drawable.cmdvic);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cmdvic));
        builder.setContentIntent(p);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descs = new String[]{"VOCE TEM " + dias + " DIAS PARA USAR O SISTEMA", "Clique aqui e atualize sua licenca.", "entre em contato com a empresa", "", ""};
        for (int i = 0; i < descs.length; i++) {
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.cmdvic, n);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        } catch (Exception e) {
        }
    }


    public void sendNotification(int quantidadeparcelasvencidas) {

        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, ListaParcelasVencidas.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, randomInt, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Central Meta Vendas");
        builder.setContentTitle("Parcelas vencidas Hoje");
        builder.setSmallIcon(R.drawable.cmdvic);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.cmdvic));
        builder.setContentIntent(contentIntent);

        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        String[] descs = new String[]{"Clique aqui para ver", "Parcelas vencidas = " + quantidadeparcelasvencidas, "", "", ""};
        for (int i = 0; i < descs.length; i++) {
            style.addLine(descs[i]);
        }
        builder.setStyle(style);

        Notification n = builder.build();
        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_AUTO_CANCEL;
        nm.notify(R.drawable.cmdvic, n);

        try {
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        } catch (Exception e) {
        }
    }

    public void vettodasvendas(View v) {
        Intent ListaVendasRealizadas = new Intent(this, ListaVendasRealizadas.class);
        startActivity(ListaVendasRealizadas);
    }


    public void exportardados(View v) {
        Intent aExec_exports = new Intent(this, aExec_exports.class);
        startActivity(aExec_exports);
    }

    public void importardados(View v) {
        Intent aExec_imports = new Intent(this, aExec_imports.class);
        startActivity(aExec_imports);
    }

    public void loadCatalogo(View v) {
        Intent ImageLoaderProdutos = new Intent(this, CatalogoProdutosRecyclerView.class);
        startActivity(ImageLoaderProdutos);
    }

    public void EditarCliente(View v) {
        Intent EditarCliente = new Intent(getBaseContext(), ListaClientesEditar.class);
        startActivity(EditarCliente);
    }

    public void receber(View v) {
        Intent ListaClientesReceber = new Intent(getBaseContext(), ListaClientesReceber.class);
        startActivity(ListaClientesReceber);
    }

    public void CadCliente(View v) {
        Intent cadcliente = new Intent(getBaseContext(), CadastraCliente.class);
        startActivity(cadcliente);

    }

    public void configuracoes(View v) {
        Intent Configuracoes = new Intent(getBaseContext(), Configuracoes.class);
        startActivity(Configuracoes);
    }

    public void adquirilicenca(View v) {
        Intent getlicenca = new Intent(this, Pedir_chave_de_licenca.class);
        startActivity(getlicenca);
    }


    public void update_sync(View v) {

        Util.Mensagem(Principal.this, "Atualizando Aplicativo...", Util.THEME_CMDV);
        // atualiza os dados do cliente quando foi cadastrado offline
        List<Cliente_SqliteBean> clientes_cad_offline = new Cliente_SqliteDao(getApplicationContext()).busca_clientes_cadastrados_offline();

        if (clientes_cad_offline != null) {
            for (Cliente_SqliteBean cliente : clientes_cad_offline) {
                new Receber_SqliteDao(getApplicationContext()).atualiza_dados_cliente_cadastrado_offline_receber(cliente.getCli_codigo(), Integer.valueOf(cliente.getCli_chave()));
                new VendaDAO(getApplicationContext()).atualiza_dados_cliente_cadastrado_offline_vendac(cliente.getCli_codigo(), Integer.valueOf(cliente.getCli_chave()));
                new Cheque_SqliteDao(getApplicationContext()).atualiza_dados_cliente_cadastrado_offline_cheque(cliente.getCli_codigo(), Integer.valueOf(cliente.getCli_chave()));
            }
        }

        AtualizaDadosVendedor.atualiza_dados_do_vendedor(rq, getApplicationContext());
    }


    public void realiza_vendas(View v) {

        Configuracoes_SqliteBean confbean = new Configuracoes_SqliteBean();
        Configuracoes_SqliteDao confdao = new Configuracoes_SqliteDao(getApplicationContext());

        confbean = confdao.BuscaParamentrosEmpresa();
        if (confbean != null) {
            if (confbean.getUSU_CODIGO().equals("0") || confbean.getUSU_CODIGO().equals("")) {
                Util.Mensagem(Principal.this, "ATUALIZE O CODIGO DO VENDEDOR EM CONFIG", Util.THEME_CMDV);
            } else {
                Intent ListaClientes = new Intent(getBaseContext(), ListaClientesVenda.class);
                startActivity(ListaClientes);
            }

        }
    }


    public void declaraObjetos() {
        btn_export = (Button) findViewById(R.id.btn_export);
        btn_import = (Button) findViewById(R.id.btn_import);
        btn_loadimages = (Button) findViewById(R.id.btn_loadimages);
        btn_licenca = (Button) findViewById(R.id.btn_licenca);
        imgwifi = (ImageView) findViewById(R.id.imgwifi);
        imgmobile = (ImageView) findViewById(R.id.imgmobile);
        imgdisconected = (ImageView) findViewById(R.id.imgdisconected);
        txvcreditos = (TextView) findViewById(R.id.txvcreditos);
        btn_atualiza_clienteoffline = (Button) findViewById(R.id.btn_atualiza_clienteoffline);

        txvcreditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Creditos = new Intent(getBaseContext(), Creditos.class);
                startActivity(Creditos);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        Applications.getInstance().setConnectivityListener(this);
        initialize();
        updateNetwork();


    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
        updateNetwork();
    }

    public void initialize() {
        isConect = ConnectivityReceiver.isConnected();
        btn_export.setEnabled(isConect);
        btn_import.setEnabled(isConect);
        btn_licenca.setEnabled(isConect);
        btn_loadimages.setEnabled(isConect);
        btn_atualiza_clienteoffline.setEnabled(isConect);
        updateNetwork();
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.i("script", " onNetworkConnectionChanged() is called ");
        btn_export.setEnabled(isConnected);
        btn_import.setEnabled(isConnected);
        btn_licenca.setEnabled(isConnected);
        btn_loadimages.setEnabled(isConnected);
        btn_atualiza_clienteoffline.setEnabled(isConnected);
        updateNetwork();
    }
}
