import java.awt.*;
import java.util.*;
import java.io.*;

public class NPC {
    public static final ArrayList<String> greetings = new ArrayList<>();
    public static final ArrayList<String> chats = new ArrayList<>();
    public static final ArrayList<String> goodbyes = new ArrayList<>();

    public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;

    private int x, y;
    private int xTile, yTile;

    private int direction = DOWN;
    private boolean moving = false;

    private final int tileSize = GamePanel.tileSize;
    private final int wSpeed = 2;

    private Hashtable<String, Image> images;

    private String name;
    private String catchphrase;

    private int movementTick = 0;
    private int frame = 0;

    private Room room;



    public NPC(String name, Hashtable<String, Image> images, int xTile, int yTile, String catchphrase, Room room) {
        this.name = name;
        this.images = images;
        this.xTile = xTile;
        this.yTile = yTile;
        this.catchphrase = catchphrase;
        this.room = room;

    }

    public static void loadDialogue() {
        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/NPCs/greetings.txt")));
            int n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                greetings.add(stdin.nextLine().trim());
            }

            stdin = new Scanner(new BufferedReader(new FileReader("Assets/NPCs/chats.txt")));
            n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                chats.add(stdin.nextLine().trim());
            }

            stdin = new Scanner(new BufferedReader(new FileReader("Assets/NPCs/goodbyes.txt")));
            n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                goodbyes.add(stdin.nextLine().trim());
            }

        }
        catch (FileNotFoundException e) {
            System.out.println("Dialogue stuffs not found");
        }
    }

    public void draw(Graphics g, int playerX, int playerY) {
        if (!moving) {
            switch (direction) {
                case (RIGHT):
                    g.drawImage(images.get("right0"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
                case (UP):
                    g.drawImage(images.get("back0"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300,null);
                    break;
                case (LEFT):
                    g.drawImage(images.get("left0"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
                case (DOWN):
                    g.drawImage(images.get("front0"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                    break;
            }
        }
        else {
            if (movementTick % 15 == 0) {
                frame++;
            }

            switch (direction) {
                case (RIGHT):
                    switch (frame % 4) {
                        case (0):
                            g.drawImage(images.get("right1"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                        case (1):
                        case (3):
                            g.drawImage(images.get("right0"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                        case (2):
                            g.drawImage(images.get("right2"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                    }
                    break;
                case (UP):
                    switch (frame % 2) {
                        case (0):
                            g.drawImage(images.get("back1"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                        case (1):
                            g.drawImage(images.get("back2"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                    }
                    break;
                case (LEFT):
                    switch (frame % 4) {
                        case (0):
                            g.drawImage(images.get("left1"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                        case (1):
                        case (3):
                            g.drawImage(images.get("left0"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                        case (2):
                            g.drawImage(images.get("left2"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                    }
                    break;
                case (DOWN):
                    switch (frame % 2) {
                        case (0):
                            g.drawImage(images.get("front1"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                        case (1):
                            g.drawImage(images.get("front2"), xTile * GamePanel.tileSize - playerX + 480, yTile * GamePanel.tileSize - playerY + 300, null);
                            break;
                    }
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

    public int getxTile() {
        return xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isMoving() {
        return moving;
    }

    public Hashtable<String, Image> getImages() {
        return images;
    }

    public String getName() {
        return name;
    }

    public String getCatchphrase() {
        return catchphrase;
    }
}
