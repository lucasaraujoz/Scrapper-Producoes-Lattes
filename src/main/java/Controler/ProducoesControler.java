package Controler;

import Entidades.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class ProducoesControler {
    private Map<String, Pessoa> pessoas;

    public ProducoesControler() throws ParserConfigurationException, IOException, SAXException {
        this.pessoas = importPessoas();
    }

    private Map<String, Pessoa> importPessoas() throws ParserConfigurationException, IOException, SAXException {
        TreeMap<String, Pessoa> pessoas = new TreeMap<>();
        File folder = new File("curriculos");
        for (File file : folder.listFiles()) {
            {
                if (file.isFile()) {
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    DocumentBuilder db;
                    db = dbf.newDocumentBuilder();
                    Document doc = db.parse(file.getPath()); // um arquivo xml por vez
                    String nomeCurriculo = doc
                            .getElementsByTagName("DADOS-GERAIS")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("NOME-COMPLETO")
                            .getNodeValue();
                    Pessoa pessoa = new Pessoa(nomeCurriculo);
                    importArtigo(doc, pessoa);
                    importLivros(doc, pessoa);
                    importCongresso(doc, pessoa);
                    importResumo(doc, pessoa);
                    pessoas.put(pessoa.getNome(), pessoa);
                }
            }
        }
        return pessoas;
    }

    private void importArtigo(Document doc, Pessoa pessoa) {
        NodeList artigos = doc.getElementsByTagName("ARTIGO-PUBLICADO");
        for (int i = 0; i < artigos.getLength(); i++) {
            Node artigo = artigos.item(i);
            Element element = (Element) artigo;
            NodeList detalhamento = element.getElementsByTagName("DADOS-BASICOS-DO-ARTIGO");
            String titulo = detalhamento.item(0).getAttributes().getNamedItem("TITULO-DO-ARTIGO").getNodeValue();
            String ano = detalhamento.item(0).getAttributes().getNamedItem("ANO-DO-ARTIGO").getNodeValue();
            NodeList autores = element.getElementsByTagName("AUTORES");
            StringBuilder autoresss = new StringBuilder();
            for (int j = 0; j < autores.getLength(); j++) {
                Node autor = autores.item(j);
                String nome = autor.getAttributes().getNamedItem("NOME-PARA-CITACAO")
                        .getNodeValue()
                        .toUpperCase();
                autoresss.append(nome).append("; ");
            }
//                System.out.println(titulo);
            Artigo artigoPublicado = new Artigo(titulo, autoresss.toString(), ano);
            pessoa.addProducao(artigoPublicado);
        }
    }

    private void importCongresso(Document doc, Pessoa pessoa) {
        NodeList congresso = doc.getElementsByTagName("TRABALHO-EM-EVENTOS");
        for (int i = 0; i < congresso.getLength(); i++) {
            Node congreso = congresso.item(i);
            Element element = (Element) congreso;
            NodeList detalhamento = element.getElementsByTagName("DADOS-BASICOS-DO-TRABALHO");
            if (detalhamento.item(0).getAttributes().
                    getNamedItem("NATUREZA").
                    getNodeValue().equals("COMPLETO")) {
                String titulo = detalhamento.item(0).getAttributes().getNamedItem("TITULO-DO-TRABALHO").getNodeValue();
                String ano = detalhamento.item(0).getAttributes().getNamedItem("ANO-DO-TRABALHO").getNodeValue();
                NodeList autores = element.getElementsByTagName("AUTORES");
                String autoresss = "";
                for (int j = 0; j < autores.getLength(); j++) {
                    Node autor = autores.item(j);
                    String nome = autor.getAttributes().getNamedItem("NOME-COMPLETO-DO-AUTOR").getNodeValue();
                    autoresss += nome + "; ";
                }
                Congresso congresoPublicado = new Congresso(titulo, autoresss, ano);
                pessoa.addProducao(congresoPublicado);
            }
        }
    }

    private void importLivros(Document doc, Pessoa pessoa) {
        NodeList livros = doc.getElementsByTagName("CAPITULO-DE-LIVRO-PUBLICADO");
        for (int i = 0; i < livros.getLength(); i++) {
            Node livro = livros.item(i);
            Element element = (Element) livro;
            NodeList detalhamento = element.getElementsByTagName("DADOS-BASICOS-DO-CAPITULO");
            String titulo = detalhamento.item(0).getAttributes().getNamedItem("TITULO-DO-CAPITULO-DO-LIVRO").getNodeValue();
            String ano = detalhamento.item(0).getAttributes().getNamedItem("ANO").getNodeValue();
            NodeList autores = element.getElementsByTagName("AUTORES");
            String autoresss = "";
            for (int j = 0; j < autores.getLength(); j++) {
                Node autor = autores.item(j);
                String nome = autor.getAttributes().getNamedItem("NOME-COMPLETO-DO-AUTOR").getNodeValue();
                autoresss += nome + "; ";
            }
            Livro livroPublicado = new Livro(titulo, autoresss, ano);
            pessoa.addProducao(livroPublicado);
        }
    }

    private void importResumo(Document doc, Pessoa pessoa) {
        NodeList resumos = doc.getElementsByTagName("TRABALHO-EM-EVENTOS");
        for (int i = 0; i < resumos.getLength(); i++) {
            Node resumo = resumos.item(i);
            Element element = (Element) resumo;
            NodeList detalhamento = element.getElementsByTagName("DADOS-BASICOS-DO-TRABALHO");
            if (detalhamento.item(0).getAttributes().
                    getNamedItem("NATUREZA").
                    getNodeValue().equals("RESUMO")) {
                String titulo = detalhamento.item(0).getAttributes().getNamedItem("TITULO-DO-TRABALHO").getNodeValue();
                String ano = detalhamento.item(0).getAttributes().getNamedItem("ANO-DO-TRABALHO").getNodeValue();
                NodeList autores = element.getElementsByTagName("AUTORES");
                String autoresss = "";
                for (int j = 0; j < autores.getLength(); j++) {
                    Node autor = autores.item(j);
                    String nome = autor.getAttributes().getNamedItem("NOME-COMPLETO-DO-AUTOR").getNodeValue();
                    autoresss += nome + "; ";
                }
                Resumo resumoCongresso = new Resumo(titulo, autoresss, ano);
                pessoa.addProducao(resumoCongresso);
            }
        }
    }

    public Map<String, Pessoa> getPessoas() {
        return this.pessoas;
    }
}
