import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

public class Player {
    // Player direction constants
    public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;

    // Gender constants
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    // Player pos
    private int x, y;
    private int xTile, yTile;

    // Player items
    private Item[][] items = new Item[6][3];
    private Item equippedItem;
    private int selectedItemR = -1;	//pos of selected item in inventory
    private int selectedItemC = -1;

    // Other player info
    private String name;
    private int gender;
    private int direction = DOWN;
    private boolean moving = false;

    // Other constants
    private final int tileSize = GamePanel.tileSize;
    private final int wSpeed = 2;
    private final int rSpeed = 4;

    // Player images
    private static Hashtable<String, Image> boyImages = new Hashtable<>();
    private static Hashtable<String, Image> girlImages = new Hashtable<>();

    // Help deal with drawing the player
    private int movementTick = 0;
    private int frame = 1;
    private boolean running = false;

    // Keys
    private boolean[] keys = new boolean[KeyEvent.KEY_LAST + 1];
    private int mostRecentKeyPress = 0;

    // Grid of current room
    private int[][] grid;

    // Flags
    private boolean goingToNewRoom = false;
    private boolean exitingRoom = false;
    private boolean inventoryOpen = false;
    private boolean escapeQueued = false;
    private boolean rightClickMenuOpen = false;
    private boolean selectedEquipped = false;
    private boolean talkingToNPC = false;
    private boolean shopOpen = false;
    private boolean sellShopOpen = false;

    private ArrayList<Rectangle> rightClickMenu = new ArrayList<>();
    private Image rightClickImage;

    private final Image inventoryImage = new ImageIcon("Assets/Misc/inventory.png").getImage();

    // How many bells player has
    private int bells = 69420;

    private GamePanel mainFrame;

    private int offsetX, offsetY;

    private final int equippedX = 682;
    private final int equippedY = 280;

    private int villagerPlayerIsTalkingTo = -1;

    public static final int TOM_NOOK = 0;
    public static final int ISABELLE = 1;
    public static final int CELESTE = 2;
    public static final int ANNIE = 3;
    public static final int BOB_THE_BUILDER = 4;
    public static final int NICK = 5;
    public static final int BOAT_OPERATOR = 6;
    public static final int BOAT_OPERATOR_ON_ISLAND = 7;

    private int goingToxTile, goingToyTile;

    private boolean dialogueSelectionOpen = false;

    private boolean selectionMenuClicked = false;


    private boolean[][] selectedItems = new boolean [6][3];
    private int sellAmount = 0;

    private Rectangle sellRect = new Rectangle(750, 40, 140, 40);
    private Rectangle cancelRect = new Rectangle(750, 90, 140, 40);

    public int selectedItemInShop = -1;


    // Constructor
    public Player(String name, int x, int y, int gender, int[][] grid, GamePanel mainFrame) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.gender = gender;
        this.grid = grid;
        this.mainFrame = mainFrame;
        goingToxTile = x / GamePanel.tileSize;
        goingToyTile = y / GamePanel.tileSize;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
            	if(j==0){
            		items[i][j] = new Item(1,"Fishing Rod", new ImageIcon("Assets/Items/General/fishing rod.png").getImage(), 500, 125);
            	}
                else{
                	items[i][j] = new Item(6, "Shovel", new ImageIcon("Assets/Items/General/shovel.png").getImage(), 500, 125);
                }
            }
        }
    }

    // Load boy and girl images
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

    // Move the player, called every frame
    public void move() {
        int speed = running ? rSpeed : wSpeed;  // Set speed

        // If moving. move in the correct direction
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

        // Player has reached a new tile
        if (x % tileSize == 0 && y % tileSize == 0) {
            // Set tile pos
            xTile = x / tileSize;
            yTile = y / tileSize;

            // Player stops if not continuing in same direction
            if (!dirIsPressed() || keyPressToDir(mostRecentKeyPress) != direction || inDir(direction) != 1 || escapeQueued) {
                // Reset movement variables
                movementTick = 0;
                frame = 0;
                moving = false;
                running = false;
            }
            else {
                changeDest();
            }

            if (escapeQueued) {
                inventoryOpen = !inventoryOpen;
                escapeQueued = false;
            }

            running = keys[KeyEvent.VK_SHIFT];
        }
    }

    // Move player in certain direction
    public void move(int dir, boolean[] keys, int[][] grid) {
        this.keys = keys;  // Update keys
        // Reset going to room stuff
        goingToNewRoom = false;
        exitingRoom = false;

        this.grid = grid; // Update grid

        // Set mostRecentKeyPress to specified direction
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

        // If player is stopped change direction
        if (!moving && !talkingToNPC) {
            running = keys[KeyEvent.VK_SHIFT];
            direction = dir;
            moving = inDir(dir) == 1;
            if (moving) {
                changeDest();
            }

            if (inDir(dir) == 2) {
                goingToNewRoom = true;
            }
            else if (inDir(dir) == 3) {
                exitingRoom = true;
            }
        }
    }

    // Check if the current direction is pressed
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

    // Converts key press to direction
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

    // Return what is in the grid in the specified direction
    public int inDir(int dir) {
        int ans = 0;
        switch (dir) {
            case (Player.RIGHT):
                ans = grid[xTile+1][yTile];
                for (NPC temp : mainFrame.getNPCs()) {
                    if ((xTile + 1 == temp.getxTile() && yTile == temp.getyTile()) || (xTile + 1 == temp.getGoingToxTile() && yTile == temp.getGoingToyTile())) {
                        ans = 0;
                    }
                }
                break;
            case (Player.UP):
                ans = grid[xTile][yTile-1];
                for (NPC temp : mainFrame.getNPCs()) {
                    if ((xTile == temp.getxTile() && yTile - 1 == temp.getyTile()) || (xTile == temp.getGoingToxTile() && yTile - 1 == temp.getGoingToyTile())) {
                        ans = 0;
                    }
                }
                break;
            case (Player.LEFT):
                ans = grid[xTile-1][yTile];
                for (NPC temp : mainFrame.getNPCs()) {
                    if ((xTile - 1 == temp.getxTile() && yTile == temp.getyTile()) || (xTile - 1 == temp.getGoingToxTile() && yTile == temp.getGoingToyTile())) {
                        ans = 0;
                    }
                }
                break;
            case (Player.DOWN):
                ans = grid[xTile][yTile+1];
                for (NPC temp : mainFrame.getNPCs()) {
                    if ((xTile == temp.getxTile() && yTile + 1 == temp.getyTile()) || (xTile == temp.getGoingToxTile() && yTile + 1 == temp.getGoingToyTile())) {
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

    // Draw the the player
    public void draw(Graphics g) {
        if (gender == Player.MALE) {
            if (!moving) {
                switch (direction) {
                    case (RIGHT):
                        g.drawImage(boyImages.get("right"), 410, 248, null);
                        break;
                    case (UP):
                        g.drawImage(boyImages.get("back"), 410, 248, null);
                        break;
                    case (LEFT):
                        g.drawImage(boyImages.get("left"), 410, 248, null);
                        break;
                    case (DOWN):
                        g.drawImage(boyImages.get("front"), 410, 248, null);
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
                                    g.drawImage(boyImages.get("rightwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("right"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("rightwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("backwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("backwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("leftwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("left"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("leftwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("frontwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("frontwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (direction) {
                        case (RIGHT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("rightrun1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("right"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("rightrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("backrun1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("backrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(boyImages.get("leftrun1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(boyImages.get("left"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(boyImages.get("leftrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(boyImages.get("frontrun1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(boyImages.get("frontrun2"), 410, 248, null);
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
                        g.drawImage(girlImages.get("right"), 410, 248, null);
                        break;
                    case (UP):
                        g.drawImage(girlImages.get("back"), 410, 248, null);
                        break;
                    case (LEFT):
                        g.drawImage(girlImages.get("left"), 410, 248, null);
                        break;
                    case (DOWN):
                        g.drawImage(girlImages.get("front"), 410, 248, null);
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
                                    g.drawImage(girlImages.get("rightwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("right"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("rightwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("backwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("backwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("leftwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("left"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("leftwalk2"), 410, 248, null);
                                        break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("frontwalk1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("frontwalk2"), 410, 248, null);
                                    break;
                            }
                            break;
                    }
                } else {
                    switch (direction) {
                        case (RIGHT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("rightrun1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("right"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("rightrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (UP):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("backrun1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("backrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (LEFT):
                            switch (frame % 4) {
                                case (0):
                                    g.drawImage(girlImages.get("leftrun1"), 410, 248, null);
                                    break;
                                case (1):
                                case (3):
                                    g.drawImage(girlImages.get("left"), 410, 248, null);
                                    break;
                                case (2):
                                    g.drawImage(girlImages.get("leftrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                        case (DOWN):
                            switch (frame % 2) {
                                case (0):
                                    g.drawImage(girlImages.get("frontrun1"), 410, 248, null);
                                    break;
                                case (1):
                                    g.drawImage(girlImages.get("frontrun2"), 410, 248, null);
                                    break;
                            }
                            break;
                    }
                }
            }
        }
        if (inventoryOpen) {
            g.drawImage(inventoryImage, 288, 20, null);
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[0].length; j++) {
                    if (items[i][j] != null) {
                        if (!(i == selectedItemR && j == selectedItemC && mainFrame.isClicked())) {
                            if (items[i][j].isFurniture()) {
                                g.drawImage(Item.leafImage, 323 + i * 68, 54 + j * 68, null);
                            }
                            else {
                                g.drawImage(items[i][j].getImage(), 323 + i * 68, 54 + j * 68, null);
                            }
                        }
                        else if (i == selectedItemR && j == selectedItemC) {
                            if (items[i][j].isFurniture()) {
                                g.drawImage(Item.leafImage, mainFrame.getMouse().x - offsetX - 19, mainFrame.getMouse().y - offsetY - 18, null);
                            }
                            else {
                                g.drawImage(items[i][j].getImage(), mainFrame.getMouse().x - offsetX - 19, mainFrame.getMouse().y - offsetY - 18, null);
                            }
                        }
                    }
                }
            }
            //System.out.println(selectedItemR + " " + selectedItemC);
            if ((selectedItemR != -1 && selectedItemC != -1) || selectedEquipped){
            	g.setColor(new Color(0,255,0));

            	if (!selectedEquipped) {
                    g.drawOval(323 + selectedItemR * 68, 54 + selectedItemC * 68, 38, 38);
                }
            	else {
                    g.drawOval(663, 262, 38, 38);
                }

               	if (rightClickMenuOpen) {
               	    rightClickMenu.clear();
               	    if (!selectedEquipped) {
                        rightClickMenu.add(new Rectangle(323 + selectedItemR * 68 + offsetX + 19, 54 + selectedItemC * 68 + offsetY + 18, 140, 40));
                        if (items[selectedItemR][selectedItemC].canBeEquipped()) {
                            rightClickMenu.add(new Rectangle(323 + selectedItemR * 68 + offsetX + 19, 94 + selectedItemC * 68 + offsetY + 18, 140, 40));
                        }
                    }
               	    else {
               	        rightClickMenu.add(new Rectangle(663 + offsetX, 262 + offsetY, 140, 40));
               	        rightClickMenu.add(new Rectangle(663 + offsetX, 262 + offsetY + 40, 140, 40));
                    }


                    for (Rectangle r : rightClickMenu) {
                        g.setColor(Color.WHITE);
                        g.fillRect(r.x, r.y, r.width, r.height);
                        g.setColor(Color.BLACK);
                        g.drawRect(r.x, r.y, r.width, r.height);
                    }
                }
            }

            if (equippedItem != null) {
                g.drawImage(equippedItem.getImage(), 663,262, null);
            }

            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;

                FontMetrics fontMetrics = new JLabel().getFontMetrics(GamePanel.finkheavy30);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(GamePanel.finkheavy30);
                g2.setColor(new Color(0,0,0));

                int x, y;  // x, y coordinates of text
                int width;  // width of text

                g2.drawString(String.valueOf(bells), 360 ,293);

                if (rightClickMenuOpen) {
                    if (selectedItemR!=-1 && selectedItemC!=-1) {
                        width = fontMetrics.stringWidth("Drop");
                        g2.drawString("Drop",323 + selectedItemR * 68 + offsetX + 19 + (140 - width) / 2, 54 + selectedItemC * 68 + offsetY + 18 + 28);
                        if (rightClickMenu.size() >= 2) {
                            width = fontMetrics.stringWidth("Equip");
                            g2.drawString("Equip",323 + selectedItemR * 68 + offsetX + 19 + (142 - width) / 2, 54 + selectedItemC * 68 + offsetY + 18 + 68);

                        }
                    }
                    else if (selectedEquipped) {
                        width = fontMetrics.stringWidth("Drop");
                        g2.drawString("Drop",663 + offsetX  + (140 - width) / 2, 262 + offsetY + 28);
                        width = fontMetrics.stringWidth("Unequip");
                        g2.drawString("Unequip",663 + offsetX + (140 - width) / 2, 262 + offsetY + 68);
                    }
                }
            }
        }

        if (sellShopOpen) {
            updateSellAmount();

            g.drawImage(inventoryImage, 288, 20, null);
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[0].length; j++) {
                    if (items[i][j] != null) {
                        if (!(i == selectedItemR && j == selectedItemC && mainFrame.isClicked())) {
                            if (items[i][j].isFurniture()) {
                                g.drawImage(Item.leafImage, 323 + i * 68, 54 + j * 68, null);
                            }
                            else {
                                g.drawImage(items[i][j].getImage(), 323 + i * 68, 54 + j * 68, null);
                            }
                        }

                        if (selectedItems[i][j]) {
                            g.setColor(Color.GREEN);
                            g.drawOval(323 + i * 68, 54 + j * 68, 38, 38);
                        }
                    }
                }
            }

            g.setColor(Color.WHITE);
            g.fillRect(sellRect.x, sellRect.y, sellRect.width, sellRect.height);
            g.fillRect(cancelRect.x, cancelRect.y, cancelRect.width, cancelRect.height);

            g.setColor(Color.BLACK);
            g.drawRect(sellRect.x, sellRect.y, sellRect.width, sellRect.height);
            g.drawRect(cancelRect.x, cancelRect.y, cancelRect.width, cancelRect.height);

            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;

                FontMetrics fontMetrics = new JLabel().getFontMetrics(GamePanel.finkheavy30);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(GamePanel.finkheavy30);
                g2.setColor(new Color(0, 0, 0));


                g2.drawString(String.valueOf(sellAmount), 360, 293);

                int width = fontMetrics.stringWidth("Sell");
                g2.drawString("Sell", 750 + (140 - width) / 2, 40 + 32);

                width = fontMetrics.stringWidth("Cancel");
                g2.drawString("Cancel", 750 + (140 - width) / 2, 90 + 32);
            }
        }


    }

    public boolean isInventoryFull() {
        boolean ans = true;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if (items[i][j] == null) {
                    ans = false;
                    break;
                }
            }
        }
        return ans;
    }
    
    public void addItem(Item item){
    	for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 6; i++) {
                if(items[i][j] == null) {
					items[i][j] = item;
					return;
                }
            }
        }
    }
    
    public void removeItem(Item item){
    	
    }
    
    public void selectItem(Point mouse){
    	int noCollision=0;
    	for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
               	if((Math.hypot(mouse.getX() - (342 + i * 68),  mouse.getY() - (72 + j * 68))) < 19 && items[i][j]!=null){
               		selectedItemR = i;
               		selectedItemC = j;
               		selectedEquipped = false;
               		offsetX = (int) (mouse.getX() - (342 + i * 68));
               		offsetY = (int) (mouse.getY() - (72 + j * 68));
               		break;
               	}
               	noCollision++;
            }    
		}

    	if (Math.hypot(mouse.getX() - equippedX,  mouse.getY() - equippedY) < 19 && equippedItem != null) {
    	    selectedItemR = -1;
    	    selectedItemC = -1;
    	    selectedEquipped = true;
            offsetX = (int) (mouse.getX() - 663);
            offsetY = (int) (mouse.getY() - 262);
        }

		if (noCollision==18){
			selectedItemR = -1;
        	selectedItemC = -1;
		}
    }

    public void selectSellItem(Point mouse){
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items[0].length; j++) {
                if((Math.hypot(mouse.getX() - (342 + i * 68),  mouse.getY() - (72 + j * 68))) < 19 && items[i][j]!=null){
                    selectedItems[i][j] = !selectedItems[i][j];
                    break;
                }
            }
        }
    }
    
    public void moveItem(Point mouse){
    	int shortestDist = Integer.MAX_VALUE;
    	int swapR=0;
    	int swapC=0;
    	if(selectedItemR < 6 && selectedItemC < 3){
	    	for (int i = 0; i < 6; i++) {
	            for (int j = 0; j < 3; j++) {
	                int dist = (int)(Math.hypot(mouse.x - (342 + i * 68),  mouse.y - (72 + j * 68)));
	                if(dist<=shortestDist){
	                	shortestDist = dist;
	                	swapR = i;
	                	swapC = j;
	                }
	            }
	        }

	    	if (selectedItemC != -1 && selectedItemR != -1) {
                if (items[swapR][swapC] == null){
                    items[swapR][swapC] = items[selectedItemR][selectedItemC];
                    items[selectedItemR][selectedItemC] = null;
                }
                else {
                    Item temp = items[selectedItemR][selectedItemC];
                    items[selectedItemR][selectedItemC] = items[swapR][swapC];
                    items[swapR][swapC] = temp;
                }
                selectedItemR=swapR;
                selectedItemC=swapC;
            }
    	}
    }

    public void dropSelectedItem() {
        if (!selectedEquipped) {
            items[selectedItemR][selectedItemC] = null;
        }
        else {
            selectedEquipped = false;
            equippedItem = null;
        }
    }


    public int clickedMenuBox(int x, int y) {
        for (int i = 0; i < rightClickMenu.size(); i++) {
            if (rightClickMenu.get(i).contains(x, y)) {
                return i;
            }
        }
        return -1;
    }

    public void equipItem() {
        Item temp = equippedItem;
        equippedItem = items[selectedItemR][selectedItemC];
        items[selectedItemR][selectedItemC] = temp;
    }

    public void unequipItem() {
        if (!isInventoryFull()) {
            addItem(equippedItem);
            equippedItem = null;
        }
    }

    public void changeDest() {
        switch (direction) {
            case (Player.RIGHT):
                goingToxTile = xTile + 1;
                goingToyTile = yTile;
                break;
            case (Player.UP):
                goingToxTile = xTile;
                goingToyTile = yTile - 1;
                break;
            case (Player.LEFT):
                goingToxTile = xTile - 1;
                goingToyTile = yTile;
                break;
            case (Player.DOWN):
                goingToxTile = xTile;
                goingToyTile = yTile + 1;
                break;
        }
    }

    public void updateSellAmount() {
        sellAmount = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if (selectedItems[i][j]) {
                    sellAmount += items[i][j].getSellCost();
                }
            }
        }
    }

    public void sellItems() {
        bells += sellAmount;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if (selectedItems[i][j]) {
                    items[i][j] = null;
                }
            }
        }

    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxTile() {
        return xTile;
    }

    public void setxTile(int xTile) {
        this.xTile = xTile;
    }

    public int getyTile() {
        return yTile;
    }

    public void setyTile(int yTile) {
        this.yTile = yTile;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isGoingToNewRoom() {
        return goingToNewRoom;
    }

    public void setGoingToNewRoom(Boolean b) {
        goingToNewRoom = b;
    }

    public boolean isExitingRoom() {
        return exitingRoom;
    }

    public void setExitingRoom(Boolean b) {
        exitingRoom = b;
    }

    public boolean isInventoryOpen() { return inventoryOpen; }

    public void setInventoryOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }
    
    public int getSelectedItemR(){
    	return selectedItemR;
    }
    
    public void setSelectedItemR(int n){
    	selectedItemR = n;
    }
    
    public int getSelectedItemC(){
    	return selectedItemC;
    }
    
    public void setSelectedItemC(int n){
    	selectedItemC = n;
    }

    public boolean isEscapeQueued() {
        return escapeQueued;
    }

    public void setEscapeQueued(boolean escapeQueued) {
        this.escapeQueued = escapeQueued;
    }

    public boolean isRightClickMenuOpen() {
        return rightClickMenuOpen;
    }

    public void setRightClickMenuOpen(boolean b) {
        rightClickMenuOpen = b;
    }

    public ArrayList<Rectangle> getRightClickMenu() {
        return rightClickMenu;
    }

    public Item getSelectedItem() {
        if (selectedItemC != -1 && selectedItemR != -1) {
            return items[selectedItemR][selectedItemC];
        }

        if (selectedEquipped) {
            return equippedItem;
        }
        return null;

    }

    public boolean isShopOpen() {
        return shopOpen;
    }

    public void setShopOpen(Boolean b) {
        this.shopOpen = b;
    }

    public boolean isSelectedEquipped() {
        return selectedEquipped;
    }

    public boolean isTalkingToNPC() {
        return talkingToNPC;
    }

    public void setTalkingToNPC(Boolean b) {
        talkingToNPC = b;
    }

    public int getVillagerPlayerIsTalkingTo() {
        return villagerPlayerIsTalkingTo;
    }

    public void setVillagerPlayerIsTalkingTo(int n) {
        villagerPlayerIsTalkingTo = n;
    }

    public int getGoingToxTile() {
        return goingToxTile;
    }

    public int getGoingToyTile() {
        return goingToyTile;
    }

    public boolean isDialogueSelectionOpen() {
        return dialogueSelectionOpen;
    }

    public void setDialogueSelectionOpen(boolean dialogueSelectionOpen) {
        this.dialogueSelectionOpen = dialogueSelectionOpen;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int dir) {
        direction = dir;
    }

    public boolean isSelectionMenuClicked() {
        return selectionMenuClicked;
    }

    public void setSelectionMenuClicked(boolean b) {
        selectionMenuClicked = b;
    }

    public boolean isSellShopOpen() {
        return sellShopOpen;
    }

    public void setSellShopOpen(Boolean b) {
        sellShopOpen = b;
    }

    public boolean[][] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(boolean[][] b) {
        this.selectedItems = b;
    }

    public int getSellAmount() {
        return sellAmount;
    }

    public void setSellAmount(int sellAmount) {
        this.sellAmount = sellAmount;
    }

    public Rectangle getSellRect() {
        return sellRect;
    }

    public Rectangle getCancelRect() {
        return cancelRect;
    }

    public int getBells() {
        return bells;
    }

    public void setBells(int n) {
        bells = n;
    }

    public int getSelectedItemInShop() {
        return selectedItemInShop;
    }

    public void setSelectedItemInShop(int n) {
        selectedItemInShop = n;
    }
}
