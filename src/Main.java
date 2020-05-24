/*
    Main.java
    Nick Liu + Annie Zhang
    ICS4U
    Main class implementing the game
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class Main extends JFrame implements ActionListener {
    private javax.swing.Timer myTimer;  // Game Timer
    private GamePanel game;  // GamePanel for the actual game

    public Main() {
        // Creating frame
        super("Pokemon Crossing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1020,695);


        game = new GamePanel(this);
        add(game);
        myTimer = new javax.swing.Timer(10, new TickListener());

        setResizable(false);
        setVisible(true);
        start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public void start() {
        myTimer.start();
    }

    // Class that deals with actions within the game
    class TickListener implements ActionListener {
        public void actionPerformed(ActionEvent evt){
            /*
                Method is called every tick while the game is running. Calls the move and paint methods
                which deal with game logic.
            */
            if (game != null) {
                game.grabFocus();
                game.move();
                game.repaint();
            }
        }
    }

    public static void main(String args[]) {
        Main frame = new Main();
    }

}

class GamePanel extends JPanel implements KeyListener, MouseListener {
    private boolean[] keys;   // Array of keys that keeps track if they are down or not
    private Main mainFrame;   // Frame of the program
    private int count = 0;

    public static final int tileSize = 60;  // Dimension of the tile
    private Player player;
    private int mostRecentKeyPress = 0;  // Most recent movement key press (WASD)
    private Room curRoom;  // Room player is in
    private Room outside;  // The outside map room
    private int[][] grid;  // Current grid of the room the player is in
    private Hashtable<Point, Room> rooms = new Hashtable<>();  // Hashtable of all rooms

    private static ArrayList<Item> items = new ArrayList<>();  // ArrayList of all items

    private Point mouse;
    private boolean clicked = false;

    private boolean fadingToBlack = false;
    private int fadeTimeStart = 0;

    private Tom_Nook tom_nook;

    private ArrayList<NPC> NPCs = new ArrayList<>();

    private final Image speechBubbleImage = new ImageIcon("Assets/Misc/speech bubble copy.png").getImage();
    private Image selectionMenuImage = new ImageIcon("Assets/Misc/With click.png").getImage();
    private Image selectionMenuNoClickImage = new ImageIcon("Assets/Misc/No Click.png").getImage();
    private final Image shopImage = new ImageIcon("Assets/Misc/shop menu.png").getImage();

    private double selectionAngle = 0;
    private int selected = -1;

    private int dialogueDelay = 0;


    public GamePanel(Main m) {
        keys = new boolean[KeyEvent.KEY_LAST + 1];  // Key presses

        // Setting panel
        mainFrame = m;
        setSize(1020, 695);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);

        init();
    }

    // Requests focus of game panel
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }

    // Deals with all player movement
    public void move() {

        Point mousePos = MouseInfo.getPointerInfo().getLocation();  // Get mouse position
        Point offset = getLocationOnScreen();  // Get window position
        mouse = new Point (mousePos.x-offset.x, mousePos.y-offset.y);
        //System.out.println("(" + (mouse.x) + ", " + (mouse.y) + ")");
        //System.out.println(player.getxTile()+ " "+ player.getyTile());

        count++;

        if (player.isTalkingToNPC() && !player.isDialogueSelectionOpen()) {
            dialogueDelay++;
        }


        // Move player in different directions if WASD is pressed and the inventory is not open
        if (!player.isInventoryOpen() && !fadingToBlack) {
            if (keys[KeyEvent.VK_D] && KeyEvent.VK_D == mostRecentKeyPress) {
                player.move(Player.RIGHT, keys, grid);
            }

            if (keys[KeyEvent.VK_W] && KeyEvent.VK_W == mostRecentKeyPress) {
                player.move(Player.UP, keys, grid);
            }

            if (keys[KeyEvent.VK_A] && KeyEvent.VK_A == mostRecentKeyPress) {
                player.move(Player.LEFT, keys, grid);
            }

            if (keys[KeyEvent.VK_S] && KeyEvent.VK_S == mostRecentKeyPress) {
                player.move(Player.DOWN, keys, grid);
            }

            player.move();

            // Deal with going to new room
            if (player.isGoingToNewRoom() && !fadingToBlack) {
                fadingToBlack = true;
                fadeTimeStart = count;
            }

            // Deal with exiting room
            else if (player.isExitingRoom() && !fadingToBlack) {
                fadingToBlack = true;
                fadeTimeStart = count;
            }
        }

        for (NPC temp : NPCs) {
            if (curRoom == outside) {
                temp.move(grid, player.getX(), player.getY(), player.getGoingToxTile(), player.getGoingToyTile(), NPCs);
            }
        }

        if (player.isDialogueSelectionOpen() && Math.hypot(510 - mouse.x, 186 - mouse.y) <= 34 && clicked) {
            player.setSelectionMenuClicked(true);
        }
        else if (!clicked) {
            player.setSelectionMenuClicked(false);
        }

        if (player.isDialogueSelectionOpen()) {
            selectionAngle = ((Math.atan2((186 - mouse.y), (mouse.x - 510)) + 2*Math.PI) % (2*Math.PI));
        }

        if (player.isTalkingToNPC() && dialogueDelay > 300 && (player.getVillagerPlayerIsTalkingTo() != Player.TOM_NOOK && tom_nook.getSpeechStage() !=Tom_Nook.SHOP)) {
            player.setTalkingToNPC(false);
            dialogueDelay = 0;
        }
    }

    public void goToNewRoom() {
        curRoom.setGrid(grid);
        System.out.println(player.getxTile() + " " + player.getyTile());
        curRoom = rooms.get(new Point(player.getxTile(), player.getyTile()));  // Set curRoom to be new the new room
        grid = curRoom.getGrid();  // Set grid

        // Set player pos
        player.setxTile(curRoom.getExitX());
        player.setyTile(curRoom.getExitY());
        player.setX(curRoom.getExitX() * tileSize);
        player.setY(curRoom.getExitY() * tileSize);

        player.setGoingToNewRoom(false);
    }

    public void exitRoom() {
        curRoom.setGrid(grid);
        // Set player pos
        player.setxTile(curRoom.getEntryX());
        player.setyTile(curRoom.getEntryY());
        player.setX(curRoom.getEntryX() * tileSize);
        player.setY(curRoom.getEntryY() * tileSize);

        // Set curRoom and grid to be the outside
        curRoom = outside;
        grid = curRoom.getGrid();

        player.setExitingRoom(false);
    }

    public void init() {
        loadMap();
        curRoom = outside;
        grid = curRoom.getGrid();
        player = new Player("NAME",2460, 3300, Player.FEMALE, grid, this);
        loadItems();
        Player.load();
        NPC.loadDialogue();
        tom_nook = new Tom_Nook("Tom Nook", null, 11, 8, "mate", rooms.get(new Point(39, 55)),0,  player);
        tom_nook.generateStoreItems();
        createNPCs();
    }

    // Read from the map and room files and loads them
    public void loadMap() {
        int[][] mapGrid = new int[94][85];

        // Reading from the map grid
        try{
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/map.txt")));
            for (int i = 0; i < 85; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 94; j++) {
                    mapGrid[j][i] = Integer.parseInt(s[j]);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading main map");
        }
        // Creating the outside room
        outside = new Room(mapGrid, new ImageIcon("Assets/Map/PC Map.png").getImage(), 0, 0, 0 ,0, 0, 0);

        // Reading from the room file
        try{
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Rooms/rooms.txt")));
            int n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                String file = stdin.nextLine();

                int entryX, entryY, exitX, exitY, exitX2, exitY2;
                String[] line = stdin.nextLine().split(" ");
                entryX = Integer.parseInt(line[0]);
                entryY = Integer.parseInt(line[1]);
                exitX = Integer.parseInt(line[2]);
                exitY = Integer.parseInt(line[3]);
                exitX2 = Integer.parseInt(line[4]);
                exitY2 = Integer.parseInt(line[5]);

                int len, wid;
                line = stdin.nextLine().split(" ");
                wid = Integer.parseInt(line[0]);
                len = Integer.parseInt(line[1]);
                int[][] grid = new int[wid][len];
                for (int j = 0; j < len; j++) {
                    line = stdin.nextLine().split(" ");
                    for (int k = 0; k < wid; k++) {
                        grid[k][j] = Integer.parseInt(line[k]);
                    }
                }

                stdin.nextLine();

                rooms.put(new Point(entryX, entryY), new Room(grid, new ImageIcon("Assets/Rooms/"+file).getImage(), entryX, entryY, exitX, exitY, exitX2, exitY2));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading rooms");
        }

    }

    // Reading from the items file and loading it to the ArrayList
    public void loadItems() {
        Hashtable<String, int[]> itemInfo = new Hashtable<>();  // Temporary info

        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Items/Items.txt")));
            int n = Integer.parseInt(stdin.nextLine());
            String[] line;
            String fileName;
            for (int i = 0 ; i < n; i++) {
                line = stdin.nextLine().split(" ");
                fileName = "";
                for (int j = 1; j < line.length-2; j++) {
                    fileName += line[j] + " ";
                }
                fileName = fileName.substring(0, fileName.length()-1);
                itemInfo.put(fileName, new int[] {Integer.parseInt(line[0]), Integer.parseInt(line[line.length-2]), Integer.parseInt(line[line.length-1])});
            }

            for (int i = 0; i < n; i++) {
                items.add(null);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Item info not found");
        }

        File folder = new File("Assets/Items");
        ArrayList<String> absolutePaths = new ArrayList<>();
        search(".*\\.png", folder, absolutePaths);

        String name;
        String[] splitFile;
        int[] info;
        for (String file : absolutePaths) {
            splitFile = file.split("\\\\");
            name = splitFile[splitFile.length-1];
            info = itemInfo.get(name);
            name = name.substring(0, name.length()-4);
            //System.out.println(name);
            items.set(info[0], new Item(info[0], capitalizeWord(name), new ImageIcon(file).getImage(), info[1], info[2]));
        }
    }

    public void search(String pattern, File folder, ArrayList<String> result) {
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }
        }
    }

    public void createNPCs() {
        File folder = new File("Assets/NPCs/Annie");
        File[] listOfFiles = folder.listFiles();
        Hashtable<String, Image> annieImages = new Hashtable<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                annieImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/NPCs/Annie/"+listOfFiles[i].getName()).getImage());
            }
        }
        NPCs.add(new NPC("Annie", annieImages, 45, 50, "my guy", outside, Player.ANNIE));

        folder = new File("Assets/NPCs/Bob the Builder");
        listOfFiles = folder.listFiles();
        Hashtable<String, Image> bobImages = new Hashtable<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                bobImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/NPCs/Bob the Builder/"+listOfFiles[i].getName()).getImage());
            }
        }

        NPCs.add(new NPC("Bob the Builder", bobImages, 45, 51, "pthhpth", outside, Player.BOB_THE_BUILDER));


        folder = new File("Assets/NPCs/Nick");
        listOfFiles = folder.listFiles();
        Hashtable<String, Image> nickImages = new Hashtable<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                nickImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/NPCs/Nick/"+listOfFiles[i].getName()).getImage());
            }
        }

        NPCs.add(new NPC("Nick", nickImages, 45, 52, "kid", outside, Player.NICK));
    }


    public boolean isAdjacentToPlayer(int xTile, int yTile) {
        return (xTile == player.getxTile() && yTile == player.getyTile() - 1) || (xTile == player.getxTile() && yTile == player.getyTile() + 1) ||
            (xTile == player.getxTile() - 1 && yTile == player.getyTile()) || (xTile == player.getxTile() + 1 && yTile == player.getyTile());
    }

    public NPC npcAtPoint(int xTile, int yTile) {
        for (NPC temp : NPCs) {
            if (curRoom == temp.getRoom() && (xTile == temp.getxTile() && yTile == temp.getyTile()) || (xTile == temp.getGoingToxTile() && yTile == temp.getGoingToyTile())) {
                return temp;
            }
        }
        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!keys[e.getKeyCode()] && (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_W ||
            e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_S)) {
            mostRecentKeyPress = e.getKeyCode();
        }
        keys[e.getKeyCode()] = true;  // Set key in key array to be down

        if (keys[KeyEvent.VK_ESCAPE]) {
            if (!player.isShopOpen() && !player.isTalkingToNPC()) {
                if (!player.isInventoryOpen()) {
                    player.setEscapeQueued(true);
                }
                else {
                    player.setInventoryOpen(false);
                }

                player.setSelectedItemR(-1);
                player.setSelectedItemC(-1);
            }
            else if (player.isShopOpen()) {
                player.setShopOpen(false);
            }

        }

        player.setRightClickMenuOpen(false);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }


    @Override
    public void mouseClicked(MouseEvent e) {
		
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicked = true;
            if (player.isInventoryOpen()){
                if (player.isRightClickMenuOpen()) {
                    if (player.clickedMenuBox(mouse.x, mouse.y) == 0 && grid[player.getxTile()][player.getyTile()] != 4) {
                        curRoom.addDroppedItem(new DroppedItem(player.getSelectedItem(), player.getxTile(), player.getyTile()));
                        grid[player.getxTile()][player.getyTile()] = 4;
                        player.dropSelectedItem();
                    }
                    else if (player.clickedMenuBox(mouse.x, mouse.y) == 1) {
                        if (!player.isSelectedEquipped()) {
                            player.equipItem();
                        }
                        else {
                            player.unequipItem();
                        }

                    }
                }

                player.selectItem(mouse);
            }

            else if (player.isSellShopOpen()) {
                player.selectSellItem(mouse);
            }
        }

        player.setRightClickMenuOpen(false);

        if (e.getButton() == MouseEvent.BUTTON3) {
            if (player.isInventoryOpen()) {
                player.selectItem(mouse);
                if (player.getSelectedItem() != null) {
                    player.setRightClickMenuOpen(true);
                }
            }
            else if (!player.isTalkingToNPC()) {
                int xTile = (int) ((mouse.getX() + player.getX() - 480) / 60);
                int yTile = (int) ((mouse.getY() + player.getY() - 300) / 60);

                NPC npc = npcAtPoint(xTile, yTile);
                if (npc != null && isAdjacentToPlayer(npc.getxTile(), npc.getyTile())) {
                    player.setTalkingToNPC(true);
                    player.setDialogueSelectionOpen(true);
                    player.setVillagerPlayerIsTalkingTo(npc.getId());
                    if (!npc.isMoving()) {
                        npc.setTalking(true);
                        npc.setSpeechStage(NPC.GREETING);
                    }
                    else {
                        npc.setStopQueued(true);
                    }

                    int playerDir;

                    if (npc.getxTile() == player.getxTile() + 1) {
                        playerDir = Player.RIGHT;
                    }
                    else if (npc.getyTile() == player.getyTile() - 1) {
                        playerDir = Player.UP;
                    }
                    else if (npc.getxTile() == player.getxTile() - 1) {
                        playerDir = Player.LEFT;
                    }
                    else {
                        playerDir = Player.DOWN;
                    }

                    player.setDirection(playerDir);
                    npc.setDirection((playerDir + 2) % 4);

                }


                if (curRoom == tom_nook.getRoom() && (xTile == tom_nook.getxTile() && (yTile == tom_nook.getyTile() || yTile == tom_nook.getyTile() + 1))) {
                    player.setTalkingToNPC(true);
                    player.setDialogueSelectionOpen(true);
                    player.setVillagerPlayerIsTalkingTo(Player.TOM_NOOK);
                }


                if (Math.hypot(xTile*tileSize + 30 - (mouse.getX() + player.getX() - 480), yTile*tileSize + 30 - (mouse.getY() + player.getY() - 300)) < 19) {
                    DroppedItem droppedItem = curRoom.getDroppedItems().get(new Point(xTile, yTile));

                    if (grid[xTile][yTile] == 4 && droppedItem != null && !player.isInventoryFull() && isAdjacentToPlayer(xTile, yTile)) {
                        player.addItem(new Item(droppedItem.getId(), droppedItem.getName(), droppedItem.getImage(), droppedItem.getBuyCost(), droppedItem.getSellCost()));
                        Hashtable<Point, DroppedItem> temp = curRoom.getDroppedItems();
                        temp.remove(new Point(xTile, yTile));
                        curRoom.setDroppedItems(temp);
                        grid[xTile][yTile] = curRoom.getOriginalGrid()[xTile][yTile];
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicked = false;
            if (player.isInventoryOpen()) {
                player.moveItem(mouse);
            }

            if (player.isDialogueSelectionOpen() && Math.hypot(510 - mouse.x, 186 - mouse.y) > 34) {
                player.setSelectionMenuClicked(false);
                selectDialogue();
            }
        }


    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void fadingToBlack(boolean isEntering, Graphics g) {
        //System.out.println(count - fadeTimeStart);
        if (count - fadeTimeStart == 100) {
            if (isEntering) {
                goToNewRoom();
            }
            else {
                exitRoom();
            }
            fadingToBlack = false;
            return;
        }
        int alpha = Math.min((int) ((double) (count - fadeTimeStart + 20) / 100 * 255), 255);
        g.setColor(new Color(0, 0, 0, alpha));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void paintComponent(Graphics g) {
        g.drawImage(curRoom.getImage(), 480 - player.getX(), 303 - player.getY(), null);  // Drawing room
        g.setColor(new Color(255, 255, 255));
        // Drawing grids
        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(0, 0, 0));
        for (int i = 0; i < 1020; i+=tileSize) {
            g.drawLine(i, 0, i, 660);
        }

        for (int i = 0; i < 660; i+=tileSize) {
            g.drawLine(0, i, 1020, i);
        }

        //System.out.println((player.getxTile()+1) + " " + (player.getyTile()+1));
        /*g.setColor(new Color(255,0,0));
        for (int i = Math.max(0, player.getxTile()-8); i <= Math.min(94, player.getxTile() + 8); i++) {
            for (int j = Math.max(0, player.getyTile()-5); j < Math.min(85, player.getyTile() + 5); j++) {
                int a = i - Math.max(0, player.getxTile()-8);
                int b = j - Math.max(0, player.getyTile()-5);

                if (grid[i][j] == 0) {
                    g.drawLine(a*60, b*60, a*60 + 60, b*60 + 60);
                    g.drawLine(a*60, b*60+60, a*60+60, b*60);
                }
            }
        }*/

        for (Map.Entry<Point, DroppedItem> pair : curRoom.getDroppedItems().entrySet()) {
            DroppedItem item = pair.getValue();
            g.drawImage(item.getImage(), (item.getxTile()+8)*tileSize - player.getX()+13, (item.getyTile()+5)*tileSize - player.getY()+13, null);
        }


        if (curRoom == tom_nook.getRoom()) {
            tom_nook.draw(g, player.getX(), player.getY());
        }

        for (NPC temp : NPCs) {
            if (temp.getRoom() == curRoom) {
                temp.draw(g, player.getX(), player.getY());

            }
        }

        player.draw(g);

        if (fadingToBlack) {
            fadingToBlack(player.isGoingToNewRoom(), g);
        }

        if (player.isTalkingToNPC()) {
            NPC npc = null;

            if (player.getVillagerPlayerIsTalkingTo() >= 3) {
                npc = NPCs.get(player.getVillagerPlayerIsTalkingTo() - 3);
            }
            else if (player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK) {
                npc = tom_nook;
            }

            assert npc != null;
            if (!(npc == tom_nook && npc.getSpeechStage() == Tom_Nook.SHOP)) {
                g.drawImage(speechBubbleImage, (1020 - 700) / 2, 350, null);
            }


            if (player.isDialogueSelectionOpen()) {
                if (player.isSelectionMenuClicked()) {
                    g.drawImage(selectionMenuNoClickImage, 421, 120, null);
                }
                else {
                    g.drawImage(selectionMenuImage, 421, 120, null);
                }
            }

            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;
                Font finkHeavy = null;

                try {
                    //create the font to use. Specify the size!
                    finkHeavy = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Misc/FinkHeavy.ttf")).deriveFont(32f);
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    //register the font
                    ge.registerFont(finkHeavy);
                } catch (IOException | FontFormatException e) {
                    e.printStackTrace();
                }

                FontMetrics fontMetrics = new JLabel().getFontMetrics(finkHeavy);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(finkHeavy);
                g2.setColor(Color.BLACK);

                int x, y;  // x, y coordinates of text
                int width;  // width of text

                width = fontMetrics.stringWidth(npc.getName());

                x = 204 + (416 - 204 - width) / 2;
                y = 397;


                if (!(npc == tom_nook && (npc.getSpeechStage() == Tom_Nook.SHOP || npc.getSpeechStage() == Tom_Nook.SELL_SHOP))) {
                    g2.drawString(npc.getName(), x, y);
                }


                if (npc.getSpeechStage() == NPC.GREETING) {
                    if (npc.getCurrentGreeting().equals("")) {
                        npc.generateGreeting(player.getName());
                    }

                    g2.setColor(new Color(240, 240, 240));

                    ArrayList<String> dialogue = wordWrap(npc.getCurrentGreeting(), 450);

                    for (int i = 0; i < dialogue.size(); i++) {
                        g2.drawString(dialogue.get(i), 270, 470 + 40 * i);
                    }
                }

                else if (npc.getSpeechStage() == NPC.CHAT) {
                    if (npc.getCurrentChat().equals("")) {
                        npc.generateChat(player.getName());
                    }


                    g2.setColor(new Color(240, 240, 240));

                    ArrayList<String> dialogue = wordWrap(npc.getCurrentChat(), 450);

                    for (int i = 0; i < dialogue.size(); i++) {
                        g2.drawString(dialogue.get(i), 270, 470 + 40 * i);
                    }
                }

                else if (npc.getSpeechStage() == NPC.GOODBYE) {
                    if (npc.getCurrentGoodbye().equals("")) {
                        npc.generateGoodbye(player.getName());
                    }

                    g2.setColor(new Color(240, 240, 240));

                    ArrayList<String> dialogue = wordWrap(npc.getCurrentGoodbye(), 450);

                    for (int i = 0; i < dialogue.size(); i++) {
                        g2.drawString(dialogue.get(i), 270, 470 + 40 * i);
                    }
                }


                g2.setColor(Color.BLACK);
                if (player.isDialogueSelectionOpen()) {
                    if (npc.getPlayerOptions().size() == 2) {
                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(0));
                        g2.drawString(npc.getPlayerOptions().get(0), (1020 - width) / 2, 140);

                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(1));
                        g2.drawString(npc.getPlayerOptions().get(1), (1020 - width) / 2, 250);
                    }

                    else if (npc.getPlayerOptions().size() == 3) {
                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(0));
                        g2.drawString(npc.getPlayerOptions().get(0), (1020 - width) / 2, 140);

                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(1));
                        g2.drawString(npc.getPlayerOptions().get(1), 570, 195);


                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(2));
                        g2.drawString(npc.getPlayerOptions().get(2), (1020 - width) / 2, 250);
                    }

                    else if (npc.getPlayerOptions().size() == 4) {
                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(0));
                        g2.drawString(npc.getPlayerOptions().get(0), (1020 - width) / 2, 140);

                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(1));
                        g2.drawString(npc.getPlayerOptions().get(1), 570, 195);

                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(2));
                        g2.drawString(npc.getPlayerOptions().get(2), (1020 - width) / 2, 250);

                        width = fontMetrics.stringWidth(npc.getPlayerOptions().get(3));
                        g2.drawString(npc.getPlayerOptions().get(3), 450 - width, 195);
                    }
                }
            }
        }
        if (player.isTalkingToNPC() && player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK) {
            if (player.isShopOpen()) {
                g.drawImage(shopImage, (1020 - 825) / 2, (695 - 587) / 2, null);

                for (int i = 0; i < tom_nook.getStoreItems().size(); i++) {
                    if (tom_nook.getStoreItems().get(i).isFurniture()) {
                        g.drawImage(Item.storeLeafImage, 200,200 + 50*i, null);
                    }
                    else {
                        g.drawImage(tom_nook.getStoreItemImages()[tom_nook.getStoreItems().get(i).getId()], 200,200 + 50*i, null);
                    }
                }
            }
        }
    }

    public static int[][] transpose(int[][] arr) {
        /*  Takes an axb array and transposes it to a bxa array such that arr[a][b] == out[b][a]
         */

        int[][] out = new int[arr[0].length][arr.length];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                out[j][i] = arr[i][j];
            }
        }
        return out;
    }

    public static ArrayList<String> wordWrap(String str, int width) {
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

        ArrayList<String> ans = new ArrayList<>();
        String[] wordsArray = str.split(" ");
        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(wordsArray));
        Collections.reverse(words);

        String word = "";
        String string;

        while (words.size() > 0) {
            string = "";
            while (words.size() > 0) {
                if (word.equals("")) {
                    word = words.get(words.size() - 1);
                    words.remove(words.size() - 1);
                }

                if (fontMetrics.stringWidth(string) + fontMetrics.stringWidth(" " + word) <= width) {
                    string += " " + word;
                    word = "";
                }
                else {
                    break;
                }
            }
            ans.add(string.substring(1));
        }

        return ans;
    }

    public void selectDialogue() {
        NPC npc;
        player.setDialogueSelectionOpen(false);
        dialogueDelay = 0;

        if (player.getVillagerPlayerIsTalkingTo() >= 3) {
            npc = NPCs.get(player.getVillagerPlayerIsTalkingTo() - 3);

            if (npc.getPlayerOptions().size() == 2) {
                if (selectionAngle >= 0 && selectionAngle <= Math.PI) {
                    if (npc.getSpeechStage() == NPC.GREETING) {
                        npc.setSpeechStage(NPC.CHAT);
                    }
                }
                else {
                    npc.setSpeechStage(NPC.GOODBYE);
                }
            }
        }

        if (player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK) {
            if (selectionAngle > (7.0/4.0)*Math.PI || selectionAngle <= Math.PI/4) {
                player.setDialogueSelectionOpen(false);
                player.setSellShopOpen(true);
                player.setSellAmount(0);
                player.setSelectedItems(new boolean[6][3]);
                tom_nook.setSpeechStage(Tom_Nook.SELL_SHOP);

            }
            else if (selectionAngle > Math.PI/4 && selectionAngle <= 3.0/4.0 * Math.PI) {
                player.setDialogueSelectionOpen(false);
                player.setShopOpen(true);
                tom_nook.setSpeechStage(Tom_Nook.SHOP);
            }
            else if (selectionAngle > (3.0/4.0) * Math.PI && selectionAngle <= 5.0/4.0 * Math.PI) {

            }
            else {
                tom_nook.setSpeechStage(NPC.GOODBYE);
            }
        }

    }

    public Point getMouse() {
        return mouse;
    }

    public boolean isClicked() {
        return clicked;
    }

    public static ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<NPC> getNPCs() {
        return NPCs;
    }

    public static boolean contains(int n, int[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }

    public static String capitalizeWord(String str){
        String words[]=str.split("\\s");
        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    public static int randint(int low, int high){
        /*
            Returns a random integer on the interval [low, high].
        */
        return (int) (Math.random()*(high-low+1)+low);
    }
}