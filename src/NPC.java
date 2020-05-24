import sun.plugin2.applet.context.NoopExecutionContext;

import javax.swing.*;
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

    private final String name;
    private final String catchphrase;

    private int movementTick = 0;
    private int frame = 0;

    private Room room;
    private int id;

    private boolean talking = false;
    private boolean stopQueued = false;

    private String currentGreeting = "";
    private String currentChat = "";
    private String currentGoodbye = "";

    private ArrayList<String> playerOptions = new ArrayList<>();

    private int speechStage = 0;
    public static final int GREETING = 0;
    public static final int CHAT = 1;
    public static final int GOODBYE = 2;
    public static final int QUEST = 3;




    public NPC(String name, Hashtable<String, Image> images, int xTile, int yTile, String catchphrase, Room room, int id) {
        this.name = name;
        this.images = images;
        this.xTile = xTile;
        this.yTile = yTile;
        this.catchphrase = catchphrase;
        this.room = room;
        this.x = tileSize * xTile;
        this.y = tileSize * yTile;
        goingToxTile = xTile;
        goingToyTile = yTile;
        this.id = id;

        playerOptions.add("Let's chat!");
        playerOptions.add("Never mind.");

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

    public void move(int[][] grid, int playerX, int playerY, int pgX, int pgY, ArrayList<NPC> npcs) {
        if (!moving && GamePanel.randint(0, 200) == 0 && !talking) {
            if (GamePanel.randint(0, 10) == 0 || inDir(grid, direction, playerX, playerY, pgX, pgY, npcs) != 1) {
                int newDir = direction;
                int count = 0;
                while (newDir == direction || inDir(grid, direction, playerX, playerY, pgX, pgY, npcs) != 1 && count < 5) {
                    newDir = GamePanel.randint(0, 3);
                    count++;
                }
                direction = newDir;
            }
            if (inDir(grid, direction, playerX, playerY, pgX, pgY, npcs) == 1) {
                moving = true;
                movementTick = 0;
                frame = 0;

                switch (direction) {
                    case (RIGHT):
                        goingToxTile = xTile + 1;
                        goingToyTile = yTile;
                        break;
                    case (UP):
                        goingToxTile = xTile;
                        goingToyTile = yTile - 1;
                        break;
                    case (LEFT):
                        goingToxTile = xTile - 1;
                        goingToyTile = yTile;
                        break;
                    case (DOWN):
                        goingToxTile = xTile;
                        goingToyTile = yTile + 1;
                        break;
                }
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
                    break;
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

    public int inDir(int[][] grid, int dir, int playerX, int playerY, int pgX, int pgY, ArrayList<NPC> npcs) {
        int ans = 0;
        playerX /= GamePanel.tileSize;
        playerY /= GamePanel.tileSize;

        switch (dir) {
            case (Player.RIGHT):
                ans = grid[xTile+1][yTile];
                if ((xTile + 1 == playerX && yTile == playerY) || (xTile + 1 == pgX && yTile == pgY)) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && ((xTile + 1 == temp.xTile && yTile == temp.yTile) || (xTile + 1 == temp.goingToxTile && yTile == temp.goingToyTile))) {
                        ans = 0;
                    }
                }
                break;
            case (Player.UP):
                ans = grid[xTile][yTile-1];
                if ((xTile == playerX && yTile - 1 == playerY) || (xTile == pgX && yTile - 1 == pgY)) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && ((xTile == temp.xTile && yTile - 1 == temp.yTile) || (xTile == temp.goingToxTile && yTile - 1 == temp.goingToyTile))) {
                        ans = 0;
                    }
                }
                break;
            case (Player.LEFT):
                ans = grid[xTile-1][yTile];
                if ((xTile - 1 == playerX && yTile == playerY) || (xTile - 1 == pgX && yTile == pgY)) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && ((xTile - 1 == temp.xTile && yTile == temp.yTile) || (xTile - 1 == temp.goingToxTile && yTile == temp.goingToyTile))) {
                        ans = 0;
                    }
                }
                break;
            case (Player.DOWN):
                ans = grid[xTile][yTile+1];
                if ((xTile == playerX && yTile + 1 == playerY) || (xTile == pgX && yTile + 1 == pgY)) {
                    ans = 0;
                }

                for (NPC temp : npcs) {
                    if (!temp.name.equals(name) && ((xTile == temp.xTile && yTile + 1 == temp.yTile) || (xTile == temp.goingToxTile && yTile + 1 == temp.goingToyTile))) {
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

    public void setDirection(int dir) {
        direction = dir;
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

    public int getId() {
        return id;
    }

    public boolean isTalking() {
        return talking;
    }

    public void setTalking(Boolean b) {
        talking = b;
    }

    public boolean isStopQueued() {
        return stopQueued;
    }

    public void setStopQueued(Boolean b) {
        stopQueued = b;
    }

    public void generateGreeting(String name) {
        String greeting = greetings.get(GamePanel.randint(0, greetings.size() - 1));
        greeting = greeting.replace("[PLAYER]", name);
        greeting = greeting.replace("[CATCHPHRASE]", catchphrase);

        currentGreeting = greeting;
    }

    public String getCurrentGreeting() {
        return currentGreeting;
    }

    public void setCurrentGreeting(String s) {
        currentGreeting = s;
    }

    public void generateChat(String name) {
        String chat = chats.get(GamePanel.randint(0, chats.size() - 1));
        chat = chat.replace("[PLAYER]", name);
        chat = chat.replace("[CATCHPHRASE]", catchphrase);

        currentChat = chat;
    }

    public String getCurrentChat() {
        return currentChat;
    }

    public void generateGoodbye(String name) {
        String goodbye = goodbyes.get(GamePanel.randint(0, goodbyes.size() - 1));
        goodbye = goodbye.replace("[PLAYER]", name);
        goodbye = goodbye.replace("[CATCHPHRASE]", catchphrase);

        currentGoodbye = goodbye;
    }

    public String getCurrentGoodbye() {
        return currentGoodbye;
    }

    public ArrayList<String> getPlayerOptions() {
        return playerOptions;
    }

    public int getSpeechStage() {
        return speechStage;
    }

    public void setSpeechStage(int n) {
        speechStage = n;
    }
}


class Tom_Nook extends NPC {
    private final Image image = new ImageIcon("Assets/NPCs/tom nook.png").getImage();
    private final Image speechBubble = new ImageIcon("Assets/Misc/speech bubble copy.png").getImage();
    private final Image shopImage = new ImageIcon("Assets/Misc/shop menu.png").getImage();

    private Player player;

    private ArrayList<Item> storeItems = new ArrayList<>();

    private final Image[] storeItemImages = new Image[GamePanel.getItems().size()];

    private ArrayList<String> playerOptions = new ArrayList<>();

    public Tom_Nook(String name, Hashtable<String, Image> images, int xTile, int yTile, String catchphrase, Room room, int id, Player player) {
        super(name, images, xTile, yTile, catchphrase, room, id);
        this.player = player;

        playerOptions.add("Buy.");
        playerOptions.add("Sell.");
        playerOptions.add("Never mind.");
        playerOptions.add("Housing.");
    }

    @Override
    public void draw(Graphics g, int playerX, int playerY) {
        g.drawImage(image, getxTile() * GamePanel.tileSize - playerX + 480, getyTile() * GamePanel.tileSize - playerY + 300, null);

        if (player.isTalkingToNPC() && player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK) {
            if (player.isShopOpen()) {
                g.drawImage(shopImage, (1020 - 825) / 2, (695 - 587) / 2, null);

                for (int i = 0; i < storeItems.size(); i++) {
                    if (storeItems.get(i).isFurniture()) {
                        g.drawImage(Item.storeLeafImage, 200,200 + 50*i, null);
                    }
                    else {
                        g.drawImage(storeItemImages[storeItems.get(i).getId()], 200,200 + 50*i, null);
                    }
                }
            }
        }
    }


    public void generateStoreItems() {
        for (int i = 0; i < 4; i++) {
            storeItems.add(GamePanel.getItems().get(Item.soldAtStore[GamePanel.randint(0, Item.soldAtStore.length - 1)]));
        }

        for (int i : Item.soldAtStore) {
            storeItemImages[i] = GamePanel.getItems().get(i).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        }
    }

    public Image getImage() {
        return image;
    }
}
