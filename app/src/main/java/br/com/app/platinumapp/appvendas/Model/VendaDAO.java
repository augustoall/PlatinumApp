package br.com.app.platinumapp.appvendas.Model;

import android.content.ContentValues;
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

public class VendaDAO {

    private static final String TABLE_NAME_VENDAC = "VENDAC";
    private static final String TABLE_NAME_VENDAD = "VENDAD";

    private Context ctx;
    private String Sql;
    private boolean Gravacao;
    private int vendad_nro_item;
    private Cursor cursor;
    private SQLiteDatabase db;
    private SQLiteStatement stmt;

    public VendaDAO(Context ctx) {
        this.ctx = ctx;
    }

    public List<VendaC_SqliteBean> lista_pedidos_do_cliente(Integer cli_codigo) {
        List<VendaC_SqliteBean> listadepedidos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from VENDAC where vendac_cli_codigo = ? order by vendac_id desc ", new String[]{cli_codigo.toString()});

            while (cursor.moveToNext()) {

                VendaC_SqliteBean venda = new VendaC_SqliteBean();

                venda.setVendac_cli_codigo(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DO_CLIENTE)));
                venda.setVendac_id(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DA_VENDA)));


                venda.setVendac_chave(cursor.getString(cursor.getColumnIndex(venda.CHAVE_DA_VENDA)));
                venda.setVendac_cli_nome(cursor.getString(cursor.getColumnIndex(venda.NOME_DO_CLIENTE)));
                venda.setVendac_datahoravenda(cursor.getString(cursor.getColumnIndex(venda.DATA_HORA_DA_VENDA)));
                venda.setVendac_fpgto_tipo(cursor.getString(cursor.getColumnIndex(venda.FORMA_DE_PAGAMENTO)));
                venda.setVendac_valor(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(venda.VALOR_DA_VENDA))).setScale(2, RoundingMode.HALF_UP));

                listadepedidos.add(venda);
            }

        } catch (SQLiteException e) {
            Log.d("lista_pedidos_do_cliente", e.getMessage());
        } finally {
            db.close();
        }

        return listadepedidos;
    }


    public void atualiza_vendac_para_enviada(String vendac_chave) {

        try {

            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            Sql = "update VENDAC set vendac_enviada = 'S' where vendac_chave = 	?";
            stmt = db.compileStatement(Sql);

            stmt.bindString(1, vendac_chave);
            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_vendac_para_enviada", e.getMessage());
        } finally {
            db.close();

        }
    }


    public void atualiza_dados_cliente_cadastrado_offline_vendac(Integer new_codigo,Integer old_codigo) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            Sql = "update vendac set vendac_cli_codigo=? where vendac_cli_codigo=? ";
            stmt = db.compileStatement(Sql);
            stmt.bindLong(1,new_codigo);
            stmt.bindLong(2,old_codigo);
            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualiza_dados_cliente_cadastrado_offline", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public List<VendaC_SqliteBean> busca_todas_vendac_nao_enviadas() {
        List<VendaC_SqliteBean> listadepedidos = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from VENDAC where vendac_enviada = 'N' ", null);

            while (cursor.moveToNext()) {

                VendaC_SqliteBean venda = new VendaC_SqliteBean();
                venda.setVendac_chave(cursor.getString(cursor.getColumnIndex(venda.CHAVE_DA_VENDA)));
                venda.setVendac_datahoravenda(cursor.getString(cursor.getColumnIndex(venda.DATA_HORA_DA_VENDA)));
                venda.setVendac_previsao_entrega(cursor.getString(cursor.getColumnIndex(venda.PREVISAO_ENTREGA)));
                venda.setVendac_usu_codigo(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DO_USUARIO_VENDEDOR)));
                venda.setVendac_usu_nome(cursor.getString(cursor.getColumnIndex(venda.NOME_DO_USUARIO_VENDEDOR)));
                venda.setVendac_cli_codigo(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DO_CLIENTE)));
                venda.setVendac_cli_nome(cursor.getString(cursor.getColumnIndex(venda.NOME_DO_CLIENTE)));
                venda.setVendac_fpgto_codigo(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DA_FORMA_DE_PAGAMENTO)));
                venda.setVendac_fpgto_tipo(cursor.getString(cursor.getColumnIndex(venda.FORMA_DE_PAGAMENTO)));
                venda.setVendac_valor(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(venda.VALOR_DA_VENDA))).setScale(2,RoundingMode.HALF_UP));
                venda.setVendac_peso_total(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(venda.PESO_TOTAL_DOS_PRODUTOS))).setScale(2,RoundingMode.HALF_UP));
                venda.setVendac_observacao(cursor.getString(cursor.getColumnIndex(venda.OBSERVACAO)));
                venda.setVendac_latitude(cursor.getString(cursor.getColumnIndex(venda.LATITUDE)));
                venda.setVendac_longitude(cursor.getString(cursor.getColumnIndex(venda.LONGITUDE)));
                listadepedidos.add(venda);
            }

        } catch (SQLiteException e) {
            Log.d("exporta_venda_c", e.getMessage());
        } finally {
            db.close();
        }

        return listadepedidos;
    }


    public List<VendaD_SqliteBean> busca_todas_vendad() {
        List<VendaD_SqliteBean> listadeitensdavenda = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from VENDAD", null);

            while (cursor.moveToNext()) {

                VendaD_SqliteBean itemvnd = new VendaD_SqliteBean();

                itemvnd.setVendac_chave(cursor.getString(cursor.getColumnIndex(itemvnd.CHAVE_DA_VENDA)));
                itemvnd.setVendad_nro_item(cursor.getInt(cursor.getColumnIndex(itemvnd.NUMERO_DO_ITEM)));
                itemvnd.setVendad_codigo_produto(cursor.getInt(cursor.getColumnIndex(itemvnd.CODIGO_DO_PRODUTO)));
                itemvnd.setVendad_descricao_produto(cursor.getString(cursor.getColumnIndex(itemvnd.DESCRICAO_PRODUTO)));
                itemvnd.setVendad_ean(cursor.getString(cursor.getColumnIndex(itemvnd.CODIGO_DE_BARRAS)));
                itemvnd.setVendad_precovenda(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(itemvnd.PRECO_DE_VENDA))));
                itemvnd.setVendad_quantidade(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(itemvnd.QUANTIDADE))));
                itemvnd.setVendad_total(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(itemvnd.TOTAL_QTDE_X_PRECOVENDA))));
                listadeitensdavenda.add(itemvnd);

            }

        } catch (SQLiteException e) {
            Log.d("busca_todas_vendad", e.getMessage());
        } finally {
            db.close();
        }

        return listadeitensdavenda;
    }

    public long gravaVenda(VendaC_SqliteBean venda, List<VendaD_SqliteBeanTEMP> items_venda_temporarios) {

        // adicionando todos os items em VendaD_SqliteBean para poder apagar a
        // tabela temporaria VendaD_SqliteBeanTEMP

        vendad_nro_item = 1;
        for (VendaD_SqliteBeanTEMP items_temp : items_venda_temporarios) {
            VendaD_SqliteBean itemvnd = new VendaD_SqliteBean();

            itemvnd.setVendac_chave(venda.getVendac_chave());
            itemvnd.setVendad_nro_item(vendad_nro_item);
            itemvnd.setVendad_codigo_produto(items_temp.getVendad_codigo_produto());
            itemvnd.setVendad_descricao_produto(items_temp.getVendad_descricao_produto());
            itemvnd.setVendad_ean(items_temp.getVendad_ean());
            itemvnd.setVendad_precovenda(items_temp.getVendad_precovenda());
            itemvnd.setVendad_quantidade(items_temp.getVendad_quantidade());
            itemvnd.setVendad_total(items_temp.getSubtotal());
            venda.getItens_da_venda().add(itemvnd);
            vendad_nro_item++;

            // excluindo o item que ja foi add em VendaD_SqliteBeanTEMP
            new VendaD_SqliteDaoTEMP(ctx).exclui_um_item_da_venda(items_temp);

        }

        long id_venda = -1;
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        db.beginTransaction();

        try {

            ContentValues vnc = new ContentValues();
            vnc.put(venda.CHAVE_DA_VENDA, venda.getVendac_chave());
            vnc.put(venda.DATA_HORA_DA_VENDA, venda.getVendac_datahoravenda());
            vnc.put(venda.PREVISAO_ENTREGA, venda.getVendac_previsao_entrega());
            // *********
            vnc.put(venda.CODIGO_DO_CLIENTE, venda.getVendac_cli_codigo());
            vnc.put(venda.NOME_DO_CLIENTE, venda.getVendac_cli_nome());
            vnc.put(venda.CODIGO_DA_FORMA_DE_PAGAMENTO, venda.getVendac_fpgto_codigo());
            vnc.put(venda.FORMA_DE_PAGAMENTO, venda.getVendac_fpgto_tipo());
            vnc.put(venda.VALOR_DA_VENDA, venda.getVendac_valor().toString());
            // **********
            vnc.put(venda.PESO_TOTAL_DOS_PRODUTOS, venda.getVendac_peso_total().toString());
            vnc.put(venda.OBSERVACAO, venda.getVendac_observacao());
            vnc.put(venda.VENDA_ENVIADA_SERVIDOR, venda.getVendac_enviada());
            // ************
            vnc.put(venda.LATITUDE, venda.getVendac_latitude());
            vnc.put(venda.LONGITUDE, venda.getVendac_longitude());
            // *************
            vnc.put(venda.CODIGO_DO_USUARIO_VENDEDOR, venda.getVendac_usu_codigo());
            vnc.put(venda.NOME_DO_USUARIO_VENDEDOR, venda.getVendac_usu_nome());
            // *************

            id_venda = db.insert(TABLE_NAME_VENDAC, null, vnc);

            if (id_venda != -1) {
                boolean deuErro = false;

                // recuperando os items que estao na tabela
                // temporaria e colocando-os na real tabela de items da venda

                for (int i = 0; i < venda.getItens_da_venda().size(); i++) {

                    VendaD_SqliteBean vnd = (VendaD_SqliteBean) venda.getItens_da_venda().get(i);
                    ContentValues item = new ContentValues();
                    if (vnd.getVendad_quantidade().compareTo(BigDecimal.ZERO) > 0) {

                        item.put(vnd.CHAVE_DA_VENDA, vnd.getVendac_chave());
                        item.put(vnd.NUMERO_DO_ITEM, vnd.getVendad_nro_item());
                        item.put(vnd.CODIGO_DO_PRODUTO, vnd.getVendad_codigo_produto());
                        item.put(vnd.DESCRICAO_PRODUTO, vnd.getVendad_descricao_produto());
                        item.put(vnd.CODIGO_DE_BARRAS, vnd.getVendad_ean());
                        item.put(vnd.PRECO_DE_VENDA, vnd.getVendad_precovenda().setScale(2,RoundingMode.HALF_UP).toString());
                        item.put(vnd.QUANTIDADE, vnd.getVendad_quantidade().toString());
                        item.put(vnd.TOTAL_QTDE_X_PRECOVENDA,vnd.getVendad_quantidade().multiply(vnd.getVendad_precovenda()).setScale(2,RoundingMode.HALF_UP).toString());


                        if (db.insert(TABLE_NAME_VENDAD, null, item) == -1) {
                            deuErro = true;
                            break;
                        }
                    }
                }
                if (!deuErro) {
                    db.setTransactionSuccessful();
                }
            }

        } catch (SQLiteException e) {
            // implementar roollback no sqlite mvc
            Log.d("Erro nos items", "erro nos iten " + e.getMessage());
        } finally {

            db.endTransaction();
            db.close();
        }

        return id_venda;
    }

    public List<VendaC_SqliteBean> busca_vendas_realizadas(String dataini,String datafim) {
        List<VendaC_SqliteBean> listavendasrealizadas = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from vendac where vendac_datahoravenda BETWEEN ? and  ?", new String[]{dataini,datafim});
            while (cursor.moveToNext()) {
                VendaC_SqliteBean venda = new VendaC_SqliteBean();
                venda.setVendac_cli_codigo(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DO_CLIENTE)));
                venda.setVendac_id(cursor.getInt(cursor.getColumnIndex(venda.CODIGO_DA_VENDA)));
                venda.setVendac_chave(cursor.getString(cursor.getColumnIndex(venda.CHAVE_DA_VENDA)));
                venda.setVendac_cli_nome(cursor.getString(cursor.getColumnIndex(venda.NOME_DO_CLIENTE)));
                venda.setVendac_datahoravenda(Util.FormataDataDDMMAAAA(cursor.getString(cursor.getColumnIndex(venda.DATA_HORA_DA_VENDA))));
                venda.setVendac_fpgto_tipo(cursor.getString(cursor.getColumnIndex(venda.FORMA_DE_PAGAMENTO)));
                venda.setVendac_valor(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(venda.VALOR_DA_VENDA))));
                venda.setVendac_enviada(cursor.getString(cursor.getColumnIndex(venda.VENDA_ENVIADA_SERVIDOR)));
                listavendasrealizadas.add(venda);
            }
        } catch (SQLiteException e) {
            Log.d("lista_pedidos_do_cliente", e.getMessage());
        } finally {
            db.close();
        }
        return listavendasrealizadas;
    }
}
