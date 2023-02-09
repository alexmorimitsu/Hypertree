package image;




public interface IRGBImage extends IImage{
    
    public void initImage(int rgb);
    
    public IRGBImage duplicate();
    
    public int getPixel(int x, int y);
    
    public int getPixel(int i);
    
    public int[] getPixels();
    
    public int getSize();
    
    public void setPixel(int x, int y, int value[]);
    
    public void setPixel(int x, int y, int rgb);
    
    public void setPixel(int i, int value[]);

    public void setPixel(int i, int rgb);
    
    public void setPixels(int width, int height, int pixels[]);
        
    public IGrayScaleImage convertGrayScaleImage();

    public void paintBoundBox(int x1, int y1, int x2, int y2, int rgb);
    
    public void addSubImage(IRGBImage img, int x, int y);
    
    public void addSubImage(IGrayScaleImage img, int x, int y);
    
    public void setAlpha(int value);
    
    public int getRed(int i);
    public int getGreen(int i);
    public int getBlue(int i);
    
    public int getRed(int x, int y);
    public int getGreen(int x, int y);
    public int getBlue(int x, int y);
    
    public void setRed(int x, int y, int value);
    public void setGreen(int x, int y, int value);
    public void setBlue(int x, int y, int value);
    
    public void setRed(int i, int value);
    public void setGreen(int i, int value);
    public void setBlue(int i, int value);
    
}
    