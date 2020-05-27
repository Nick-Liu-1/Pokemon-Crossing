import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Options extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();

    public Options() {
		super("Pokemon Crossing");
		setSize(1020,695);
	
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
    }
    
    public static void main(String[] arguments) {
		Options frame = new Options();
    }
}
