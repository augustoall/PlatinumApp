package br.com.app.platinumapp.appvendas.Model;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.math.BigDecimal;

public class Configuracoes_SqliteDao {

    private Context ctx;
    private String sql;
    private boolean Gravacao;

    public Configuracoes_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravarConfiguracoes(Configuracoes_SqliteBean config) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        Gravacao = false;
        sql = "INSERT INTO CONFIGURACOES (USU_CODIGO,NOME_VENDEDOR,IMPORTAR_TODOS_CLIENTES,IP_SERVER,DESCONTO_VENDEDOR,VENDER_SEM_ESTOQUE,PEDIR_SENHA_NA_VENDA,PERMITIR_VENDER_ACIMA_DO_LIMITE,JUROS_VENDA_PRAZO) values (?,?,?,?,?,?,?,?,?)";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1, config.getUSU_CODIGO());
            stmt.bindString(2, config.getNOME_VENDEDOR());
            stmt.bindString(3, config.getIMPORTAR_TODOS_CLIENTES());
            stmt.bindString(4, config.getIP_SERVER());
            stmt.bindDouble(5, config.getDESCONTO_VENDEDOR().doubleValue());
            stmt.bindString(6, config.getVENDER_SEM_ESTOQUE());
            stmt.bindString(7, config.getPEDIR_SENHA_NA_VENDA());
            stmt.bindString(8, config.getPERMITIR_VENDER_ACIMA_DO_LIMITE());
            stmt.bindDouble(9, config.getJUROS_VENDA_PRAZO().doubleValue());
            if (stmt.executeInsert() > 0) {
                Gravacao = true;
                sql = "";
            }
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("GravarConfiguracoes", e.getMessage());
            Gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }
        return Gravacao;
    }

    public void atualiza_configuracoes(Configuracoes_SqliteBean config) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "UPDATE CONFIGURACOES SET  USU_CODIGO=?,NOME_VENDEDOR=?,IMPORTAR_TODOS_CLIENTES=?,IP_SERVER=?,DESCONTO_VENDEDOR=?,VENDER_SEM_ESTOQUE=?,PEDIR_SENHA_NA_VENDA=?,PERMITIR_VENDER_ACIMA_DO_LIMITE=?,JUROS_VENDA_PRAZO=?";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1, config.getUSU_CODIGO());
            stmt.bindString(2, config.getNOME_VENDEDOR());
            stmt.bindString(3, config.getIMPORTAR_TODOS_CLIENTES());
            stmt.bindString(4, config.getIP_SERVER());
            stmt.bindDouble(5, config.getDESCONTO_VENDEDOR().doubleValue());
            stmt.bindString(6, config.getVENDER_SEM_ESTOQUE());
            stmt.bindString(7, config.getPEDIR_SENHA_NA_VENDA());
            stmt.bindString(8, config.getPERMITIR_VENDER_ACIMA_DO_LIMITE());
            stmt.bindDouble(9, config.getJUROS_VENDA_PRAZO().doubleValue());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("atualiza_configuracoes", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public void atualiza_dados_vendedor(Configuracoes_SqliteBean config) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "UPDATE CONFIGURACOES SET USU_CODIGO=?,NOME_VENDEDOR=?,DESCONTO_VENDEDOR=?";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1, config.getUSU_CODIGO());
            stmt.bindString(2, config.getNOME_VENDEDOR());
            stmt.bindDouble(3, config.getDESCONTO_VENDEDOR().doubleValue());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("AtualizaDadosVendedor", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


    @SuppressLint("LongLogTag")
    public void atualiza_codigo_vendedor(Configuracoes_SqliteBean config) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "UPDATE CONFIGURACOES SET USU_CODIGO=?";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindLong(1, config.getUSU_CODIGO());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("atualiza_codigo_vendedor", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    @SuppressLint("LongLogTag")
    public void atualiza_endereco_servidor(Configuracoes_SqliteBean config) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "UPDATE CONFIGURACOES SET IP_SERVER=?";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.bindString(1, config.getIP_SERVER());

            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("atualiza_endereco_servidor", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public Configuracoes_SqliteBean BuscaParamentrosEmpresa() {
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Configuracoes_SqliteBean conf = null;
        try {
            String sql = "SELECT * from CONFIGURACOES";
            Cursor rs = db.rawQuery(sql, null);
            if (rs.moveToFirst()) {
                conf = new Configuracoes_SqliteBean();
                conf.setUSU_CODIGO(rs.getInt(rs.getColumnIndex("USU_CODIGO")));
                conf.setNOME_VENDEDOR(rs.getString(rs.getColumnIndex("NOME_VENDEDOR")));
                conf.setIMPORTAR_TODOS_CLIENTES(rs.getString(rs.getColumnIndex("IMPORTAR_TODOS_CLIENTES")));
                conf.setIP_SERVER(rs.getString(rs.getColumnIndex("IP_SERVER")));
                conf.setDESCONTO_VENDEDOR(new BigDecimal(rs.getDouble(rs.getColumnIndex("DESCONTO_VENDEDOR"))));
                conf.setJUROS_VENDA_PRAZO(new BigDecimal(rs.getDouble(rs.getColumnIndex("JUROS_VENDA_PRAZO"))));
                conf.setVENDER_SEM_ESTOQUE(rs.getString(rs.getColumnIndex("VENDER_SEM_ESTOQUE")));
                conf.setPEDIR_SENHA_NA_VENDA(rs.getString(rs.getColumnIndex("PEDIR_SENHA_NA_VENDA")));
                conf.setPERMITIR_VENDER_ACIMA_DO_LIMITE(rs.getString(rs.getColumnIndex("PERMITIR_VENDER_ACIMA_DO_LIMITE")));
            }
        } catch (SQLiteException e) {
            Log.d("Script", "BuscaParamentrosEmpresa: " + e.getMessage());
        } finally {
            db.close();
        }
        return conf;
    }
}
