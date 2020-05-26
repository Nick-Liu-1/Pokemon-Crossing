import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;

public class Tree {
    private int xTile, yTile;
    private int size;
    private int fruit;
    private int numFruit;

    public static final int APPLE = 1;
    public static final int ORANGE = 2;
    public static final int PEACH = 3;
    public static final int PEAR = 4;

    private Hashtable<String, Image> fruitImages = new Hashtable<>();

    public Tree(int xTile, int yTile, int size) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.size = size;
    }

    public void loadFruits() {
        File folder = new File("Assets/Fruits/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                fruitImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Fruits/"+listOfFiles[i].getName()).getImage());
            }
        }
    }

    public void draw(Graphics g) {

    }
}
