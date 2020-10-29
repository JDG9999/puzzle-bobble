package puzzle;

import puzzle.gui.Window;

/**
 * starts the app
 * @author Juan David
 */
public class App {    
    
    /** 
     * @param args entry for the main method
     * starts the app
     */
    public static void main(String[] args) {      
        Window gameWindow = new Window();
        gameWindow.setVisible(true);
    }

    private App() {
    }

}
