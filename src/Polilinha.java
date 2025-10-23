import java.awt.*;

public class Polilinha extends Ponto{

    private Ponto[] vetPontos;
    private int qtosDados = 1;

    public Polilinha(Ponto pontoInicial, int tamanhoFisico, Color cor){
        //super(pontoInicial.getX(), pontoInicial.getY(),cor);
        this.vetPontos = new Ponto[tamanhoFisico];
        this.vetPontos[0] = pontoInicial;
    }
    public Polilinha(){
        super(0,0,Color.black);
        this.vetPontos = new Ponto[10];
    }
    public Polilinha(int xBase, int yBase, int tamanhoFisico, Color cor){
        //super(xBase,yBase,cor);
        this.vetPontos = new Ponto[tamanhoFisico];
        this.vetPontos[0] = new Ponto(xBase,yBase,cor);
    }
    public void incluirAoFinal(Ponto novoDado){
        if(qtosDados + 1 >= vetPontos.length){
            expandirVetor();
        }
        vetPontos[qtosDados++] = novoDado;
    }

    private void expandirVetor(){
        Ponto[] proc = new Ponto[2*vetPontos.length];
        for(int i = 0 ; i < qtosDados;i++){
            proc[i] = vetPontos[i];
        }
        vetPontos = proc;
    }

    public void desenhar(Color cor, Graphics g){
        g.setColor(cor);
        for(int i = 1;i < qtosDados;i+=1){
            Ponto pontoAnterior = valorDe(i-1);
            Ponto pontoAtual = valorDe(i);
            g.drawLine(pontoAnterior.getX(),pontoAnterior.getY(), pontoAtual.getX(), pontoAtual.getY());
        }
    }

    public Ponto valorDe(int posicaoDeAcesso){
        if(posicaoDeAcesso < 0 || posicaoDeAcesso > qtosDados){
            throw new IndexOutOfBoundsException("Posição inválida.");
        }
        return vetPontos[posicaoDeAcesso];
    }

    public void desenhar(Color cor, Graphics g, int x, int y){
        vetPontos[0].setX(x);
        vetPontos[0].setY(y);
        g.setColor(cor);
        for(int i = 1; i < qtosDados; i+=1){
            Ponto inicio = valorDe(i-1);
            Ponto fim = valorDe(i);
            g.drawLine(inicio.getX(),inicio.getY(), fim.getX(), fim.getY());
        }
    }

    public void desenharLinha(Color cor, Graphics g){
        Ponto pontoAtual = vetPontos[qtosDados-1];
        Ponto pontoAnterior = vetPontos[qtosDados-2];
        g.setColor(cor);
        g.drawLine(pontoAnterior.getX(),pontoAnterior.getY(), pontoAtual.getX(), pontoAtual.getY());
    }

    public void desenharComEspessura(Color cor, Graphics2D g2d){
        g2d.setColor(cor);
        for(int i = 1;i < qtosDados;i+=1){
            Ponto pontoAnterior = valorDe(i-1);
            Ponto pontoAtual = valorDe(i);
            g2d.drawLine(pontoAnterior.getX(),pontoAnterior.getY(), pontoAtual.getX(), pontoAtual.getY());
        }
    }

    @Override
    public String formatoDeArquivo() {
        String stringPoli = "pl;" + super.getX() + ";" + super.getY() + ";"+
                super.getCor().getRed() + ";"+super.getCor().getGreen() + ";" + super.getCor().getBlue() + ";"+
                qtosDados + ";";
        for(int i = 0; i < qtosDados; i++) {
            stringPoli += valorDe(i).formatoDeArquivo();
            stringPoli += ";";
        }
        return stringPoli;
    }

    public void deslocar(int dx, int dy){
        for (int i = 0; i < qtosDados; i++){
            vetPontos[i].deslocar(dx, dy);
        }
    }
}