import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.*;

public class Thin_Ice extends JPanel implements KeyListener, MouseListener {
    private Main mainFrame;
    private boolean[] keys;   // Array of keys that keeps track if they are down or not
    public static final int tileSize = 40;  // Dimension of the tile
    private int mostRecentKeyPress = 0;  // Most recent movement key press (WASD)

    private int level;
    private int[][] grid;
    private int score;
    private int scoreAtStartOfLevel;

    private int offX = 130;
    private int offY = 30;

    private Point mouse;
    private boolean clicked;

    private static int[][][] levelGrids = new int[10][19][15];
    private static ArrayList<Image> levelImages = new ArrayList<>();
    private static ArrayList<Point> levelStarts = new ArrayList<>();

    private int playerX, playerY;
    private int playerxTile, playeryTile;

    private Image titleScreen = new ImageIcon("Assets/Minigames/Thin Ice/Title Screen.png").getImage();
    private Image waterTile = new ImageIcon("Assets/Minigames/Thin Ice/water.png").getImage();
    private Image iceTile = new ImageIcon("Assets/Minigames/Thin Ice/ice.png").getImage();

    public static final int RIGHT = 0;
    public static final int UP = 1;
    public static final int LEFT = 2;
    public static final int DOWN = 3;

    private int direction;

    private boolean moving;
    private boolean onTile;
    private boolean sinking;

    private int sinkingCounter;

    private static Font helvetica30;

    private Rectangle resetRect;
    private Rectangle exitRect;

    private Image playerGif = Toolkit.getDefaultToolkit().createImage("Assets/Minigames/Thin Ice/puffle.gif");
    private Image sinkingGif = Toolkit.getDefaultToolkit().createImage("Assets/Minigames/Thin Ice/puffleSinking.gif");



    public Thin_Ice(Main m) {
        mainFrame = m;
        setSize(1020, 695);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);
        init();
    }

    public void init() {
        level = 0;
        mouse = new Point(0, 0);
        clicked = false;
        moving = false;
        direction = UP;
        score = 0;
        scoreAtStartOfLevel = 0;

        onTile = false;
        keys = new boolean[KeyEvent.KEY_LAST + 1];
        grid = new int[19][15];
        goToNextLevel();

        helvetica30 = new Font("Helvetica", Font.PLAIN, 30);

        resetRect = new Rectangle(10, 230, 110, 40);
        exitRect = new Rectangle(10, 300, 110, 40);

        sinkingCounter = 0;
    }

    public static void load() {
        File folder = new File("Assets/Minigames/Thin Ice/Levels");
        File[] listOfFiles = folder.listFiles();

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                levelImages.add(new ImageIcon("Assets/Minigames/Thin Ice/Levels/" + listOfFile.getName()).getImage());
            }
        }


        try {
            Scanner stdin = new Scanner(new BufferedReader(new FileReader("Assets/Minigames/Thin Ice/levels.txt")));
            int n = Integer.parseInt(stdin.nextLine());

            for (int i = 0; i < n; i++) {
                String[] line = stdin.nextLine().split(" ");
                levelStarts.add(new Point(Integer.parseInt(line[0]), Integer.parseInt(line[1])));

                for (int j = 0; j < 15; j++) {
                    line = stdin.nextLine().split(" ");
                    for (int k = 0; k < 19; k++) {
                        levelGrids[i][k][j] = Integer.parseInt(line[k]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("error loading thin ice level stuff");
        }
    }

    public void move() {
        Point mousePos = MouseInfo.getPointerInfo().getLocation();  // Get mouse position
        Point offset = getLocationOnScreen();  // Get window position
        mouse = new Point(mousePos.x - offset.x, mousePos.y - offset.y);
        //System.out.println("(" + (mouse.x) + ", " + (mouse.y) + ")");


        if (keys[KeyEvent.VK_D] && KeyEvent.VK_D == mostRecentKeyPress) {
            movePlayer(RIGHT);
        }

        if (keys[KeyEvent.VK_W] && KeyEvent.VK_W == mostRecentKeyPress) {
            movePlayer(UP);
        }

        if (keys[KeyEvent.VK_A] && KeyEvent.VK_A == mostRecentKeyPress) {
            movePlayer(LEFT);
        }

        if (keys[KeyEvent.VK_S] && KeyEvent.VK_S == mostRecentKeyPress) {
            movePlayer(DOWN);
        }

        //System.out.println(playerxTile + " " + playeryTile);
        movePlayer();

        if (sinking) {
            sinkingCounter++;
            if (sinkingCounter >= 108) {
                sinking = false;
                sinkingGif.flush();
                resetLevel();
            }
        }
    }

    public void movePlayer(int dir) {
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
            direction = dir;
            moving = (inDir(dir) == 1 || inDir(dir) == 2 || inDir(dir) == 3);
            if (moving) {
                onTile = false;
            }
        }
    }

    public void movePlayer() {
        int speed = 2;

        // If moving. move in the correct direction
        if (moving) {
            if (grid[playerxTile][playeryTile] == 1 && !onTile) {
                grid[playerxTile][playeryTile] = 4;
                score++;
            }
            else if (grid[playerxTile][playeryTile] == 2) {
                grid[playerxTile][playeryTile] = 1;
                onTile = true;
                score++;
            }


            switch (direction) {
                case (RIGHT):
                    playerX += speed;
                    break;
                case (UP):
                    playerY -= speed;
                    break;
                case (LEFT):
                    playerX -= speed;
                    break;
                case (DOWN):
                    playerY += speed;
                    break;
            }
        }

        //System.out.println((playerX - offX) + " " + (playerY - offY));

        // Player has reached a new tile
        if ((playerX - offX) % tileSize == 0 && (playerY - offY) % tileSize == 0) {
            // Set tile pos
            playerxTile = (playerX - offX) / tileSize;
            playeryTile = (playerY - offY) / tileSize;

            if (grid[playerxTile][playeryTile] == 3) {
                goToNextLevel();
            }

            if (playerHasNoMoreMoves() && !sinking) {
                sinkingCounter = 0;
                sinking = true;
                grid[playerxTile][playeryTile] = 4;
            }


            // Player stops if not continuing in same direction
            if (!dirIsPressed() || keyPressToDir(mostRecentKeyPress) != direction || (inDir(direction) != 1 || inDir(direction) != 2 || inDir(direction) != 3)) {
                moving = false;
            }
        }
    }

    public void goToNextLevel() {
        if (level <= 9) {
            if (allCleared()) {
                score += level * 10;
            }

            level++;
            System.out.println(level);
            playerxTile = levelStarts.get(level - 1).x;
            playeryTile = levelStarts.get(level - 1).y;

            playerX = playerxTile * tileSize + offX;
            playerY = playeryTile * tileSize + offY;

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    grid[i][j] = levelGrids[level-1][i][j];
                }
            }

            scoreAtStartOfLevel = score;
        }
    }

    public void resetLevel() {
        playerxTile = levelStarts.get(level - 1).x;
        playeryTile = levelStarts.get(level - 1).y;

        playerX = playerxTile * tileSize + offX;
        playerY = playeryTile * tileSize + offY;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = levelGrids[level-1][i][j];
            }
        }

        score = scoreAtStartOfLevel;
    }

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

    public int inDir(int dir) {
        int ans = 0;

        switch (dir) {
            case (Player.RIGHT):
                ans = grid[playerxTile+1][playeryTile];
                break;
            case (Player.UP):
                ans = grid[playerxTile][playeryTile-1];
                break;
            case (Player.LEFT):
                ans = grid[playerxTile-1][playeryTile];
                break;
            case (Player.DOWN):
                ans = grid[playerxTile][playeryTile+1];
                break;
        }

        return ans;
    }

    public boolean playerHasNoMoreMoves() {
        return (inDir(RIGHT) != 1 && inDir(RIGHT) != 2 && inDir(RIGHT) != 3) && (inDir(LEFT) != 1 && inDir(LEFT) != 2 && inDir(LEFT) != 3) &&
            (inDir(DOWN) != 1 && inDir(DOWN) != 2 && inDir(DOWN) != 3) && (inDir(UP) != 1 && inDir(UP) != 2 && inDir(UP) != 3);
    }

    public boolean allCleared() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1 || grid[i][j] == 2) {
                    System.out.println(i + " " + j);
                    return false;
                }
            }
        }
        return true;
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
        if (e.getButton() == MouseEvent.BUTTON1) {
            if (resetRect.contains(mouse)) {
                resetLevel();
            }
        }
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
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 1020, 695);

        if (level > 0) {
            g.drawImage(levelImages.get(level - 1), offX,  offY, null);

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == 1) {
                        g.drawImage(iceTile, offX + tileSize * i, offY + tileSize * j, null);
                    }
                    else if (grid[i][j] == 4) {
                        g.drawImage(waterTile, offX + tileSize * i, offY + tileSize * j, null);
                    }
                }
            }


            g.setColor(new Color(125, 255, 233));
            g.fillRect(resetRect.x, resetRect.y, resetRect.width, resetRect.height);
            g.fillRect(exitRect.x, exitRect.y, exitRect.width, exitRect.height);

            g.setColor(Color.BLACK);
            g.drawRect(resetRect.x, resetRect.y, resetRect.width, resetRect.height);
            g.drawRect(exitRect.x, exitRect.y, exitRect.width, exitRect.height);

            if (!sinking) {
                g.drawImage(playerGif, playerX - 21, playerY - 36, null);
            }
            else {
                g.drawImage(sinkingGif, playerX - 12, playerY - 55, null);
            }

            if (g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;

                FontMetrics fontMetrics = new JLabel().getFontMetrics(helvetica30);
                g2.setFont(helvetica30);
                g2.setColor(Color.BLACK);

                g2.drawString("Level:", (130 - fontMetrics.stringWidth("Level:")) / 2, 57);
                g2.drawString(String.valueOf(level), (130 - fontMetrics.stringWidth(String.valueOf(level))) / 2, 90);

                g2.drawString("Score:", (130 - fontMetrics.stringWidth("Score:")) / 2, 157);
                g2.drawString(String.valueOf(score), (130 - fontMetrics.stringWidth(String.valueOf(score))) / 2, 190);

                g2.drawString("Reset", (130 - fontMetrics.stringWidth("Reset")) / 2, 261);
                g2.drawString("Exit", (130 - fontMetrics.stringWidth("Exit")) / 2, 331);
            }
        }
    }
}

