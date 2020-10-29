package puzzle.gui;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import puzzle.library.Animations;

import puzzle.logic.Bubble;
import puzzle.logic.EventController;
import puzzle.logic.Gameplay;
import puzzle.logic.Imager;
import puzzle.logic.Spriter;
import puzzle.library.Images;

import static puzzle.library.PBConstants.BOARD_HEIGHT;
import static puzzle.library.PBConstants.BOARD_WIDTH;
import static puzzle.library.PBConstants.GEAR_SIZE;
import static puzzle.library.PBConstants.PLANK_HEIGHT;
import static puzzle.library.PBConstants.BUBBLE_SIZE;
import static puzzle.library.PBConstants.MAX_ROWS;
import static puzzle.library.PBConstants.MAX_COLUMNS;

/**
 * the element where the gameplay occurs
 *
 * @author JD Gamboa
 */
public class GameBoard extends JLayeredPane {

    /* game elements */
    private Gameplay gameplay;
    /* controller of events */
    private EventController eventController;
    /* character */
    private Spriter character;
    /* stuff */
    private Imager backGround,
            plankBars,
            plank,
            gear;
    private Bubble currentBubble,
            nextBubble;
    private Bubble[][] bubbleMap,
            bubbleMapFall;
    private int plankPos;

    JLabel target;

    public GameBoard() {
        setUp();
    }

    private void setUp() {
        initWindow();
        initElements();
        editElements();
        addElements();
    }

    private void initWindow() {
        setSize(500, 500);
        setLayout(null);
    }

    private void initElements() {
        /* game controller */
        gameplay = new Gameplay(this);
        /* event controller */
        eventController = new EventController(gameplay);
        addKeyListener(eventController);
        /* sprites in the map */
        character = new Spriter();
        backGround = new Imager();
        gear = new Imager();
        plankBars = new Imager();
        plank = new Imager();
        currentBubble = new Bubble();
        nextBubble = new Bubble();
        plankPos = 0;
        target = new JLabel();
    }

    private void editElements() {
        character.setName("Character");
        character.addMouseListener(eventController);
        character.setBounds(60, 380, 64, 69);
        character.setImage(Images.BUBBLUN_STAND);
        backGround.setBounds(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        backGround.setImage(Images.BACK_TONERI_MEADOW);
        gear.setBounds(112, 344, GEAR_SIZE, GEAR_SIZE);
        gear.setImage(Images.GEAR);
        plank.setBounds(0, plankPos, BOARD_WIDTH, PLANK_HEIGHT);
        plank.setImage(Images.PLANK);
        plankBars.setBounds(0, 0, 0, 0);
        plankBars.setImage(Images.PLANK_BARS);
        currentBubble.setName("CurrentBubble");
        currentBubble.addMouseListener(eventController);
        currentBubble.setBounds(148, 380, BUBBLE_SIZE, BUBBLE_SIZE);
        nextBubble.setName("NextBubble");
        nextBubble.addMouseListener(eventController);
        nextBubble.setBounds(233, 391, BUBBLE_SIZE, BUBBLE_SIZE);
        // modify this so I can add in a layered manner
        drawBubbleMap();
    }

    private void addElements() {
        add(target);
        add(character);
        add(currentBubble);
        add(nextBubble);
        add(gear);
        add(plank);
        add(plankBars);
        add(backGround);
        validate();
    }

    private void drawBubbleMap() {
        bubbleMap = new Bubble[MAX_ROWS][MAX_COLUMNS];
        bubbleMapFall = new Bubble[MAX_ROWS][MAX_COLUMNS];
        int numBubblesRow;
        for (int i = 0; i < MAX_ROWS; i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (int j = 0; j <= numBubblesRow; j++) {
                bubbleMap[i][j] = new Bubble();
                bubbleMap[i][j].setRow(i);
                bubbleMap[i][j].setColumn(j);
                if ((i % 2) == 0) {
                    bubbleMap[i][j].setBoundsInMap(-4 + (j * BUBBLE_SIZE), 37 + (i * BUBBLE_SIZE), 62, 44);
                } else {
                    bubbleMap[i][j].setBoundsInMap(8 + (j * BUBBLE_SIZE), 37 + (i * BUBBLE_SIZE), 62, 44);
                }
                try {
                    bubbleMapFall[i][j] = (Bubble) bubbleMap[i][j].clone();
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(GameBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
                add(bubbleMap[i][j]);
                add(bubbleMapFall[i][j]);
            }
        }
        generateBubbleMap();
    }
    
    public void generateBubbleMap() {
        // *** FILL MAP
        int numBubblesRow;
        for (int i = 0; i < 5; i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (int j = 0; j <= numBubblesRow; j++) {
                int random = (int) (Math.random() * 4);
                bubbleMap[i][j].setBubble(Images.SOME_BUBBLES[random]);
            }
        }
        generateNextBubble();
        passNextBubble();
        generateNextBubble();
    }

    public Spriter getCharacter() {
        return character;
    }

    public Imager getGear() {
        return gear;
    }

    public Bubble getCurrentBubble() {
        return currentBubble;
    }

    public Bubble getNextBubble() {
        return nextBubble;
    }

    public int getPlankPosition() {
        return plankPos;
    }

    public Bubble[][] getBubbleMap() {
        return bubbleMap;
    }

    public Bubble[][] getBubbleMapFall() {
        return bubbleMapFall;
    }
    
    public void moveBubble(int x, int y) {
        currentBubble.setLocation(x, y);
        validate();
    }

    public void lowerPlank() {
        int plankLevel = gameplay.getPlankLevel();
        plankPos = plankLevel * BUBBLE_SIZE;
        plank.setLocation(0, plankPos);
        plankBars.setBounds(0, 0, BOARD_WIDTH, plankPos);
        int numBubblesRow;
        for (byte i = 0; i < MAX_ROWS; i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (byte j = 0; j <= numBubblesRow; j++) {
                if ((i % 2) == 0) {
                    bubbleMap[i][j].setLocationInMap(-4 + (j * BUBBLE_SIZE), 37 + (i * BUBBLE_SIZE) + plankPos);
                } else {
                    bubbleMap[i][j].setLocationInMap(8 + (j * BUBBLE_SIZE), 37 + (i * BUBBLE_SIZE) + plankPos);
                }
                if (i == (MAX_ROWS - plankLevel)) {
                    remove(bubbleMap[i][j]);
                    bubbleMap[i][j].setBubble(Images.NO_BUBBLE);
                }
            }
        }
        validate();
    }

    public boolean checkCollision(int x, int y) {
        boolean collision = false;
        int centerXMov = x + (BUBBLE_SIZE / 2);
        int centerYMov = y + (BUBBLE_SIZE / 2);
        double distance;
        byte rowLimit = (byte) gameplay.getPlankLevel();
        byte numBubblesRow;
        for (byte i = 0; i < (MAX_ROWS - rowLimit); i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (byte j = 0; j <= numBubblesRow; j++) {
                if (!bubbleMap[i][j].isEmpty()) {
                    distance = Math.sqrt(Math.pow((centerXMov - bubbleMap[i][j].getCenterX()), 2)
                            + Math.pow((centerYMov - bubbleMap[i][j].getCenterY()), 2));
                    if (distance <= BUBBLE_SIZE) {
                        collision = true;
                    }
                }
            }
        }
        return collision;
    }

    public int[] fillClosestEmptySpace(int x, int y) {
        int row = 0, column = 0;
        int[] coords = new int[2];
        byte rowLimit = (byte) gameplay.getPlankLevel();
        byte numBubblesRow;
        double distance;
        double minDistance = Double.POSITIVE_INFINITY;
        for (byte i = 0; i < (MAX_ROWS - rowLimit); i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (byte j = 0; j <= numBubblesRow; j++) {
                if (bubbleMap[i][j].isEmpty()) {
                    distance = Math.sqrt(Math.pow((x - bubbleMap[i][j].getPosX()), 2)
                            + Math.pow((y - bubbleMap[i][j].getPosY()), 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        row = i;
                        column = j;
                    }
                }
            }
        }
        bubbleMap[row][column].setBubble(currentBubble.getBubbleColor());
        coords[0] = row;
        coords[1] = column;
        return coords;
    }

    public void checkBubbleBurst(int row, int column) {
        ArrayList queueBubbles = new ArrayList<Object>();
        ArrayList checkedBubbles = new ArrayList<Object>();
        ArrayList burstBubbles = new ArrayList<Object>();
        Bubble hitBubble = bubbleMap[row][column];
        String hitColor = hitBubble.getBubbleColor();
        Bubble checkBubble;
        String checkColor;
        queueBubbles.add(hitBubble);
        checkedBubbles.add(hitBubble);
        while (!queueBubbles.isEmpty()) {
            //get a bubble from the queue
            checkBubble = (Bubble) queueBubbles.remove(0);
            checkColor = checkBubble.getBubbleColor();
            //if the color is the same as the wanted color, add the neighbors to the queue
            if (checkColor.equals(hitColor)) {
                burstBubbles.add(checkBubble);
                //neighboring
                ArrayList bubbleNeighbors = bubbleMap[checkBubble.getRow()][checkBubble.getColumn()].getNeighbors(bubbleMap);
                for (Object neighbor : bubbleNeighbors) {
                    if(!checkedBubbles.contains((Bubble) neighbor)) {
                        queueBubbles.add(neighbor);
                        checkedBubbles.add(neighbor);
                    }
                }
            }
        }
        if (burstBubbles.size() < 3) {
            burstBubbles.clear();
        } else {
            for (Object burstBubble : burstBubbles) {
                Bubble bubble = (Bubble) burstBubble;
                bubble.burst();
            }
            character.playAnimation(Animations.BUBBLUN_HAPPINESS, true);
        }
    }

    public void checkGameOver() {
        boolean gameOver = false;
        int lastRow = MAX_ROWS - 1;
        int numBubblesRow;
        int plankLevel = gameplay.getPlankLevel();
        if (((lastRow - plankLevel) % 2) == 0) {
            numBubblesRow = 11;
        } else {
            numBubblesRow = 10;
        }
        for (byte j = 0; j <= numBubblesRow; j++) {
            if (!bubbleMap[lastRow - plankLevel][j].isEmpty()) {
                gameOver = true;
            }
        }
        if (gameOver) {
            for (byte i = 0; i < MAX_ROWS; i++) {
                if ((i % 2) == 0) {
                    numBubblesRow = 11;
                } else {
                    numBubblesRow = 10;
                }
                for (byte j = 0; j <= numBubblesRow; j++) {
                    bubbleMap[i][j].turnIntoStone();
                }
            }
            character.removeMouseListener(eventController);
            character.playLoopedAnimation(Animations.BUBBLUN_DEFEAT, false, 4, 0);
            gameplay.setGameState("GAME OVER");
        }
        validate();
    }
    
    public void checkVictory() {
        boolean victory = true;
        byte numBubblesRow;
        for (byte i = 0; i < MAX_ROWS; i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (byte j = 0; j <= numBubblesRow; j++) {
                if(!bubbleMap[i][j].isEmpty()) {
                    victory = false;
                }
            }
        }
        if (victory) {
            character.removeMouseListener(eventController);
            character.playLoopedAnimation(Animations.BUBBLUN_VICTORY, true, 0, 0);
            gameplay.setGameState("VICTORY");
        }
    }
    
    public void passNextBubble() {
        currentBubble.setBubble(nextBubble.getBubbleColor());
        validate();
    }
    
    public void generateNextBubble() {
        int numBubblesRow;
        //get all colors available in the map
        ArrayList colorsAvailable = new ArrayList<Object>();
        int numColorsAvailable = 0;
        for (byte i = 0; i < MAX_ROWS; i++) {
            if ((i % 2) == 0) {
                numBubblesRow = 11;
            } else {
                numBubblesRow = 10;
            }
            for (byte j = 0; j <= numBubblesRow; j++) {
                if (!bubbleMap[i][j].isEmpty() && !colorsAvailable.contains(bubbleMap[i][j].getBubbleColor())) {
                    colorsAvailable.add(bubbleMap[i][j].getBubbleColor());
                    numColorsAvailable++;
                }
            }
        }
        int random = (int) (Math.random() * numColorsAvailable);
        nextBubble.setBubble((String)colorsAvailable.get(random));
    }

    public void drawTarget(int x, int y) {
        target.setBackground(Color.red);
        target.setOpaque(true);
        target.setBounds(x, y, 24, 24);
        validate();
    }

}
