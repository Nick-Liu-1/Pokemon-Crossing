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

    private Rectangle rightClickMenu = null;

    private final Image inventoryImage = new ImageIcon("Assets/Misc/inventory.png").getImage();

    // How many bells player has
    private int bells = 69420;

    private GamePanel mainFrame;

    private int offsetX, offsetY;

    // Constructor
    public Player(int x, int y, int gender, int[][] grid, GamePanel mainFrame) {
        this.x = x;
        this.y = y;
        this.gender = gender;
        this.grid = grid;
        this.mainFrame = mainFrame;

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
            	if(j==0){
            		items[i][j] = new Item(0, new ImageIcon("Assets/Items/General/bells.png").getImage(), 0, 0);
            	}
                else{
                	items[i][j] = new Item(0, new ImageIcon("Assets/Items/General/shovel.png").getImage(), 0, 0);
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
        if (!moving) {
            running = keys[KeyEvent.VK_SHIFT];
            direction = dir;
            moving = inDir(dir) == 1;

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
                break;
            case (Player.UP):
                ans = grid[xTile][yTile-1];
                break;
            case (Player.LEFT):
                ans = grid[xTile-1][yTile];
                break;
            case (Player.DOWN):
                ans = grid[xTile][yTile+1];
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
                            g.drawImage(items[i][j].getImage(), 323 + i * 68, 54 + j * 68, null);
                        }
                        else if (i == selectedItemR && j == selectedItemC) {
                            g.drawImage(items[i][j].getImage(), mainFrame.getMouse().x - offsetX - 19, mainFrame.getMouse().y - offsetY - 18, null);
                        }
                    }
                }
            }
            //System.out.println(selectedItemR + " " + selectedItemC);
            if (selectedItemR!=-1 && selectedItemC!=-1){
            	g.setColor(new Color(0,255,0));
               	g.drawOval(323 + selectedItemR * 68, 54 + selectedItemC * 68, 38, 38);
               	if (rightClickMenuOpen) {
               	    rightClickMenu = new Rectangle(323 + selectedItemR * 68 + offsetX + 19, 54 + selectedItemC * 68 + offsetY + 18, 120, 40);
               	    g.setColor(Color.WHITE);
                    g.fillRect(323 + selectedItemR * 68 + offsetX + 19, 54 + selectedItemC * 68 + offsetY + 18, 120, 40);
                    g.setColor(Color.BLACK);
                    g.drawRect(323 + selectedItemR * 68 + offsetX + 19, 54 + selectedItemC * 68 + offsetY + 18, 120, 40);
                }

            }

            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;
                Font finkHeavy = null;

                try {
                    //create the font to use. Specify the size!
                    finkHeavy = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Misc/FinkHeavy.ttf")).deriveFont(30f);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    //register the font
                    ge.registerFont(finkHeavy);
                } catch (IOException | FontFormatException e) {
                    e.printStackTrace();
                }
               
                FontMetrics fontMetrics = new JLabel().getFontMetrics(finkHeavy);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(finkHeavy);
                g2.setColor(new Color(0,0,0));

                int x, y;  // x, y coordinates of text
                int width;  // width of text

                // Score text
                g2.drawString(String.valueOf(bells), 360 ,293);

                if (rightClickMenuOpen && selectedItemR!=-1 && selectedItemC!=-1) {
                    g2.drawString("Drop",323 + selectedItemR * 68 + offsetX + 19 + 10, 54 + selectedItemC * 68 + offsetY + 18 + 25);
                }
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
               		offsetX = (int) (mouse.getX() - (342 + i * 68));
               		offsetY = (int) (mouse.getY() - (72 + j * 68));
               		break;
               	}
               	noCollision++;
            }    
		}
		if (noCollision==18){
			selectedItemR = -1;
        	selectedItemC = -1;
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
	                	swapC=j;
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
        items[selectedItemR][selectedItemC] = null;
    }

    // Getters and setters
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

    public boolean isGoingToNewRoom() {
        return goingToNewRoom;
    }

    public boolean isExitingRoom() {
        return exitingRoom;
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

    public Rectangle getRightClickMenu() {
        return rightClickMenu;
    }

    public Item getSelectedItem() {
        if (selectedItemC != -1 && selectedItemR != -1) {
            return items[selectedItemR][selectedItemC];
        }
        return null;

    }
   
}
