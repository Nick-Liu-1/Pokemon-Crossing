/*
    Tree.java
    Nick Liu + Annie Zhang
    ICS4U
    Tree class allows tree objects to be created and contain the necessary information ind dealing with trees.
 */

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class Tree {
    private int xTile, yTile;  // pos
    private int size;  // size x size tiles are the dimensions of the tree
    private int fruit;
    private int numFruit;

    // Types of fruit
    public static final int NO_FRUIT = 0;
    public static final int APPLE = 1;
    public static final int ORANGE = 2;
    public static final int PEACH = 3;
    public static final int PEAR = 4;

    private static Hashtable<String, Image> fruitImages = new Hashtable<>();
    private Room room;  // What room the tree is in

    // Constructor
    public Tree(int xTile, int yTile, int size, int fruit, Room room, int numFruit) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.size = size;
        this.fruit = fruit;
        this.room = room;
        this.numFruit = numFruit;
    }

    // Loads the fruit images
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

    // Checks if the tree contains the specified tile
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

    // Checks if the specified tile is adjacent to the tree
    public boolean isTileAdjacent(int xTile, int yTile) {
        System.out.println(this.xTile + " " + this.yTile + " " + xTile + " " + yTile);

        // Check top and bottom edges
        for (int i = this.xTile - 1; i < this.xTile + size + 1; i++) {
            if (xTile == i && (yTile == this.yTile - 1 || yTile == this.yTile + size)) {
                return true;
            }
        }

        // Check left and right edges
        for (int i = this.yTile; i < this.yTile + size + 1; i++) {
            if (yTile == i && (xTile == this.xTile - 1 || xTile == this.xTile + size)) {
                return true;
            }
        }
        return false;
    }

    // Draws the fruit on the tree
    public void draw(Graphics g, int playerX, int playerY) {
        if (numFruit > 0) {  // Draws the corresponding image based on fruit type and number of fruit
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

    public void pickFruit() {  // decrease number of fruit by 1
        if (numFruit >= 1) {
            numFruit--;
        }
    }

    public int getxTile() {
        return xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public int getSize() {
        return size;
    }

    public void setNumFruit(int n) {
        numFruit = n;
    }
}
