/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzle.animators;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import puzzle.logic.Spriter;

/**
 *
 * @author Juan David
 */
public class SpriteLoopPlayer implements ActionListener {
    
    private final Spriter origin;
    private byte animationStep;
    private final String[] originalAnimation;
    private final String[] animation;
    private final boolean restore;
    private final int loopPoint;
    private int loopCounter;
        
    private final String originalImagePath;
    
    public SpriteLoopPlayer(Spriter origin, String[] animation, boolean restore, int loopPoint, int loopCounter) {
        this.origin = origin;
        this.restore = restore;
        this.loopPoint = loopPoint;
        this.loopCounter = loopCounter;
        this.originalAnimation = animation;
        if (loopCounter == 0) {
            this.animation = animation;
        } else {
            this.animation = Arrays.copyOfRange(animation, loopPoint, animation.length);
        }
        animationStep = 0;
        originalImagePath = origin.getImagePath();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        loopCounter++;
        if (animationStep < animation.length) {
            origin.setImage(animation[animationStep]);
            animationStep++;
        } else {
            origin.getAnimationTimer().stop();
            if (restore) {
                origin.setImage(originalImagePath);
            }
            origin.setState("AVAILABLE");
            origin.playLoopedAnimation(originalAnimation, restore, loopPoint, loopCounter);
        }
    }
    
}
