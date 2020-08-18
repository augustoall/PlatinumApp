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

public class Cheque_SqliteDao {

    private static final String TABLE_NAME = "CHEQUE";
    private Context ctx;
    private String Sql;
    private boolean gravacao;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;

    public Cheque_SqliteDao(Context ctx) {
        this.ctx = ctx;
    }

    public boolean gravar_cheque(Cheques_SqliteBean cheque) {
        try {

            db = new Db(ctx).getWritableDatabase();
            gravacao = false;
            Sql = "insert into cheque (chq_cli_codigo,chq_numerocheque,chq_telefone1,chq_telefone2,chq_cpf_dono,chq_nomedono,chq_nomebanco,chq_vencimento,chq_valorcheque,chq_terceiro,vendac_chave,chq_enviado,chq_dataCadastro) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";

            stmt = db.compileStatement(Sql);

            stmt.bindLong(1, cheque.getChq_cli_codigo());
            stmt.bindString(2, cheque.getChq_numerocheque());

            stmt.bindString(3, cheque.getChq_telefone1());
            stmt.bindString(4, cheque.getChq_telefone2());

            stmt.bindString(5, cheque.getChq_cpf_dono());
            stmt.bindString(6, cheque.getChq_nomedono());
            stmt.bindString(7, cheque.getChq_nomebanco());

            stmt.bindString(8, cheque.getChq_vencimento());
            stmt.bindDouble(9, cheque.getChq_valorcheque().doubleValue());

            stmt.bindString(10, cheque.getChq_terceiro());
            stmt.bindString(11, cheque.getVendac_chave());

            stmt.bindString(12, cheque.getChq_enviado());

            stmt.bindString(13, cheque.getChq_dataCadastro());

            if (stmt.executeInsert() > 0) {
                gravacao = true;
                Sql = "";
            }

            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("gravar_cheque", e.getMessage());
            gravacao = false;
        } finally {
            db.close();
            stmt.close();
        }

        return gravacao;
    }

    public void atualiza_dados_cliente_cadastrado_offline_cheque(Integer new_codigo, Integer old_codigo) {
        try {
            db = new Db(ctx).getWritableDatabase();
            Sql = "update cheque set chq_cli_codigo=? where chq_cli_codigo=?";
            stmt = db.compileStatement(Sql);
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


    public List<Cheques_SqliteBean> busca_todos_cheques_nao_enviados() {

        ArrayList<Cheques_SqliteBean> lista_de_cheques = new ArrayList<>();
        db = new Db(ctx).getReadableDatabase();

        try {

            cursor = db.rawQuery("select * from cheque where chq_enviado = 'N' and vendac_chave != '' ", null);

            while (cursor.moveToNext()) {

                Cheques_SqliteBean cheque = new Cheques_SqliteBean();

                cheque.setChq_codigo(cursor.getInt(cursor.getColumnIndex(cheque.CHEQUE_ID)));
                cheque.setChq_cli_codigo(cursor.getInt(cursor.getColumnIndex(cheque.CHEQUE_CLI_CODIGO)));
                cheque.setChq_numerocheque(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_NUMERO)));
                cheque.setChq_telefone1(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_TELEFONE1)));
                cheque.setChq_telefone2(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_TELEFONE2)));
                cheque.setChq_cpf_dono(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_CPF_DONO)));
                cheque.setChq_nomedono(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_DONO)));
                cheque.setChq_nomebanco(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_NOME_BANCO)));
                cheque.setChq_vencimento(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_VECIMENTO)));
                cheque.setChq_valorcheque(new BigDecimal(cursor.getDouble(cursor.getColumnIndex(cheque.CHEQUE_VALOR))));
                cheque.setChq_terceiro(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_TERCEIRO)));
                cheque.setVendac_chave(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_CHAVEVENDA)));
                cheque.setChq_enviado(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_ENVIADO)));
                cheque.setChq_dataCadastro(cursor.getString(cursor.getColumnIndex(cheque.CHEQUE_DATA_CADASTRO)));

                lista_de_cheques.add(cheque);

            }

        } catch (SQLiteException e) {
            Log.d("script", e.getMessage());
        } finally {
            db.close();
        }


        return lista_de_cheques;


    }

    public void atualiza_vendac_chave_no_cheque(String Vendac_chave, Integer cli_codigo, String chq_dataCadastro) {

        try {
            db = new Db(ctx).getWritableDatabase();

            Sql = "update cheque set vendac_chave = ? where chq_cli_codigo = ? and chq_dataCadastro = ?";
            stmt = db.compileStatement(Sql);

            stmt.bindString(1, Vendac_chave);
            stmt.bindLong(2, cli_codigo);
            stmt.bindString(3, chq_dataCadastro);

            stmt.executeUpdateDelete();
            stmt.clearBindings();

        } catch (SQLiteException e) {
            Log.d("update_chave_no_cheque", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public void atualiza_cheque_para_enviado(Cheques_SqliteBean cheque) {

        try {
            db = new Db(ctx).getWritableDatabase();
            Sql = "update cheque set chq_enviado = 'S' where vendac_chave = ? and  chq_codigo =? and chq_numerocheque = ? ";
            stmt = db.compileStatement(Sql);
            stmt.bindString(1, cheque.getVendac_chave());
            stmt.bindLong(2, cheque.getChq_codigo());
            stmt.bindString(3, cheque.getChq_numerocheque());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("atualiza_cheque_para_enviado", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }

    }


    public void exclui_cheque_sem_venda_vinculada() {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "delete from cheque where vendac_chave = '' ";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.executeUpdateDelete();
            Log.i("script", "cheques sem vendas excluidos");
        } catch (SQLiteException e) {
            Log.d("exclui_cheque_sem", e.getMessage());
        }
    }


    public void excluir_cheque(int chq_cli_codigo) {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "delete from cheque where chq_cli_codigo =  " + chq_cli_codigo;
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.executeUpdateDelete();
        } catch (SQLiteException e) {
            Log.d("excluir_cheque", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }

}
