package br.com.app.platinumapp.appvendas.Importacoes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Configuracoes_SqliteDao;
import br.com.app.platinumapp.appvendas.R;

public class Trocar_ip_servidor extends Activity {

	private Configuracoes_SqliteBean confBean;
	private Configuracoes_SqliteDao confDao;
	private EditText conf_txt_ipserver2;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.muda_ip_servidor);

		conf_txt_ipserver2 = (EditText) findViewById(R.id.conf_txt_ipserver2);
		confBean = new Configuracoes_SqliteBean();
		confDao = new Configuracoes_SqliteDao(this);

		confBean = confDao.BuscaParamentrosEmpresa();
		if (confBean != null) {
			conf_txt_ipserver2.setText(confBean.getIP_SERVER().toString());
		}

	}

	public void UpdateConf(View v) {

		confBean = new Configuracoes_SqliteBean();
		confDao = new Configuracoes_SqliteDao(this);

		confBean.setIP_SERVER(conf_txt_ipserver2.getText().toString());

		confDao.atualiza_endereco_servidor(confBean);

		Intent RegistraEmpresa = new Intent(getBaseContext(),RegistraEmpresa.class);
		startActivity(RegistraEmpresa);
		finish();
	}


    @Override
    public void onBackPressed() {
        finish();
    }

}
