package br.com.app.platinumapp.appvendas.Model;

/**
 * Created by JAVA-NOT on 07/09/2017.
 */

public class FirebaseBean {

    public static String FIREBASE_CODIGO = "fbs_codigo";
    public static String FIREBASE_MSG = "fbs_mensagem";
    public static String FIREBASE_TITULO = "fbs_titulo";
    public static String FIREBASE_DATAHORA = "fbs_datahora";
    public static String FIREBASE_TYPE = "fbs_type";

    private Integer fbs_codigo;
    private String fbs_mensagem;
    private String fbs_titulo;
    private String fbs_datahora;
    private String fbs_type;

    public FirebaseBean(String fbs_mensagem, String fbs_titulo, String fbs_datahora, String fbs_type) {
        this.fbs_mensagem = fbs_mensagem;
        this.fbs_titulo = fbs_titulo;
        this.fbs_datahora = fbs_datahora;
        this.fbs_type = fbs_type;
    }

    public FirebaseBean() {
    }

    public static String getFirebaseCodigo() {
        return FIREBASE_CODIGO;
    }

    public static void setFirebaseCodigo(String firebaseCodigo) {
        FIREBASE_CODIGO = firebaseCodigo;
    }

    public static String getFirebaseMsg() {
        return FIREBASE_MSG;
    }

    public static void setFirebaseMsg(String firebaseMsg) {
        FIREBASE_MSG = firebaseMsg;
    }

    public static String getFirebaseTitulo() {
        return FIREBASE_TITULO;
    }

    public static void setFirebaseTitulo(String firebaseTitulo) {
        FIREBASE_TITULO = firebaseTitulo;
    }

    public static String getFirebaseDatahora() {
        return FIREBASE_DATAHORA;
    }

    public static void setFirebaseDatahora(String firebaseDatahora) {
        FIREBASE_DATAHORA = firebaseDatahora;
    }

    public static String getFirebaseType() {
        return FIREBASE_TYPE;
    }

    public static void setFirebaseType(String firebaseType) {
        FIREBASE_TYPE = firebaseType;
    }

    public Integer getFbs_codigo() {
        return fbs_codigo;
    }

    public void setFbs_codigo(Integer fbs_codigo) {
        this.fbs_codigo = fbs_codigo;
    }

    public String getFbs_mensagem() {
        return fbs_mensagem;
    }

    public void setFbs_mensagem(String fbs_mensagem) {
        this.fbs_mensagem = fbs_mensagem;
    }

    public String getFbs_titulo() {
        return fbs_titulo;
    }

    public void setFbs_titulo(String fbs_titulo) {
        this.fbs_titulo = fbs_titulo;
    }

    public String getFbs_datahora() {
        return fbs_datahora;
    }

    public void setFbs_datahora(String fbs_datahora) {
        this.fbs_datahora = fbs_datahora;
    }

    public String getFbs_type() {
        return fbs_type;
    }

    public void setFbs_type(String fbs_type) {
        this.fbs_type = fbs_type;
    }
}
