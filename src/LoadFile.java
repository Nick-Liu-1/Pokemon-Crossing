import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoadFile extends JFrame{
	private JLayeredPane layeredPane=new JLayeredPane();

    public LoadFile() {
		super("Pokemon Crossing");
		setSize(1020,695);

		ImageIcon background = new ImageIcon("Assets/Misc/Game Files Screen.png");
		JLabel back = new JLabel(background);		
		back.setBounds(0, 0, 1020, 695);
		layeredPane.add(back,2);
		
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
    			System.out.println("slot1");
    		}			
		});
		slot1.setBounds(326, 249, 400, 60);
		layeredPane.add(slot1,1);
		slot1.setOpaque(false);
		
		JButton slot2 = new JButton();	
		slot2.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			System.out.println("slot2");
    		}			
		});
		slot2.setBounds(326, 309, 400, 60);
		layeredPane.add(slot2,1);
		slot2.setOpaque(false);
		
		JButton slot3 = new JButton();	
		slot3.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			System.out.println("slot3");
    		}			
		});
		slot3.setBounds(326, 369, 400, 60);
		layeredPane.add(slot3,3);
		slot3.setOpaque(false);
			
		setContentPane(layeredPane);        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
    }
    
    public static void main(String[] arguments) {
		LoadFile frame = new LoadFile();
    }
}
