package image;

import structs.Pixel;

public interface IImage {
    
    public Pixel getOrigin();
    
    public void setOrigin(Pixel p);
    
    public int getHeight();
    
    public int getWidth();
    
    public int getSize();
    
    public boolean isPixelValid(int x, int y);

    public boolean isPixelValid(int p);
}
