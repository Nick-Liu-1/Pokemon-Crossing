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
    private int goingToxTile, goingToyTile;

    private int direction = DOWN;
    private boolean moving = false;

    private final int tileSize = GamePanel.tileSize;
    private final int speed = 2;

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
        this.x = tileSize * xTile;
        this.y = tileSize * yTile;

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
                    g.drawImage(images.get("right0"), x - playerX + 480, y - playerY + 300 - 12, null);
                    break;
                case (UP):
                    g.drawImage(images.get("back0"), x - playerX + 480, y - playerY + 300 - 12,null);
                    break;
                case (LEFT):
                    g.drawImage(images.get("left0"), x - playerX + 480, y - playerY + 300 - 12, null);
                    break;
                case (DOWN):
                    g.drawImage(images.get("front0"), x - playerX + 480, y - playerY + 300 - 12, null);
                    break;
            }
        }
        else {
            if (movementTick % 15 == 0) {
                frame++;
            }
            //System.out.println(movementTick + " " + frame);

            switch (direction) {
                case (RIGHT):
                    switch (frame % 4) {
                        case (0):
                            g.drawImage(images.get("right1"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                        case (1):
                        case (3):
                            g.drawImage(images.get("right0"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                        case (2):
                            g.drawImage(images.get("right2"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                    }
                    break;
                case (UP):
                    switch (frame % 2) {
                        case (0):
                            g.drawImage(images.get("back1"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                        case (1):
                            g.drawImage(images.get("back2"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                    }
                    break;
                case (LEFT):
                    switch (frame % 4) {
                        case (0):
                            g.drawImage(images.get("left1"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                        case (1):
                        case (3):
                            g.drawImage(images.get("left0"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                        case (2):
                            g.drawImage(images.get("left2"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                    }
                    break;
                case (DOWN):
                    switch (frame % 2) {
                        case (0):
                            g.drawImage(images.get("front1"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                        case (1):
                            g.drawImage(images.get("front2"), x - playerX + 480, y - playerY + 300 - 12, null);
                            break;
                    }
                    break;
            }
        }
    }

    public void move(int[][] grid, int playerX, int playerY, ArrayList<NPC> npcs) {
        if (!moving && GamePanel.randint(0, 100) == 0) {
            if (GamePanel.randint(0, 10) == 0 || inDir(grid, direction, playerX, playerY, npcs) != 1) {
                int newDir = direction;
                int count = 0;
                while (newDir == direction || inDir(grid, direction, playerX, playerY, npcs) != 1 && count < 5) {
                    newDir = GamePanel.randint(0, 3);
                    System.out.println("hi" + " " +  count);
                    count++;
                }
                direction = newDir;
            }
            if (inDir(grid, direction, playerX, playerY, npcs) == 1) {
                moving = true;
                movementTick = 0;
                frame = 0;
            }
        }

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
            // Set tile pos
            xTile = x / tileSize;
            yTile = y / tileSize;
            moving = false;
        }
    }

    public int inDir(int[][] grid, int dir, int playerX, int playerY, ArrayList<NPC> npcs) {
        int ans = 0;
        playerX /= GamePanel.tileSize;
        playerY /= GamePanel.tileSize;

        System.out.println((xTile) + " " + (yTile+1) + playerX + " " + playerY);
        switch (dir) {
            case (Player.RIGHT):
                ans = grid[xTile+1][yTile];
                if (xTile + 1 == playerX && yTile == playerY) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && xTile + 1 == temp.xTile && yTile == temp.yTile) {
                        ans = 0;
                    }
                }
                break;
            case (Player.UP):
                ans = grid[xTile][yTile-1];
                if (xTile == playerX && yTile - 1 == playerY) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && xTile == temp.xTile && yTile - 1 == temp.yTile) {
                        ans = 0;
                    }
                }
                break;
            case (Player.LEFT):
                ans = grid[xTile-1][yTile];
                if (xTile - 1 == playerX && yTile == playerY) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && xTile - 1 == temp.xTile && yTile == temp.yTile) {
                        ans = 0;
                    }
                }
                break;
            case (Player.DOWN):
                ans = grid[xTile][yTile+1];
                if (xTile == playerX && yTile + 1 == playerY) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (temp.name != name && xTile == temp.xTile && yTile + 1 == temp.yTile) {
                        ans = 0;
                    }
                }
                break;
        }
        if (ans == 4) {
            ans = 1;
        }
        return ans;
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

    public Room getRoom() {
        return room;
    }

    public int getGoingToxTile() {
        return goingToxTile;
    }

    public int getGoingToyTile() {
        return goingToyTile;
    }
}
