package image;

import structs.Pixel;


/**
 * Project: Computer Vision Framework
 * 
 * @author Wonder Alexandre Luz Alves
 * @advisor Ronaldo Fumio Hashimoto
 * 
 * @date 10/09/2007
 *  
 * @description
 * Classe que representa uma imagem digital. 
 * Essa classe utiliza somente as APIs do java para manipular os pixels da imagens  
 * 
 */ 
public class RGBImage implements IRGBImage{
    private int width; //largura da imagem
    private int height; //altura da imagem 
    private int pixels[]; //matriz de pixel da imagem
    private Pixel origin; //origem da imagem
    int alpha = 255;
    
    /**
     * Construtor para criar uma nova imagem
     * @param width - largura
     * @param height - altura
     */
    public RGBImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        this.origin = new Pixel(0,0);
    }

    /**
     * Construtor para criar uma nova imagem com os dados da imagem de entrada
     * @param img - imagem de entrada
     */
    public RGBImage(IGrayScaleImage img) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.pixels = new int[width * height];
        this.origin = new Pixel(0,0);
        for (int i = 0; i < getSize(); i++){
            	pixels[i] = ((alpha & 0xFF) << 24) |
            				((img.getPixel(i) & 0xFF) << 16) |
            				((img.getPixel(i) & 0xFF) << 8)  |
            				((img.getPixel(i) & 0xFF) << 0);
        }
    }
    

    /**
     * Construtor para criar uma nova imagem com os dados da imagem de entrada
     * @param img - imagem de entrada
     */
    public RGBImage(IBinaryImage img) {
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.pixels = new int[width * height];
        this.origin = new Pixel(0,0);
        for (int i = 0; i < getSize(); i++){
        	pixels[i] = ((alpha & 0xFF) << 24) |
        				((img.getPixel(i) & 0xFF) << 16) |
        				((img.getPixel(i) & 0xFF) << 8)  |
        				((img.getPixel(i) & 0xFF) << 0);
        } 
        
    }

    /**
     * Inverter os pixels da imagem [255 - pixel(x,y)]
     * @return
     */
    public IRGBImage invert(){
        IRGBImage imgOut = new RGBImage(this.getWidth(), this.getHeight());
        for(int w=0;w<this.getWidth();w++){
            for(int h=0;h<this.getHeight();h++){
            	for(int b=0; b < 3; b++)
            		imgOut.setRed(w, h, 255 - this.getRed(w, h));
            		imgOut.setGreen(w, h, 255 - this.getGreen(w, h));
            		imgOut.setBlue(w, h, 255 - this.getBlue(w, h));
            }
        } 
        return imgOut;
    }
    
    public void paintBoundBox(int x1, int y1, int x2, int y2, int c){
        int w = x2 - x1;
        int h = y2 - y1;
        for(int i=0; i < w; i++){
            for(int j=0; j < h; j++){
                if(i <= 1 || j <= 1 || i > w-3 || j > h-3)
                    setPixel(x1 + i, y1 + j, c);        
            }
        }
    }
    
    
    /**
     * Inicializar todos os pixel da imagem para um dado nivel de cinza
     * @param color
     */
    public void initImage(int color){
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
    public IRGBImage duplicate(){
        RGBImage clone = new RGBImage(getWidth(), getHeight());
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
    
    
    public void setAlpha(int value){
    	this.alpha = value;
    }
    
   
    
    
    /**
     * Modifica a matriz de pixel da imagem para os valores da matriz dada
     * @param matrix 
     */
    public void setPixels(int width, int height, int pixels[]){
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
     * Retorna a largura da imagem
     * @return int - largura
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Retorna o tamanho da imagem
     * @return
     */
    public int getSize(){
        return getHeight() * getWidth();
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


    public void setPixel(int x, int y, int[] value) {
    	setPixel(y * width + x, value);
    }
    
    public int[] getPixels() {
        return pixels;
    }
    
    public void setPixel(int x, int y, int rgb) {
        pixels[y * width + x] = rgb;
    }

    public void setPixel(int i, int[] value) {
    	pixels[i] = ((alpha & 0xFF) << 24) |
				((value[0] & 0xFF) << 16) |
				((value[1] & 0xFF) << 8)  |
				((value[2] & 0xFF) << 0);
	}
 
    
    public void setPixels(int[] matrix) {
        pixels = matrix;
        
    }


    public void setPixel(int i, int rgb){
    	pixels[i] = rgb;
    }
    
    public int getPixel(int i){
        return pixels[i];
    }
    
    /**
     * Converte uma imagem em RGB para niveis de cinza
     */
    public IGrayScaleImage convertGrayScaleImage() {
        IGrayScaleImage image = new GrayScaleImage(this.getWidth(), this.getHeight());
        int r, g, b;
        for(int w=0;w<this.getWidth();w++){
            for(int h=0;h<this.getHeight();h++){
                r = this.getRed(w, h);
                g = this.getGreen(w, h);
                b = this.getBlue(w, h);
                image.setPixel(w, h, (int) Math.round(.299*r + .587*g + .114*b)); //convertendo para niveis de cinza
            }
        } 
        return image;
    }

   
    public void addSubImage(IRGBImage img, int x, int y){
        for(int i=x; i < img.getWidth(); i++){
            for(int j=y; j < img.getHeight(); j++){
                if(this.isPixelValid(i, j))
                    setPixel(i, j, img.getPixel(i, j));
            }
        }
    }
    
    public void addSubImage(IGrayScaleImage img, int x, int y){
        for(int i=x; i < img.getWidth(); i++){
            for(int j=y; j < img.getHeight(); j++){
                if(this.isPixelValid(i, j))
                    setPixel(i, j, img.getPixel(i, j));
            }
        }
    }

    public boolean isPixelValid(int x, int y){
        return (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight());
    }

    public boolean isPixelValid(int p){
    	return isPixelValid(p % this.getWidth(), p / this.getWidth());
    }

	public int getRed(int i) {
		return (pixels[i] >> 16) & 0xFF; //red
	}

	public int getGreen(int i) {
		return (pixels[i] >> 8) & 0xFF; //green
	}

	public int getBlue(int i) {
		return (pixels[i] >> 0) & 0xFF; //blue
	}

	public int getRed(int x, int y) {
		return getRed(y * width + x);
	}

	
	public int getGreen(int x, int y) {
		return getGreen(y * width + x);
	}

	
	public int getBlue(int x, int y) {
		return getBlue(y * width + x);
	}

	public void setRed(int x, int y, int value) {
		setRed(y * width + x, value);
	}

	public void setGreen(int x, int y, int value) {
		setGreen(y * width + x, value);
	}

	public void setBlue(int x, int y, int value) {
		setBlue(y * width + x, value);		
	}

	public void setRed(int i, int value) {
		pixels[i] = ((alpha & 0xFF) << 24) |
					((value & 0xFF) << 16) |
					((getGreen(i) & 0xFF) << 8)  |
					((getBlue(i) & 0xFF) << 0);
	}

	public void setGreen(int i, int value) {
		pixels[i] = ((alpha & 0xFF) << 24) |
					((getRed(i) & 0xFF) << 16) |
					((value & 0xFF) << 8)  |
					((getBlue(i) & 0xFF) << 0);
	}

	public void setBlue(int i, int value) {
		pixels[i] = ((alpha & 0xFF) << 24) |
					((getRed(i) & 0xFF) << 16) |
					((getGreen(i) & 0xFF) << 8)  |
					((value & 0xFF) << 0);
		
	}

	
    
}
