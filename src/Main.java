import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.util.*;
import java.awt.*;
import java.io.*;

public class Main extends JFrame implements ActionListener {
    private javax.swing.Timer myTimer;  // Game Timer
    private GamePanel game;  // GamePanel for the actual game

    public Main() {
        // Creating frame
        super("Pokemon Crossing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1260,900);


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

    private final Image map = new ImageIcon("Assets/Map/AC Map - Copy.png").getImage();

    public static final int tileSize = 60;
    private Player player;


    public GamePanel(Main m) {
        keys = new boolean[KeyEvent.KEY_LAST + 1];

        // Setting panel
        mainFrame = m;
        setSize(1000, 800);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);

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
        if (keys[KeyEvent.VK_D]) {
            player.move(Player.RIGHT);
        }

        if (keys[KeyEvent.VK_W]) {
            player.move(Player.UP);
        }

        if (keys[KeyEvent.VK_A]) {
            player.move(Player.LEFT);
        }

        if (keys[KeyEvent.VK_S]) {
            player.move(Player.DOWN);
        }

        player.move();
    }

    public void init() {
        player = new Player(600, 420, Player.MALE);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
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
        g.drawImage(map, 600 - player.getX(), 420 - player.getY(), null);
        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, getWidth(), getHeight());

        g.setColor(new Color(0, 0, 0));
        for (int i = 0; i < 1260; i+=tileSize) {
            g.drawLine(i, 0, i, 900);
        }

        for (int i = 0; i < 900; i+=tileSize) {
            g.drawLine(0, i, 1260, i);
        }

        player.draw(g);

    }
}