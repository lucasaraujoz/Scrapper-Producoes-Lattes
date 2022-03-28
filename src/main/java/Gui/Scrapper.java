package Gui;

import Controler.ProducoesControler;
import Entidades.*;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class Scrapper {
    private JPanel panel1;
    private JTable table1;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JTable table2;
    private JButton exportarButton;
    private JTabbedPane tabbedPane1;
    private JButton button1;

    private void atualizaComboBox(ProducoesControler producoesControler) {
        Pessoa pessoa = producoesControler.getPessoas().get(comboBox2.getSelectedItem());
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0); //limpa a tabela
        int i = 1;
        for (Producao producoes : pessoa.getProducoes().values()) {
            if (producoes.getClass().getSimpleName().equals(comboBox1.getSelectedItem())) {
                model.addRow(new Object[]{i, producoes.getTitulo(), producoes.getAutor(), producoes.getAno()});
                i++;
            }
        }
    }

    private void loadingButtons() {
        Image img = new ImageIcon("img/pdf3.png").getImage();
        ImageIcon icon = new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH));
        exportarButton.setIcon(icon);
        exportarButton.setBorder(BorderFactory.createRaisedBevelBorder());
        exportarButton.setForeground(Color.BLACK);
        exportarButton.setFocusPainted(true);
        exportarButton.setFocusable(true);
        exportarButton.setContentAreaFilled(true);
        exportarButton.setOpaque(true);
        exportarButton.setBackground(Color.WHITE);
        exportarButton.setForeground(Color.BLACK);
        exportarButton.setBorderPainted(true);
        exportarButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        exportarButton.setToolTipText("Exporta toda a tabela para PDF");
        exportarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exportarButton.setHorizontalTextPosition(SwingConstants.CENTER);
        exportarButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        exportarButton.setFont(new Font("Arial", Font.PLAIN, 12));
        exportarButton.setMargin(new Insets(0, 0, 0, 0));
        exportarButton.setBounds(new Rectangle(new Point(10, 10), new Dimension(32, 32)));
        exportarButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                exportarButton.setBackground(Color.PINK);
            }
        });
        exportarButton.addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent evt) {
                exportarButton.setBackground(Color.WHITE);
            }
        });

        button1.setIcon(icon);
        button1.setBorder(BorderFactory.createRaisedBevelBorder());
        button1.setForeground(Color.BLACK);
        button1.setFocusPainted(true);
        button1.setFocusable(true);
        button1.setContentAreaFilled(true);
        button1.setOpaque(true);
        button1.setBackground(Color.WHITE);
        button1.setForeground(Color.BLACK);
        button1.setBorderPainted(true);
        button1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        button1.setToolTipText("Exporta o quantitativo para PDF");
        button1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button1.setHorizontalTextPosition(SwingConstants.CENTER);
        button1.setVerticalTextPosition(SwingConstants.BOTTOM);
        button1.setFont(new Font("Arial", Font.PLAIN, 12));
        button1.setMargin(new Insets(0, 0, 0, 0));
        button1.setBounds(new Rectangle(new Point(10, 10), new Dimension(32, 32)));
        button1.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button1.setBackground(Color.PINK);
            }
        });
        button1.addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent evt) {
                button1.setBackground(Color.WHITE);
            }
        });

    }

    private void exportarPdf(ProducoesControler producoesControler) throws FileNotFoundException {
        Pessoa pessoa = producoesControler.getPessoas().get(comboBox2.getSelectedItem());
        String nome = pessoa.getNome().split(" ")[0];
        JFileChooser filex = new JFileChooser();
        filex.setDialogTitle("Tentativa PDF");
        filex.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filex.setAcceptAllFileFilterUsed(false);
        filex.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
        filex.setSelectedFile(new File(nome + "-Relatorio" + ".pdf"));
        int returnVal = filex.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (filex.getSelectedFile().exists()) {
                //TODO - erro so ta salvando se o arquivo existe
                //TODO - ideia, criar arquivo temporario e depois renomear, caso o usuario cancele, o arquivo nao sera salvo

                int result = JOptionPane.showConfirmDialog(null, "O arquivo já existe, deseja sobrescrever?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    File file = filex.getSelectedFile();
                    if (file.getName().endsWith(".pdf")) {
                        try {
                            //criar arquivo pdf com o nome do arquivo
                            PdfWriter writer = new PdfWriter(file.getAbsolutePath());
                            //criar documento
                            PdfDocument pdf = new PdfDocument(writer);
                            pdf.setDefaultPageSize(PageSize.A4);
                            //criar pagina
                            Document document = new Document(pdf);
                            //criar tabela
                            document.add(new Paragraph("Tabela de Produções"));
                            JOptionPane.showMessageDialog(null, "PDF criado com sucesso!");
                            document.add(new Paragraph("Relatório de " + pessoa.getNome())
                                    .setBold().setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER));
                            Table header = new Table(new float[]{190f, 190f, 190f});
                            header.setBackgroundColor(com.itextpdf.kernel.color.Color.BLACK, 0.4f);
                            header.addCell(new Cell().add(new Paragraph("Produção"))
                                    .setBold()
                                    .setFontColor(com.itextpdf.kernel.color.Color.WHITE));
                            header.addCell(new Cell().add(new Paragraph("Autor"))
                                    .setBold()
                                    .setFontColor(com.itextpdf.kernel.color.Color.WHITE));
                            header.addCell(new Cell().add(new Paragraph("Ano"))
                                    .setBold()
                                    .setFontColor(com.itextpdf.kernel.color.Color.WHITE));
                            Table artigos = new Table(new float[]{190f, 190f, 190f});
                            Table congressos = new Table(new float[]{190f, 190f, 190f});
                            Table livro = new Table(new float[]{190f, 190f, 190f});
                            Table resumo = new Table(new float[]{190f, 190f, 190f});
                            for (Producao producao : pessoa.getProducoes().values()) {
                                if (producao instanceof Artigo) {
                                    artigos.addCell(new Cell().add(new Paragraph(producao.getTitulo()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    artigos.addCell(new Cell().add(new Paragraph(producao.getAutor()))
                                            .setTextAlignment(TextAlignment.CENTER));
                                    artigos.addCell(new Cell().add(new Paragraph(producao.getAno()))
                                            .setTextAlignment(TextAlignment.RIGHT));
//                    System.out.println(producao.getTitulo());
                                } else if (producao instanceof Congresso) {
                                    congressos.addCell(new Cell().add(new Paragraph(producao.getTitulo()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    congressos.addCell(new Cell().add(new Paragraph(producao.getAutor()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    congressos.addCell(new Cell().add(new Paragraph(producao.getAno()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                } else if (producao instanceof Livro) {
                                    livro.addCell(new Cell().add(new Paragraph(producao.getTitulo()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    livro.addCell(new Cell().add(new Paragraph(producao.getAutor()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    livro.addCell(new Cell().add(new Paragraph(producao.getAno()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                } else if (producao instanceof Resumo) {
                                    resumo.addCell(new Cell().add(new Paragraph(producao.getTitulo()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    resumo.addCell(new Cell().add(new Paragraph(producao.getAutor()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                    resumo.addCell(new Cell().add(new Paragraph(producao.getAno()))
                                            .setTextAlignment(TextAlignment.LEFT));
                                }
                            }
                            Paragraph p = new Paragraph("Artigos publicados");
                            document.add(p.setBorderBottom(new SolidBorder(1))
                                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
                            document.add(header);
                            document.add(artigos).setBottomMargin(20f);
                            Paragraph c = new Paragraph("Congressos");
                            document.add(c.setBorderBottom(new SolidBorder(1))
                                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
                            document.add(header);
                            document.add(congressos).setBottomMargin(20f);
                            Paragraph l = new Paragraph("Livros");
                            document.add(l.setBorderBottom(new SolidBorder(1))
                                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
                            document.add(header);
                            document.add(livro).setBottomMargin(20f);
                            Paragraph r = new Paragraph("Resumos");
                            document.add(r.setBorderBottom(new SolidBorder(1))
                                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
                            document.add(header);
                            document.add(resumo).setBottomMargin(20f);
                            document.close();
                            JOptionPane.showMessageDialog(null, "Relatório exportado com sucesso");

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar pdf");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
//            PdfWriter pdfWriter = new PdfWriter(path + "/" + nome + "-Relatorio.pdf");
//            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
//            pdfDocument.setDefaultPageSize(PageSize.A4);
//            Document document = new Document(pdfDocument);


        }
    }

    private void exportarPdfQnt(ProducoesControler producoesControler) throws FileNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar quantitativo pdf");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getAbsolutePath();
            Pessoa pessoa = producoesControler.getPessoas().get(comboBox2.getSelectedItem());
            String nome = pessoa.getNome().split(" ")[0];
            PdfWriter pdfWriter = new PdfWriter(path + "/" + nome + "-RelatorioQuantitativo.pdf");
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4);
            Document document = new Document(pdfDocument);

            document.add(new Paragraph("Relatório Quantitativo de " + pessoa.getNome())
                    .setBold().setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
            Table header = new Table(new float[]{190f, 190f});

            header.setBackgroundColor(com.itextpdf.kernel.color.WebColors.getRGBColor("#006400"), 0.4f);

            header.addCell(new Cell().add(new Paragraph("Ano"))
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.color.Color.WHITE));
            header.addCell(new Cell().add(new Paragraph("Quantidade"))
                    .setBold()
                    .setFontColor(com.itextpdf.kernel.color.Color.WHITE));

            Table artigos = new Table(new float[]{190f, 190f});
            Table congressos = new Table(new float[]{190f, 190f});
            Table livro = new Table(new float[]{190f, 190f});
            Table resumo = new Table(new float[]{190f, 190f});
            String[] tipos = {"Artigo", "Congresso", "Livro", "Resumo"};
            for (String t : tipos) {
                Map<String, Integer> datas = dataProducoes(pessoa, t);
                for (var data : datas.keySet()) {
                    if (t.equals("Artigo")) {
                        artigos.addCell(new Cell().add(new Paragraph(data)));
                        artigos.addCell(new Cell().add(new Paragraph(datas.get(data).toString())));
                    } else if (t.equals("Congresso")) {
                        congressos.addCell(new Cell().add(new Paragraph(data)));
                        congressos.addCell(new Cell().add(new Paragraph(datas.get(data).toString())));
                    } else if (t.equals("Livro")) {
                        livro.addCell(new Cell().add(new Paragraph(data)));
                        livro.addCell(new Cell().add(new Paragraph(datas.get(data).toString())));
                    } else if (t.equals("Resumo")) {
                        resumo.addCell(new Cell().add(new Paragraph(data)));
                        resumo.addCell(new Cell().add(new Paragraph(datas.get(data).toString())));
                    }
                }
            }
            document.add(new Paragraph("Artigos").setBorderBottom(new SolidBorder(1))
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
            document.add(header).setBottomMargin(20f);
            document.add(artigos).setBottomMargin(20f);
            document.add(new Paragraph("Congresso").setBorderBottom(new SolidBorder(1))
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
            document.add(header).setBottomMargin(20f);
            document.add(congressos).setBottomMargin(20f);
            document.add(new Paragraph("Livro").setBorderBottom(new SolidBorder(1))
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
            document.add(header).setBottomMargin(20f);
            document.add(livro).setBottomMargin(20f);
            document.add(new Paragraph("Resumo").setBorderBottom(new SolidBorder(1))
                    .setBold().setFontSize(20).setTextAlignment(TextAlignment.LEFT));
            document.add(header).setBottomMargin(20f);
            document.add(resumo).setBottomMargin(20f);

            document.close();
            JOptionPane.showMessageDialog(null, "Relatório Quantitativo gerado com sucesso!");
        }
    }

    public Scrapper() throws ParserConfigurationException, IOException, SAXException {
        loadingButtons();
        ProducoesControler producoesControler = new ProducoesControler();
        for (Pessoa pessoa : producoesControler.getPessoas().values()) { //Map<String,Pessoa>
            comboBox2.addItem(pessoa.getNome());
        }
        atualizaComboBox(producoesControler);
        atualizaQuanitativo(producoesControler);
        comboBox2.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                atualizaQuanitativo(producoesControler);
                atualizaComboBox(producoesControler);
            }
        });
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                atualizaComboBox(producoesControler);
                atualizaQuanitativo(producoesControler);
            }
        });
        exportarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exportarPdf(producoesControler);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        table1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.getClickCount() == 2) {
                    int row = table1.getSelectedRow();
                    //selecionar coluna especifica
                    String titulo = (String) table1.getValueAt(row, 1);
                    Producao producao = producoesControler.getPessoas()
                            .get(comboBox2.getSelectedItem()
                                    .toString()).getProducoes().get(titulo);
                    String texto = "Titulo: " + producao.getTitulo() + "\n";
                    texto += "Autor: " + producao.getAutor() + "\n";
                    texto += "Ano: " + producao.getAno() + "\n";
                    JOptionPane.showMessageDialog(null, texto);
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    exportarPdfQnt(producoesControler);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void atualizaQuanitativo(ProducoesControler producoesControler) {
        Pessoa pessoa = producoesControler.getPessoas().get(comboBox2.getSelectedItem());
        String[] tipos = {"Artigo", "Congresso", "Livro", "Resumo"};
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        model.setRowCount(0);
        for (String t : tipos) {
            System.out.println(t + ":");
            model.addRow(new Object[]{"#" + t});
            mostrarDatas(dataProducoes(pessoa, t));
        }
    }

    private void mostrarDatas(Map<String, Integer> datas) {
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        for (String data : datas.keySet()) {
            model.addRow(new Object[]{data, datas.get(data)});
        }
    }

    private Map<String, Integer> dataProducoes(Pessoa pessoa, String tipo) {
        Map<String, Integer> aux = new TreeMap<>();
        for (Producao producao : pessoa.getProducoes().values()) {
            if (producao.getClass().getSimpleName().equals(tipo)) {
                if (aux.containsKey(producao.getAno())) {
                    aux.put(producao.getAno(), aux.get(producao.getAno()) + 1);
                } else {
                    aux.put(producao.getAno(), 1);
                }
            }
        }
        return aux;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Scrapper");
        try {
            frame.setContentPane(new Scrapper().panel1);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        //change look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        //change icon of the frame
        frame.setIconImage(new ImageIcon("./img/crab.png").getImage());
        //aumentar icone do frame

    }

    private void createUIComponents() {
        String header2[] = {"Ano", "Quantidade"};
        DefaultTableModel model2 = new DefaultTableModel(0, 2);
        model2.setColumnIdentifiers(header2);
        table2 = new JTable(model2);
        table2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                Object inicio = table.getValueAt(row, 0);
                if (inicio != null && inicio.toString().startsWith("#")) {
                    setBackground(new Color(255, 123, 13));
                } else {
                    setBackground(new Color(255, 255, 255));
                }
                return this;
            }
        });
        table2.setDefaultEditor(Object.class, null);
        //define width of columns
        String header[] = {"id", "Titulo", "Autores", "Ano"};
        DefaultTableModel model = new DefaultTableModel(0, 4);
        model.setColumnIdentifiers(header);
        table1 = new JTable(model);
        table1.getColumnModel().getColumn(0).setPreferredWidth(5);
        table1.getColumnModel().getColumn(1).setPreferredWidth(1000);
        table1.getColumnModel().getColumn(2).setPreferredWidth(400);
        table1.getColumnModel().getColumn(3).setPreferredWidth(25);
        //bloquear edição de celulas
        table1.setDefaultEditor(Object.class, null);
        table1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (row % 2 == 0) { //&& !isSelected
                    setBackground(new Color(183, 217, 241));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(new Color(255, 255, 255));
                    setForeground(Color.BLACK);
                }
                if (isSelected) {
                    setBackground(new Color(255, 123, 13));
                    setForeground(Color.WHITE);
                    setFont(new Font("Arial", Font.BOLD, 12));
                }
                if (column == 0) {
                    setBackground(Color.LIGHT_GRAY);
                }
                if (column == 3) {
                    setBackground(Color.LIGHT_GRAY);
                }
                //ilumina so a celula selecionada
//                if(hasFocus){
//                    setBackground(Color.ORANGE);
//                }
                return this;
            }
        });
    }
}
