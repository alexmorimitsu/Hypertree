package image;

public interface IFloatImage extends IImage{
    
    public void initImage(float color);
    
    public IFloatImage duplicate();
    
    public float getPixel(int x, int y);
    
    public void setPixel(int x, int y, float value);
    
    public float[] getPixels();
    
    public void setPixels(int width, int height, float pixels[]);
    
    public float getPixelMax();
    
    public float getPixelMin();

    public void setPixel(int i, float level);
    
    public float getPixel(int i);
  
}
