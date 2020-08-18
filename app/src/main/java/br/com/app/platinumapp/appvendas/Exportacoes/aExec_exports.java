package br.com.app.platinumapp.appvendas.Exportacoes;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;

import br.com.app.platinumapp.appvendas.BroadcastReceivers.ConnectivityReceiver;
import br.com.app.platinumapp.appvendas.Importacoes.AtualizaDadosVendedor;
import br.com.app.platinumapp.appvendas.Importacoes.Importa_dias_licenca;
import br.com.app.platinumapp.appvendas.Importacoes.Importa_produtos;
import br.com.app.platinumapp.appvendas.Importacoes.VolleySingleton;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Empresa_SqliteDao;
import br.com.app.platinumapp.appvendas.Model.Produto_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class aExec_exports extends Activity {

    private Empresa_SqliteDao empDao;
    private Empresa_SqliteBean empBean;
    private Configuracoes_SqliteBean confBean;
    private Configuracoes_SqliteDao confDao;
    private RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exporta_dados);

        empDao = new Empresa_SqliteDao(this);
        empBean = new Empresa_SqliteBean();
        empBean = empDao.buscarEmpresa();

        rq = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
    }

    public void exporta(View v) {

        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected) {
            Button btn_exportar = (Button) findViewById(R.id.btn_exportar);
            btn_exportar.setVisibility(View.GONE);
            Util.Mensagem(aExec_exports.this, "Atualizando app...aguarde", Util.THEME_CMDV);
            Util.Mensagem(aExec_exports.this, "Exportando dados...", Util.THEME_CMDV);
            Importa_dias_licenca.atualiza_dias_de_licenca(getApplicationContext(), empBean, rq);
            AtualizaDadosVendedor.atualiza_dados_do_vendedor(rq, getApplicationContext());
            Exporta_vendac.exporta_venda_c(rq, getApplicationContext(), empBean);
            Exporta_vendad.exporta_venda_D(rq, getApplicationContext(), empBean);
            ExcluirHistoricosPgto_na_web.excluir_historicos_inseridos_na_web(rq, getApplicationContext(), empBean);
            Exporta_clientes.exporta_clientes_nao_enviados(rq, getApplicationContext(), empBean);
            Exporta_cheques.exporta_cheques_nao_exportados(rq, getApplicationContext(), empBean);
            Exporta_confFpgto.exportaConfFormaPagamento(rq, getApplicationContext(), empBean);
            Exporta_receber.exporta_receber(rq, getApplicationContext(), empBean);
            Exporta_histPgto.exportaHistoricoPgto(rq, getApplicationContext(), empBean);
            new Produto_SqliteDao(getApplicationContext()).excluir_produtos();
            Importa_produtos.importaProdutos(rq, getApplicationContext(), empBean);
            Util.Mensagem(aExec_exports.this, "Consulte www.centralmetadevendas.com.br", Util.THEME_CMDV);
        } else {
            Util.Mensagem(aExec_exports.this, "sem conexão com internet", Util.THEME_CMDV);
        }
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
