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

public class Cliente_SqliteDao {

    public static final int NOME_CLIENTE = 1;
    public static final int BAIRRO = 2;
    public static final int FANTASIA = 3;
    public static final int ENDERECO = 4;
    private static final String TABLE_NAME = "CLIENTES";
    private Context ctx;
    private String Sql;
    private boolean Gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public Cliente_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean GravarCliente_Sqlite(Cliente_SqliteBean cliente) {

        try {

            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            Sql = "insert into clientes (cli_codigo,cli_nome,cli_fantasia,cli_endereco,cli_bairro,cli_cep,cid_codigo,cli_contato1,cli_contato2,cli_contato3,cli_nascimento,cli_cpfcnpj,cli_rginscricaoest,cli_limite,cli_email,cli_observacao,usu_codigo,cli_enviado,cli_senha,cli_chave) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            stmt = db.compileStatement(Sql);

            stmt.bindLong(1, cliente.getCli_codigo());
            stmt.bindString(2, cliente.getCli_nome().toUpperCase());
            stmt.bindString(3, cliente.getCli_fantasia().toUpperCase());

            stmt.bindString(4, cliente.getCli_endereco().toUpperCase());
            stmt.bindString(5, cliente.getCli_bairro().toUpperCase());
            stmt.bindString(6, cliente.getCli_cep());

            stmt.bindLong(7, cliente.getCid_codigo());
            stmt.bindString(8, cliente.getCli_contato1());
            stmt.bindString(9, cliente.getCli_contato2());

            stmt.bindString(10, cliente.getCli_contato3());
            stmt.bindString(11, cliente.getCli_nascimento().toString());
            stmt.bindString(12, cliente.getCli_cpfcnpj());

            stmt.bindString(13, cliente.getCli_rginscricaoest());
            stmt.bindDouble(14, Util.Bigdecimal_to_double(cliente.getCli_limite()));
            stmt.bindString(15, cliente.getCli_email());

            stmt.bindString(16, cliente.getCli_observacao());
            stmt.bindLong(17, cliente.getUsu_codigo());
            stmt.bindString(18, cliente.getCli_enviado());

            stmt.bindString(19, cliente.getCli_senha());
            stmt.bindString(20, cliente.getCli_chave());

            if (stmt.executeInsert() > 0) {
                Gravacao = true;
                Sql = "";
            }

            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("GravarCliente_Sqlite", e.getMessage());
            Gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }

        return Gravacao;
    }

    public Cliente_SqliteBean BuscarClientePorCli_codigo(Integer cli_codigo) {
        db = new Db(ctx).getReadableDatabase();
        Cliente_SqliteBean cli = null;
        try {

            cursor = db.rawQuery("select * from clientes where cli_codigo = ?", new String[]{cli_codigo.toString()});
            if (cursor.moveToFirst()) {
                cli = new Cliente_SqliteBean();

                cli.setCli_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_DO_CLIENTE)));
                cli.setCli_nome(cursor.getString(cursor.getColumnIndex(cli.NOME_DO_CLIENTE)));
                cli.setCli_fantasia(cursor.getString(cursor.getColumnIndex(cli.NOME_FANTASIA)));
                cli.setCli_endereco(cursor.getString(cursor.getColumnIndex(cli.ENDERECO_CLIENTE)));

                cli.setCli_bairro(cursor.getString(cursor.getColumnIndex(cli.BAIRRO_CLIENTE)));
                cli.setCli_cep(cursor.getString(cursor.getColumnIndex(cli.CEP_CIDADE)));
                cli.setCid_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_CIDADE)));

                cli.setCli_contato1(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE1)));
                cli.setCli_contato2(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE2)));
                cli.setCli_contato3(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE3)));

                cli.setCli_nascimento(cursor.getString(cursor.getColumnIndex(cli.DATA_NASCIMETO)));
                cli.setCli_cpfcnpj(cursor.getString(cursor.getColumnIndex(cli.CPF_CNPJ_CLIENTE)));
                cli.setCli_rginscricaoest(cursor.getString(cursor.getColumnIndex(cli.RG_INSCRICAO_ESTADUAL)));

                cli.setCli_limite(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(cli.LIMITE_CLIENTE))).setScale(2, RoundingMode.HALF_UP));
                cli.setCli_email(cursor.getString(cursor.getColumnIndex(cli.EMAIL_CLIENTE)));
                cli.setCli_observacao(cursor.getString(cursor.getColumnIndex(cli.OBSERVACAO)));

                cli.setUsu_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_VENDEDOR_USUARIO)));
                cli.setCli_senha(cursor.getString(cursor.getColumnIndex(cli.SENHA_DO_CLIENTE)));
                cli.setCli_enviado(cursor.getString(cursor.getColumnIndex(cli.CLIENTE_ENVIADO)));
                cli.setCli_chave(cursor.getString(cursor.getColumnIndex(cli.CHAVE_CLIENTE)));

            }
        } catch (SQLiteException e) {
            Log.d("BuscarClientePelo_ID", e.getMessage());
        } finally {
            db.close();
            cursor.close();
        }
        return cli;
    }

    public void atualizaCliente(Cliente_SqliteBean cliente) {

        try {

            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            Sql = "update clientes set cli_nome=?,cli_fantasia=?,cli_endereco=?,cli_bairro=?,cli_cep=?,cid_codigo=?,cli_contato1=?,cli_contato2=?,cli_contato3=?,cli_nascimento=?,cli_cpfcnpj=?,cli_rginscricaoest=?,cli_limite=?,cli_email=?,cli_observacao=?,usu_codigo=?,cli_enviado=?,cli_senha=?,cli_chave=? where cli_codigo=?";
            stmt = db.compileStatement(Sql);

            stmt.bindString(1, cliente.getCli_nome().toUpperCase());
            stmt.bindString(2, cliente.getCli_fantasia().toUpperCase());

            stmt.bindString(3, cliente.getCli_endereco().toUpperCase());
            stmt.bindString(4, cliente.getCli_bairro().toLowerCase());
            stmt.bindString(5, cliente.getCli_cep());

            stmt.bindLong(6, cliente.getCid_codigo());
            stmt.bindString(7, cliente.getCli_contato1());
            stmt.bindString(8, cliente.getCli_contato2());

            stmt.bindString(9, cliente.getCli_contato3());
            stmt.bindString(10, cliente.getCli_nascimento().toString());
            stmt.bindString(11, cliente.getCli_cpfcnpj());

            stmt.bindString(12, cliente.getCli_rginscricaoest());
            stmt.bindDouble(13, cliente.getCli_limite().doubleValue());
            stmt.bindString(14, cliente.getCli_email());

            stmt.bindString(15, cliente.getCli_observacao());
            stmt.bindLong(16, cliente.getUsu_codigo());
            stmt.bindString(17, cliente.getCli_enviado());

            stmt.bindString(18, cliente.getCli_senha());
            stmt.bindString(19, cliente.getCli_chave());

            stmt.bindLong(20, cliente.getCli_codigo());

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("atualizaCliente", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


    public void excluir_clientes_enviados() {
        try {
            SQLiteDatabase db = new Db(ctx).getWritableDatabase();
            String sql = "delete from clientes where cli_enviado='S'";
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.executeUpdateDelete();
        } catch (SQLiteException e) {
            Log.d("excluir_clientes_nao_enviados", e.getMessage());
        }
    }

    public void atualiza_CLI_CODIGO_pela_chave_ao_exportar(Integer novo_codigo,Integer codigo_off_line) {

        try {

            db = new Db(ctx).getWritableDatabase();
            Gravacao = false;
            Sql = "update clientes set cli_codigo = ?,cli_enviado = 'S' where cli_codigo = ?";
            stmt = db.compileStatement(Sql);

            stmt.bindLong(1, novo_codigo);
            stmt.bindLong(2, codigo_off_line);

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.i("atualiza_CLI_CODIGO_pel", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

    public List<Cliente_SqliteBean> busca_clientes_nao_enviados_pro_servidor() {
        List<Cliente_SqliteBean> listadeclientes = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from clientes where cli_enviado = 'N' ", null);


            while (cursor.moveToNext()) {

                Cliente_SqliteBean cli = new Cliente_SqliteBean();

                cli.setCli_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_DO_CLIENTE)));
                cli.setCli_nome(cursor.getString(cursor.getColumnIndex(cli.NOME_DO_CLIENTE)));
                cli.setCli_fantasia(cursor.getString(cursor.getColumnIndex(cli.NOME_FANTASIA)));
                cli.setCli_endereco(cursor.getString(cursor.getColumnIndex(cli.ENDERECO_CLIENTE)));

                cli.setCli_bairro(cursor.getString(cursor.getColumnIndex(cli.BAIRRO_CLIENTE)));
                cli.setCli_cep(cursor.getString(cursor.getColumnIndex(cli.CEP_CIDADE)));
                cli.setCid_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_CIDADE)));

                cli.setCli_contato1(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE1)));
                cli.setCli_contato2(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE2)));
                cli.setCli_contato3(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE3)));

                cli.setCli_nascimento(cursor.getString(cursor.getColumnIndex(cli.DATA_NASCIMETO)));
                cli.setCli_cpfcnpj(cursor.getString(cursor.getColumnIndex(cli.CPF_CNPJ_CLIENTE)));
                cli.setCli_rginscricaoest(cursor.getString(cursor.getColumnIndex(cli.RG_INSCRICAO_ESTADUAL)));

                cli.setCli_limite(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(cli.LIMITE_CLIENTE))).setScale(2,RoundingMode.HALF_UP));
                cli.setCli_email(cursor.getString(cursor.getColumnIndex(cli.EMAIL_CLIENTE)));
                cli.setCli_observacao(cursor.getString(cursor.getColumnIndex(cli.OBSERVACAO)));

                cli.setUsu_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_VENDEDOR_USUARIO)));
                cli.setCli_senha(cursor.getString(cursor.getColumnIndex(cli.SENHA_DO_CLIENTE)));
                cli.setCli_enviado(cursor.getString(cursor.getColumnIndex(cli.CLIENTE_ENVIADO)));
                cli.setCli_chave(cursor.getString(cursor.getColumnIndex(cli.CHAVE_CLIENTE)));

                listadeclientes.add(cli);
            }

        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();
        }


        return listadeclientes;

    }


    public List<Cliente_SqliteBean> busca_clientes_cadastrados_offline() {
        List<Cliente_SqliteBean> listadeclientes = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from clientes where cli_chave !=''", null);


            while (cursor.moveToNext()) {

                Cliente_SqliteBean cli = new Cliente_SqliteBean();

                cli.setCli_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_DO_CLIENTE)));
                cli.setCli_nome(cursor.getString(cursor.getColumnIndex(cli.NOME_DO_CLIENTE)));
                cli.setCli_fantasia(cursor.getString(cursor.getColumnIndex(cli.NOME_FANTASIA)));
                cli.setCli_endereco(cursor.getString(cursor.getColumnIndex(cli.ENDERECO_CLIENTE)));

                cli.setCli_bairro(cursor.getString(cursor.getColumnIndex(cli.BAIRRO_CLIENTE)));
                cli.setCli_cep(cursor.getString(cursor.getColumnIndex(cli.CEP_CIDADE)));
                cli.setCid_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_CIDADE)));

                cli.setCli_contato1(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE1)));
                cli.setCli_contato2(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE2)));
                cli.setCli_contato3(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE3)));

                cli.setCli_nascimento(cursor.getString(cursor.getColumnIndex(cli.DATA_NASCIMETO)));
                cli.setCli_cpfcnpj(cursor.getString(cursor.getColumnIndex(cli.CPF_CNPJ_CLIENTE)));
                cli.setCli_rginscricaoest(cursor.getString(cursor.getColumnIndex(cli.RG_INSCRICAO_ESTADUAL)));

                cli.setCli_limite(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(cli.LIMITE_CLIENTE))).setScale(2,RoundingMode.HALF_UP));
                cli.setCli_email(cursor.getString(cursor.getColumnIndex(cli.EMAIL_CLIENTE)));
                cli.setCli_observacao(cursor.getString(cursor.getColumnIndex(cli.OBSERVACAO)));

                cli.setUsu_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_VENDEDOR_USUARIO)));
                cli.setCli_senha(cursor.getString(cursor.getColumnIndex(cli.SENHA_DO_CLIENTE)));
                cli.setCli_enviado(cursor.getString(cursor.getColumnIndex(cli.CLIENTE_ENVIADO)));
                cli.setCli_chave(cursor.getString(cursor.getColumnIndex(cli.CHAVE_CLIENTE)));

                listadeclientes.add(cli);
            }

        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();
        }


        return listadeclientes;

    }


    public List<Cliente_SqliteBean> getAll(String query, String field) {

        List<Cliente_SqliteBean> listadeclientes = new ArrayList<>();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from clientes where " + field + " like '%" + query + "%'", null);


            while (cursor.moveToNext()) {

                Cliente_SqliteBean cli = new Cliente_SqliteBean();

                cli.setCli_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_DO_CLIENTE)));
                cli.setCli_nome(cursor.getString(cursor.getColumnIndex(cli.NOME_DO_CLIENTE)));
                cli.setCli_fantasia(cursor.getString(cursor.getColumnIndex(cli.NOME_FANTASIA)));
                cli.setCli_endereco(cursor.getString(cursor.getColumnIndex(cli.ENDERECO_CLIENTE)));

                cli.setCli_bairro(cursor.getString(cursor.getColumnIndex(cli.BAIRRO_CLIENTE)));
                cli.setCli_cep(cursor.getString(cursor.getColumnIndex(cli.CEP_CIDADE)));
                cli.setCid_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_CIDADE)));

                cli.setCli_contato1(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE1)));
                cli.setCli_contato2(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE2)));
                cli.setCli_contato3(cursor.getString(cursor.getColumnIndex(cli.CONTATO_CLIENTE3)));

                cli.setCli_nascimento(cursor.getString(cursor.getColumnIndex(cli.DATA_NASCIMETO)));
                cli.setCli_cpfcnpj(cursor.getString(cursor.getColumnIndex(cli.CPF_CNPJ_CLIENTE)));
                cli.setCli_rginscricaoest(cursor.getString(cursor.getColumnIndex(cli.RG_INSCRICAO_ESTADUAL)));

                cli.setCli_limite(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(cli.LIMITE_CLIENTE))).setScale(2,RoundingMode.HALF_UP));
                cli.setCli_email(cursor.getString(cursor.getColumnIndex(cli.EMAIL_CLIENTE)));
                cli.setCli_observacao(cursor.getString(cursor.getColumnIndex(cli.OBSERVACAO)));

                cli.setUsu_codigo(cursor.getInt(cursor.getColumnIndex(cli.CODIGO_VENDEDOR_USUARIO)));
                cli.setCli_senha(cursor.getString(cursor.getColumnIndex(cli.SENHA_DO_CLIENTE)));
                cli.setCli_enviado(cursor.getString(cursor.getColumnIndex(cli.CLIENTE_ENVIADO)));
                cli.setCli_chave(cursor.getString(cursor.getColumnIndex(cli.CHAVE_CLIENTE)));

                listadeclientes.add(cli);
            }

        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();
        }


        return listadeclientes;

    }

    public Cursor buscatodosClientes() {
        Cliente_SqliteBean cliente = new Cliente_SqliteBean();
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        try {

            cursor = db.query(TABLE_NAME, new String[]{cliente.CODIGO_DO_CLIENTE + " as  _id", cliente.NOME_DO_CLIENTE, cliente.NOME_FANTASIA, cliente.ENDERECO_CLIENTE, cliente.CONTATO_CLIENTE1, cliente.CPF_CNPJ_CLIENTE, cliente.LIMITE_CLIENTE, cliente.EMAIL_CLIENTE, cliente.CLIENTE_ENVIADO}, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();

        }

        return cursor;
    }

    public Cursor busca_cliente_where_field(String inputText, int field) throws SQLiteException {
        SQLiteDatabase db = new Db(ctx).getReadableDatabase();
        Cliente_SqliteBean cliente = new Cliente_SqliteBean();
        Cursor resultset = null;

        try {

            if (inputText == null || inputText.length() == 0) {
                resultset = db.query(TABLE_NAME, new String[]{cliente.CODIGO_DO_CLIENTE + " as  _id", cliente.NOME_DO_CLIENTE, cliente.NOME_FANTASIA, cliente.ENDERECO_CLIENTE, cliente.CONTATO_CLIENTE1, cliente.CPF_CNPJ_CLIENTE, cliente.LIMITE_CLIENTE, cliente.EMAIL_CLIENTE, cliente.CLIENTE_ENVIADO}, null, null, null, null, null);

            } else {

                switch (field) {
                    case NOME_CLIENTE:
                        resultset = db.query(true, TABLE_NAME, new String[]{cliente.CODIGO_DO_CLIENTE + " as  _id", cliente.NOME_DO_CLIENTE, cliente.NOME_FANTASIA, cliente.ENDERECO_CLIENTE, cliente.CONTATO_CLIENTE1, cliente.CPF_CNPJ_CLIENTE, cliente.LIMITE_CLIENTE, cliente.EMAIL_CLIENTE, cliente.CLIENTE_ENVIADO}, cliente.NOME_DO_CLIENTE + " like '%" + inputText + "%'", null, null, null, null, null);
                        break;

                    case BAIRRO:
                        resultset = db.query(true, TABLE_NAME, new String[]{cliente.CODIGO_DO_CLIENTE + " as  _id", cliente.NOME_DO_CLIENTE, cliente.NOME_FANTASIA, cliente.ENDERECO_CLIENTE, cliente.CONTATO_CLIENTE1, cliente.CPF_CNPJ_CLIENTE, cliente.LIMITE_CLIENTE, cliente.EMAIL_CLIENTE, cliente.CLIENTE_ENVIADO}, cliente.BAIRRO_CLIENTE + " like '%" + inputText + "%'", null, null, null, null, null);
                        break;

                    case FANTASIA:
                        resultset = db.query(true, TABLE_NAME, new String[]{cliente.CODIGO_DO_CLIENTE + " as  _id", cliente.NOME_DO_CLIENTE, cliente.NOME_FANTASIA, cliente.ENDERECO_CLIENTE, cliente.CONTATO_CLIENTE1, cliente.CPF_CNPJ_CLIENTE, cliente.LIMITE_CLIENTE, cliente.EMAIL_CLIENTE, cliente.CLIENTE_ENVIADO}, cliente.NOME_FANTASIA + " like '%" + inputText + "%'", null, null, null, null, null);
                        break;

                    case ENDERECO:
                        resultset = db.query(true, TABLE_NAME, new String[]{cliente.CODIGO_DO_CLIENTE + " as  _id", cliente.NOME_DO_CLIENTE, cliente.NOME_FANTASIA, cliente.ENDERECO_CLIENTE, cliente.CONTATO_CLIENTE1, cliente.CPF_CNPJ_CLIENTE, cliente.LIMITE_CLIENTE, cliente.EMAIL_CLIENTE, cliente.CLIENTE_ENVIADO}, cliente.ENDERECO_CLIENTE + " like '%" + inputText + "%'", null, null, null, null, null);
                        break;

                }

            }
            if (resultset != null) {
                resultset.moveToFirst();
            }

        } catch (SQLiteException e) {
            Log.d("Script", e.getMessage());
        } finally {

            db.close();

        }

        return resultset;

    }

}
