package br.com.app.platinumapp.appvendas.Model;

import android.view.View;

/**
 * Created by JAVA-NOT-I3 on 14/09/2017.
 */

public class CatalogoProdutoModel  {

    private String Prd_codigo;
    private String Img_url;
    private String prd_descricao;
    private String prd_preco;


    public String getPrd_codigo() {
        return Prd_codigo;
    }

    public void setPrd_codigo(String prd_codigo) {
        Prd_codigo = prd_codigo;
    }

    public String getImg_url() {
        return Img_url;
    }

    public void setImg_url(String img_url) {
        Img_url = img_url;
    }

    public String getPrd_descricao() {
        return prd_descricao;
    }

    public void setPrd_descricao(String prd_descricao) {
        this.prd_descricao = prd_descricao;
    }

    public String getPrd_preco() {
        return prd_preco;
    }

    public void setPrd_preco(String prd_preco) {
        this.prd_preco = prd_preco;
    }
}
