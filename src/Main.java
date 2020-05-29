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

    private Room minigameIsland;

    private static ArrayList<Item> items = new ArrayList<>();  // ArrayList of all items

    private Point mouse = new Point(0, 0);
    private boolean clicked = false;

    private boolean fadingToBlack = false;
    private int fadeTimeStart = 0;

    private Tom_Nook tom_nook;
    private Boat_Operator boat_operator;
    private Boat_Operator boat_operator_on_island;
    private Celeste celeste;
    private Isabelle isabelle;

    private ArrayList<NPC> NPCs = new ArrayList<>();

    private final Image speechBubbleImage = new ImageIcon("Assets/Misc/speech bubble copy.png").getImage();
    private final Image selectionMenuImage = new ImageIcon("Assets/Misc/With click.png").getImage();
    private final Image selectionMenuNoClickImage = new ImageIcon("Assets/Misc/No Click.png").getImage();
    private final Image shopImage = new ImageIcon("Assets/Misc/shop menu.png").getImage();
    private final Image rightClickImage = new ImageIcon("Assets/Misc/right click.png").getImage();
    private final Image museumMenuImage = new ImageIcon("Assets/Misc/Museum Menu.png").getImage();
    private final Image exitButtonImage = new ImageIcon("Assets/Misc/exit button.png").getImage();
    private final Image museumBugsImage = new ImageIcon("Assets/Misc/bugs.png").getImage();
    private final Image museumFishImage = new ImageIcon("Assets/Misc/fish.png").getImage();
    private final Image museumFossilImage = new ImageIcon("Assets/Misc/fossils.png").getImage();

    private int[][] diggableTiles = new int[94][85];
    private int[][] minigameIslandDiggable = new int[49][46];

    private int[][] waterTiles = new int[94][85];
    private int[][] minigameIslandWater = new int[49][46];

    private ArrayList<Tree> trees = new ArrayList<>();

    private double selectionAngle = 0;
    private int selected = -1;

    private int dialogueDelay = 0;
    public static Font finkheavy15 = null;
    public static Font finkheavy30 = null;
    public static Font finkheavy32 = null;
    public static Font finkheavy36 = null;


    public GamePanel(Main m) {
        keys = new boolean[KeyEvent.KEY_LAST + 1];  // Key presses

        // Setting panel
        mainFrame = m;
        setSize(1020, 695);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);
        GamePanel.loadFonts();
        init();

    }

    // Requests focus of game panel
    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }

    public void init() {
        loadMap();
        curRoom = outside;
        grid = curRoom.getGrid();
        player = new Player("NAME",3840, 2880, Player.FEMALE, grid, this);
        loadItems();
        Player.load();
        NPC.loadDialogue();

        for (int i = 0; i < 8; i++) {
            NPCs.add(null);
        }

        tom_nook = new Tom_Nook("Tom Nook", null, 11, 8, "mate", rooms.get(new Point(39, 55)),0);
        tom_nook.generateStoreItems();
        NPCs.set(0, tom_nook);

        boat_operator = new Boat_Operator("Boat Operator", null,30, 75, "dude", outside, 6);
        NPCs.set(6, boat_operator);

        boat_operator_on_island = new Boat_Operator("Boat Operator", null,22, 36, "dude", minigameIsland, 7);
        NPCs.set(7, boat_operator_on_island);

        celeste = new Celeste("Celeste", null, 16, 11, "my guy", rooms.get(new Point(64, 48)),2);
        NPCs.set(2, celeste);

        isabelle = new Isabelle("Isabelle", null, 15, 8, "my guy", rooms.get(new Point(63, 35)), 1);
        NPCs.set(1, isabelle);

        createNPCs();
        Tree.loadFruits();

        for (int i = 0; i < 20; i++) {
            celeste.addBug(items.get(randint(7, 37)));
            celeste.addFish(items.get(randint(38, 108)));
            celeste.addFossil(new Item(2, Item.fossilNames.get(randint(0, 66)), new ImageIcon("Assets/Items/General/Fossil.png").getImage(), 0, 100));
        }

    }

    public static void loadFonts() {
        try {
            finkheavy15 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Misc/FinkHeavy.ttf")).deriveFont(15f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(finkheavy15);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        try {
            finkheavy30 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Misc/FinkHeavy.ttf")).deriveFont(30f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(finkheavy30);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        try {
            finkheavy32 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Misc/FinkHeavy.ttf")).deriveFont(32f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(finkheavy32);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        try {
            //create the font to use. Specify the size!
            finkheavy36 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/Misc/FinkHeavy.ttf")).deriveFont(36f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(finkheavy36);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
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

        int[][] minigameIslandMapGrid = new int[49][46];

        // Reading from the map grid
        try{
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/minigame island map.txt")));
            for (int i = 0; i < 46; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 49; j++) {
                    minigameIslandMapGrid[j][i] = Integer.parseInt(s[j]);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading minigame island map");
        }

        minigameIsland = new Room(minigameIslandMapGrid, new ImageIcon("Assets/Map/Minigame Island.png").getImage(), 31, 75, 23, 36,0, 0);
        rooms.put(new Point(31, 75), minigameIsland);

        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/outside diggable tiles.txt")));
            for (int i = 0; i < 85; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 94; j++) {
                    diggableTiles[j][i] = Integer.parseInt(s[j]);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading diggable tiles map");
        }

        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/Main Island Water.txt")));
            for (int i = 0; i < 85; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 94; j++) {
                    waterTiles[j][i] = Integer.parseInt(s[j]);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading water tiles");
        }


        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/minigame island diggable tiles.txt")));
            for (int i = 0; i < 46; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 49; j++) {
                    minigameIslandDiggable[j][i] = Integer.parseInt(s[j]);
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading minigame island diggable tiles map");
        }

        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/minigame island water.txt")));
            for (int i = 0; i < 46; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 49; j++) {
                    minigameIslandWater[j][i] = Integer.parseInt(s[j]);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("error loading minigame island water");
        }

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

        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/trees.txt")));
            int n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                String[] line = stdin.nextLine().split(" ");
                trees.add(new Tree(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), outside, Integer.parseInt(line[4])));
            }
            n = Integer.parseInt(stdin.nextLine());
            for (int i = 0; i < n; i++) {
                String[] line = stdin.nextLine().split(" ");
                trees.add(new Tree(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3]), minigameIsland, Integer.parseInt(line[4])));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("error loading trees");
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
            //System.out.println(name + " " + Arrays.toString(info));
            items.set(info[0], new Item(info[0], capitalizeWord(name), new ImageIcon(file).getImage(), info[1], info[2]));
        }

        Item.loadFossils();
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
        NPCs.set(3, new NPC("Annie", annieImages, 45, 50, "my guy", outside, Player.ANNIE));

        folder = new File("Assets/NPCs/Bob the Builder");
        listOfFiles = folder.listFiles();
        Hashtable<String, Image> bobImages = new Hashtable<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                bobImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/NPCs/Bob the Builder/"+listOfFiles[i].getName()).getImage());
            }
        }

        NPCs.set(4, new NPC("Bob the Builder", bobImages, 45, 51, "pthhpth", outside, Player.BOB_THE_BUILDER));


        folder = new File("Assets/NPCs/Nick");
        listOfFiles = folder.listFiles();
        Hashtable<String, Image> nickImages = new Hashtable<>();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                nickImages.put(listOfFiles[i].getName().substring(0, listOfFiles[i].getName().length() - 4),
                    new ImageIcon("Assets/NPCs/Nick/"+listOfFiles[i].getName()).getImage());
            }
        }

        NPCs.set(5, new NPC("Nick", nickImages, 45, 52, "kid", outside, Player.NICK));
    }

    // Deals with all player movement
    public void move() {
        //System.out.println(celeste.getSpeechStage() + " " + player.isTalkingToNPC());
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
        if (!player.isInventoryOpen() && !fadingToBlack && !player.isActionProgressOpen()) {
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

        if (player.isTalkingToNPC() && dialogueDelay >= 300) {
            if (!(player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK && tom_nook.getSpeechStage() != Tom_Nook.GOODBYE) &&
            !(player.getVillagerPlayerIsTalkingTo() == Player.CELESTE && celeste.getSpeechStage() != Celeste.GOODBYE)) {
                player.setTalkingToNPC(false);
                dialogueDelay = 0;
                NPCs.get(player.getVillagerPlayerIsTalkingTo()).setTalking(false);
            }
        }

        if (player.isActionProgressOpen() && player.getActionProgress() >= 160) {
            player.setActionProgress(0);
            player.setActionProgressOpen(false);
        }
    }

    public void goToNewRoom() {
        curRoom.setGrid(grid);
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
        if (curRoom.getEntryX() == 24) {
            curRoom = minigameIsland;
        }
        else {
            curRoom = outside;
        }

        grid = curRoom.getGrid();

        player.setExitingRoom(false);
    }

    public boolean isAdjacentToPlayer(int xTile, int yTile) {
        return (xTile == player.getxTile() && yTile == player.getyTile() - 1) || (xTile == player.getxTile() && yTile == player.getyTile() + 1) ||
            (xTile == player.getxTile() - 1 && yTile == player.getyTile()) || (xTile == player.getxTile() + 1 && yTile == player.getyTile());
    }

    public NPC npcAtPoint(int xTile, int yTile) {
        for (NPC temp : NPCs) {
            if ((curRoom == temp.getRoom()) && ((xTile == temp.getxTile() && yTile == temp.getyTile()) || (xTile == temp.getGoingToxTile() && yTile == temp.getGoingToyTile()))) {
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
            if (!player.isShopOpen() && !player.isTalkingToNPC() && !player.isActionProgressOpen()) {
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
            else if (player.isShopOpen()) {
                for (int i = 0; i < 5; i++) {
                    if (tom_nook.getItemRects().get(i).contains(mouse)) {
                        player.setSelectedItemInShop(i);
                    }
                }

                if (tom_nook.getBuyRect().contains(mouse)) {
                    Item item = tom_nook.getStoreItems().get(player.getSelectedItemInShop());
                    if (player.getBells() >= item.getBuyCost()) {
                        player.addItem(item);
                        player.setBells(player.getBells() - item.getBuyCost());
                        tom_nook.getStoreItems().remove(item);
                    }

                    player.setShopOpen(false);
                    player.setDialogueSelectionOpen(true);
                    tom_nook.setSpeechStage(NPC.GREETING);
                }

                else if (tom_nook.getCancelRect().contains(mouse)) {
                    player.setShopOpen(false);
                    player.setDialogueSelectionOpen(true);
                    tom_nook.setSpeechStage(NPC.GREETING);
                }
            }

            else if (player.isSellShopOpen()) {
                player.selectSellItem(mouse);
                if (player.getSellRect().contains(mouse)) {
                    player.sellItems();
                    player.setSellShopOpen(false);
                    player.setDialogueSelectionOpen(true);
                    tom_nook.setSpeechStage(NPC.GREETING);
                }
                else if (player.getCancelRect().contains(mouse)) {
                    player.setSellShopOpen(false);
                    player.setDialogueSelectionOpen(true);
                    tom_nook.setSpeechStage(NPC.GREETING);
                }
            }

            else if (player.isMuseumOpen()) {
                Rectangle upRect = new Rectangle(483, 223, 62, 20);
                Rectangle downRect = new Rectangle(483, 512, 62, 20);

                if (Math.hypot(950 - mouse.x, 140 - mouse.y) < 20) {
                    player.setMuseumOpen(false);
                    player.setDialogueSelectionOpen(true);
                    celeste.setSpeechStage(NPC.GREETING);
                }
                else if (celeste.getBugRect().contains(mouse)) {
                    celeste.setPage(Celeste.BUG_PAGE);
                }
                else if (celeste.getFishRect().contains(mouse)) {
                    celeste.setPage(Celeste.FISH_PAGE);
                }
                else if (celeste.getFossilRect().contains(mouse)) {
                    celeste.setPage(Celeste.FOSSIL_PAGE);
                }
                else if (upRect.contains(mouse)) {
                    if (celeste.getPage() == Celeste.BUG_PAGE) {
                        if (celeste.getBugStart() - 5 >= 0) {
                            celeste.setBugStart(celeste.getBugStart() - 5);
                        }
                    }
                    else if (celeste.getPage() == Celeste.FISH_PAGE) {
                        if (celeste.getFishStart() - 5 >= 0) {
                            celeste.setFishStart(celeste.getFishStart() - 5);
                        }
                    }
                    else {
                        if (celeste.getFossilStart() - 5 >= 0) {
                            celeste.setFossilStart(celeste.getFossilStart() - 5);
                        }
                    }
                }
                else if (downRect.contains(mouse)) {
                    if (celeste.getPage() == Celeste.BUG_PAGE) {
                        if (celeste.getBugStart() + 5 <= celeste.getBugs().size() - 1) {
                            celeste.setBugStart(celeste.getBugStart() + 5);
                        }
                    }
                    else if (celeste.getPage() == Celeste.FISH_PAGE) {
                        if (celeste.getFishStart() + 5 <= celeste.getFish().size() - 1) {
                            celeste.setFishStart(celeste.getFishStart() + 5);
                        }
                    }
                    else {
                        if (celeste.getFossilStart() + 5 <= celeste.getBugs().size() - 1) {
                            celeste.setFossilStart(celeste.getFossilStart() + 5);
                        }
                    }
                }
            }
            else if (player.isDonateMenuOpen()) {
                player.selectDonateItem(mouse);
                if (player.getSellRect().contains(mouse)) {
                    donateItems();
                    player.setDonateMenuOpen(false);
                    player.setDialogueSelectionOpen(true);
                    celeste.setSpeechStage(NPC.GREETING);
                }
                else if (player.getCancelRect().contains(mouse)) {
                    player.setDonateMenuOpen(false);
                    player.setDialogueSelectionOpen(true);
                    celeste.setSpeechStage(NPC.GREETING);
                }
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
            else if (!player.isTalkingToNPC() && !player.isActionProgressOpen()) {
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
                    tom_nook.setSpeechStage(NPC.GREETING);
                    tom_nook.resetDialogue();
                }

                else if (curRoom == boat_operator.getRoom() && (xTile == boat_operator.getxTile()) && (yTile == boat_operator.getyTile()) && isAdjacentToPlayer(xTile, yTile)) {
                    player.setTalkingToNPC(true);
                    player.setDialogueSelectionOpen(true);
                    player.setVillagerPlayerIsTalkingTo(Player.BOAT_OPERATOR);
                    boat_operator.setSpeechStage(NPC.GREETING);
                    boat_operator.resetDialogue();
                }

                else if (curRoom == boat_operator_on_island.getRoom() && (xTile == boat_operator_on_island.getxTile()) && (yTile == boat_operator_on_island.getyTile()) && isAdjacentToPlayer(xTile, yTile)) {
                    player.setTalkingToNPC(true);
                    player.setDialogueSelectionOpen(true);
                    player.setVillagerPlayerIsTalkingTo(Player.BOAT_OPERATOR_ON_ISLAND);
                    boat_operator_on_island.setSpeechStage(NPC.GREETING);
                    boat_operator_on_island.resetDialogue();
                }

                else if (curRoom == celeste.getRoom() && (xTile == celeste.getxTile()) && (yTile == celeste.getyTile())) {
                    player.setTalkingToNPC(true);
                    player.setDialogueSelectionOpen(true);
                    player.setVillagerPlayerIsTalkingTo(Player.CELESTE);
                    celeste.setSpeechStage(NPC.GREETING);
                    celeste.resetDialogue();
                }

                else if (curRoom == isabelle.getRoom() && (xTile == isabelle.getxTile()) && (yTile == isabelle.getyTile())) {
                    player.setTalkingToNPC(true);
                    player.setDialogueSelectionOpen(true);
                    player.setVillagerPlayerIsTalkingTo(Player.ISABELLE);
                    isabelle.setSpeechStage(NPC.GREETING);
                    isabelle.resetDialogue();
                }

                else if (Math.hypot(xTile*tileSize + 30 - (mouse.getX() + player.getX() - 480), yTile*tileSize + 30 - (mouse.getY() + player.getY() - 300)) < 19) {
                    DroppedItem droppedItem = curRoom.getDroppedItems().get(new Point(xTile, yTile));

                    if (grid[xTile][yTile] == 4 && droppedItem != null && !player.isInventoryFull() && isAdjacentToPlayer(xTile, yTile)) {
                        player.addItem(new Item(droppedItem.getId(), droppedItem.getName(), droppedItem.getImage(), droppedItem.getBuyCost(), droppedItem.getSellCost()));
                        Hashtable<Point, DroppedItem> temp = curRoom.getDroppedItems();
                        temp.remove(new Point(xTile, yTile));
                        curRoom.setDroppedItems(temp);
                        grid[xTile][yTile] = curRoom.getOriginalGrid()[xTile][yTile];
                    }
                }

                else if (treeAtTile(xTile, yTile) != null && treeAtTile(xTile, yTile).getNumFruit() > 0 &&
                    !player.isInventoryFull() && treeAtTile(xTile, yTile).isTileAdjacent(player.getxTile(), player.getyTile())
                && !(player.getEquippedItem() != null && player.getEquippedItem().getId() == 5)) {
                    Tree tree = treeAtTile(xTile, yTile);
                    tree.pickFruit();

                    switch (tree.getFruit()) {
                        case (Tree.APPLE):
                            player.addItem(items.get(112));
                            break;
                        case (Tree.ORANGE):
                            player.addItem(items.get(113));
                            break;
                        case (Tree.PEACH):
                            player.addItem(items.get(114));
                            break;
                        case (Tree.PEAR):
                            player.addItem(items.get(115));
                            break;
                    }
                }

                else if (treeAtTile(xTile, yTile) != null && treeAtTile(xTile, yTile).isTileAdjacent(player.getxTile(), player.getyTile())
                    && (player.getEquippedItem() != null && player.getEquippedItem().getId() == 5)) {
                    if (!player.isInventoryFull()) {
                        player.setActionProgressOpen(true);
                        player.setActionMessage("Catching bugs");
                        if (randint(1, 10) <= 2) {
                            player.addItem(items.get(randint(7, 31)));
                        }
                    }
                }
            }
        }
    }

    public boolean[][] findCanBeDonatedItems() {
        boolean[][] ans = new boolean[6][3];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                boolean canBeDonated = false;
                if (player.getItems()[i][j] != null) {
                    canBeDonated = true;
                    Item temp = player.getItems()[i][j];
                    if (temp.isBug()) {
                        for (int k = 0; k < celeste.getBugs().size(); k++) {
                            if (temp.getName().equals(celeste.getBugs().get(k).getName())) {
                                canBeDonated = false;
                            }
                        }
                    }
                    else if (temp.isOceanFish() || temp.isPondFish() || temp.isRiverFish()) {
                        for (int k = 0; k < celeste.getFish().size(); k++) {
                            if (temp.getName().equals(celeste.getFish().get(k).getName())) {
                                canBeDonated = false;
                            }
                        }
                    }
                    else if (temp.isFossil()) {
                        for (int k = 0; k < celeste.getFossils().size(); k++) {
                            if (temp.getName().equals(celeste.getFossils().get(k).getName())) {
                                canBeDonated = false;
                            }
                        }
                    }
                    else {
                        canBeDonated = false;
                    }
                }
                ans[i][j] = canBeDonated;

            }
        }

        return ans;
    }

    public void donateItems() {
        Item[][] temp = player.getItems();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                if (player.getSelectedItems()[i][j]) {
                    if (temp[i][j].isBug()) {
                        celeste.addBug(temp[i][j]);
                    }
                    else if (temp[i][j].isFossil()) {
                        celeste.addFossil(temp[i][j]);
                    }
                    else if (temp[i][j].isRiverFish() || temp[i][j].isPondFish() || temp[i][j].isOceanFish()) {
                        celeste.addFish(temp[i][j]);
                    }
                    temp[i][j] = null;

                }
            }
        }
    }

    public Tree treeAtTile(int xTile, int yTile) {
        Tree ans = null;
        for (Tree tree : trees) {
            if (tree.getRoom() == curRoom && tree.isTileOnTree(xTile, yTile)) {
                ans = tree;
            }
        }

        return ans;

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            clicked = false;
            if (player.isInventoryOpen()) {
                player.moveItem(mouse);
            }

            if (player.isDialogueSelectionOpen() && player.isSelectionMenuClicked() && Math.hypot(510 - mouse.x, 186 - mouse.y) > 34) {
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

        //drawGrids(g);
        //drawXs(g);


        for (Map.Entry<Point, DroppedItem> pair : curRoom.getDroppedItems().entrySet()) {
            DroppedItem item = pair.getValue();
            g.drawImage(item.getImage(), (item.getxTile()+8)*tileSize - player.getX()+13, (item.getyTile()+5)*tileSize - player.getY()+13, null);
        }

        for (Tree tree : trees) {
            if (tree.getRoom() == curRoom) {
                tree.draw(g, player.getX(), player.getY());
            }
        }

        if (curRoom == tom_nook.getRoom()) {
            tom_nook.draw(g, player.getX(), player.getY());
        }
        if (curRoom == celeste.getRoom()) {
            celeste.draw(g, player.getX(), player.getY());
        }
        if (curRoom == isabelle.getRoom()) {
            isabelle.draw(g, player.getX(), player.getY());
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

        drawTalkingToGeneralNPC(g);
        drawTalkingToTomNook(g);
        drawTalkingToCeleste(g);

        if (!player.isTalkingToNPC() && !player.isInventoryOpen() && !player.isActionProgressOpen()) {
            drawHoverText(g);
        }
    }

    public void drawGrids(Graphics g) {
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
    }

    public void drawXs(Graphics g) {
        g.setColor(new Color(255,0,0));
        for (int i = Math.max(0, player.getxTile()-8); i <= Math.min(94, player.getxTile() + 8); i++) {
            for (int j = Math.max(0, player.getyTile()-5); j < Math.min(85, player.getyTile() + 5); j++) {
                int a = i - Math.max(0, player.getxTile()-8);
                int b = j - Math.max(0, player.getyTile()-5);

                if (grid[i][j] == 0) {
                    g.drawLine(a*60, b*60, a*60 + 60, b*60 + 60);
                    g.drawLine(a*60, b*60+60, a*60+60, b*60);
                }
            }
        }
    }

    public void drawTalkingToGeneralNPC(Graphics g) {
        if (player.isTalkingToNPC()) {
            NPC npc = NPCs.get(player.getVillagerPlayerIsTalkingTo());


            assert npc != null;
            if (!(npc == tom_nook && npc.getSpeechStage() == Tom_Nook.SHOP) && !(npc == celeste && npc.getSpeechStage() == Celeste.MUSEUM)) {
                g.drawImage(speechBubbleImage, (1020 - 700) / 2, 350, null);
            }

            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;

                FontMetrics fontMetrics = new JLabel().getFontMetrics(finkheavy32);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setFont(finkheavy32);
                g2.setColor(Color.BLACK);

                int x, y;  // x, y coordinates of text
                int width;  // width of text

                width = fontMetrics.stringWidth(npc.getName());

                x = 204 + (416 - 204 - width) / 2;
                y = 397;


                if (!(npc == tom_nook && (npc.getSpeechStage() == Tom_Nook.SHOP))) {
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

                if (npc == tom_nook && npc.getSpeechStage() == Tom_Nook.SELL_SHOP) {
                    g2.setColor(new Color(240, 240, 240));

                    ArrayList<String> dialogue = wordWrap("Select which items to sell.", 450);
                    for (int i = 0; i < dialogue.size(); i++) {
                        g2.drawString(dialogue.get(i), 270, 470 + 40 * i);
                    }
                }

                if (npc == celeste && npc.getSpeechStage() == Celeste.DONATION) {
                    g2.setColor(new Color(240, 240, 240));
                    if (player.hasItemToDonate()) {
                        ArrayList<String> dialogue = wordWrap("Select which items to donate.", 450);
                        for (int i = 0; i < dialogue.size(); i++) {
                            g2.drawString(dialogue.get(i), 270, 470 + 40 * i);
                        }
                    }
                    else {
                        ArrayList<String> dialogue = wordWrap("It seems you do not have anything to donate.", 450);
                        for (int i = 0; i < dialogue.size(); i++) {
                            g2.drawString(dialogue.get(i), 270, 470 + 40 * i);
                        }
                    }
                }


                if (player.isDialogueSelectionOpen()) {
                    g.setColor(new Color(255, 255, 255, 100));
                    g.fillRect(310, 86, 400, 200);


                    if (player.isSelectionMenuClicked()) {
                        g.drawImage(selectionMenuNoClickImage, 421, 118, null);
                    }
                    else {
                        g.drawImage(selectionMenuImage, 421, 120, null);
                    }
                    g2.setColor(Color.BLACK);

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
    }

    public void drawTalkingToTomNook(Graphics g) {
        if (player.isTalkingToNPC() && player.getVillagerPlayerIsTalkingTo() == Player.TOM_NOOK) {
            if (player.isShopOpen()) {
                g.drawImage(shopImage, (1020 - 390) / 2,  10, null);

                Rectangle br = tom_nook.getBuyRect();
                Rectangle cr = tom_nook.getCancelRect();

                g.setColor(Color.WHITE);
                g.fillRect(br.x, br.y, br.width, br.height);
                g.fillRect(cr.x, cr.y, cr.width, cr.height);

                g.setColor(Color.BLACK);
                g.drawRect(br.x, br.y, br.width, br.height);
                g.drawRect(cr.x, cr.y, cr.width, cr.height);

                if (player.selectedItemInShop != -1) {
                    Rectangle temp = tom_nook.getItemRects().get(player.getSelectedItemInShop());

                    g.setColor(Color.GREEN);
                    g.drawRect(temp.x, temp.y, temp.width, temp.height);
                }

                g.setColor(Color.BLACK);

                for (int i = 0; i < tom_nook.getStoreItems().size(); i++) {
                    if (tom_nook.getStoreItems().get(i).isFurniture()) {
                        g.drawImage(Item.storeLeafImage, 330,63 + 110*i, null);
                    }
                    else {
                        g.drawImage(tom_nook.getStoreItemImages()[tom_nook.getStoreItems().get(i).getId()], 330,63 + 110*i, null);
                    }

                    if (g instanceof Graphics2D) {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.drawString(tom_nook.getStoreItems().get(i).getName(), 375, 101 + 110*i);
                        g2.drawString(String.valueOf(tom_nook.getStoreItems().get(i).getBuyCost()), 630, 101 + 110*i);

                        FontMetrics fontMetrics = new JLabel().getFontMetrics(finkheavy30);

                        int width = fontMetrics.stringWidth("Buy");
                        g2.drawString("Buy", 353 + (140 - width) / 2, 575 + 32);

                        width = fontMetrics.stringWidth("Cancel");
                        g2.drawString("Cancel", 533 + (140 - width) / 2, 575 + 32);
                    }

                }
                if (g instanceof Graphics2D) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawString(String.valueOf(player.getBells()), 400, 48);
                }
            }
        }
    }

    public void drawTalkingToCeleste(Graphics g) {
         if (player.isMuseumOpen()) {
             if (g instanceof Graphics2D) {
                 Graphics2D g2 = (Graphics2D) g;
                 g2.setColor(Color.BLACK);
                 g2.setFont(finkheavy30);

                 g.drawImage(museumMenuImage, 50, 120, null);
                 g.drawImage(exitButtonImage, 930, 120, null);

                 if (celeste.getPage() == Celeste.BUG_PAGE) {
                     g.drawImage(museumBugsImage, 50, 120, null);
                 }
                 else if (celeste.getPage() == Celeste.FISH_PAGE) {
                     g.drawImage(museumFishImage, 50, 120, null);
                 }
                 else {
                     g.drawImage(museumFossilImage, 50, 120, null);
                 }


                 for (int i = 0; i < 5; i++) {
                     if (celeste.getPage() == Celeste.BUG_PAGE && i + celeste.getBugStart() < celeste.getBugs().size()) {
                         g.drawImage(celeste.getBugs().get(i + celeste.getBugStart()).getImage(), 764, 260 + 49 * i, null);
                         g2.drawString(celeste.getBugs().get(i + celeste.getBugStart()).getName(), 220, 290 + 49 * i);
                     }
                     else if (celeste.getPage() == Celeste.FISH_PAGE && i + celeste.getFishStart() < celeste.getFish().size()) {
                         g.drawImage(celeste.getFish().get(i + celeste.getFishStart()).getImage(), 764, 260 + 49 * i, null);
                         g2.drawString(celeste.getFish().get(i + celeste.getFishStart()).getName(), 220, 290 + 49 * i);
                     }
                     else if (celeste.getPage() == Celeste.FOSSIL_PAGE && i + celeste.getFossilStart() < celeste.getFossils().size()){
                         g.drawImage(celeste.getFossils().get(i + celeste.getFossilStart()).getImage(), 764, 260 + 49 * i, null);
                         g2.drawString(celeste.getFossils().get(i + celeste.getFossilStart()).getName(), 220, 290 + 49 * i);
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
        FontMetrics fontMetrics = new JLabel().getFontMetrics(finkheavy32);
        ArrayList<String> ans = new ArrayList<>();
        String[] wordsArray = str.split(" ");
        ArrayList<String> words = new ArrayList<>();
        words.addAll(Arrays.asList(wordsArray));
        Collections.reverse(words);

        String word = "";
        String string;

        while (words.size() > 0 || !word.equals("")) {
            string = "";
            while (words.size() > 0 || !word.equals("")) {
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

    public void drawHoverText(Graphics g) {
        int xTile = (int) ((mouse.getX() + player.getX() - 480) / 60);
        int yTile = (int) ((mouse.getY() + player.getY() - 300) / 60);

        int width;
        int height = 30;
        int x = mouse.x + 20;
        int y = mouse.y - 15;


        String msg = "";
        boolean draw = false;




        if (player.getEquippedItem() != null && player.getEquippedItem().getId() == 1 && (curRoom == outside || curRoom == minigameIsland)) {
            if (validFishingTile(xTile, yTile)) {
                msg = "Cast line.";
                draw = true;
            }
        }

        else if (player.getEquippedItem() != null && player.getEquippedItem().getId() == 6 && (curRoom == outside || curRoom == minigameIsland)) {
            if (curRoom == outside && diggableTiles[xTile][yTile] == 1) {
                msg = "Dig";
                draw = true;
            }
            else if (curRoom == minigameIsland && minigameIslandDiggable[xTile][yTile] == 1) {
                msg = "Dig";
                draw = true;
            }

        }

        else if (npcAtPoint(xTile, yTile) != null) {
            msg = "Talk to villager.";
            draw = true;
        }

        else if (xTile < grid.length && yTile < grid[0].length && grid[xTile][yTile] == 4) {
            msg = "Pick up item.";
            draw = true;
        }

        else if (treeAtTile(xTile, yTile) != null && treeAtTile(xTile, yTile).getNumFruit() > 0 &&
            !(player.getEquippedItem() != null && player.getEquippedItem().getId() == 5)) {
            msg = "Pick fruit";
            draw = true;
        }
        else if (treeAtTile(xTile, yTile) != null && (player.getEquippedItem() != null && player.getEquippedItem().getId() == 5)) {
            msg = "Catch bugs";
            draw = true;
        }

        if (draw) {
            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;

                FontMetrics fontMetrics = new JLabel().getFontMetrics(finkheavy15);
                width = fontMetrics.stringWidth(msg) + 40;

                g.setColor(new Color(255, 255, 255, 100));
                g.fillRect(x, y, width, height);
                g.drawImage(rightClickImage, x - 18, y - 15, null);

                g2.setFont(finkheavy15);
                g2.setColor(Color.BLACK);
                g2.drawString(msg, x + 25, y + 20);

            }
        }
    }

    public void selectDialogue() {
        NPC npc = NPCs.get(player.getVillagerPlayerIsTalkingTo());
        player.setDialogueSelectionOpen(false);
        dialogueDelay = 0;

        if ((player.getVillagerPlayerIsTalkingTo() >= 3 && player.getVillagerPlayerIsTalkingTo() < 6)) {
            if (npc.getPlayerOptions().size() == 2) {
                if (selectionAngle >= 0 && selectionAngle <= Math.PI) {
                    if (npc.getSpeechStage() == NPC.GREETING) {
                        npc.setSpeechStage(NPC.CHAT);
                    }
                }
                else {
                    npc.setSpeechStage(NPC.GOODBYE);
                    npc.resetDialogue();
                }
            }
        }

        if (npc == isabelle) {
            if (npc.getPlayerOptions().size() == 2) {
                if (selectionAngle >= 0 && selectionAngle <= Math.PI) {
                    if (npc.getSpeechStage() == NPC.GREETING) {
                        npc.setSpeechStage(NPC.CHAT);
                    }
                }
                else {
                    npc.setSpeechStage(NPC.GOODBYE);
                    npc.resetDialogue();
                }
            }
        }

        if (npc == tom_nook) {
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
                player.setSelectedItemInShop(-1);
            }
            else {
                tom_nook.setSpeechStage(NPC.GOODBYE);
                npc.resetDialogue();
            }
        }

        else if (npc == boat_operator) {
            if (selectionAngle >= 0 && selectionAngle <= Math.PI) {
                player.setDialogueSelectionOpen(false);
                player.setTalkingToNPC(false);

                npc.setTalking(false);

                player.setGoingToNewRoom(true);
            }
            else {
                npc.setSpeechStage(NPC.GOODBYE);
                npc.resetDialogue();
            }
        }

        else if (npc == boat_operator_on_island) {
            if (selectionAngle >= 0 && selectionAngle <= Math.PI) {
                player.setDialogueSelectionOpen(false);
                player.setTalkingToNPC(false);

                npc.setTalking(false);

                player.setExitingRoom(true);
            }
            else {
                npc.setSpeechStage(NPC.GOODBYE);
                npc.resetDialogue();
            }
        }

        else if (npc == celeste) {
            if (selectionAngle > (7.0/4.0)*Math.PI || selectionAngle <= Math.PI/4) {
                player.setDialogueSelectionOpen(false);
                player.setDonateMenuOpen(true);
                npc.setSpeechStage(Celeste.DONATION);
                player.setSelectedItems(new boolean[6][3]);
                player.setCanBeDonatedItems(findCanBeDonatedItems());

            }
            else if (selectionAngle > Math.PI/4 && selectionAngle <= 3.0/4.0 * Math.PI) {
                player.setMuseumOpen(true);
                player.setDialogueSelectionOpen(false);
                npc.setSpeechStage(Celeste.MUSEUM);
            }

            else if (selectionAngle > 5.0/4.0 * Math.PI && selectionAngle < 7.0/4.0 * Math.PI) {
                npc.setSpeechStage(NPC.GOODBYE);
                npc.resetDialogue();
            }
        }

    }

    public boolean validFishingTile(int xTile, int yTile) {
        //System.out.println(xTile + " " + yTile + " " + waterTiles[xTile][yTile]);

        if (curRoom == outside) {
            if (waterTiles[xTile][yTile] != 0) {
                if (xTile == player.getxTile() && (Math.abs(yTile - player.getyTile()) <= 2 && Math.abs(yTile - player.getyTile()) > 0)) {
                    return true;
                }
                else if (yTile == player.getyTile() && (Math.abs(xTile - player.getxTile()) <= 2 && Math.abs(xTile - player.getxTile()) > 0)) {
                    return true;
                }
            }
        }
        else if (curRoom == minigameIsland) {
            if (minigameIslandWater[xTile][yTile] != 0) {
                if (xTile == player.getxTile() && (Math.abs(yTile - player.getyTile()) <= 2 && Math.abs(yTile - player.getyTile()) > 0)) {
                    return true;
                }
                else if (yTile == player.getyTile() && (Math.abs(xTile - player.getxTile()) <= 2 && Math.abs(xTile - player.getxTile()) > 0)) {
                    return true;
                }
            }
        }
        return false;
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

    public static String capitalizeWord(String str) {
        String[] words = str.split("\\s");
        String capitalizeWord = "";
        for (String w:words) {
            String first = w.substring(0,1);
            String afterFirst = w.substring(1);
            capitalizeWord += first.toUpperCase() + afterFirst + " ";
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