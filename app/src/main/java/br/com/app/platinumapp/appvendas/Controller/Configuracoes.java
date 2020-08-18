package br.com.app.platinumapp.appvendas.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.math.BigDecimal;

import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class Configuracoes extends Activity {

    private EditText conf_txt_usu_codigo;
    private RadioGroup conf_rg_importclientes;
    private EditText conf_txt_ipserver;
    private CheckBox conf_cb_vender_sem_estoque;
    private CheckBox conf_cb_pedir_senha_venda;
    private CheckBox conf_cb_permitir_vendaacimalimite;
    private EditText conf_txt_descontovendedor;
    private EditText conf_txt_jurosvendaprazo;
    private String importar_todos_clientes;


    private Configuracoes_SqliteBean confBean;
    private Configuracoes_SqliteDao confDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configuracoes);

        declaraObjetos();
        confBean = new Configuracoes_SqliteBean();
        confDao = new Configuracoes_SqliteDao(this);

        // desabilitando o campo de desconto, so pode alterar o desconto o adm no sistema web
        conf_txt_descontovendedor.setEnabled(false);


        confBean = confDao.BuscaParamentrosEmpresa();
        if (confBean != null) {

            int Opcoes = 0;
            if (confBean.getIMPORTAR_TODOS_CLIENTES().equals("S"))
                Opcoes = R.id.conf_rb_todosclientes;
            else
                Opcoes = R.id.conf_rb_meusclientes;
            conf_rg_importclientes.check(Opcoes);


            conf_txt_usu_codigo.setText(confBean.getUSU_CODIGO().toString());
            conf_txt_ipserver.setText(confBean.getIP_SERVER().toString());
            conf_txt_descontovendedor.setText(confBean.getDESCONTO_VENDEDOR().toString());
            conf_txt_jurosvendaprazo.setText(confBean.getJUROS_VENDA_PRAZO().toString());

            if (confBean.getVENDER_SEM_ESTOQUE().equals("S"))
                conf_cb_vender_sem_estoque.setChecked(true);
            else
                conf_cb_vender_sem_estoque.setChecked(false);

            if (confBean.getPEDIR_SENHA_NA_VENDA().equals("S"))
                conf_cb_pedir_senha_venda.setChecked(true);
            else
                conf_cb_pedir_senha_venda.setChecked(false);

            if (confBean.getPERMITIR_VENDER_ACIMA_DO_LIMITE().equals("S"))
                conf_cb_permitir_vendaacimalimite.setChecked(true);
            else
                conf_cb_permitir_vendaacimalimite.setChecked(false);

        }

    }

    private boolean validou_campos() {

        declaraObjetos();

        if (conf_txt_ipserver.getText().toString().length() <= 0) {
            conf_txt_ipserver.setError("informe o endereço do servidor");
            conf_txt_ipserver.requestFocus();
            return false;
        }

        if (conf_txt_usu_codigo.getText().toString().length() <= 0) {
            conf_txt_usu_codigo.setError("informe o código do vendedor neste app");
            conf_txt_usu_codigo.requestFocus();
            return false;
        }

        if (conf_txt_jurosvendaprazo.getText().toString().length() <= 0) {
            conf_txt_jurosvendaprazo.setError("informe (0)ZERO ou o juros a ser aplicado na venda a prazo");
            conf_txt_jurosvendaprazo.requestFocus();
            return false;
        }

        return true;
    }

    public void declaraObjetos() {
        conf_txt_usu_codigo = (EditText) findViewById(R.id.conf_txt_usu_codigo);
        conf_rg_importclientes = (RadioGroup) findViewById(R.id.conf_rg_importclientes);
        conf_txt_ipserver = (EditText) findViewById(R.id.conf_txt_ipserver);
        conf_txt_descontovendedor = (EditText) findViewById(R.id.conf_txt_descontovendedor);
        conf_cb_vender_sem_estoque = (CheckBox) findViewById(R.id.conf_cb_vender_sem_estoque);
        conf_cb_pedir_senha_venda = (CheckBox) findViewById(R.id.conf_cb_pedir_senha_venda);
        conf_cb_permitir_vendaacimalimite = (CheckBox) findViewById(R.id.conf_cb_permitir_vendaacimalimite);
        conf_txt_jurosvendaprazo = (EditText) findViewById(R.id.conf_txt_jurosvendaprazo);
    }

    public void atualizar_config(View v) {

        if (validou_campos()) {


            Configuracoes_SqliteBean config = new Configuracoes_SqliteBean();
            Configuracoes_SqliteDao mConfDao = new Configuracoes_SqliteDao(getApplicationContext());


            switch (conf_rg_importclientes.getCheckedRadioButtonId()) {
                case R.id.conf_rb_todosclientes:
                    importar_todos_clientes = "S";
                    break;
                case R.id.conf_rb_meusclientes:
                    importar_todos_clientes = "N";
                    break;
            }

            config.setUSU_CODIGO(Integer.valueOf(String.valueOf(conf_txt_usu_codigo.getText())));
            config.setIMPORTAR_TODOS_CLIENTES(importar_todos_clientes);
            config.setIP_SERVER(conf_txt_ipserver.getText().toString());


            config.setDESCONTO_VENDEDOR(new BigDecimal(conf_txt_descontovendedor.getText().toString()));

            if (conf_cb_vender_sem_estoque.isChecked())
                config.setVENDER_SEM_ESTOQUE("S");
            else
                config.setVENDER_SEM_ESTOQUE("N");

            if (conf_cb_pedir_senha_venda.isChecked())
                config.setPEDIR_SENHA_NA_VENDA("S");
            else
                config.setPEDIR_SENHA_NA_VENDA("N");

            if (conf_cb_permitir_vendaacimalimite.isChecked())
                config.setPERMITIR_VENDER_ACIMA_DO_LIMITE("S");
            else
                config.setPERMITIR_VENDER_ACIMA_DO_LIMITE("N");

            config.setJUROS_VENDA_PRAZO(new BigDecimal(conf_txt_jurosvendaprazo.getText().toString()));

            config.setNOME_VENDEDOR(confBean.getNOME_VENDEDOR());

            mConfDao.atualiza_configuracoes(config);

            finish();
            Util.Mensagem(getBaseContext(), "configurações atualizadas", Util.THEME_CMDV);
        }

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
