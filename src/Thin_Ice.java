import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class Thin_Ice extends JPanel implements KeyListener, MouseListener {
    private Main mainFrame;
    private boolean[] keys;   // Array of keys that keeps track if they are down or not
    public static final int tileSize = 60;  // Dimension of the tile
    private int mostRecentKeyPress = 0;  // Most recent movement key press (WASD)

    private int level = 1;
    private int[][] grid;

    private int[][][] levelGrids = new int[10][19][15];

    private Point mouse = new Point(0, 0);
    private boolean clicked = false;

    private ArrayList<Image> levelImages = new ArrayList<>();
    private ArrayList<Point> levelStarts = new ArrayList<>();

    private int playerX, playerY;
    private int playerxTile, playeryTile;

    public Thin_Ice(Main m) {
        mainFrame = m;
        setSize(1020, 695);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);
        init();
    }

    public void init() {

    }

    public void load() {

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

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
}
