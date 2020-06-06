import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;


public class StartMenu extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();
	private int num = 1;
	private NewFile newFile;
	private LoadFile loadFile;
	private Main main;
	private Options options;

    public StartMenu() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Title Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);
		
		JButton loadBtn = new JButton();	
		loadBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			loadFile = new LoadFile();
    			setVisible(false);
    		}
		});
		loadBtn.setBounds(152, 384, 219, 64);
		layeredPane.add(loadBtn,1);
		loadBtn.setOpaque(false);
		
		JButton newBtn = new JButton();	
		newBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			newFile = new NewFile();
    			setVisible(false);
    		}			
		});
		newBtn.setBounds(401, 384, 219, 64);
		layeredPane.add(newBtn,1);
		newBtn.setOpaque(false);
		
		JButton optionsBtn = new JButton();	
		optionsBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			options = new Options();
    			setVisible(false);
    		}
		});
		optionsBtn.setBounds(653, 384, 219, 64);
		layeredPane.add(optionsBtn,1);
		optionsBtn.setOpaque(false);
		
		JButton tempBtn = new JButton();	
		tempBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			Main PokemonCrossing = new Main(num);
    			setVisible(false);
    		}
		});
		tempBtn.setBounds(0, 470, 1020, 235);
		layeredPane.add(tempBtn,1);
		tempBtn.setOpaque(false);
			
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);

    }

    
    public static void main(String[] arguments) {
		StartMenu menu = new StartMenu();
    }
}
