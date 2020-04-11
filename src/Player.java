import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Player {
    // CONSTANTS
    public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;

    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private int x, y;
    private int[] items = new int[16];
    private int gender;
    private int direction = DOWN;
    private boolean moving = false;

    private final int tileSize = GamePanel.tileSize;
    private final int speed = 2;

    private static Hashtable<String, Image> boyImages = new Hashtable<>();

    private int movementTick = 0;
    private int frame = 1;


    public Player(int x, int y, int gender) {
        this.x = x;
        this.y = y;
        this.gender = gender;
    }

    public static void load() {
        File folder = new File("Assets/Player/Boy");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                boyImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Player/Boy/"+listOfFiles[i].getName()).getImage());
            }
        }
    }

    public void move() {
        if (moving) {
            switch (direction) {
                case (RIGHT):
                    x += speed;
                    break;
                case (UP):
                    y -= speed;
                    break;
                case (LEFT):
                    x -= speed;
                    break;
                case (DOWN):
                    y += speed;
            }
            movementTick++;
        }

        if (x % tileSize == 0 && y % tileSize == 0) {
            movementTick = 0;
            frame = 1;

            moving = false;


        }
    }

    public void move(int dir) {
        if (!moving) {
            //System.out.println(dir);
            direction = dir;
            moving = true;
            move();
        }
    }

    public void draw(Graphics g) {
        if (!moving) {
            switch (direction) {
                case (RIGHT):
                    g.drawImage(boyImages.get("right"), 600, 420, null);
                    break;
                case (UP):
                    g.drawImage(boyImages.get("back"), 600, 420, null);
                    break;
                case (LEFT):
                    g.drawImage(boyImages.get("left"), 600, 420, null);
                    break;
                case (DOWN):
                    g.drawImage(boyImages.get("front"), 600, 420, null);
                    break;
            }
        }
        else {
            if (movementTick % 15 == 0) {
                frame = frame % 2 + 1;
            }
            System.out.println(frame + " " + movementTick);

            switch (direction) {
                case (RIGHT):
                    g.drawImage(boyImages.get("rightwalk" + frame), 600, 420, null);
                    break;
                case (UP):
                    g.drawImage(boyImages.get("backwalk" + frame), 600, 420, null);
                    break;
                case (LEFT):
                    g.drawImage(boyImages.get("leftwalk" + frame), 600, 420, null);
                    break;
                case (DOWN):
                    g.drawImage(boyImages.get("frontwalk" + frame), 600, 420, null);
                    break;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
