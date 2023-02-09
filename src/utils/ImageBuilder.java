package utils;

import image.BinaryImage;
import image.GrayScaleImage;
import image.IBinaryImage;
import image.IGrayScaleImage;
import image.IImage;
import image.IRGBImage;
import image.RGBImage;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

public class ImageBuilder {


	public static BufferedImage convertToImage(IImage image) {
		BufferedImage bi = new BufferedImage(image.getWidth(), image.getHeight(),	BufferedImage.TYPE_INT_RGB);
		if (image instanceof IGrayScaleImage){
			IGrayScaleImage img = (IGrayScaleImage) image;
			for (int w = 0; w < img.getWidth(); w++) {
				for (int h = 0; h < img.getHeight(); h++) {
					bi.setRGB(w,h, new Color(img.getPixel(w, h), img.getPixel(w, h), img.getPixel(w, h)).getRGB());
				}
			}
		}
		else if (image instanceof IBinaryImage){
			IBinaryImage img = (IBinaryImage) image;
			
			for (int w = 0; w < img.getWidth(); w++) {
				for (int h = 0; h < img.getHeight(); h++) {
					if( img.isPixelForeground(w, h) )
						bi.setRGB(w, h, Color.WHITE.getRGB());
					else
						bi.setRGB(w, h, Color.BLACK.getRGB());
				}
			}
		}
		else if (image instanceof IRGBImage){
			IRGBImage img = (IRGBImage) image;
			for (int w = 0; w < img.getWidth(); w++) {
				for (int h = 0; h < img.getHeight(); h++) {
					bi.setRGB(w, h, img.getPixel(w, h));
				}
			}
		}
		bi.getSource();
		return bi;
	}
	

	/**
	 * salvando uma imagem
	 * 
	 * @param image
	 * @throws IOException
	 */
	public static void saveImage(BufferedImage image, File file) {
		try {
			ImageIO.write(image, file.getName().substring(file.getName().length() - 3), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * salvando uma imagem
	 * 
	 * @param image
	 * @throws IOException
	 */
	public static void saveImage(IImage image, File file) {
		if (image instanceof IGrayScaleImage)
			saveImage(ImageBuilder.convertToImage((IGrayScaleImage) image),	file);
		else if (image instanceof IBinaryImage)
			saveImage(ImageBuilder.convertToImage(((IBinaryImage) image)), file);
		else if (image instanceof IRGBImage)
			saveImage(ImageBuilder.convertToImage(((IRGBImage) image)), file);
	}

	public static File windowOpenFile() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.showOpenDialog(null);
		return fc.getSelectedFile();
	}
	
	public static File[] windowOpenDir() {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.showOpenDialog(null);
		return fc.getSelectedFile().listFiles();
	}

	/**
	 * Construtor que recebe uma imagem e extrai a sua matriz de pixels
	 * 
	 * @param img
	 *            - Image
	 */
	public static IBinaryImage convertToBinaryImage(BufferedImage image, int back) {
		int width = image.getWidth();
		int height = image.getHeight();
		IBinaryImage img = new BinaryImage(width, height);
		img.setBackground(back);
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY || image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			for (int w = 0; w < width; w++) {
				for (int h = 0; h < height; h++) {
					img.setPixel(w, h, image.getRaster().getSample(w, h, 0));
				}
			}
		} else {
			int rgb, r, g, b;
			for (int w = 0; w < width; w++) {
				for (int h = 0; h < height; h++) {
					rgb = image.getRGB(w, h);

					// int alpha = (rgb >> 24) & 0xff;
					r = (int) ((rgb & 0x00FF0000) >>> 16); // Red level
					g = (int) ((rgb & 0x0000FF00) >>> 8); // Green level
					b = (int) (rgb & 0x000000FF); // Blue leve
					img.setPixel(w, h, (r + g + b) / 3);
					
				}
			}
		}
		return img;
		
	}

	public static IGrayScaleImage convertToGrayImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		IGrayScaleImage img = new GrayScaleImage(width, height);
		if (image.getType() == BufferedImage.TYPE_BYTE_GRAY || image.getType() == BufferedImage.TYPE_BYTE_BINARY) {
			for (int w = 0; w < width; w++) {
				for (int h = 0; h < height; h++) {
					img.setPixel(w, h,  image.getRaster().getSample(w, h, 0));
					
					//System.out.println(((image.getRaster().getSample(x, y, b).getRGB(x, y)(w, h) >> 16) & 0xff) == img.getPixel(w, h));
					
				}
			}
		} else {
			int rgb, r, g, b;
			for (int w = 0; w < width; w++) {
				for (int h = 0; h < height; h++) {
					rgb = image.getRGB(w, h);

					// int alpha = (rgb >> 24) & 0xff;
					r = (int) ((rgb & 0x00FF0000) >>> 16); // Red level
					g = (int) ((rgb & 0x0000FF00) >>> 8); // Green level
					b = (int) (rgb & 0x000000FF); // Blue level

					img.setPixel(w, h,  (int) Math.round(.299 * r + .587 * g + .114 * b)); // convertendo para niveis de cinza
					
				}
			}
		}
		return img;
	}

	public static IRGBImage convertToRGBImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		IRGBImage img = new RGBImage(width, height);
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				img.setPixel(w, h, image.getRGB(w, h));				
			}
		}
		return img;
		
	}



	public static IRGBImage openRGBImage(File file) {
		try {
			return convertToRGBImage(ImageIO.read(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static IGrayScaleImage openGrayImage(File file) {
		try {
			return convertToGrayImage(ImageIO.read(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static IBinaryImage openBinaryImage(File file) {
		try {
			return convertToBinaryImage(ImageIO.read(file), 0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static IBinaryImage openBinaryImage(File file, int background) {
		try {
			return convertToBinaryImage(ImageIO.read(file), background);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
