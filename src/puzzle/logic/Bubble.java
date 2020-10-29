package puzzle.logic;

import java.util.ArrayList;
import javax.swing.SwingConstants;
import puzzle.library.Animations;
import puzzle.library.Images;
import static puzzle.library.PBConstants.MAX_COLUMNS;
import static puzzle.library.PBConstants.MAX_ROWS;

public class Bubble extends Spriter implements Cloneable {

    private String color;
    private int centerX, centerY;
    private int posX, posY;
    private int row, column;
    private boolean hold = false;

    public Bubble() {
        setUp();
    }

    private void setUp() {
        setVerticalAlignment(SwingConstants.CENTER);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBubble(Images.NO_BUBBLE);
    }

    @Override
    public String toString() {
        String actualColor = (color.substring(color.indexOf("-") + 1, color.indexOf("."))).toUpperCase();
        return row + "," + column + " " + actualColor;
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean takesHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public String getBubbleColor() {
        return color;
    }

    public void setBubble(String color) {
        this.color = color;
        setImage(color);
    }

    public boolean isEmpty() {
        return color.equals(Images.NO_BUBBLE);
    }

    public void setBoundsInMap(int x, int y, int height, int width) {
        super.setBounds(x, y, height, width);
        centerX = x + 31;
        centerY = y + 22;
        posX = x + 19;
        posY = y + 10;
    }

    public void setLocationInMap(int x, int y) {
        super.setLocation(x, y);
        centerX = x + 31;
        centerY = y + 22;
        posX = x + 19;
        posY = y + 10;
    }

    public ArrayList<Bubble> getNeighbors(Bubble[][] map) {
        ArrayList<Bubble> neighbors = new ArrayList<>();
        if((column - 1) >= 0 && map[row][column - 1] != null) {
            neighbors.add(map[row][column - 1]);
        }
        if((column + 1) < MAX_COLUMNS && map[row][column + 1] != null) {
            neighbors.add(map[row][column + 1]);
        }
        if((row - 1) >= 0 && map[row - 1][column] != null) {
            neighbors.add(map[row - 1][column]);
        }
        if((row + 1) < MAX_ROWS && map[row + 1][column] != null) {
            neighbors.add(map[row + 1][column]);
        }
        if ((row % 2) == 0) {
            if((row - 1) >= 0 && (column - 1) >= 0 && map[row - 1][column - 1] != null) {
                neighbors.add(map[row - 1][column - 1]);
            }
            if((row + 1) < MAX_ROWS && (column - 1) >= 0 && map[row + 1][column - 1] != null) {
                neighbors.add(map[row + 1][column - 1]);
            }
        } else {
            if((row - 1) >= 0 && (column + 1) < MAX_COLUMNS && map[row - 1][column + 1] != null) {
                neighbors.add(map[row - 1][column + 1]);
            }
            if((row + 1) < MAX_ROWS && (column + 1) < MAX_COLUMNS && map[row + 1][column + 1] != null) {
                neighbors.add(map[row + 1][column + 1]);
            }
        }
        return neighbors;
    }
    
    public void glow() {
        if (color.equals(Images.BUBBLE_RED)) {
            playAnimation(Animations.RED_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_BLUE)) {
            playAnimation(Animations.BLUE_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_BLACK)) {
            playAnimation(Animations.BLACK_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_GRAY)) {
            playAnimation(Animations.GRAY_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_GREEN)) {
            playAnimation(Animations.GREEN_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_ORANGE)) {
            playAnimation(Animations.ORANGE_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_PURPLE)) {
            playAnimation(Animations.PURPLE_BUBBLE_GLOW, true);
        } else if (color.equals(Images.BUBBLE_YELLOW)) {
            playAnimation(Animations.YELLOW_BUBBLE_GLOW, true);
        }
    }

    public void burst() {
        if (color.equals(Images.BUBBLE_RED)) {
            playAnimation(Animations.RED_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_BLUE)) {
            playAnimation(Animations.BLUE_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_BLACK)) {
            playAnimation(Animations.BLACK_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_GRAY)) {
            playAnimation(Animations.GRAY_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_GREEN)) {
            playAnimation(Animations.GREEN_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_ORANGE)) {
            playAnimation(Animations.ORANGE_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_PURPLE)) {
            playAnimation(Animations.PURPLE_BUBBLE_BURST, false);
        } else if (color.equals(Images.BUBBLE_YELLOW)) {
            playAnimation(Animations.YELLOW_BUBBLE_BURST, false);
        }
        color = Images.NO_BUBBLE;
    }

    public void turnIntoStone() {
        if (color.equals(Images.BUBBLE_RED)) {
            setImage(Images.BUBBLE_RED_STONE);
        } else if (color.equals(Images.BUBBLE_BLUE)) {
            setImage(Images.BUBBLE_BLUE_STONE);
        } else if (color.equals(Images.BUBBLE_BLACK)) {
            setImage(Images.BUBBLE_BLACK_STONE);
        } else if (color.equals(Images.BUBBLE_GRAY)) {
            setImage(Images.BUBBLE_GRAY_STONE);
        } else if (color.equals(Images.BUBBLE_GREEN)) {
            setImage(Images.BUBBLE_GREEN_STONE);
        } else if (color.equals(Images.BUBBLE_ORANGE)) {
            setImage(Images.BUBBLE_ORANGE_STONE);
        } else if (color.equals(Images.BUBBLE_PURPLE)) {
            setImage(Images.BUBBLE_PURPLE_STONE);
        } else if (color.equals(Images.BUBBLE_YELLOW)) {
            setImage(Images.BUBBLE_YELLOW_STONE);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
