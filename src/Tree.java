import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class Tree {
    private int xTile, yTile;
    private int size;
    private int fruit;
    private int numFruit;

    public static final int NO_FRUIT = 0;
    public static final int APPLE = 1;
    public static final int ORANGE = 2;
    public static final int PEACH = 3;
    public static final int PEAR = 4;

    private static Hashtable<String, Image> fruitImages = new Hashtable<>();
    private Room room;

    public Tree(int xTile, int yTile, int size, int fruit, Room room, int numFruit) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.size = size;
        this.fruit = fruit;
        this.room = room;
        this.numFruit = numFruit;
    }

    public static void loadFruits() {
        File folder = new File("Assets/Fruits/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fruitImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Fruits/"+listOfFiles[i].getName()).getImage());
            }
        }
    }

    public boolean isTileOnTree(int xTile, int yTile) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i + this.xTile == xTile && j + this.yTile == yTile) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTileAdjacent(int xTile, int yTile) {
        System.out.println(this.xTile + " " + this.yTile + " " + xTile + " " + yTile);

        for (int i = this.xTile - 1; i < this.xTile + size + 1; i++) {
            if (xTile == i && (yTile == this.yTile - 1 || yTile == this.yTile + size)) {
                return true;
            }
        }
        for (int i = this.yTile; i < this.yTile + size + 1; i++) {
            if (yTile == i && (xTile == this.xTile - 1 || xTile == this.xTile + size)) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g, int playerX, int playerY) {
        if (numFruit > 0) {
            switch (fruit) {
                case (APPLE):
                    g.drawImage(fruitImages.get("apples" + numFruit), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
                case (ORANGE):
                    g.drawImage(fruitImages.get("oranges" + numFruit), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
                case (PEACH):
                    g.drawImage(fruitImages.get("peaches" + numFruit), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
                case(PEAR):
                    g.drawImage(fruitImages.get("pears" + numFruit), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
            }
        }
        //g.drawRect(xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, 60 * size, 60* size);
    }

    public Room getRoom() {
        return room;
    }

    public int getNumFruit() {
        return numFruit;
    }

    public int getFruit() {
        return fruit;
    }

    public void pickFruit() {
        if (numFruit >= 1) {
            numFruit--;
        }
    }
}
