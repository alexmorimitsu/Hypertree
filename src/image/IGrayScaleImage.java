package image;

import java.util.LinkedList;





public interface IGrayScaleImage extends IImage{
    
    public void initImage(int color);
    
    public IGrayScaleImage duplicate();
    
    public int getPixel(int x, int y);
    
    public int getPixel(int i);
    
    public void setPixel(int x, int y, int value);
    
    public void setPixel(int i, int level);
    
    public void setPixels(int width, int height, int pixels[]);
   
    public int[] getPixels();
    
    public int countPixels(int color);
    
    public int getSize();
    
    public int numValues();
    
    public IBinaryImage convertBinaryImage(int limiar, int background);
    
    public int getValueMax();
    
    public int getValueMin();
    
    public int getValueMean();
    
    public int getPixelMax();
    
    public int getPixelMin();
    
    public IGrayScaleImage invert();

    public void add(int a);
    
    public void multiply(int a);
    
    public int[] getHistogram();
    
    public LinkedList<Integer>[] getPixelsOfHistogram();
    
    public void resize(int x, int y);
    
    public void resizeCenter(int x, int y);
    
    public void paintBoundBox(int x1, int y1, int x2, int y2, int color);
    
    public void replaceValue(int colorOld, int colorNew);
    
    public IRGBImage randomColor();
    
    public IRGBImage randomColor(int alpha);
}
    