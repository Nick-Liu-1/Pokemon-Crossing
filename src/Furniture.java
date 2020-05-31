import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class Furniture {
    private int xTile, yTile;
    private int length, width;
    private Image image;
    private int id;

    public static Hashtable<String, Image> wallpaperImages = new Hashtable<>();
    public static Hashtable<String, Image> floorImages = new Hashtable<>();

    public static final String[] wallpaperNames = new String[] {"blue and grey", "heart", "orange", "simple", "wood", "yellow"};
    public static final String[] floorNames = new String[] {"blue carpet", "checkered", "green woven", "grey", "red", "yellow checkered", "yellow"};

    public static final Hashtable<String, Image> furnitureImages = new Hashtable<>();
    public static final Hashtable<String, Pair<Integer, Integer>> furnitureSizes = new Hashtable<>();

    public Furniture(int xTile, int yTile, int length, int width, int id, Image image) {
        this.xTile = xTile;
        this.yTile = yTile;
        this.image = image;
        this.length = length;
        this.width = width;
        this.id = id;
    }

    public static void loadImages() {
        File folder = new File("Assets/Rooms/Wallpaper");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {
                wallpaperImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Rooms/Wallpaper/"+listOfFiles[i].getName()).getImage());
            }
        }

        folder = new File("Assets/Rooms/Floor");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                floorImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Rooms/Floor/"+listOfFiles[i].getName()).getImage());
            }
        }

        folder = new File("Assets/Items/Furniture");
        listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                furnitureImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Items/Furniture/"+listOfFiles[i].getName()).getImage());
            }
        }
    }

    public static void loadFurnitureSizes() {
        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Items/Furniture/Furniture Sizes.txt")));
            int n = Integer.parseInt(stdin.nextLine());
            String[] line;
            String fileName;
            for (int i = 0 ; i < n; i++) {
                line = stdin.nextLine().split(" ");
                fileName = "";
                for (int j = 0; j < line.length-2; j++) {
                    fileName += line[j] + " ";
                }

                fileName = fileName.substring(0, fileName.length()-1);

                furnitureSizes.put(fileName, new Pair<>(Integer.parseInt(line[line.length - 2]), Integer.parseInt(line[line.length - 1])));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading furniture sizes");
        }
    }

    public void draw(Graphics g, int playerX, int playerY) {
        g.drawImage(image, GamePanel.tileSize * xTile - playerX + 480, GamePanel.tileSize * yTile - playerY + 300 - 12, null);
    }

    public int getxTile() {
        return xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }
}
