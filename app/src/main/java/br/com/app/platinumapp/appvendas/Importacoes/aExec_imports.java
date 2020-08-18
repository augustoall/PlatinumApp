package br.com.app.platinumapp.appvendas.Importacoes;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Model.Cliente_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class aExec_imports extends Activity {

    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private Configuracoes_SqliteBean confBean;
    private Configuracoes_SqliteDao confDao;
    private RequestQueue rq;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.importa_dados);

        empDao = new Empresa_SqliteDao(this);
        empBean = new Empresa_SqliteBean();
        empBean = empDao.buscarEmpresa();
        rq = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
    }


    public void importa(View v) {
       // showProgressDeterminateDialog();
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected) {

            Button btn_importar = (Button) findViewById(R.id.btn_importar);
            btn_importar.setVisibility(View.GONE);

            confBean = new Configuracoes_SqliteBean();
            confDao = new Configuracoes_SqliteDao(getApplicationContext());
            confBean = confDao.BuscaParamentrosEmpresa();

            Util.Mensagem(aExec_imports.this, "Atualizando app...aguarde", Util.THEME_CMDV);
            Util.Mensagem(aExec_imports.this, "Importando dados...", Util.THEME_CMDV);
            Importa_dias_licenca.atualiza_dias_de_licenca(getApplicationContext(), empBean, rq);


            //************************************************* CLIENTES
            new Cliente_SqliteDao(getApplicationContext()).excluir_clientes_enviados();

            if (confBean.getIMPORTAR_TODOS_CLIENTES().equals("S")) {
                String url_all = Util.getBaseUrl(getApplicationContext()) + "/json/exporta_todos_clientes.php";
                Importa_clientes.importaClientes(v, url_all, false, "", rq, getApplicationContext(), empBean);

            }
            if (confBean.getIMPORTAR_TODOS_CLIENTES().equals("N")) {
                String url_where = Util.getBaseUrl(getApplicationContext()) + "/json/exporta_clientes_do_vendedor.php";
                Importa_clientes.importaClientes(v, url_where, true, confBean.getUSU_CODIGO().toString(), rq, getApplicationContext(), empBean);
            }
            //*************************************************

            //************************************************* CIDADES
            Importa_cidades.importa_cidades(v, rq, getApplicationContext(), empBean);
            //*************************************************


            //************************************************* PRODUTOS
            new Produto_SqliteDao(getApplicationContext()).excluir_produtos();
            Importa_produtos.importaProdutos(rq, getApplicationContext(), empBean);
            //*************************************************


            Util.Mensagem(aExec_imports.this, "importacao realizada com sucesso", Util.THEME_CMDV);
        } else {
            Util.Mensagem(aExec_imports.this, "sem conexão com internet", Util.THEME_CMDV);
        }

    }

    public void showProgressDeterminateDialog() {
        new MaterialDialog.Builder(this)
                .title("Progress")
                .content("aguarde...")
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 150, true)
                .cancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (thread != null) {
                            thread.interrupt();
                        }
                    }
                })
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;

                        startThread(new Runnable() {
                            @Override
                            public void run() {
                                while (dialog.getCurrentProgress() != dialog.getMaxProgress()
                                        && !Thread.currentThread().isInterrupted()) {
                                    if (dialog.isCancelled()) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        break;
                                    }
                                    dialog.incrementProgress(1);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        thread = null;
                                        dialog.setContent(getString(R.string.md_done_label));
                                    }
                                });
                            }
                        });


                    }
                })
                .show();
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        rq.cancelAll("tag");
    }


    @Override
    public void onResume() {
        super.onResume();
    }

}
