package Entidades;

import java.util.Map;
import java.util.TreeMap;

public class Pessoa {
    private String nome;
    private Map<String, Producao> producoes;

    public Pessoa(String nome) {
        this.nome = nome;
        this.producoes = new TreeMap<>();
    }

    public void addProducao(Producao producao) {
        this.producoes.put(producao.getTitulo(), producao);
    }

    public String getNome() {
        return nome;
    }

    public Map<String, Producao> getProducoes() {
        return producoes;
    }
}
