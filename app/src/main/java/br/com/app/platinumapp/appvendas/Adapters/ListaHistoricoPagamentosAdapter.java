package br.com.app.platinumapp.appvendas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.math.RoundingMode;
import java.util.List;

import br.com.app.platinumapp.appvendas.Model.HistoricoPagamento_SqliteBean;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.Util;


public class ListaHistoricoPagamentosAdapter extends BaseAdapter {

	private Context ctx;
	private List<HistoricoPagamento_SqliteBean> lista_de_historico_parcelas;

	public ListaHistoricoPagamentosAdapter(Context c, List<HistoricoPagamento_SqliteBean> l) {
		this.ctx = c;
		this.lista_de_historico_parcelas = l;

	}
	
	

	@Override
	public int getCount() {

		return lista_de_historico_parcelas.size();
	}

	@Override
	public Object getItem(int position) {

		return lista_de_historico_parcelas.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		HistoricoPagamento_SqliteBean historico = (HistoricoPagamento_SqliteBean) getItem(position);

		LayoutInflater layout = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layout.inflate(R.layout.inflater_lista_dos_hist_das_parcelas_pagas_row, null);

		TextView txv_histparcela = (TextView) view.findViewById(R.id.txv_histparcela);
		TextView txv_histvalorparc = (TextView) view.findViewById(R.id.txv_histvalorparc);
		TextView txv_histvalorpago = (TextView) view.findViewById(R.id.txv_histvalorpago);
		TextView txv_histdatapagamento = (TextView) view.findViewById(R.id.txv_histdatapagamento);
		TextView txv_histrestante = (TextView) view.findViewById(R.id.txv_histrestante);

		txv_histparcela.setText(historico.getHist_numero_parcela().toString());
		txv_histvalorparc.setText(historico.getHist_valor_real_parcela().setScale(2, RoundingMode.HALF_UP).toString());
		txv_histvalorpago.setText(historico.getHist_valor_pago_no_dia().setScale(2, RoundingMode.HALF_UP).toString());
		txv_histdatapagamento.setText(Util.FormataDataDDMMAAAA(historico.getHist_datapagamento()).toString());
		txv_histrestante.setText(historico.getHist_restante_a_pagar().setScale(2, RoundingMode.HALF_UP).toString());

		return view;
	}

}
