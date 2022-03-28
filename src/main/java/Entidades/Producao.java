package Entidades;

public class Producao {
    private String titulo;
    private String autor;
    private String ano;

    public Producao(String titulo, String autor, String ano) {
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getAno() {
        return ano;
    }
}
