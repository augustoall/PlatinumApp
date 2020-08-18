package br.com.app.platinumapp.appvendas.Model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JAVA-NOT on 07/09/2017.
 */

public class FireBaseDao {

    private Context ctx;
    private SQLiteStatement stmt;
    private SQLiteDatabase db;
    private Cursor cursor;
    private String sql;

    public FireBaseDao(Context ctx) {
        this.ctx = ctx;
    }


    public FireBaseDao save_msg(FirebaseBean firebase) {
        try {
            db = new Db(ctx).getWritableDatabase();
            sql = "insert into FIREBASE (fbs_mensagem,fbs_titulo,fbs_datahora,fbs_type) values (?,?,?,?)";
            stmt = db.compileStatement(sql);
            stmt.bindString(1, firebase.getFbs_mensagem());
            stmt.bindString(2, firebase.getFbs_titulo());
            stmt.bindString(3, firebase.getFbs_datahora());
            stmt.bindString(4, firebase.getFbs_type());
            stmt.executeInsert();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("save_msg", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
        return null;
    }


    public void deleteFirebaseLIC() {
        SQLiteDatabase db = new Db(ctx).getWritableDatabase();
        String sql = "delete from FIREBASE where fbs_type='LIC' ";
        SQLiteStatement stmt = db.compileStatement(sql);
        try {
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        } catch (SQLiteException e) {
            Log.d("delete", e.getMessage());
        } finally {
            db.close();
            stmt.close();
        }
    }


    public List<FirebaseBean> getAllFirebasesTypeLIC() {
        List<FirebaseBean> ListaFirebaseBean = new ArrayList<>();
        db = new Db(ctx).getReadableDatabase();
        try {
            cursor = db.rawQuery("select * from FIREBASE where fbs_type='LIC'", null);
            while (cursor.moveToNext()) {
                FirebaseBean firebaseBean = new FirebaseBean();
                firebaseBean.setFbs_mensagem(cursor.getString(cursor.getColumnIndex(firebaseBean.FIREBASE_MSG)));
                firebaseBean.setFbs_titulo(cursor.getString(cursor.getColumnIndex(firebaseBean.FIREBASE_TITULO)));
                firebaseBean.setFbs_datahora(cursor.getString(cursor.getColumnIndex(firebaseBean.FIREBASE_DATAHORA)));
                firebaseBean.setFbs_type(cursor.getString(cursor.getColumnIndex(firebaseBean.FIREBASE_TYPE)));
                ListaFirebaseBean.add(firebaseBean);
            }
        } catch (SQLiteException e) {
            Log.d("ListaCidades", e.getMessage());
        }
        return ListaFirebaseBean;
    }
}
