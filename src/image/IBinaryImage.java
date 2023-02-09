package image;



public interface IBinaryImage extends IImage{
	
    public void initImage(int color);
    
    public IBinaryImage duplicate();
    
    public int getPixel(int x, int y);
   
    public void setPixel(int x, int y, int value);
    
    public byte[] getPixels();
    
    public void setPixels(int width, int height, byte pixels[]);
    
    public int getBackground();
    
    public void setBackground(int background);
    
    public int getForeground();
    
    public IBinaryImage invert();
    
    public IGrayScaleImage convertGrayScale();
    
    public int getArea();
    
    public IBinaryImage rot90();

    public IBinaryImage rot180();

    public IBinaryImage rot270();
    
    public int[] getHistogramXprojection();
    
    public int[] getHistogramYprojection();
    
    public void setPixel(int i, int level); 
    
    public int getPixel(int i);
    
    public int getSize();
    
    public boolean isPixelBackground(int i);
    
    public boolean isPixelForeground(int i);
    
    public boolean isPixelBackground(int x, int y);
    
    public boolean isPixelForeground(int x, int y);
    
    public void setId(Integer id);
    
    public Integer getId();
}
    