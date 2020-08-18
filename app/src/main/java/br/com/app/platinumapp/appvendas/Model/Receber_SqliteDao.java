package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.app.platinumapp.appvendas.Util.Util;


public class Receber_SqliteDao {

    private static final String TABLE_NAME = "receber";
    private Context ctx;
    private String sql;
    private boolean Gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public Receber_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean grava_receber(Receber_SqliteBean rec) {

        try {

            Gravacao = false;
            db = new Db(ctx).getWritableDatabase();
            sql = "insert into RECEBER   (rec_num_parcela,rec_cli_codigo ,rec_cli_nome ,vendac_chave ,rec_datamovimento,rec_valorreceber ,rec_datavencimento,rec_datavencimento_extenso,rec_data_que_pagou,rec_valor_pago ,rec_recebeu_com ,rec_parcelas_cartao,rec_enviado ) values (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
            stmt = db.compileStatement(sql);

            stmt.bindLong(1, rec.getRec_num_parcela());
            stmt.bindLong(2, rec.getRec_cli_codigo());
            stmt.bindString(3, rec.getRec_cli_nome());
            // *******
            stmt.bindString(4, rec.getVendac_chave());
            stmt.bindString(5, rec.getRec_datamovimento().toString());
            stmt.bindDouble(6, rec.getRec_valorreceber().setScale(2, RoundingMode.HALF_UP).doubleValue());
            // ********
            stmt.bindString(7, rec.getRec_datavencimento().toString());
            stmt.bindString(8, rec.getRec_datavencimento_extenso().toString());

            stmt.bindString(9, rec.getRec_data_que_pagou().toString());
            stmt.bindDouble(10, rec.getRec_valor_pago().setScale(2, RoundingMode.HALF_UP).doubleValue());
            // ********
            stmt.bindString(11, rec.getRec_recebeu_com());

            stmt.bindLong(12, rec.getRec_parcelas_cartao());

            stmt.bindString(13, rec.getRec_enviado());

            if (stmt.executeInsert() > 0) {
                Gravacao = true;
                sql = "";
            }

            stmt.clearBindings();

        } catch (SQLiteException e) {
            Gravacao = false;
            Log.d("grava_receber", e.getMessage());

        } finally {
            db.close();
            stmt.close();
        }

        return Gravacao;

    }

    public BigDecimal buscar_contas_a_receber_hoje(String data) {

        db = new Db(ctx).getReadableDatabase();
        BigDecimal a_receber_hoje = BigDecimal.ZERO;
        try {

            sql = "select sum(rec_valorreceber) as receber from receber where rec_datavencimento like  '%" + data + "%'  and rec_valor_pago == 0      ";


            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {

                a_receber_hoje = a_receber_hoje.add(new BigDecimal(cursor.getDouble(cursor.getColumnIndex("receber"))).setScale(2, RoundingMode.HALF_UP));

            }

        } catch (SQLiteException e) {
            Log.d("buscar_contas_a_receber_hoje", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return a_receber_hoje;

    }


    public BigDecimal buscar_contas_pagas_em_caixa_hoje(String data) {

        db = new Db(ctx).getReadableDatabase();
        BigDecimal recebido_hoje = BigDecimal.ZERO;
        try {

            sql = "select sum(rec_valor_pago) as receber from receber where rec_datavencimento like  '%" + data + "%'";


            cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {

                recebido_hoje = recebido_hoje.add(new BigDecimal(cursor.getDouble(cursor.getColumnIndex("receber"))).setScale(2, RoundingMode.HALF_UP));

            }

        } catch (SQLiteException e) {
            Log.d("buscar_contas_a_receber_hoje", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return recebido_hoje;

    }


    public Receber_SqliteBean busca_numero_parcela(Receber_SqliteBean chave) {
        db = new Db(ctx).getReadableDatabase();
        Receber_SqliteBean receber = null;
        try {
            cursor = db.rawQuery("select min(rec_num_parcela) as rec_num_parcela ,rec_cli_codigo ,rec_cli_nome ,vendac_chave ,rec_datamovimento,rec_valorreceber ,rec_datavencimento,rec_datavencimento_extenso,rec_data_que_pagou,rec_valor_pago ,rec_recebeu_com ,rec_parcelas_cartao,rec_enviado from receber where vendac_chave = ? and rec_valor_pago = 0", new String[]{chave.getVendac_chave()});
            if (cursor.moveToFirst()) {
                receber = new Receber_SqliteBean();
                receber.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(receber.NUMERO_DA_PARCELA)));
                receber.setVendac_chave(cursor.getString(cursor.getColumnIndex(receber.CHAVE_DA_VENDA)));
                receber.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(receber.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
            }

        } catch (SQLiteException e) {
            Log.d("busca_numero_parcela", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return receber;
    }


    public Receber_SqliteBean busca_proxima_parcela(Integer rec_codigo, String vendac_chave) {
        db = new Db(ctx).getReadableDatabase();
        Receber_SqliteBean receber = null;
        try {
            cursor = db.rawQuery("SELECT * FROM receber WHERE rec_num_parcela > ? and vendac_chave = ? ORDER BY rec_num_parcela LIMIT 1", new String[]{rec_codigo.toString(), vendac_chave});
            if (cursor.moveToFirst()) {
                receber = new Receber_SqliteBean();
                receber.setRec_codigo(cursor.getInt(cursor.getColumnIndex(receber.CODIGO_RECEBER)));
                receber.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(receber.NUMERO_DA_PARCELA)));
                receber.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(receber.CODIGO_DO_CLIENTE)));
                receber.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(receber.NOME_DO_CLIENTE)));
                receber.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(receber.DATA_DO_MOVIMENTO)));
                receber.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(receber.DATA_DO_VENCIMENTO)));
                receber.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(receber.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
                receber.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(receber.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                receber.setVendac_chave(cursor.getString(cursor.getColumnIndex(receber.CHAVE_DA_VENDA)));
            }

        } catch (SQLiteException e) {
            Log.d("busca_proxima_parcela", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return receber;
    }

    public List<Receber_SqliteBean> busca_parcelas_do_cliente(Integer cli_codigo) {
        List<Receber_SqliteBean> lista_de_pacelas = new ArrayList<>();

        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("select * from receber where rec_cli_codigo = ? and rec_valor_pago = 0", new String[]{cli_codigo.toString()});
            while (cursor.moveToNext()) {
                Receber_SqliteBean parcela = new Receber_SqliteBean();

                parcela.setRec_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_RECEBER)));
                parcela.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(parcela.NUMERO_DA_PARCELA)));
                parcela.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_DO_CLIENTE)));
                parcela.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(parcela.NOME_DO_CLIENTE)));
                parcela.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_MOVIMENTO)));
                parcela.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO)));
                parcela.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                parcela.setVendac_chave(cursor.getString(cursor.getColumnIndex(parcela.CHAVE_DA_VENDA)));
                lista_de_pacelas.add(parcela);
            }

        } catch (SQLiteException e) {
            Log.d("busca_parcelas_do_cliente", e.getMessage());
        } finally {
            db.close();
        }
        return lista_de_pacelas;
    }

    public List<Receber_SqliteBean> busca_parcelas_geradas_na_compra(String vendac_chave) {
        List<Receber_SqliteBean> parcelas_geradas = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {

            cursor = db.rawQuery("select * from receber where vendac_chave = ? order by rec_num_parcela asc ", new String[]{vendac_chave});

            while (cursor.moveToNext()) {
                Receber_SqliteBean parcela = new Receber_SqliteBean();
                parcela.setRec_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_RECEBER)));
                parcela.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(parcela.NUMERO_DA_PARCELA)));
                parcela.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_DO_CLIENTE)));
                parcela.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(parcela.NOME_DO_CLIENTE)));
                parcela.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_MOVIMENTO)));
                parcela.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO)));
                parcela.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                parcela.setVendac_chave(cursor.getString(cursor.getColumnIndex(parcela.CHAVE_DA_VENDA)));
                parcelas_geradas.add(parcela);
            }

        } catch (SQLiteException e) {
            Log.d("busca_parcelas_geradas_na_compra", e.getMessage());
        } finally {
            db.close();
        }
        return parcelas_geradas;
    }

    public List<Receber_SqliteBean> busca_todos_titulos_nao_pagos(Integer cli_codigo) {
        List<Receber_SqliteBean> titulos_nao_pagos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {
            sql = "select * from receber where rec_cli_codigo = " + cli_codigo.toString() + " and rec_valor_pago = 0  order by rec_num_parcela asc  ";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                Receber_SqliteBean parcela = new Receber_SqliteBean();
                parcela.setRec_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_RECEBER)));
                parcela.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(parcela.NUMERO_DA_PARCELA)));
                parcela.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_DO_CLIENTE)));
                parcela.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(parcela.NOME_DO_CLIENTE)));
                parcela.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_MOVIMENTO)));
                parcela.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO)));
                parcela.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                parcela.setVendac_chave(cursor.getString(cursor.getColumnIndex(parcela.CHAVE_DA_VENDA)));
                titulos_nao_pagos.add(parcela);
            }

        } catch (SQLiteException e) {
            Log.d("busca_todos_titulos_nao_pagos", e.getMessage());
        } finally {
            db.close();
        }
        return titulos_nao_pagos;
    }

    public List<Receber_SqliteBean> busca_parcelas_do_cliente(Integer cli_codigo, String datainicio, String datafim, boolean titulospagos, String vendac_chave) {
        List<Receber_SqliteBean> lista_de_pacelas = new ArrayList<>();

        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            String data1 = Util.FormataDataAAAAMMDD(datainicio);
            String data2 = Util.FormataDataAAAAMMDD(datafim);

            if (titulospagos) {
                sql = "select * from receber where  vendac_chave  = '" + vendac_chave + "' and  rec_cli_codigo = " + cli_codigo.toString() + " and rec_datavencimento  between '" + data1.trim() + "' and '" + data2.trim() + "' and rec_valor_pago > 0  order by rec_num_parcela asc   ";
                cursor = db.rawQuery(sql, null);
            } else {
                sql = "select * from receber where   vendac_chave  = '" + vendac_chave + "' and  rec_cli_codigo = " + cli_codigo.toString() + " and rec_datavencimento  between '" + data1.trim() + "' and '" + data2.trim() + "'  and rec_valor_pago = 0  order by rec_num_parcela asc  ";

                cursor = db.rawQuery(sql, null);
            }


            while (cursor.moveToNext()) {
                Receber_SqliteBean parcela = new Receber_SqliteBean();

                parcela.setRec_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_RECEBER)));
                parcela.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(parcela.NUMERO_DA_PARCELA)));
                parcela.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_DO_CLIENTE)));
                parcela.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(parcela.NOME_DO_CLIENTE)));
                parcela.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_MOVIMENTO)));
                parcela.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO)));
                parcela.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                parcela.setVendac_chave(cursor.getString(cursor.getColumnIndex(parcela.CHAVE_DA_VENDA)));
                lista_de_pacelas.add(parcela);
            }

        } catch (SQLiteException e) {
            Log.d("busca_parcelas_do_cliente data inicio e data fim", e.getMessage());
        } finally {
            db.close();
        }
        return lista_de_pacelas;
    }

    public void atualiza_valor_parcela_recvaloreceber(Receber_SqliteBean rec) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            sql = "update receber set rec_valorreceber = ? , rec_enviado = ? where vendac_chave = ? and rec_num_parcela = ? ";
            stmt = db.compileStatement(sql);

            stmt.bindDouble(1, rec.getRec_valorreceber().doubleValue());
            stmt.bindString(2, "N");
            stmt.bindString(3, rec.getVendac_chave());
            stmt.bindLong(4, rec.getRec_num_parcela());

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_valor_parcela_recvaloreceber", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public void atualiza_valor_e_data_vencimento_parcela_recvaloreceber(Receber_SqliteBean rec) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            sql = "update receber set rec_datavencimento = ? ,rec_valorreceber = ? , rec_enviado = ? where vendac_chave = ? and rec_num_parcela = ? ";
            stmt = db.compileStatement(sql);

            stmt.bindString(1, rec.getRec_datavencimento());
            stmt.bindDouble(2, rec.getRec_valorreceber().doubleValue());
            stmt.bindString(3, "N");
            stmt.bindString(4, rec.getVendac_chave());
            stmt.bindLong(5, rec.getRec_num_parcela());

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_valor_parcela_recvaloreceber", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public void atualiza_parcela_para_enviada(String vendac_chave, int rec_num_parcela) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            sql = "update receber set rec_enviado = ? where vendac_chave = ? and rec_num_parcela = ? ";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, "S");
            stmt.bindString(2, vendac_chave);
            stmt.bindLong(3, rec_num_parcela);

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_parcela_para_enviada", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public void baixa_parcela_cliente(Receber_SqliteBean rec) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            sql = "update receber set rec_valor_pago = ?  , rec_data_que_pagou  = ? , rec_recebeu_com = ?  , rec_enviado = ?  where vendac_chave = ? and rec_num_parcela = ? ";
            stmt = db.compileStatement(sql);

            stmt.bindDouble(1, rec.getRec_valor_pago().doubleValue());
            stmt.bindString(2, rec.getRec_data_que_pagou());
            stmt.bindString(3, rec.getRec_recebeu_com());
            stmt.bindString(4, "N");
            stmt.bindString(5, rec.getVendac_chave());
            stmt.bindLong(6, rec.getRec_num_parcela());

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_valor_parcela_recvaloreceber", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public void atualiza_dados_cliente_cadastrado_offline_receber(Integer new_codigo, Integer old_codigo) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            sql = "update receber set rec_cli_codigo=? where rec_cli_codigo=?";
            stmt = db.compileStatement(sql);
            stmt.bindLong(1, new_codigo);
            stmt.bindLong(2, old_codigo);
            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_dados_cliente_cadastrado_offline", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public Cursor busca_parcelas_do_cliente() {
        Receber_SqliteBean rec = new Receber_SqliteBean();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {

            cursor = db.query(TABLE_NAME, new String[]{rec.CODIGO_RECEBER + " as  _id", rec.NUMERO_DA_PARCELA, rec.DATA_DO_MOVIMENTO, rec.DATA_DO_VENCIMENTO, rec.DATA_QUE_PAGOU, rec.VALOR_PAGO, rec.VALOR_A_RECEBER}, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLiteException e) {
            Log.d("busca_parcelas_do_cliente", e.getMessage());
        } finally {
            db.close();

        }

        return cursor;
    }

    public Cursor busca_parcelas_do_cliente_cursor(Integer cli_codigo, String datainicio, String datafim, boolean titulospagos) throws SQLiteException {
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Receber_SqliteBean rec = new Receber_SqliteBean();
        Cursor resultset = null;
        try {

            // sql =
            // "select rec_codigo as _id , rec_num_parcela ,rec_datamovimento , rec_datavencimento , rec_data_que_pagou , rec_valor_pago ,rec_valorreceber from receber where rec_cli_codigo = "
            // + cli_codigo;
            // resultset = db.query(sql, null, null, null, null, null, null);

            String data1 = Util.FormataDataAAAAMMDD(datainicio);
            String data2 = Util.FormataDataAAAAMMDD(datafim);

            if (titulospagos) {
                sql = "select rec_codigo as _id , rec_num_parcela ,rec_datamovimento , rec_datavencimento , rec_data_que_pagou , rec_valor_pago ,rec_valorreceber from receber where rec_cli_codigo = " + cli_codigo.toString() + " and rec_datavencimento  between '" + data1.trim() + "' and '" + data2.trim() + "'   ";
                resultset = db.rawQuery(sql, null);
            } else {
                sql = "select rec_codigo as _id , rec_num_parcela ,rec_datamovimento , rec_datavencimento , rec_data_que_pagou , rec_valor_pago ,rec_valorreceber from receber where rec_cli_codigo = " + cli_codigo.toString() + " and rec_valor_pago = 0";
                resultset = db.rawQuery(sql, null);
            }

            if (resultset != null) {
                resultset.moveToFirst();
            }

        } catch (SQLiteException e) {
            Log.d("busca_parcelas_do_cliente", e.getMessage());
        } finally {

            db.close();

        }

        return resultset;

    }


    public List<Receber_SqliteBean> busca_contas_receber_para_exportar() {
        List<Receber_SqliteBean> parcelas = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {
            sql = "select * from receber where rec_enviado = 'N'  ";
            cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                Receber_SqliteBean parcela = new Receber_SqliteBean();


                parcela.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(parcela.NUMERO_DA_PARCELA)));
                parcela.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_DO_CLIENTE)));
                parcela.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(parcela.NOME_DO_CLIENTE)));

                parcela.setVendac_chave(cursor.getString(cursor.getColumnIndex(parcela.CHAVE_DA_VENDA)));
                parcela.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_MOVIMENTO)));
                parcela.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));

                parcela.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO)));
                parcela.setRec_datavencimento_extenso(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO_EXTENSO)));
                parcela.setRec_data_que_pagou(cursor.getString(cursor.getColumnIndex(parcela.DATA_QUE_PAGOU)));

                parcela.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_recebeu_com(cursor.getString(cursor.getColumnIndex(parcela.FORMATO_RECEBIMENTO)));
                parcela.setRec_parcelas_cartao(cursor.getInt(cursor.getColumnIndex(parcela.PARCELAS_CARTAO)));


                parcelas.add(parcela);
            }

        } catch (SQLiteException e) {
            Log.d("busca_contas_receber_para_exportar", e.getMessage());
        } finally {
            db.close();
        }
        return parcelas;
    }


    public List<Receber_SqliteBean> buscar_parcelas_vencidas_hoje(String data_vencimento) {
        List<Receber_SqliteBean> parcelas = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {
            sql = "SELECT  * FROM receber WHERE rec_datavencimento = ? and rec_valor_pago = 0";
            cursor = db.rawQuery(sql, new String[]{data_vencimento});

            while (cursor.moveToNext()) {
                Receber_SqliteBean parcela = new Receber_SqliteBean();
                parcela.setRec_num_parcela(cursor.getInt(cursor.getColumnIndex(parcela.NUMERO_DA_PARCELA)));
                parcela.setRec_cli_codigo(cursor.getInt(cursor.getColumnIndex(parcela.CODIGO_DO_CLIENTE)));
                parcela.setRec_cli_nome(cursor.getString(cursor.getColumnIndex(parcela.NOME_DO_CLIENTE)));
                parcela.setVendac_chave(cursor.getString(cursor.getColumnIndex(parcela.CHAVE_DA_VENDA)));
                parcela.setRec_datamovimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_MOVIMENTO)));
                parcela.setRec_valorreceber(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_A_RECEBER))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_datavencimento(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO)));
                parcela.setRec_datavencimento_extenso(cursor.getString(cursor.getColumnIndex(parcela.DATA_DO_VENCIMENTO_EXTENSO)));
                parcela.setRec_data_que_pagou(cursor.getString(cursor.getColumnIndex(parcela.DATA_QUE_PAGOU)));
                parcela.setRec_valor_pago(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(parcela.VALOR_PAGO))).setScale(2, RoundingMode.HALF_UP));
                parcela.setRec_recebeu_com(cursor.getString(cursor.getColumnIndex(parcela.FORMATO_RECEBIMENTO)));
                parcela.setRec_parcelas_cartao(cursor.getInt(cursor.getColumnIndex(parcela.PARCELAS_CARTAO)));
                parcelas.add(parcela);
            }

        } catch (SQLiteException e) {
            Log.d("listar_parcelas_vencidas", e.getMessage());
        } finally {
            db.close();
        }
        return parcelas;
    }


}
