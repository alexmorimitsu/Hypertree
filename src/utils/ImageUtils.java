package utils;

import image.BinaryImage;
import image.GrayScaleImage;
import image.IBinaryImage;
import image.IGrayScaleImage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



/**
 * Project: Computer Vision Framework
 * 
 * @author Wonder Alexandre Luz Alves
 * @advisor Ronaldo Fumio Hashimoto
 * 
 * @date 13/09/2007
 * 
 * @description
 * Esta classe define alguns metodos utilitarios para operacoes com imagens
 */
public class ImageUtils {

    
    
    public static double log2(double d) {
        return Math.log(d)/Math.log(2.0);
     }

    public static double log10(double d) {
        return Math.log(d)/Math.log(10.0);
     }
    
    public static String numberToString(double x){
        String s = new BigDecimal(x).toPlainString();
        return s;
    }

    
    /**
     * Reescala a imagem de entrada para valores no intervalode 0 a 255
     * @param img - imagem de entrada
     * @return IGrayScaleImage
     */
    public static IGrayScaleImage normalizedPixels(IGrayScaleImage img){
        IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
        int tmp = 0;
        for(int x = 0 ; x < img.getWidth() ; x++){
            for(int y = 0 ; y < img.getHeight(); y++){
                tmp = (int) (( 255.0 / (img.getValueMax() - img.getValueMin())) * (img.getPixel(x, y) - img.getValueMin())); 
                imgOut.setPixel(x, y, tmp);
            }
        }
        return imgOut;
    }

    
    
    /**
     * Normaliza os pixel diferentes de zero para proximos de 255
     * @param img
     * @return
     */
    public static IGrayScaleImage normalizedPixelsMax(IGrayScaleImage img){
        IGrayScaleImage imgOut = new GrayScaleImage(img.getWidth(), img.getHeight());
        int max = img.getValueMax();
        for (int w = 0; w < img.getWidth(); w++)
            for (int h = 0; h < img.getHeight(); h++)
                if(img.getPixel(w,h) > 0)
                    imgOut.setPixel(w, h, 255 / max * img.getPixel(w,h));
        
        return imgOut;
    }
    

    
    
    /**
     * Determina a distancia entre dois pixels
     * @param w1 - coordenada da largura do primeiro pixel
     * @param h1 - coordenada da altura do primeiro pixel
     * @param w2 - coordenada da largura do segundo pixel
     * @param h2 - coordenada da altura do segundo pixel
     * @param type - tipo da distancia. 
     *               Sendo que type = 1 para euclidiana; 
     *                         type = 2 para city block; 
     *                         type = 3 para clessbord
     * @return double
     */
    public static double distance(double w1, double h1, double w2, double h2, int type){
        if(type == 1){
            return Math.sqrt(Math.pow(w1 - w2, 2) + Math.pow(h1 - h2, 2));
        }else if(type == 2){
            return Math.abs(w1 - w2) + Math.abs(h1 - h2);
        }else{
            return Math.max(Math.abs(w1 - w2), Math.abs(h1 - h2));
        }
        
    }
    
  
    
    
    public static int randomInteger (int low, int high){
    	int k;
    	k = (int) (Math.random() * (high - low + 1));
    	return low + k;
    }
    
    

    /**
     * Conjunto de pixels vizinhos de 8
     * @param x
     * @param y
     * @param width
     * @param height
     * @return
     */
    public static List<Integer> getNeighbors(int i, int width, int height) {
    	int x = i % width;
    	int y =i / width;
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        
        if (x < width-1) {
            neighbors.add( (y) * width + (x+1) );
        }
        if (x > 0) {
            neighbors.add( (y) * width + (x-1) );
        }
        if (y > 0) {
            neighbors.add( (y-1) * width + (x) );
        }
        if (y < height-1) {
            neighbors.add( (y+1) * width + (x) );
        }

        if (x > 0 && y > 0) {
            neighbors.add( (y-1) * width + (x-1) );
        }

        if (x < width - 1 && y > 0) {
            neighbors.add( (y-1) * width + (x+1) );
        }

        if (x > 0 && y < height - 1) {
            neighbors.add( (y+1) * width + (x-1) );
        }

        if (x < width - 1 && y < height - 1) {
            neighbors.add( (y+1) * width + (x+1) );
        }
        return neighbors;
    }
    

    /**
     * Ajustar o tamanho da imagem (boundingbox)
     * @param img
     * @return
     */
    public static IBinaryImage adjustImage(IBinaryImage img){
        IBinaryImage imgOut = null;

        int beginHeight = img.getHeight();
        int endHeight = 0;
        int beginWidth = img.getWidth();
        int endWidth = 0;
        
        for(int w=0; w < img.getWidth(); w++){ //procurando os pontos que estao a imagem
            for(int h=0; h < img.getHeight(); h++){
                if(img.getPixel(w, h) == img.getForeground() && beginHeight > h){
                    beginHeight = h;
                }
                if(img.getPixel(w, h) == img.getForeground() && beginWidth > w){
                    beginWidth = w;
                }
                if(img.getPixel(w, h) == img.getForeground() && endWidth < w){
                    endWidth = w;
                }
                if(img.getPixel(w, h) == img.getForeground() && endHeight < h){
                    endHeight = h;
                }                
            }
        }
        if(endWidth - beginWidth + 1 < 0 || endHeight - beginHeight+1 < 0) return img;
        imgOut = new BinaryImage(endWidth - beginWidth + 1, endHeight - beginHeight+1); //o tamanho da imagem sao os limites + 10 //10 e uma folga
        imgOut.setBackground(img.getBackground());
        for(int w= beginWidth, i = 0; w < endWidth+1; w++, i++){ //w para imagem original e i para nova imagem 
            for(int h=beginHeight, j =0; h < endHeight+1; h++, j++){ //h para imagem original e j para nova imagem
               if(imgOut.isPixelValid(i, j))
                   imgOut.setPixel(i, j, img.getPixel(w, h));
               else
                   imgOut.setPixel(i, j, img.getBackground());
            }
        }
        
        return imgOut;
    }
    
    
  
    
}
