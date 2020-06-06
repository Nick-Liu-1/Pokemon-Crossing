import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class LoadFile extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();
	private JPanel panel = new JPanel();
	private boolean[] slotsUsed = {false, false, false};
	private String[] names = new String[3];
	private Timer myTimer;
	private JLabel[] saveFileNames = new JLabel[3];


    public LoadFile() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Game Files Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);

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


		for (int i = 0; i < 3; i++) {
			if (slotsUsed[i]) {
				JLabel text = new JLabel(names[i]);
				Dimension size = text.getPreferredSize();
				text.setBounds(150, 100 + 100*i, size.width, size.height);
				layeredPane.setLayout(null);
				layeredPane.add(text);
			}

		}


		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
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
