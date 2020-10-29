package puzzle.logic;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import puzzle.animators.SpriteLoopPlayer;
import puzzle.animators.SpritePlayer;

public class Spriter extends JLabel {

    //curent image
    private String imagePath;
    //animation variables
    private Timer animationTimer;
    private String state;
    
    /**
     * sets up the object: - image Alignment as (CENTER, BOTTOM) - double buffer
     * for repainting
     */
    public Spriter() {
        state = "AVAILABLE";
        setUp();
    }

    private void setUp() {
        setDoubleBuffered(true);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.BOTTOM);
    }

    /**
     * turns a path into a new ImageIcon
     *
     * @param path the path of the icon to create
     * @return the imageIcon if the path was valid, otherwise returns null
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
     *
     * @param path an URL as a string
     */
    public void setImage(String path) {
        setIcon(createImageIcon(path));
        imagePath = path;
        validate();
    }

    /**
     * displays an animation in the spriter
     *
     * @param animation the animation the spriter will play, as an URL string
     * array
     * @param restore if is desired to restore the original image
     */
    public void playAnimation(final String[] animation, final boolean restore) {
        SpritePlayer spritePlayer = new SpritePlayer(this, animation, restore);
        if (state.equals("AVAILABLE")) {
            state = "BUSY";
            animationTimer = new Timer(90, spritePlayer);
            animationTimer.start();
        }
    }

    /**
     * displays an animation in the spriter
     *
     * @param animation the animation the spriter will play, as an URL string
     * array
     * @param restore if is desired to restore the original image
     * @param loopPoint
     * @param loopCounter
     */
    public void playLoopedAnimation(final String[] animation, final boolean restore, int loopPoint, int loopCounter) {
        SpriteLoopPlayer spritePlayer = new SpriteLoopPlayer(this, animation, restore, loopPoint, loopCounter);
        if (state.equals("AVAILABLE")) {
            state = "BUSY";
            animationTimer = new Timer(90, spritePlayer);
            animationTimer.start();
        }
    }
    
    public String getImagePath() {
        return this.imagePath;
    }

    public Timer getAnimationTimer() {
        return animationTimer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
