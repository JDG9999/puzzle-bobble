package puzzle.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Window extends JFrame {

    private GameBoard userBoard;    
    private ImageIcon windowIcon;

    public Window() {
        setUp();        
    }
    
    private void setUp() {
        initWindow();
        initElements();
        editElements();
        addElements();
        repaint();
        validate(); 
    }

    private void initWindow() {
        setSize(327, 510);        
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        windowIcon = new ImageIcon(getClass().getResource(
                "/puzzle/sprites/icon.png"));
        setIconImage(windowIcon.getImage());
        setResizable(false);
        setTitle("PUZZLE BOBBLE");
    }

    private void initElements() {
        userBoard = new GameBoard();
    }

    private void editElements() {        
        userBoard.setFocusable(true);        
    }

    private void addElements() {
        add(userBoard);
    }

}
