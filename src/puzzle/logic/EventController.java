package puzzle.logic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import puzzle.library.Animations;

public class EventController implements KeyListener, MouseListener {

    private final Gameplay gameplay;
    private int keyUsed;
    private String mouseEventSource;

    public EventController(Gameplay gameEvent) {
        this.gameplay = gameEvent;        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyUsed = e.getKeyCode();        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyUsed = e.getKeyCode();
        if (keyUsed == KeyEvent.VK_LEFT) {
            gameplay.turnGear("LEFT");
        }
        if (keyUsed == KeyEvent.VK_RIGHT) {
            gameplay.turnGear("RIGHT");
        }        
        if (keyUsed == KeyEvent.VK_SPACE) {
            gameplay.shootBubble();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseEventSource = e.getComponent().getName();
        if (mouseEventSource.equals("Character") && !gameplay.getGameState().equals("VICTORY")) {            
            Spriter spriteEvent = (Spriter) e.getSource();
            spriteEvent.playAnimation(Animations.BUBBLUN_HAPPINESS, true);            
        }
        else if (mouseEventSource.equals("CurrentBubble") || 
                mouseEventSource.equals("NextBubble")) {
            Bubble bubbleEvent = (Bubble) e.getSource();
            bubbleEvent.glow();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
