import java.awt.*;

public class Retangulo extends Ponto{

    protected int largura;
    protected int altura;

    public Retangulo(int altura, int largura, Ponto ponto, Color cor){
        super(ponto.getX(), ponto.getY(), cor);
        setAltura(altura);
        setLargura(largura);
    }

    public Retangulo(int xCentro,int yCentro,int altura, int largura, Color novaCor){
        super(xCentro,yCentro,novaCor);
        setAltura(altura);
        setLargura(largura);
    }


    public void setAltura(int altura) {
        if(altura < 0){
            int y = super.getY();
            y += altura;
            super.setY(y);
            altura = -altura;
        }
        this.altura = altura;
    }

    public void setLargura(int largura) {
        if(largura < 0){
            int x = (super.getX());
            x+=largura;
            super.setX(x);
            largura = -largura;
        }
        this.largura = largura;
    }

    public int getLargura() {return largura;}

    public int getAltura() {return altura;}


    public void desenhar(Color c , Graphics g){

        g.setColor(c);
        g.drawRect(super.getX(),super.getY(),largura,altura);
    }

    public void desenhar(Color c , Graphics g, int x , int y){

        g.setColor(c);
        g.drawRect(x,y,largura,altura);
    }

    public void desenharComEspessura(Color cor, Graphics2D g2d){
        g2d.setColor(cor);
        g2d.drawRect(super.getX(),super.getY(),largura,altura);
    }

    public String formatoDeArquivo(){
        return "r;" +super.getX()+ ";" + super.getY()+
                ";" + cor.getRed() + ";"+cor.getGreen()+
                ";" + cor.getBlue() + ";"+altura + ";" + largura + ";";
    }
}
