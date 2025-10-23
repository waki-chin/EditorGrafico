import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import static java.lang.Math.abs;

public class Editor extends JFrame implements ChangeListener{
    static Color corAtual = Color.black;
    static boolean esperaPonto, esperaInicioLinha, esperaFimLinha, esperaInicioOval,
            esperaRaioOvalA, esperaRaioOvalB, esperaInicioCirculo, esperaFimCirculo,
            esperaInicioRetangulo, esperaLarguraRetangulo, esperaAlturaRetangulo,
            esperaInicioPolilinha, desenhandoPolilinha;
    static private MeuJPanel pnlDesenho;
    private static ManterFiguras figuras;     // objeto de manutenção de vetor de figuras geométricas
    private static Polilinha novaPoli = null;
    private static JLabel statusBar1, statusBar2;
    private static Ponto p1 = new Ponto();  // ponto inicial de linha, circulo, etc.
    private static Ponto p2 = new Ponto();
    private JPopupMenu menuCor, menuSelecionar;
    private JColorChooser cCor;
    private static int[] vetSelecionadas;
    private static int qtasSelecionadas;

    private final JButton btnPonto, btnLinha, btnCirculo, btnElipse, btnCor, btnAbrir,
            btnSalvar,btnApagar, btnSair, btnRetangulo, btnPolilinha, btnSelecionar, btnDeslocar,
            btnLimpar, btnDesselecionar;

    private JPanel pnlBotoes;   // container dos botões

    static private JInternalFrame janelaFilha;

    public Editor()	// construtor de Editor que criará o JFrame, colocará seu
    {			          // título, estabelecerá um tamanho para o formulário e o
                    // exibirá
        super("Editor Gráfico");	      // cria o JFrame e coloca um título

        figuras = new ManterFiguras(100);
        vetSelecionadas = new int[100];
        qtasSelecionadas = 0;
        Icon imgAbrir = new ImageIcon("botoes\\abrir.jpg");
        btnAbrir = new JButton("Abrir", imgAbrir);
        btnSalvar = new JButton("Salvar", new ImageIcon("botoes\\salvar.jpg"));
        btnPonto = new JButton("Ponto", new ImageIcon("botoes\\ponto.jpg"));
        btnLinha = new JButton("Linha", new ImageIcon("botoes\\linha.jpg"));
        btnCirculo = new JButton("Circulo", new ImageIcon("botoes\\circulo.jpg"));
        btnElipse = new JButton("Elipse", new ImageIcon("botoes\\elipse.jpg"));
        btnRetangulo = new JButton("Retângulo");
        btnPolilinha = new JButton("Polilinha");
        btnSelecionar = new JButton("Selecionar");
        btnDesselecionar = new JButton("Desselecionar");
        btnDeslocar = new JButton("Deslocar");
        btnLimpar = new JButton("Limpar");
        btnCor = new JButton("Cores", new ImageIcon("botoes\\cores.jpg"));
        btnApagar = new JButton("Apagar", new ImageIcon("botoes\\apagar.jpg"));
        btnSair = new JButton("Sair", new ImageIcon("botoes\\sair.jpg"));

        // cria o JPanel que armazenará os botões
        pnlBotoes = new JPanel();
        // cria o layout usado para dispor fisicamente os botões no JPanel
        FlowLayout flwBotoes = new FlowLayout();
        // informa que os componentes do pnlBotoes serão dispostos em forma livre
        pnlBotoes.setLayout(flwBotoes);

        // adiciona os controles visuais (botões) ao painel de botões, de cima
        // para baixo, da esquerda para direita.
        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnElipse);
        pnlBotoes.add(btnRetangulo);
        pnlBotoes.add(btnPolilinha);
        pnlBotoes.add(btnCor);
        pnlBotoes.add(btnSelecionar);
        pnlBotoes.add(btnDesselecionar);
        pnlBotoes.add(btnDeslocar);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnLimpar);
        pnlBotoes.add(btnSair);
        pnlBotoes.add(btnLimpar);

        // associação de tratadores de eventos aos botões
        btnAbrir.addActionListener(new FazAbertura());
        btnSalvar.addActionListener(new FazSalvar());
        btnPonto.addActionListener(new DesenhaPonto());  // dá inicio ao fornecimento de um Ponto
        btnLinha.addActionListener(new DesenhaLinha());  // dá inicio ao fornecimento de uma Linha
        btnElipse.addActionListener(new DesenhaOval());
        btnCirculo.addActionListener(new DesenhaCirculo());
        btnRetangulo.addActionListener(new DesenhaRetangulo());
        btnPolilinha.addActionListener(new DesenhaPolilinha());
        btnCor.addActionListener(new MudarCor());
        btnSelecionar.addActionListener(new SelecionaFigura());
        btnDesselecionar.addActionListener(new DesselecionaFiguras());
        btnDeslocar.addActionListener(new DeslocaFiguras());
        btnApagar.addActionListener(new ApagaFiguras());
        btnLimpar.addActionListener(new LimpaDesenho());
        cCor = new JColorChooser(corAtual);
        cCor.getSelectionModel().addChangeListener(this);
        cCor.setPreviewPanel(new JPanel());
        menuCor = new JPopupMenu();
        menuCor.add(cCor);
        Container cntForm = getContentPane(); // acessa o painel de conteúdo do JFrame
        cntForm.setLayout(new BorderLayout());  // COnfigura layout do JFrame para Border
        cntForm.add(pnlBotoes , BorderLayout.NORTH);    // coloca JPanel no topo do Layout

        JDesktopPane panDesenho = new JDesktopPane();
        cntForm.add(panDesenho);

        // cria uma janela interna (janela filha) no modelo MDI
        janelaFilha = new JInternalFrame("Nenhum arquivo aberto", true, true, true, true);
        panDesenho.add(janelaFilha);

        // exibe o formulário
        setSize(700,500);			// dimensões do formulário em pixels
        setVisible(true);

        janelaFilha.setOpaque(true);
        // dimensiona a janela filha e a exibe
        janelaFilha.setSize(this.getWidth()/2, this.getHeight() / 2);
        janelaFilha.show();

        pnlDesenho = new MeuJPanel();
        Container cntFrame = janelaFilha.getContentPane();  // área de desenho da janela filha
        cntFrame.add(pnlDesenho);   // adiciona o JPanel à área de desenho da janela filha
    }
    public void stateChanged(ChangeEvent e){
        corAtual = cCor.getColor();
        if (qtasSelecionadas != 0) {
            for (int i = 0; i < qtasSelecionadas; i++) {
                Ponto figAtual = figuras.valorDe(vetSelecionadas[i]);
                redesenhaComEspessura(figAtual, pnlDesenho.getGraphics(), corAtual);
            }
        }
    }

    public static void main(String[] args)
    {
        Editor aplicacao = new Editor();

        // ouvinte de eventos sobre janelas, implementando
        // o tratador de evento WindowClosing(), que trata
        // do que fazer quando a janela for fechada
        aplicacao.addWindowListener (
                new WindowAdapter() {   //  cria instância da interface
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                }
        );
    }

    public static void desenharObjetos(Graphics g)
    {
        pnlDesenho.paintComponent(g);
    }

    private void limparEsperas()
    {
        esperaPonto = false;
        esperaInicioLinha = false;
        esperaFimLinha = false;
        esperaInicioOval = false;
        esperaRaioOvalA = false;
        esperaRaioOvalB = false;
        esperaInicioCirculo = false;
        esperaFimCirculo = false;
        esperaInicioRetangulo = false;
        esperaLarguraRetangulo = false;
        esperaAlturaRetangulo = false;
        esperaInicioPolilinha = false;
        desenhandoPolilinha = false;
    }

    private class FazAbertura implements ActionListener {
        public void actionPerformed(ActionEvent e)	// código executado no evento
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showOpenDialog(Editor.this);

            //código de verificação se um arquivo foi selecionado e obter seu nome
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File arquivo = arqEscolhido.getSelectedFile();
                System.out.println("Processando "+arquivo.getName());
                try {
                    Scanner arqFiguras = new Scanner(new File(arquivo.getName()));
                    try {

                        while (arqFiguras.hasNextLine())  // enquanto não acabou o arquivo
                        {
                            String linha = arqFiguras.nextLine();   // lemos a próxima linha do arquivo
                            String[] campos = linha.split(";");  // cria vetor de strings com os dados separados por ";"

                            String tipo = campos[0].trim();
                            int xBase = Integer.parseInt(campos[1].trim());
                            int yBase = Integer.parseInt(campos[2].trim());
                            int corR  = Integer.parseInt(campos[3].trim());
                            int corG  = Integer.parseInt(campos[4].trim());
                            int corB  = Integer.parseInt(campos[5].trim());
                            Color cor = new Color(corR, corG, corB);
                            switch (tipo)
                            {
                                case "p" : // figura é um ponto
                                    figuras.incluirNoFinal(new Ponto(xBase, yBase, cor));
                                    break;
                                case "l" : // figura é uma linha
                                    int xFinal = Integer.parseInt(campos[6].trim());
                                    int yFinal = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Linha(xBase, yBase, xFinal, yFinal, cor));
                                    break;
                                case "c" : // figura é um círculo
                                    int raio = Integer.parseInt(campos[6].trim());
                                    figuras.incluirNoFinal(new Circulo(xBase, yBase, raio, cor));
                                    break;
                                case "o" : // figura é uma oval
                                    int raioA = Integer.parseInt(campos[6].trim());
                                    int raioB = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Oval(xBase, yBase, raioA, raioB, cor));
                                    break;
                                case "r":
                                    int altura = Integer.parseInt(campos[6].trim());
                                    int largura = Integer.parseInt(campos[7].trim());
                                    figuras.incluirNoFinal(new Retangulo(xBase,yBase,altura,largura,cor));
                                    break;
                                case "pl":
                                    int qtosDados = Integer.parseInt(campos[6].trim());
                                    Polilinha novaPolilinha = new Polilinha(new Ponto(xBase,yBase,cor),qtosDados,corAtual);
                                    int campoAtual = 6;
                                    for(int i = 0; i < qtosDados;i++){
                                        int corRAtual = Integer.parseInt(campos[campoAtual+=1].trim());
                                        int corGAtual = Integer.parseInt(campos[campoAtual+=1].trim());
                                        int corBAtual = Integer.parseInt(campos[campoAtual+=1].trim());
                                        Color corAtual = new Color(corRAtual,corBAtual,corGAtual);
                                        int xAtual = Integer.parseInt(campos[campoAtual+=2].trim());
                                        int yAtual = Integer.parseInt(campos[campoAtual+=1].trim());
                                        novaPolilinha.incluirAoFinal(new Ponto(xAtual,yAtual,corAtual));
                                    }
                                    figuras.incluirNoFinal(novaPolilinha);
                            }
                        }
                        arqFiguras.close();

                        janelaFilha.setTitle(arquivo.getName());
                        desenharObjetos(pnlDesenho.getGraphics());
                    }
                    catch (Exception erroLeitura){
                        System.out.println("Erro de leitura no arquivo");
                    }
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }

    private class FazSalvar implements ActionListener {
        public void actionPerformed(ActionEvent e)  // código executado no evento
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showSaveDialog(Editor.this);

            //código de verificação se um arquivo foi selecionado e obter seu nome
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    figuras.gravarDados(arqEscolhido.getSelectedFile().getName());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    private class DesenhaPonto implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            statusBar1.setText("Mensagem: clique no local do ponto desejado:");
            limparEsperas();
            esperaPonto = true;
        }
    }

    private class DesenhaLinha implements ActionListener {
        public void actionPerformed(ActionEvent e)
        {
            statusBar1.setText("Mensagem: clique no ponto inicial da linha:");
            limparEsperas();
            esperaInicioLinha = true;
        }
    }

    private class DesenhaOval implements ActionListener{
        public void actionPerformed(ActionEvent e){
            statusBar1.setText("Mensagem: clique no centro da oval:");
            limparEsperas();
            esperaInicioOval = true;
        }
    }

    private class DesenhaCirculo implements ActionListener{
        public void actionPerformed(ActionEvent e){
            statusBar1.setText("Mensagem: clique no centro da circunferência:");
            limparEsperas();
            esperaInicioCirculo = true;
        }
    }

    private class DesenhaRetangulo implements ActionListener{
        public void actionPerformed(ActionEvent e){
            statusBar1.setText("Mensagem: clique no início do retângulo:");
            limparEsperas();
            esperaInicioRetangulo = true;
        }
    }

    private class DesenhaPolilinha implements ActionListener{
        public void actionPerformed(ActionEvent e){
            statusBar1.setText("Mensagem: clique no início da polilinha:");
            limparEsperas();
            esperaInicioPolilinha = true;
        }
    }

    private class MudarCor implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            menuCor.show(btnCor, 0, btnCor.getHeight());
        }
    }

    private class SelecionaFigura implements ActionListener {
        public void actionPerformed(ActionEvent e){
            limparEsperas();
            if (novaPoli != null){
                figuras.incluirNoFinal(novaPoli);
                novaPoli = null;
            }
            String strIndice = JOptionPane.showInputDialog(null, "Digite um índice:");
            if (strIndice != null){
                try{
                    int indice = Integer.parseInt(strIndice);
                    if (0 <= indice && indice < figuras.getTamanho()){
                        vetSelecionadas[qtasSelecionadas++] = indice;
                        redesenhaComEspessura(figuras.valorDe(indice), pnlDesenho.getGraphics(), figuras.valorDe(indice).getCor());
                    }
                }
                catch (NumberFormatException err){
                    JOptionPane.showMessageDialog(null, "Índice inválido.");
                }
            }
        }
    }

    private class DeslocaFiguras implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if (qtasSelecionadas != 0) {
                for (int i = 0; i < qtasSelecionadas; i++) {
                    Ponto figAtual = figuras.valorDe(vetSelecionadas[i]);
                    figAtual.deslocar(100, 100);
                    repaint();
                }
                qtasSelecionadas = 0;
            }
        }
    }

    private class ApagaFiguras implements ActionListener {
        public void actionPerformed(ActionEvent e){
            if (qtasSelecionadas != 0) {
                for (int i = 0; i < qtasSelecionadas; i++) {
                    figuras.excluir(vetSelecionadas[i]);
                    qtasSelecionadas = 0;
                    repaint();
                }
            }
        }
    }

    private class LimpaDesenho implements ActionListener {
        public void actionPerformed(ActionEvent e){
            figuras.limparVetor();
            repaint();
        }
    }

    private class DesselecionaFiguras implements ActionListener {
        public void actionPerformed(ActionEvent e){
            qtasSelecionadas = 0;
            repaint();
        }
    }

    public void redesenhaComEspessura(Ponto fig, Graphics g, Color cor){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3));
        fig.desenharComEspessura(cor, g2d);
    }


    private class MeuJPanel extends JPanel implements MouseListener, MouseMotionListener
    {
        JPanel pnlStatus = new JPanel();        // barra de status

        public MeuJPanel() {
            super();
            pnlStatus.setLayout(new GridLayout(1, 2)); // painel com 2 colunas
            statusBar1 = new JLabel("Mensagem");
            statusBar2 = new JLabel("Coordenada");
            pnlStatus.add(statusBar1); // label na coluna da esquerda
            pnlStatus.add(statusBar2); // label na coluna da direita
            getContentPane().add(pnlStatus, BorderLayout.SOUTH); // status no fundo do formulário
            addMouseListener(this);         // esta classe “ouve” cliques do mouse
            addMouseMotionListener(this);   // e “ouve” também seus movimentos
        }

        public void mouseMoved(MouseEvent e) {
            statusBar2.setText("Coordenada: "+e.getX()+","+e.getY());
        }
        public void mouseDragged(MouseEvent e) {
// não faz nada por enquanto
        }

        public void mouseClicked (MouseEvent e) {
            statusBar1.setText("Mensagem:");
        }


        public void mousePressed (MouseEvent e) {

            if (novaPoli != null && !desenhandoPolilinha){
                figuras.incluirNoFinal(novaPoli);
                novaPoli = null;
            }
            if (esperaPonto) {
                Ponto novoPonto = new Ponto(e.getX(), e.getY(), corAtual);
                figuras.incluirNoFinal(novoPonto);
                novoPonto.desenhar(novoPonto.getCor(), pnlDesenho.getGraphics());
                esperaPonto = false;
            }
            else if (esperaInicioLinha) {
                // guarda em p1 o 1o ponto da Linha
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                // ainda não podemos guardar uma Linha no vetor, pois
                // não temos o 2o ponto extremo da mesma
                esperaInicioLinha = false;
                esperaFimLinha = true; // entra no modo de pedir o 2o ponto
                statusBar1.setText("Clique no ponto final da linha:");
            }
            else if (esperaFimLinha) {
                Ponto pontoFinal = new Ponto(e.getX(), e.getY(), corAtual);
                Linha novaLinha = new Linha(p1, pontoFinal, corAtual);
                figuras.incluirNoFinal(novaLinha);
                novaLinha.desenhar(corAtual, pnlDesenho.getGraphics());
                esperaFimLinha = false;  // linha terminada e incluída
            }
            else if (esperaInicioOval) {
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                esperaInicioOval = false;
                esperaRaioOvalA = true;
                statusBar1.setText("Defina a altura do oval:");
            }
            else if (esperaRaioOvalA) {
                p2.setX(0);
                p2.setY(abs(e.getY() - p1.getY()));
                p2.setCor(corAtual);
                esperaRaioOvalA = false;
                esperaRaioOvalB = true;
                statusBar1.setText("Defina o raio do oval:");
            }
            else if (esperaRaioOvalB) {
                Ponto raio = new Ponto(abs(e.getX() - p1.getX()), 0, corAtual);
                Oval novaOval = new Oval(p1, raio.getX(), p2.getY(), corAtual);
                figuras.incluirNoFinal(novaOval);
                novaOval.desenhar(corAtual, pnlDesenho.getGraphics());
                esperaRaioOvalB = false;
            }
            else if (esperaInicioCirculo){
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                esperaFimCirculo = true;
                esperaInicioCirculo = false;
                statusBar1.setText("Defina o raio do círculo:");
            }
            else if(esperaFimCirculo){
                Ponto novoP = new Ponto(abs(p1.getX() - e.getX()),0,corAtual);
                Circulo novoC = new Circulo(p1,novoP.getX(),corAtual);
                figuras.incluirNoFinal(novoC);
                novoC.desenhar(corAtual,pnlDesenho.getGraphics());
                esperaFimCirculo = false;
            }
            else if(esperaInicioRetangulo){
                p1.setX(e.getX());
                p1.setY(e.getY());
                p1.setCor(corAtual);
                statusBar1.setText("Defina a altura do retângulo:");
                esperaInicioRetangulo = false;
                esperaAlturaRetangulo = true;
            }
            else if (esperaAlturaRetangulo){
                p2.setX(0);
                p2.setY((e.getY() - p1.getY()));
                p2.setCor(corAtual);
                statusBar1.setText("Defina a largura do retângulo:");
                esperaAlturaRetangulo = false;
                esperaLarguraRetangulo = true;
            }
            else if (esperaLarguraRetangulo){
                Ponto novoP = new Ponto((e.getX() - p1.getX()),0,corAtual );
                Retangulo novoR = new Retangulo(p2.getY(),novoP.getX(),p1,corAtual);
                figuras.incluirNoFinal(novoR);
                novoR.desenhar(corAtual,pnlDesenho.getGraphics());
                esperaLarguraRetangulo = false;
            }
            else if (esperaInicioPolilinha) {
                esperaInicioPolilinha = false;
                desenhandoPolilinha = true;
                Ponto novoP = new Ponto(e.getX(), e.getY(), corAtual);
                novaPoli = new Polilinha(novoP,100,corAtual);
                statusBar1.setText("Defina o início da polilinha:");
            }
            else if (desenhandoPolilinha){
                novaPoli.incluirAoFinal(new Ponto(e.getX(), e.getY(), corAtual));
                novaPoli.desenharLinha(corAtual, pnlDesenho.getGraphics());
                statusBar1.setText("Defina o próximo ponto da polilinha:");
            }


        }
        public void mouseEntered (MouseEvent e) {
// não faz nada por enquanto
        }
        public void mouseExited (MouseEvent e) {
// não faz nada por enquanto
        }
        public void mouseReleased (MouseEvent e) {
// não faz nada por enquanto
        }

        public void paintComponent(Graphics g)
        {
            for (int atual = 0; atual < figuras.getTamanho(); atual++)
            {
                Ponto figuraAtual = figuras.valorDe(atual);
                figuraAtual.desenhar(figuraAtual.getCor(), g);
            }
        }
    }

}