package puzzle.logic;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * JSwing component where you can put images adding some effects
 * @author Juan David
 */
public class Imager extends JLabel {        
    
    /**
     * sets up the object:
     * - image Alignment as (CENTER, BOTTOM)
     * - double buffer for repainting
     */
    public Imager() {        
        setUp();        
    }

    private void setUp() {
        setDoubleBuffered(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.BOTTOM);
    }
    
    /**
     * turns a path into a new ImageIcon
     * @param path the path of the icon to create
     * @return the imageIcon if the path was valid, otherwise
     * returns null
     */
    private ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find the file " + path);
            return null;
        }
    }

    /**
     * displays an image in the spriter
     * @param image an URL as a string
     */
    public void setImage(String image) {
        setIcon(createImageIcon(image));
        validate();
    }

    /**
     * displays a rotated image
     * @param image the url of the image
     * @param angle the angle at which the icon will be rotated
     */
    public void setRotatedImage(String image, int angle) {        
        ImageIcon icon = createImageIcon(image);        
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();        
        BufferedImage buffImage = new BufferedImage(h, w, BufferedImage.TRANSLUCENT);        
        Graphics2D graphics2D = buffImage.createGraphics();        
        double x = (h - w) / 2.0;
        double y = (w - h) / 2.0;
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);        
        at.rotate(Math.toRadians(angle), (w / 2.0), (h / 2.0));        
        graphics2D.drawImage(icon.getImage(), at, this);        
        graphics2D.dispose();        
        setIcon(new ImageIcon(buffImage));        
        validate();
    }   

}
