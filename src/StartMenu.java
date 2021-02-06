import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class StartMenu extends JFrame implements ActionListener{
	private StartMenu2 startMenu2; 
	private NewFile newFile;
	private LoadFile loadFile;
	private Main main;

	private JPanel cards;
	private CardLayout cLayout = new CardLayout();

	//StartMenu stuff
	JButton loadBtn = new JButton();
	JButton newBtn = new JButton();	

	//NewFile stuff
	JButton backBtn = new JButton();
	JButton slot1 = new JButton();	
	JButton slot2 = new JButton();
	JButton slot3 = new JButton();

	//LoadFile stuff
	JButton LbackBtn = new JButton();
	JButton Lslot1 = new JButton();	
	JButton Lslot2 = new JButton();
	JButton Lslot3 = new JButton();

	String cur = "menu";



    public StartMenu() {
		super("Pokemon Crossing");
		setSize(1020,695);

		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);

		cards = new JPanel(cLayout);

		// Main menu
		startMenu2 = new StartMenu2();
		menuInit();
		cards.add(startMenu2, "menu");

		// New file
		newFile = new NewFile();
		newFileInit();
		cards.add(newFile, "new file");

		// Load File
		loadFile = new LoadFile();
		loadFileInit();
		cards.add(loadFile, "load file");


		add(cards); 


		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);

    }

    public void menuInit() {
    	startMenu2.setLayout(null);

		loadBtn.setBounds(152, 384, 219, 64);
		loadBtn.setOpaque(false);
		loadBtn.setContentAreaFilled(false);
		loadBtn.setBorderPainted(false);
		loadBtn.addActionListener(this);

		startMenu2.add(loadBtn);

		
		newBtn.setBounds(401, 384, 219, 64);
		newBtn.setOpaque(false);
		newBtn.setContentAreaFilled(false);
		newBtn.setBorderPainted(false);
		newBtn.addActionListener(this);

		startMenu2.add(newBtn);
    }

    public void newFileInit() {
    	newFile.setLayout(null);

    	backBtn.setBounds(54, 566, 259, 77);
    	backBtn.setOpaque(false);
		backBtn.setContentAreaFilled(false);
		backBtn.setBorderPainted(false);
		backBtn.addActionListener(this);
		newFile.add(backBtn);

		slot1.setBounds(326, 249, 400, 60);
		slot2.setBounds(326, 309, 400, 60);
		slot3.setBounds(326, 369, 400, 60);

		slot1.setOpaque(false);
		slot1.setContentAreaFilled(false);
		slot1.setBorderPainted(false);

		slot2.setOpaque(false);
		slot2.setContentAreaFilled(false);
		slot2.setBorderPainted(false);

		slot3.setOpaque(false);
		slot3.setContentAreaFilled(false);
		slot3.setBorderPainted(false);

		slot1.addActionListener(this);
		slot2.addActionListener(this);
		slot3.addActionListener(this);

		newFile.add(slot1);
		newFile.add(slot2);
		newFile.add(slot3);

    }


    public void loadFileInit() {
    	loadFile.setLayout(null);

    	LbackBtn.setBounds(54, 566, 259, 77);
    	LbackBtn.setOpaque(false);
		LbackBtn.setContentAreaFilled(false);
		LbackBtn.setBorderPainted(false);
		LbackBtn.addActionListener(this);
		loadFile.add(LbackBtn);

		Lslot1.setBounds(326, 249, 400, 60);
		Lslot2.setBounds(326, 309, 400, 60);
		Lslot3.setBounds(326, 369, 400, 60);

		Lslot1.setOpaque(false);
		Lslot1.setContentAreaFilled(false);
		Lslot1.setBorderPainted(false);

		Lslot2.setOpaque(false);
		Lslot2.setContentAreaFilled(false);
		Lslot2.setBorderPainted(false);

		Lslot3.setOpaque(false);
		Lslot3.setContentAreaFilled(false);
		Lslot3.setBorderPainted(false);

		Lslot1.addActionListener(this);
		Lslot2.addActionListener(this);
		Lslot3.addActionListener(this);

		loadFile.add(Lslot1);
		loadFile.add(Lslot2);
		loadFile.add(Lslot3);

    }

    public void actionPerformed(ActionEvent evt){
        /*
            Deals with the buttons as they are pressed. Takes user to the specified page depending on what
            button was clicked.
         */

        Object source = evt.getSource();

        if (source == newBtn) {
            cLayout.show(cards,"new file");
            newFile.grabFocus();  
            cur = "new file";
        }

        if (source == backBtn || source == LbackBtn) {
        	cLayout.show(cards,"menu");
            startMenu2.grabFocus();
            cur = "menu";
        }

        if (source == loadBtn) {
        	cLayout.show(cards,"load file");
            loadFile.grabFocus();
            cur = "load file";
        }

        if (source == slot1 && cur.equals("new file")) {
        	createNewFile(1);
        }

        if (source == slot2 && cur.equals("new file")) {
        	createNewFile(2);
        }

        if (source == slot3 && cur.equals("new file")) {
        	createNewFile(3);
        }

        if (source == Lslot1 && cur.equals("load file")) {
        	setVisible(false);
        	main = new Main(1);
        }

        if (source == Lslot2 && cur.equals("load file")) {
        	setVisible(false);
        	main = new Main(2);
        }

        if (source == Lslot3 && cur.equals("load file")) {
        	setVisible(false);
        	main = new Main(3);
        }


    }

    public void createNewFile(int n) {
    	String name = JOptionPane.showInputDialog("Name:");
		String gender = JOptionPane.showInputDialog("Gender: (male/female)");
		int g = gender.toLowerCase().equals("male") ? Player.MALE : Player.FEMALE;
		newFile.createNewFile(n, name, g);
		setVisible(false);
		Main main = new Main(n);
    }

    
    public static void main(String[] arguments) {
		StartMenu menu = new StartMenu();
    }
}
