package br.com.app.platinumapp.appvendas.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.app.platinumapp.appvendas.Adapters.ListaParcelasVencidasAdapter;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteBean;
import br.com.app.platinumapp.appvendas.Model.Receber_SqliteDao;
import br.com.app.platinumapp.appvendas.R;
import br.com.app.platinumapp.appvendas.Util.RecyclerTouchListener;


public class ListaParcelasVencidas extends AppCompatActivity {

    private ListaParcelasVencidasAdapter mParcelasVencidasAdapter;
    private RecyclerView rv_lista_parcelas_vencidas;
    private List<Receber_SqliteBean> mReceber_sqliteBeenList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private View positiveAction;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_parcelas_vencidas);

        rv_lista_parcelas_vencidas = (RecyclerView) findViewById(R.id.rv_lista_parcelas_vencidas);

        rv_lista_parcelas_vencidas.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rv_lista_parcelas_vencidas.setLayoutManager(mLayoutManager);

        Calendar calendario = Calendar.getInstance(new Locale("pt", "BR"));
        Date data = calendario.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Log.i("script", "data de hoje :: " + dateFormat.format(data));

        mReceber_sqliteBeenList = new Receber_SqliteDao(getApplicationContext()).buscar_parcelas_vencidas_hoje(dateFormat.format(data));
        mParcelasVencidasAdapter = new ListaParcelasVencidasAdapter(mReceber_sqliteBeenList, this);
        rv_lista_parcelas_vencidas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_lista_parcelas_vencidas.setAdapter(mParcelasVencidasAdapter);

        rv_lista_parcelas_vencidas.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rv_lista_parcelas_vencidas, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Produto_SqliteBean produto = mProduto_SqliteBeanList.get(position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

}
