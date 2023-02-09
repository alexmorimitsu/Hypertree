package image;

import structs.Pixel;


/**
 * Project: score-image
 * 
 * @author Wonder Alexandre Luz Alves
 *   
 * @description
 * Classe que representa uma imagem binaria
 * A profundidade dos pixels eh 8 bits.   
 * 
 */ 
public class BinaryImage implements IBinaryImage{
    private int width; //largura da imagem
    private int height; //altura da imagem
    private byte pixels[]; //matriz de pixel da imagem
    private int background; //valor do pixel que representara o background da imagem
    private int foreground; //valor do pixel que representara o foreground da imagem
    private Pixel origin; //origem da imagem
    private Integer id;
    public static final int COLOR_BINARY_IMAGE_BLACK = 0;
    public static final int COLOR_BINARY_IMAGE_WHITE = 1;
    public static final int DONT_CARE = -1;
    
    
    
    /**
     * 
     * @param matrix
     * @param back - valor do background
     
    public BinaryImage(byte matrix[], int back){
    	this.width = matrix.length;
    	this.height = matrix[0].length;
    	this.pixels = matrix;
    	this.origin = new Pixel(0,0);
        setBackground(back);
    }*/

    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }
    
    /**
     * Construtor para criar uma nova imagem
     * @param width - largura
     * @param height - altura 
     */
    public BinaryImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.foreground = COLOR_BINARY_IMAGE_WHITE;
        this.background = COLOR_BINARY_IMAGE_BLACK;
        pixels = new byte[width * height];
        origin = new Pixel(0,0);
    }

    
    /**
     * Inicializar todos os pixel da imagem para um dado nivel de cinza
     * @param color
     */
    public void initImage(int color){
        if(!(color == COLOR_BINARY_IMAGE_BLACK || color == COLOR_BINARY_IMAGE_WHITE || color == DONT_CARE)) throw new RuntimeException("Color ("+color+") invalid!");
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                setPixel(i,j, color);
            }
        }
    }
    
    /**
     * Cria uma copia da imagem original
     * @return BinaryImage - nova imagem
     */
    public IBinaryImage duplicate(){
        BinaryImage clone = new BinaryImage(getWidth(), getHeight());
        clone.background = background;
        clone.foreground = foreground;
        clone.origin = origin;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                clone.setPixel(i,j,getPixel(i, j));
            }
        }
        return clone;
    }
    
    /**
     * Pega o valor do pixel (x, y)
     * @param x - largura
     * @param y - altura
     * @return float - valor do pixel
     */
    public int getPixel(int x, int y){
    	return pixels[y * width + x];
    }
    
    /**
     * Calculando uma estimacao da orientacao do componente conexo (em radiano)
     * Esse procedimento e baseado em momentos estatiscos
     * @param img - imagem de entrada
     * @return orientacao
     */
    public static double getOrientationEstimationInRadian(IBinaryImage img){
        double xx = 0, yy = 0;
        double u00 = 0, u20 = 0, u02 = 0, u11 = 0;
        double thetaR = 0;
         
        //calculando o momento central
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                if(img.isPixelBackground(x, y))
            	xx += x;
                yy += y; 
            }
        }
        double m00 = img.getArea();
        xx = xx / m00;
        yy = yy / m00;
        
        //Calculado os momentos de segunda ordem 
        u00 = getMoment(img, xx, yy, 0, 0);
        u20 = getMoment(img, xx, yy, 2, 0);
        u02 = getMoment(img, xx, yy, 0, 2);
        u11 = getMoment(img, xx, yy, 1, 1);
       
        //calculando a orientacao
        thetaR = 0.5 * Math.atan2( 2.0 * u11, u20 - u02 );
        return thetaR;
    }
   
    public static double getMoment(IBinaryImage img, double xCentroide, double yCentroide, int p, int q){
        double result = 0;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
            	if(img.isPixelBackground(x, y))
            		result += Math.pow(x - xCentroide, p) * Math.pow(y - yCentroide, q) ;
            }
        }
        return result;
    }
   
    
    /**
     * Modifica o valor do pixel (x, y) = value
     * @param x - largura
     * @param y - altura
     * @param value - valor do pixel
     */
    public void setPixel(int x, int y, int value){
        if(!(value == COLOR_BINARY_IMAGE_BLACK || value == COLOR_BINARY_IMAGE_WHITE || value == DONT_CARE)) throw new RuntimeException("Color ("+value+") invalid!");
        pixels[y * width + x] = (byte) value;
    }
    
    /**
     * Pega uma matriz bidimensional de pixel da imagem
     * @return int[][]
     */
    public byte[] getPixels(){
        return pixels;
    }
    
    /**
     * Modifica a matriz de pixel da imagem para os valores da matriz dada
     * @param matrix 
     */
    public void setPixels(int width, int height, byte pixels[]){
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }
    
    
    /**
     * Retorna a altura da imagem
     * @return int - altura
     */
    public int getHeight(){
        return height;
    }
    
    /**
     * Retorna o tamanho da imagem
     * @return
     */
    public int getSize(){
        return getHeight() * getWidth();
    }
    

    /**
     * Retorna a largura da imagem
     * @return int - largura
     */
    public int getWidth(){
        return width;
    }
    
    
    /**
     * pega o valor do background das imagens binarias
     * @return
     */
    public int getBackground() {
        return background;
    }

    /**
     * Modifica a cor do background da imagem
     * @param background
     */
    public void setBackground(int background){
        if(background == COLOR_BINARY_IMAGE_BLACK){
            this.background = COLOR_BINARY_IMAGE_BLACK;
            this.foreground = COLOR_BINARY_IMAGE_WHITE;
        }else{
            this.background = COLOR_BINARY_IMAGE_WHITE;
            this.foreground = COLOR_BINARY_IMAGE_BLACK;
        }
    }

    /**
     * Pega o valor do foreground das imagens binarias
     * @return
     */
    public int getForeground() {
        return foreground;
    }


    /**
     * Pega da origem da imagem
     * @return Point 
     */
    public Pixel getOrigin(){
        return origin;
    }
    
    
    /**
     * Modifica a origem da imagem
     * @param p
     */
    public void setOrigin(Pixel p){
        origin = p;
    }
    
    /**
     * Converte uma imagem binaria em niveis de cinza
     * @return IGrayScaleImage
     */
    public IGrayScaleImage convertGrayScale(){
        IGrayScaleImage clone = new GrayScaleImage(getWidth(), getHeight());
        clone.setOrigin(this.getOrigin());
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                clone.setPixel(i,j,getPixel(i, j));
            }
        }
        return clone;
    }
    
    /**
     * Pega um histograma da imagem
     * @return int[]
     */
    public int[] getHistogram() {
       int result[] = new int[2];
       for(int w=0; w < getWidth(); w++){
           for(int h=0; h < getHeight(); h++){
               result[getPixel(w, h)]++;
           }
       }
       return result;
    }
    
    public int getArea(){
        int area =0;
        for(int w=0; w < getWidth(); w++){
            for(int h=0; h < getHeight(); h++){
                if(getPixel(w, h) == getForeground()){
                    area++;
                }
            }
        }
        return area;
    }

    
    
    public void paintBoundBox(int x1, int y1, int x2, int y2, int color){
        int w = x2 - x1;
        int h = y2 - y1;
        for(int i=0; i < w; i++){
            for(int j=0; j < h; j++){
                if(i == 0 || j == 0 || i == w-1 || j == h-1)
                    setPixel(x1 + i, y1 + j, color);        
            }
        }
    }
    
    /**
     * Verifica se duas imagens sao iguais
     * @param img - IGrayScaleImage
     * @return true se forem iguais false caso contrario
     */
    public boolean equals(Object o){
        IBinaryImage img = (IBinaryImage) o;
        for(int x = 0 ; x < getWidth() ; x++)
            for(int y = 0 ; y < getHeight(); y++)
                if(getPixel(x, y) != img.getPixel(x, y)) 
                    return false;
                
        return true;
    }

    /**
     * Inverte as tonalidades de cores da imagem
     */
    public IBinaryImage invert() {
        BinaryImage clone = new BinaryImage(getWidth(), getHeight());
        clone.background = foreground;
        clone.foreground = background;
        clone.origin = origin;
        int value;
        for (int i = 0; i < getWidth(); i++){
            for (int j = 0; j < getHeight(); j++){
                if(getPixel(i, j) == foreground)
                    value = background;
                else if (getPixel(i, j) == background)
                    value = foreground;
                else
                    value = getPixel(i, j);
                clone.setPixel(i,j,value);
            }
        }
        return clone;
    }
    
    /**
     *  Rotaciona 90 graus sentido anti-horario.
     */
    public IBinaryImage rot90(){
        BinaryImage d = new BinaryImage(this.getHeight(), this.getWidth());
        d.background = foreground;
        d.foreground = background;
        d.origin = origin;
        for (int l=0; l < this.getWidth(); ++l)
            for (int c=0; c < this.getHeight(); ++c){
                if(d.isPixelValid(this.getHeight() - c - 1 , l))
                    d.setPixel(this.getHeight() - c - 1 , l,  getPixel(l,c));
            }
        return d;
    }

    /**
     *  Rotaciona 180 graus sentido anti-horario.
     */
    public IBinaryImage rot180(){
        BinaryImage d = new BinaryImage(this.getWidth(), this.getHeight());
        d.background = foreground;
        d.foreground = background;
        d.origin = origin;
        for (int l=0; l < this.getWidth(); ++l)
            for (int c=0; c < this.getHeight(); ++c)
                if(d.isPixelValid(this.getWidth() - l - 1, getHeight() - c - 1))
                    d.setPixel(this.getWidth() - l - 1, getHeight() - c - 1,  getPixel(l,c));
      
        return d;
    }

    /**
     * Rotaciona 270 graus sentido anti-horario.
     */
    public IBinaryImage rot270(){
         BinaryImage d = new BinaryImage(this.getHeight(), this.getWidth());
         d.background = foreground;
         d.foreground = background;
         d.origin = origin;
         for (int l=0; l < this.getWidth(); ++l)
             for (int c=0; c < this.getHeight(); ++c)
              //   if(ImageUtils.isIndexInImage(d,this.getWidth() - l - 1, getHeight() - c - 1))
                     d.setPixel(c, getWidth() - l - 1, getPixel(l,c));
         return d;
    }
    
    /**
     * Pega o histograma da projecao dos pixels em X
     */
    public int[] getHistogramXprojection() {
        int hist[] = new int[this.getWidth()];
        for(int i=0; i < this.getWidth(); i++){
            for(int j=0; j < this.getHeight(); j++){
                if(this.getPixel(i, j) == this.getForeground()){
                    hist[i]++;
                }
            }
        }
        return hist;
    } 

    /**
     * Pega o histograma da projecao dos pixels em Y
     */
    public int[] getHistogramYprojection() {
        int hist[] = new int[this.getHeight()];
        for(int i=0; i < this.getWidth(); i++){
            for(int j=0; j < this.getHeight(); j++){
                if(this.getPixel(i, j) == this.getForeground()){
                    hist[j]++;
                }
            }
        }
        return hist;  
    }
    

    public void setPixel(int i, int level){
    	pixels[i] = (byte)level;
    }
    
    public int getPixel(int i){
    	return pixels[i];
    }
    
    public boolean isPixelBackground(int i){
        return getPixel(i) == getBackground();
    }
    
    public boolean isPixelForeground(int i){
        return getPixel(i) == getForeground();
    }
    
    public boolean isPixelBackground(int x, int y){
        return getPixel(x, y) == getBackground();
    }
    
    public boolean isPixelForeground(int x, int y){
        return getPixel(x, y) == getForeground();
    }
    
    public boolean isPixelValid(int x, int y){
        return (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight());
    }

    public boolean isPixelValid(int p){
    	return isPixelValid(p % this.getWidth(), p / this.getWidth());
    }
    
   
    
}
