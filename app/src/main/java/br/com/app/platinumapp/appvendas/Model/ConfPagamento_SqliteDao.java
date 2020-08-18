package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConfPagamento_SqliteDao {

    private static final String TABLE_NAME = "CONF_PAGAMENTO";

    private Context ctx;
    private String Sql;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;
    private boolean Gravacao;

    public ConfPagamento_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean p_gravar_confpagamento(ConfPagamento_SqliteBean pagamento) {

        db = new Db(ctx).getWritableDatabase();
        Gravacao = false;

        try {

            Sql = "insert into CONF_PAGAMENTO  (pag_sementrada_comentrada,pag_tipo_pagamento,pag_recebeucom_din_chq_cart,pag_valor_recebido,pag_parcelas_normal,pag_parcelas_cartao,vendac_chave,pag_enviado)  values (?,?,?,?,?,?,?,?)";

            stmt = db.compileStatement(Sql);

            stmt.bindString(1, pagamento.getPag_sementrada_comentrada());
            stmt.bindString(2, pagamento.getPag_tipo_pagamento());
            stmt.bindString(3, pagamento.getPag_recebeucom_din_chq_cart());
            stmt.bindDouble(4, pagamento.getPag_valor_recebido().doubleValue());
            stmt.bindLong(5, pagamento.getPag_parcelas_normal());
            stmt.bindLong(6, pagamento.getPag_parcelas_cartao());
            stmt.bindString(7, pagamento.getVendac_chave());
            stmt.bindString(8, pagamento.getPag_enviado());

            stmt.executeInsert();

        } catch (SQLiteException e) {
            Log.d("gravar_confpagamento", e.getMessage());
            Gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }

        return Gravacao;
    }


    public void AtualizaVendac_chaveConfPagamento(String vendac_chave) {

        try {

            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "update CONF_PAGAMENTO set vendac_chave = ? where vendac_chave  = 'sem_chave' ";
            SQLiteStatement stmt = db.compileStatement(sql);

            stmt.bindString(1, vendac_chave);

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("AtualizaVendac_chaveC", e.getMessage());
        }
    }


    public void AtualizaConfPagamentoParaEnviado(String vendac_chave) {

        try {

            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "update CONF_PAGAMENTO set pag_enviado = 'S' where vendac_chave  = ? ";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, vendac_chave);
            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("AtualizaConfPagamentoPa", e.getMessage());
        }
    }

    public List<ConfPagamento_SqliteBean> p_lista_de_pagamentos() {
        List<ConfPagamento_SqliteBean> lista = new ArrayList<>();

        try {
            db = new Db(ctx).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CONF_PAGAMENTO WHERE vendac_chave = 'sem_chave' ", null);
            while (cursor.moveToNext()) {
                ConfPagamento_SqliteBean conf = new ConfPagamento_SqliteBean();
                conf.setPag_codigo(cursor.getInt(cursor.getColumnIndex(conf.CODIGO_PAGAMENTO)));
                conf.setPag_parcelas_cartao(cursor.getInt(cursor.getColumnIndex(conf.PARCELA_CARTAO)));
                conf.setPag_parcelas_normal(cursor.getInt(cursor.getColumnIndex(conf.PARCELA_NORMAL)));
                conf.setPag_recebeucom_din_chq_cart(cursor.getString(cursor.getColumnIndex(conf.RECEBEU_COM)));
                conf.setPag_sementrada_comentrada(cursor.getString(cursor.getColumnIndex(conf.ENTRADA)));
                conf.setPag_tipo_pagamento(cursor.getString(cursor.getColumnIndex(conf.TIPO_PAGAMENTO)));
                conf.setPag_valor_recebido(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(conf.VALOR_RECEBIDO))));
                conf.setVendac_chave(cursor.getString(cursor.getColumnIndex(conf.VENDAC_CHAVE)));
                lista.add(conf);
            }
        } catch (SQLiteException e) {
            Log.d("lista_de_pagamentos", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return lista;
    }


    public List<ConfPagamento_SqliteBean> busca_todos_confPagamentos() {
        List<ConfPagamento_SqliteBean> lista = new ArrayList<>();

        try {
            db = new Db(ctx).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CONF_PAGAMENTO WHERE pag_enviado = 'N' and  vendac_chave  != 'sem_chave' ", null);
            while (cursor.moveToNext()) {
                ConfPagamento_SqliteBean conf = new ConfPagamento_SqliteBean();
                conf.setPag_codigo(cursor.getInt(cursor.getColumnIndex(conf.CODIGO_PAGAMENTO)));
                conf.setPag_parcelas_cartao(cursor.getInt(cursor.getColumnIndex(conf.PARCELA_CARTAO)));
                conf.setPag_parcelas_normal(cursor.getInt(cursor.getColumnIndex(conf.PARCELA_NORMAL)));
                conf.setPag_recebeucom_din_chq_cart(cursor.getString(cursor.getColumnIndex(conf.RECEBEU_COM)));
                conf.setPag_sementrada_comentrada(cursor.getString(cursor.getColumnIndex(conf.ENTRADA)));
                conf.setPag_tipo_pagamento(cursor.getString(cursor.getColumnIndex(conf.TIPO_PAGAMENTO)));
                conf.setPag_valor_recebido(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(conf.VALOR_RECEBIDO))));
                conf.setVendac_chave(cursor.getString(cursor.getColumnIndex(conf.VENDAC_CHAVE)));
                lista.add(conf);
            }
        } catch (SQLiteException e) {
            Log.d("busca_todos_confPagamentos", e.getMessage());
        } finally {
            db.close();
        }

        return lista;
    }

    public ConfPagamento_SqliteBean p_busca_confpagamento() {
        ConfPagamento_SqliteBean c = null;

        try {
            db = new Db(ctx).getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM CONF_PAGAMENTO WHERE vendac_chave = 'sem_chave' ", null);
            if (cursor.moveToFirst()) {
                c = new ConfPagamento_SqliteBean();
                c.setPag_codigo(cursor.getInt(cursor.getColumnIndex(c.CODIGO_PAGAMENTO)));
                c.setPag_parcelas_cartao(cursor.getInt(cursor.getColumnIndex(c.PARCELA_CARTAO)));
                c.setPag_parcelas_normal(cursor.getInt(cursor.getColumnIndex(c.PARCELA_NORMAL)));
                c.setPag_recebeucom_din_chq_cart(cursor.getString(cursor.getColumnIndex(c.RECEBEU_COM)));
                c.setPag_sementrada_comentrada(cursor.getString(cursor.getColumnIndex(c.ENTRADA)));
                c.setPag_tipo_pagamento(cursor.getString(cursor.getColumnIndex(c.TIPO_PAGAMENTO)));
                c.setPag_valor_recebido(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(c.VALOR_RECEBIDO))));
                c.setVendac_chave(cursor.getString(cursor.getColumnIndex(c.VENDAC_CHAVE)));

            }
        } catch (SQLiteException e) {
            Log.d("p_busca_confpagamento", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }

        return c;
    }

    public void p_excluir_confpagamento() {
        db = new Db(ctx).getWritableDatabase();
        Sql = "delete from CONF_PAGAMENTO WHERE vendac_chave = 'sem_chave'  ";

        try {
            stmt = db.compileStatement(Sql);
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("excluir_confpagamento", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

}
