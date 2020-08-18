package br.com.app.platinumapp.appvendas.Model;



public class ImageLoaderProdutoBean {

    private String descricao_produto;
    private String url_image;
    private Integer codigo_produto;


    public ImageLoaderProdutoBean() {

    }

    public ImageLoaderProdutoBean(String descricao_produto, String url_image, Integer codigo) {
        this.descricao_produto = descricao_produto;
        this.url_image = url_image;
        this.codigo_produto = codigo;
    }

    public String getDescricao_produto() {
        return descricao_produto;
    }

    public void setDescricao_produto(String descricao_produto) {
        this.descricao_produto = descricao_produto;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }


    public Integer getCodigo_produto() {
        return codigo_produto;
    }

    public void setCodigo_produto(Integer codigo_produto) {
        this.codigo_produto = codigo_produto;
    }
}
