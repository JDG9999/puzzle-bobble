package puzzle.logic;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import puzzle.gui.GameBoard;
import puzzle.library.Animations;
import puzzle.library.Images;

import static puzzle.library.PBConstants.PLANK_HEIGHT;
import static puzzle.library.PBConstants.MAX_ROWS;

public class Gameplay {

    private final GameBoard gameboard;
    private int gearAngle;
    private int xAngle;
    private int plankLevel;
    private int bubblesShot;

    private String gameState;

    public Gameplay(GameBoard gameboard) {
        this.gameboard = gameboard;
        gearAngle = 0;
        xAngle = 90;
        plankLevel = 0;
        bubblesShot = 0;
        gameState = "FREE";
    }

    public void turnGear(String direction) {
        if (gearAngle >= -80 && gearAngle < 80 && direction.equals("RIGHT")) {
            gearAngle += 2;
            xAngle -= 2;
        }
        if (gearAngle <= 80 && gearAngle > -80 && direction.equals("LEFT")) {
            gearAngle -= 2;
            xAngle += 2;
        }
        gameboard.getGear().setRotatedImage(Images.GEAR, gearAngle);
    }

    public void shootBubble() {
        if (gameState.equals("FREE")) {
            gameboard.getCharacter().playAnimation(Animations.BUBBLUN_LOOK_UP, true);
            Runnable runAnimation = new Runnable() {
                @Override
                public void run() {
                    gameState = "BUSY";
                    Bubble[][] bubbleMap = gameboard.getBubbleMap();
                    Bubble[][] bubbleMapFall = gameboard.getBubbleMapFall();
                    int row = 0, column = 0;
                    int[] coords;
                    boolean attached = false;
                    boolean rebound = false;
                    boolean collision = false;
                    byte reboundCount = 1;
                    int xIni = 148;
                    int yIni = 380;
                    int angle = xAngle;
                    while (!attached) {
                        //two vars are used: 'ca' cathetus-adjacent and 'co' cathethus-opposite
                        double ca;
                        if (rebound) {
                            ca = 260.0;
                            rebound = false;
                        } else {
                            ca = 130.0;
                        }
                        double co = ca * Math.tan(Math.toRadians(angle));
                        if (co > (yIni - gameboard.getPlankPosition() - PLANK_HEIGHT)) {
                            //'co' can't be bigger than the max pixel distance in Y,
                            //so if it is bigger, the max distance is assumed and 'ca' is recalculated
                            co = (yIni - gameboard.getPlankPosition() - PLANK_HEIGHT);
                            ca = co * 1 / Math.tan(Math.toRadians(angle));
                        } else if (co < ((-1) * (yIni - gameboard.getPlankPosition() - PLANK_HEIGHT))) {
                            //for the second quadrant 'co' will be negative, but the same process
                            //of the first quadrant is required
                            co = (yIni - gameboard.getPlankPosition() - PLANK_HEIGHT);
                            ca = (co * 1 / Math.tan(Math.toRadians(angle)));
                        } else if (co < 0) {
                            //for the second quadrant, I need 'ca' to be negative,
                            //and, for now, 'co' to be positive
                            co = (-1) * co;
                            ca = (-1) * ca;
                        }
                        //'co' is always positive in the previous calculation.
                        //It is required to be negative.
                        co = Math.round((-1) * co);
                        ca = Math.round(reboundCount * ca);
                        int xTarget = xIni + (int) ca;
                        int yTarget = yIni + (int) co;
                        int xPos = xIni;
                        int yPos = yIni;
                        int xDist, yDist, i, movX, movY;
                        double dCA, dCO, mov;
                        while ((xPos != xTarget || yPos != yTarget) && !collision) {
                            xDist = xTarget - xPos;
                            yDist = yTarget - yPos;
                            if ((Math.abs(xDist) + Math.abs(yDist)) > 15) {
                                // a differential for movement along both axis is calculated...
                                // find the differential from the difference between current position and desired position
                                dCA = xDist / 500.0;
                                dCO = yDist / 500.0;
                                mov = 0;
                                i = 0;
                                //...so the total deplacement of the bubble is close to 10 pixels couting both axis
                                while (mov < 10.0) {
                                    i++;
                                    mov = Math.round(i * Math.abs(dCA)) + Math.round(i * Math.abs(dCO));
                                }
                                //then a deplacement for both axis is finally agreed.
                                movX = (int) Math.round(i * dCA);
                                movY = (int) Math.round(i * dCO);
                                xPos = xPos + movX;
                                yPos = yPos + movY;
                            } else {
                                xPos = xTarget;
                                yPos = yTarget;
                            }
                            //*** CHECK IF BUBBLE COLLISIONS WITH ANOTHER ONE ***
                            collision = gameboard.checkCollision(xPos, yPos);
                            if (collision) {
                                coords = gameboard.fillClosestEmptySpace(xPos, yPos);
                                row = coords[0];
                                column = coords[1];
                                xPos = 148;
                                yPos = 380;
                                attached = true;
                                bubblesShot++;
                            }
                            //***
                            // CHECK IF BUBBLE REBOUNDS AGAINST THE WALLS
                            if (xPos >= 278 || xPos <= 18) {
                                rebound = true;
                                reboundCount *= -1;
                                xIni = xTarget;
                                yIni = yTarget;
                            }
                            // CHECK IF BUBBLE HITS THE CEILING
                            if (yPos <= (gameboard.getPlankPosition() + PLANK_HEIGHT)) {
                                coords = gameboard.fillClosestEmptySpace(xPos, yPos);
                                row = coords[0];
                                column = coords[1];
                                xPos = 148;
                                yPos = 380;
                                collision = true;
                                attached = true;
                                bubblesShot++;
                            }
                            gameboard.moveBubble(xPos, yPos);
                            try {
                                Thread.sleep(15);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Gameplay.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    //pass next bubble to current
                    gameboard.passNextBubble();
                    //generate new next bubble
                    gameboard.generateNextBubble();
                    //Check for bursting
                    if (attached) {
                        gameboard.checkBubbleBurst(row, column);
                    }
                    //Check for falling
                    byte numBubblesRow;
                    //Reseting
                    for (byte i = 0; i < MAX_ROWS; i++) {
                        if ((i % 2) == 0) {
                            numBubblesRow = 11;
                        } else {
                            numBubblesRow = 10;
                        }
                        for (byte j = 0; j <= numBubblesRow; j++) {
                            bubbleMap[i][j].setHold(false);
                        }
                    }
                    //Checking in the normal direction
                    for (byte i = 0; i < MAX_ROWS; i++) {
                        if ((i % 2) == 0) {
                            numBubblesRow = 11;
                        } else {
                            numBubblesRow = 10;
                        }
                        for (byte j = 0; j <= numBubblesRow; j++) {
                            if (bubbleMap[i][j].isEmpty()) {
                                bubbleMap[i][j].setHold(true);
                            } else if (i == 0 && !bubbleMap[i][j].isEmpty()) {
                                bubbleMap[i][j].setHold(true);
                            } else if (!bubbleMap[i][j].isEmpty()) {
                                ArrayList bubbleNeighbors = bubbleMap[i][j].getNeighbors(bubbleMap);
                                for (Object neighbor : bubbleNeighbors) {
                                    if (((Bubble) neighbor).takesHold() && !((Bubble) neighbor).isEmpty()) {
                                        bubbleMap[i][j].setHold(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //Checking in the opposite direction
                    for (byte i = (MAX_ROWS - 1); i >= 0; i--) {
                        if ((i % 2) == 0) {
                            numBubblesRow = 11;
                        } else {
                            numBubblesRow = 10;
                        }
                        for (byte j = numBubblesRow; j >= 0; j--) {
                            if (i != 0 && !bubbleMap[i][j].isEmpty()) {
                                ArrayList bubbleNeighbors = bubbleMap[i][j].getNeighbors(bubbleMap);
                                for (Object neighbor : bubbleNeighbors) {
                                    if (((Bubble) neighbor).takesHold() && !((Bubble) neighbor).isEmpty()) {
                                        bubbleMap[i][j].setHold(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //make bubbles fall
                    for (byte i = 0; i < MAX_ROWS; i++) {
                        if ((i % 2) == 0) {
                            numBubblesRow = 11;
                        } else {
                            numBubblesRow = 10;
                        }
                        for (byte j = 0; j <= numBubblesRow; j++) {
                            if (!bubbleMap[i][j].takesHold()) {
                                int xPos, yPos;
                                xPos = bubbleMap[i][j].getX();
                                yPos = bubbleMap[i][j].getY();
                                bubbleMapFall[i][j].setLocation(xPos, yPos);
                                bubbleMapFall[i][j].setBubble(bubbleMap[i][j].getBubbleColor());
                                bubbleMap[i][j].setBubble(Images.NO_BUBBLE);
                            }
                        }
                    }
                    int fallPosition = 0;
                    int newX;
                    int newY;
                    while (fallPosition <= 500) {
                        for (byte i = 0; i < MAX_ROWS; i++) {
                            if ((i % 2) == 0) {
                                numBubblesRow = 11;
                            } else {
                                numBubblesRow = 10;
                            }
                            for (byte j = 0; j <= numBubblesRow; j++) {
                                if (!bubbleMap[i][j].takesHold()) {
                                    newX = bubbleMapFall[i][j].getX();
                                    newY = bubbleMapFall[i][j].getY() + 10;
                                    bubbleMapFall[i][j].setLocation(newX, newY);
                                }
                            }
                        }
                        fallPosition += 10;
                        gameboard.validate();
                        try {
                            Thread.sleep(15);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Gameplay.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    //Check if plank must be lowered
                    if ((bubblesShot % 10) == 0) {
                        plankLevel++;
                        gameboard.lowerPlank();
                    }
                    //Check for game over
                    gameboard.checkGameOver();
                    //Check for victory
                    gameboard.checkVictory();
                    //reset the state for shooting new bubbles
                    if (gameState.equals("BUSY")) {
                        gameState = "FREE";
                    }
                }
            };
            Thread executeAnimation = new Thread(runAnimation);
            executeAnimation.start();
        }
    }

    public int getxAngle() {
        return xAngle;
    }

    public int getPlankLevel() {
        return plankLevel;
    }

    public GameBoard getGameboard() {
        return gameboard;
    }

    public int getBubblesShot() {
        return bubblesShot;
    }

    public void setBubblesShot(int bubblesShot) {
        this.bubblesShot = bubblesShot;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }
    
    

}
