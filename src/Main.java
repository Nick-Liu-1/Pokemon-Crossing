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
    private int[][] grid = new int[96][96];
    private ArrayList<Room> rooms = new ArrayList<>();

    public GamePanel(Main m) {
        keys = new boolean[KeyEvent.KEY_LAST + 1];

        // Setting panel
        mainFrame = m;
        setSize(1020, 695);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);
        loadMap();
        curRoom = rooms.get(0);
        init();
        Player.load();
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
        mainFrame.start();
    }

    public void move() {
        count++;
        if (keys[KeyEvent.VK_D] && KeyEvent.VK_D == mostRecentKeyPress) {
            player.move(Player.RIGHT, keys);
        }

        if (keys[KeyEvent.VK_W] && KeyEvent.VK_W == mostRecentKeyPress) {
            player.move(Player.UP, keys);
        }

        if (keys[KeyEvent.VK_A] && KeyEvent.VK_A == mostRecentKeyPress) {
            player.move(Player.LEFT, keys);
        }

        if (keys[KeyEvent.VK_S] && KeyEvent.VK_S == mostRecentKeyPress) {
            player.move(Player.DOWN, keys);
        }

        player.move();

        if (player.isGoingToNewRoom()) {

        }
    }

    public void init() {
        player = new Player(1500, 1440, Player.MALE, grid);
    }

    public void loadMap() {
        try{
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Map/map.txt")));
            for (int i = 0; i < 85; i++) {
                String[] s = stdin.nextLine().split(" ");
                for (int j = 0; j < 94; j++) {
                    grid[j][i] = Integer.parseInt(s[j]);
                }
            }
            rooms.add(new Room(grid, new ImageIcon("Assets/Map/PC Map.png").getImage(), 0, 0));
        }
        catch (FileNotFoundException e) {
            System.out.println("error");
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

        System.out.println((player.getxTile()+1) + " " + (player.getyTile()+1));
        g.setColor(new Color(255,0,0));
        for (int i = Math.max(0, player.getxTile()-8); i <= Math.min(94, player.getxTile() + 8); i++) {
            for (int j = Math.max(0, player.getyTile()-5); j < Math.min(85, player.getyTile() + 5); j++) {
                int a = i - Math.max(0, player.getxTile()-8);
                int b = j - Math.max(0, player.getyTile()-5);

                if (grid[i][j] == 0) {
                    g.drawLine(a*60, b*60, a*60 + 60, b*60 + 60);
                }
            }
        }

        player.draw(g);

    }
}