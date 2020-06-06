import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class LoadFile extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();
	boolean[] slotsUsed = {false, false, false};
	String[] names = new String[3];
	LoadFilePanel panel;
	Timer myTimer;

    public LoadFile() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Game Files Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);

		panel = new LoadFilePanel(this);
		add(panel);

		try {
			Scanner stdin = new Scanner(new BufferedReader(new FileReader("Saves/Used Slots.txt")));
			while (stdin.hasNextLine()) {
				String[] line = stdin.nextLine().split(" ");
				slotsUsed[Integer.parseInt(line[0]) - 1] = true;
				names[Integer.parseInt(line[0]) - 1] = line[1];
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		
		JButton backBtn = new JButton();	
		backBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			StartMenu menu = new StartMenu();
    			setVisible(false);
    		}
		});
		backBtn.setBounds(54, 566, 259, 77);
		layeredPane.add(backBtn,1);
		backBtn.setOpaque(false);
		
		JButton slot1 = new JButton();	
		slot1.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			if (slotsUsed[0]) {
					Main frame = new Main(1);
				}

    		}			
		});
		slot1.setBounds(326, 249, 400, 60);
		layeredPane.add(slot1,1);
		slot1.setOpaque(false);
		
		JButton slot2 = new JButton();	
		slot2.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
				if (slotsUsed[1]) {
					Main frame = new Main(2);
				}
    		}			
		});
		slot2.setBounds(326, 309, 400, 60);
		layeredPane.add(slot2,1);
		slot2.setOpaque(false);
		
		JButton slot3 = new JButton();	
		slot3.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
				if (slotsUsed[2]) {
					Main frame = new Main(3);
				}
    		}			
		});
		slot3.setBounds(326, 369, 400, 60);
		layeredPane.add(slot3,3);
		slot3.setOpaque(false);


		myTimer = new javax.swing.Timer(10, new TickListener());
			
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		myTimer.start();
    }

	class TickListener implements ActionListener {
		public void actionPerformed(ActionEvent evt) {
			panel.repaint();
		}
	}

	public boolean[] getSlotsUsed() {
    	return slotsUsed;
	}

	public String[] getNames() {
    	return names;
	}

    public static void main(String[] arguments) {
		LoadFile frame = new LoadFile();
    }
}

class LoadFilePanel extends JPanel {
	private LoadFile mainframe;

	public LoadFilePanel(LoadFile m) {
		this.mainframe = m;
	}

	public void paintComponent(Graphics g) {
		if (g instanceof Graphics2D) {
			Graphics2D g2 = (Graphics2D) g;

			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setFont(GamePanel.finkheavy32);
			g2.setColor(new Color(0, 0, 0));

			for (int i = 0; i < mainframe.getSlotsUsed().length; i++) {
				if (mainframe.getSlotsUsed()[i]) {
					g2.drawString(mainframe.getNames()[i], 400, 300 + 80 * i);
				}
			}
		}
	}
}
