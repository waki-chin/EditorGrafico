import java.awt.*;

public class Linha extends Ponto
{
    // o ponto inicial da Linha está declarado implicitamente
    // pelo mecanismo de herança, ou seja, Linha herda de Ponto
    // tudo que foi codificado na classe Ponto
    protected Ponto pontoFinal;

    public Linha(Ponto inicio, Ponto fim, Color cor)
    {
        // super() é o construtor da classe ancestral de Linha (que é a classe Ponto)
        // abaixo estamos chamando o construtor de Ponto para que o ponto inicial da
        // Linha tenha seus valores atribuidos
        super(inicio.getX(), inicio.getY(), cor);
        pontoFinal = fim;
    }

    public Linha(int x1, int y1, int x2, int y2, Color cor)
    {
        super(x1, y1, cor);                     // instancia o 1o ponto
        pontoFinal = new Ponto(x2, y2, cor);    // precisa instanciar o 2o ponto
    }

    public void desenhar(Color corDesenho, Graphics g)
    {
        g.setColor(corDesenho);
        g.drawLine(super.x, super.y, pontoFinal.x, pontoFinal.y);
    }

    public void desenharComEspessura(Color cor, Graphics2D g2d){
        g2d.setColor(cor);
        g2d.drawLine(super.x, super.y, pontoFinal.x, pontoFinal.y);
    }


    public String formatoDeArquivo() {
        //   l		x1	y1	corR	corG	corB 	x2	y2
        return "l;" + x + ";" + y + ";" +
                cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()+
                ";"+pontoFinal.x+";"+pontoFinal.y;
    }

    public void setPontoFinal(Ponto pFinal){
        pontoFinal = pFinal;
    }

    public void deslocar(int dx, int dy){
        x += dx;
        y += dy;
        pontoFinal.deslocar(dx, dy);
    }

}
