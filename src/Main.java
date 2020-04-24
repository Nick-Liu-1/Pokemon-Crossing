import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

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

    public static final int tileSize = 60;
    private Player player;
    private int mostRecentKeyPress = 0;
    private Room curRoom;
    private Room outside;
    private int[][] grid;
    private Hashtable<Point, Room> rooms = new Hashtable<>();

    public GamePanel(Main m) {
        keys = new boolean[KeyEvent.KEY_LAST + 1];

        // Setting panel
        mainFrame = m;
        setSize(1020, 695);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);
        loadMap();
        curRoom = outside;
        grid = curRoom.getGrid();
        init();
        Player.load();
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }

    public void move() {
        //System.out.println(player.getxTile()+ " "+ player.getyTile());
        count++;
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

        if (player.isGoingToNewRoom()) {
            curRoom = rooms.get(new Point(player.getxTile(), player.getyTile()));
            grid = curRoom.getGrid();
            player.setxTile(curRoom.getExitX());
            player.setyTile(curRoom.getExitY());
            player.setX(curRoom.getExitX() * tileSize);
            player.setY(curRoom.getExitY() * tileSize);
        }

        else if (player.isExitingRoom()) {
            player.setxTile(curRoom.getEntryX());
            player.setyTile(curRoom.getEntryY());
            player.setX(curRoom.getEntryX() * tileSize);
            player.setY(curRoom.getEntryY() * tileSize);
            curRoom = outside;
            grid = curRoom.getGrid();
            System.out.println(player.getxTile() + " " + player.getyTile());
        }
    }

    public void init() {
        player = new Player(1500, 1440, Player.MALE, grid);
    }

    public void loadMap() {
        int[][] mapGrid = new int[94][85];
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
        outside = new Room(mapGrid, new ImageIcon("Assets/Map/PC Map.png").getImage(), 0, 0, 0 ,0);

        try{
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Room/rooms.txt")));

        }
        catch (FileNotFoundException e) {
            System.out.println("error loading rooms");
        }

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

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void paintComponent(Graphics g) {
        g.drawImage(curRoom.getImage(), 480 - player.getX(), 303 - player.getY(), null);
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
        g.setColor(new Color(255,0,0));
        /*for (int i = Math.max(0, player.getxTile()-8); i <= Math.min(94, player.getxTile() + 8); i++) {
            for (int j = Math.max(0, player.getyTile()-5); j < Math.min(85, player.getyTile() + 5); j++) {
                int a = i - Math.max(0, player.getxTile()-8);
                int b = j - Math.max(0, player.getyTile()-5);

                if (grid[i][j] == 0) {
                    g.drawLine(a*60, b*60, a*60 + 60, b*60 + 60);
                }
            }
        }*/

        player.draw(g);

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
}