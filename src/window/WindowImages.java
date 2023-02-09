package window;


import image.IBinaryImage;
import image.IGrayScaleImage;
import image.IImage;
import image.IRGBImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.ImageBuilder;


/**
 * Project: Computer Vision Framework
 * 
 * @author Wonder Alexandre Luz Alves
 * @advisor Ronaldo Fumio Hashimoto
 * 
 * Classe que define os metodos utilitarios 
 */
public class WindowImages {
    
    private Map<String, BufferedImage> map = new HashMap<String, BufferedImage>();
    private static final String PROJECT_NAME = "Score-Image";
    
    
    /**
     * Apresentado uma imagem uma janela
     * @param img
     */
    public static void show(BufferedImage img){
        show(new BufferedImage[]{img});
    }
    
    /**
     * Apresentado uma imagem (BinaryImage) uma janela
     * @param img
     */
    public static void show(IImage img, String title){
        show(new IImage[]{img}, PROJECT_NAME, new String[]{title}, true);
    }
    
    /**
     * Apresentado uma imagem (BinaryImage) uma janela
     * @param img
     */
    public static void show(IImage img, String titleFrame, String title){
        show(new IImage[]{img}, titleFrame, new String[]{title}, true);
    }
    
    /**
     * Apresentado uma imagem (BinaryImage) uma janela
     * @param img
     */
    public static void show(IImage img[], String title[]){
        show(img, PROJECT_NAME, title, true);
    }
    
   
    public static void show(IImage[] img, String titleFrame, String[] titles, boolean isSalvar) {
        WindowImages win = new WindowImages();
        BufferedImage im[] = new BufferedImage[img.length];
        for(int i=0; i < img.length; i++){
        	if(img[i] instanceof IGrayScaleImage)
        		im[i] = ImageBuilder.convertToImage((IGrayScaleImage) img[i]);
        	else if(img[i] instanceof IRGBImage)
        		im[i] = ImageBuilder.convertToImage((IRGBImage) img[i]);
        	else if(img[i] instanceof IBinaryImage)
        		im[i] = ImageBuilder.convertToImage((IBinaryImage) img[i]);
        	else
        		throw new RuntimeException("Tipo de imagem invalido");
        }
        win.showWindow(im,titleFrame, titles, isSalvar);
    }    
    
    /**
     * Apresentado uma imagem (BinaryImage) uma janela
     * @param img
     */
    public static void show(IImage img){
        show(new IImage[]{img}, PROJECT_NAME,new String[]{img.getClass().getName()}, true);
    }
    
    /**
     * Apresentado uma colecao de imagens em uma janela
     * @param img[]
     */
    public static void show(BufferedImage img[]){
        WindowImages win = new WindowImages();
        win.showWindow(img, null);
    }
    
    public static void show(IImage img[], String titleFrame){
        show(img, titleFrame, true);
    }
    /**
     * Apresentado uma colecao de imagens em uma janela
     * @param img[]
     */
    public static void show(IImage img[], String titleFrame, boolean isSalvar){
        WindowImages win = new WindowImages();
        BufferedImage im[] = new BufferedImage[img.length];
        String titles[] = new String[img.length];
        for(int i=0; i < img.length; i++){
        	if(img[i] instanceof IGrayScaleImage)
        		im[i] = ImageBuilder.convertToImage((IGrayScaleImage) img[i]);
        	else if(img[i] instanceof IRGBImage)
        		im[i] = ImageBuilder.convertToImage((IRGBImage) img[i]);
        	else if(img[i] instanceof IBinaryImage)
        		im[i] = ImageBuilder.convertToImage((IBinaryImage) img[i]);
        	else
        		throw new RuntimeException("Tipo de imagem invalido");
            titles[i] = String.valueOf(i);
        }
        win.showWindow(im, titleFrame, titles, isSalvar);
    }
    
    public static void show(IImage img[], String titles[], String titleFrame, boolean isSalvar){
        WindowImages win = new WindowImages();
        BufferedImage im[] = new BufferedImage[img.length];
        for(int i=0; i < img.length; i++){
        	if(img[i] instanceof IGrayScaleImage)
        		im[i] = ImageBuilder.convertToImage((IGrayScaleImage) img[i]);
        	else if(img[i] instanceof IRGBImage)
        		im[i] = ImageBuilder.convertToImage((IRGBImage) img[i]);
        	else if(img[i] instanceof IBinaryImage)
        		im[i] = ImageBuilder.convertToImage((IBinaryImage) img[i]);
        	else
        		throw new RuntimeException("Tipo de imagem invalido");
        }
        win.showWindow(im, titleFrame, titles, isSalvar);
    }
    
    
    public static void show(IImage img[]){
        show(img, PROJECT_NAME, true);
    }
    

    public static void show(IImage img[], boolean isSalvar){
        show(img, PROJECT_NAME, isSalvar);
    }
    
    
    /**
     * Apresentado uma colecao de imagens em uma janela
     * @param img[]
     */
    public static void show(BufferedImage img[], String titles[]){
        show(img, PROJECT_NAME, titles);
    }
    
    /**
     * Apresentado uma colecao de imagens em uma janela
     * @param img[]
     */
    public static void show(BufferedImage img[], String titleFrame, String titles[]){
        WindowImages win = new WindowImages();
        win.showWindow(img, titleFrame, titles);
    }
    
 
    
    public JFrame getJFrame(BufferedImage img[], String title, String titles[]){
        return getJFrame(img, title, titles, false);
    }

    public JFrame getJFrame(BufferedImage img[], String title, String titles[], boolean isSalvar){
        final JFrame dialog = new JFrame();
        dialog.setTitle(title);
        //dialog.setBackground(Color.BLACK);
        dialog.setLayout(new BorderLayout());
        boolean flag = false;
        JPanel panelPrincipal = new JPanel();
        for(int i=0; i < img.length; i++){
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            JLabel label = new JLabel();
            
            label.setIcon(new ImageIcon(img[i]));
            if(titles == null)
                label.setBorder(BorderFactory.createTitledBorder(img[i].toString()));
            else
                label.setBorder(BorderFactory.createTitledBorder(titles[i]));
            
            JButton btnSalvar = new JButton("Save");
            btnSalvar.setActionCommand(String.valueOf(btnSalvar.hashCode()));
            map.put(btnSalvar.getActionCommand(), img[i]);
            btnSalvar.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    JFileChooser fc = new JFileChooser();
                    if(fc.showSaveDialog(null)  == JFileChooser.APPROVE_OPTION)
                    	ImageBuilder.saveImage(map.get(e.getActionCommand()), fc.getSelectedFile());
                }
            });
            panel.add(label, BorderLayout.CENTER);
            if(isSalvar)
                panel.add(btnSalvar, BorderLayout.SOUTH);
            
            panelPrincipal.add(panel,  BorderLayout.CENTER);
            if(label.getIcon().getIconWidth() > 1024 || label.getIcon().getIconHeight() > 860) flag = true;
        }
        panelPrincipal.setBackground(Color.LIGHT_GRAY);
        dialog.add(panelPrincipal);
        dialog.pack();
        dialog.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
               dialog.dispose();
            }
        });
        if(flag){
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.getViewport().add( panelPrincipal );
            dialog.add(scrollPane);
            dialog.setSize(750, 580);
        }

        return dialog;
    }
    
    /**
     * Apresentado uma colecao de imagens em uma janela
     * @param img[]
     */
    public void showWindow(BufferedImage img[], String titles[]){
        final JFrame dialog = getJFrame(img,  PROJECT_NAME, titles);
        dialog.setVisible(true);
    }

    public void showWindow(BufferedImage img[], String titleFrame, String titles[]){
        final JFrame dialog = getJFrame(img, titleFrame, titles);
        dialog.setVisible(true);
    }
    
    /**
     * Apresentado uma colecao de imagens em uma janela
     * @param img[]
     */
    public void showWindow(BufferedImage img[], String titles[], boolean isSalvar){
        final JFrame dialog = getJFrame(img,  PROJECT_NAME, titles,isSalvar);
        dialog.setVisible(true);
    }

    
    public void showWindow(BufferedImage img[], String titleFrame, String titles[], boolean isSalvar){
        final JFrame dialog = getJFrame(img, titleFrame, titles, isSalvar);
        dialog.setVisible(true);
    }
    
    
    
    
}

