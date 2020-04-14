import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.Key;
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
    private final int wSpeed = 2;
    private final int rSpeed = 4;

    private static Hashtable<String, Image> boyImages = new Hashtable<>();
    private static Hashtable<String, Image> girlImages = new Hashtable<>();

    private int movementTick = 0;
    private int frame = 1;
    private boolean running = false;

    private boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1];
    private int mostRecentKeyPress = 0;



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

        folder = new File("Assets/Player/Girl");
        listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                girlImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/Player/Girl/"+listOfFiles[i].getName()).getImage());
            }
        }
    }

    public void move() {
        int speed = running ? rSpeed : wSpeed;
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
            if (!dirIsPressed() || keyPressToDir(mostRecentKeyPress) != direction) {
                movementTick = 0;
                frame = 0;
                moving = false;
                running = false;
            }
            running = keys[KeyEvent.VK_SHIFT];
        }


    }

    public void move(int dir, boolean[] keys) {
        this.keys = keys;

        switch (dir) {
            case (Player.RIGHT):
                mostRecentKeyPress = KeyEvent.VK_D;
                break;
            case (Player.UP):
                mostRecentKeyPress = KeyEvent.VK_W;
                break;
            case (Player.LEFT):
                mostRecentKeyPress = KeyEvent.VK_A;
                break;
            case (Player.DOWN):
                mostRecentKeyPress = KeyEvent.VK_S;
                break;
        }

        if (!moving) {
            running = keys[KeyEvent.VK_SHIFT];
            direction = dir;
            moving = true;
        }
    }

    public boolean dirIsPressed() {
        switch (direction) {
            case (Player.RIGHT):
                return keys[KeyEvent.VK_D];
            case (Player.UP):
                return keys[KeyEvent.VK_W];
            case (Player.LEFT):
                return keys[KeyEvent.VK_A];
            case (Player.DOWN):
                return keys[KeyEvent.VK_S];
        }
        return false;
    }

    private int keyPressToDir(int keyPress) {
        switch (keyPress) {
            case (KeyEvent.VK_D):
                return Player.RIGHT;
            case (KeyEvent.VK_W):
                return Player.UP;
            case (KeyEvent.VK_A):
                return Player.LEFT;
            case (KeyEvent.VK_S):
                return Player.DOWN;
        }
        return 0;
    }

    public void draw(Graphics g) {
        if (gender == Player.MALE) {
            if (!moving) {
                switch (direction) {
                    case (RIGHT):
                        g.drawImage(boyImages.get("right"), 410, 258, null);
                        break;
                    case (UP):
                        g.drawImage(boyImages.get("back"), 410, 258, null);
                        break;
                    case (LEFT):
                        g.drawImage(boyImages.get("left"), 410, 258, null);
                        break;
                    case (DOWN):
                        g.drawImage(boyImages.get("front"), 410, 258, null);
                        break;
                }
            }
            else {
                if (movementTick % 15 == 0) {
                    frame++;
                }
                if (!running) {
                    switch (direction) {
                        case (RIGHT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("rightwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("right"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("rightwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("backwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("backwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("leftwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("left"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("leftwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("frontwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("frontwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (direction) {
                        case (RIGHT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("rightrun1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("right"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("rightrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("backrun1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("backrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("leftrun1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("left"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("leftrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("frontrun1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("frontrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                    }
                }
            }
        }
        else {
            if (!moving) {
                switch (direction) {
                    case (RIGHT):
                        g.drawImage(girlImages.get("right"), 410, 258, null);
                        break;
                    case (UP):
                        g.drawImage(girlImages.get("back"), 410, 258, null);
                        break;
                    case (LEFT):
                        g.drawImage(girlImages.get("left"), 410, 258, null);
                        break;
                    case (DOWN):
                        g.drawImage(girlImages.get("front"), 410, 258, null);
                        break;
                }
            }
            else {
                if (movementTick % 15 == 0) {
                    frame++;
                }
                if (!running) {
                    switch (direction) {
                        case (RIGHT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("rightwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("right"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("rightwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("backwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("backwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("leftwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("left"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("leftwalk2"), 410, 258, null);
                                        break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("frontwalk1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("frontwalk2"), 410, 258, null);
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (direction) {
                        case (RIGHT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("rightrun1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("right"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("rightrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("backrun1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("backrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("leftrun1"), 410, 258, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("left"), 410, 258, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("leftrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("frontrun1"), 410, 258, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("frontrun2"), 410, 258, null);
                                    break;
                            }
                            break;
                    }
                }
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
