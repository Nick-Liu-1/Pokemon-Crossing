import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class Main extends JFrame implements ActionListener {
    private javax.swing.Timer myTimer;  // Game Timer
    private GamePanel game;  // GamePanel for the actual game

    public Main() {
        // Creating frame
        super("Pokemon Crossing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000,800);


        game = new GamePanel(this);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String args[]) {
        Main frame = new Main();
    }

}

class GamePanel extends JPanel implements KeyListener, MouseListener {
    private boolean[] keys;   // Array of keys that keeps track if they are down or not
    private Main mainFrame;   // Frame of the program

    public GamePanel(Main m) {
        keys = new boolean[KeyEvent.KEY_LAST + 1];

        // Setting panel
        mainFrame = m;
        setSize(1000, 800);

        // Adding action listeners
        addKeyListener(this);
        addMouseListener(this);
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