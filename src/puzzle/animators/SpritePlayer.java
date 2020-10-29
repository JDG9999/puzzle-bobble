/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.animators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import puzzle.logic.Spriter;

/**
 *
 * @author Juan David
 */
public class SpritePlayer implements ActionListener {

    private final Spriter origin;
    private byte animationStep;
    private final String[] animation;
    private final boolean restore;
        
    private final String originalImagePath;
    
    public SpritePlayer(Spriter origin, String[] animation, boolean restore) {
        this.origin = origin;
        this.animation = animation;
        this.restore = restore;
        animationStep = 0;
        originalImagePath = origin.getImagePath();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (animationStep < animation.length) {
            origin.setImage(animation[animationStep]);
            animationStep++;
        } else {
            origin.getAnimationTimer().stop();
            if (restore) {
                origin.setImage(originalImagePath);
            }
            origin.setState("AVAILABLE");
        }
    }
    
}
