import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class AstroBarrier extends JFrame implements ActionListener {
	private JLayeredPane layeredPane=new JLayeredPane();
    private javax.swing.Timer myTimer;
    GamePanel game; 

    public AstroBarrier() {
        super("Astro Barrier");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1020,695);

		JButton backBtn = new JButton();	
		backBtn.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e){
    			//BACK TO POKEMON CROSSING...
    			setVisible(false);
    		}
		});
		backBtn.setBounds(25, 16, 60, 38);
		layeredPane.add(backBtn,0);
		backBtn.setOpaque(false);

        game = new GamePanel();
        add(game);
        myTimer = new javax.swing.Timer(10, this);
		myTimer.start();
		
        setResizable(false);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt){
    	game.checkComplete();
		game.move();
		game.checkCollisions();
		game.repaint();
    }
    
     public static void main(String[] arguments) {
        AstroBarrier frame = new AstroBarrier();
    }
}

class GamePanel extends JPanel implements KeyListener{
	private Rocket rocket;
	private boolean [] keys;
	private boolean restarting = false;
	private ArrayList<Level> allLevels = new ArrayList<Level>();
	private int currentLevel=0;
	private int numLives = 3;
	private Image endScreen = new ImageIcon("Sprites/endScreen.png").getImage();
	private Image rocketPic = new ImageIcon("Sprites/rocketLives.png").getImage();
	private Image restartScreen = new ImageIcon("Sprites/endScreen.png").getImage();
	
	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		rocket = new Rocket(510, 548);
		try{
            Scanner levelsFile = new Scanner(new BufferedReader(new FileReader("Levels/allLevels.txt")));
            int numLevels = Integer.parseInt(levelsFile.nextLine());
            String[] line;
            for (int i = 0 ; i < numLevels; i++) {
                line = levelsFile.nextLine().split(" ");
                allLevels.add(new Level(i+1, new ImageIcon("Levels/"+line[0]).getImage(), Integer.parseInt(line[1]), Integer.parseInt(line[2]), Integer.parseInt(line[3])));
                for (int j=0; j<(Integer.parseInt(line[3]))*4; j+=4){
            		allLevels.get(allLevels.size()-1).addWall(new Rectangle(Integer.parseInt(line[3+j+1]), Integer.parseInt(line[3+j+2]), Integer.parseInt(line[3+j+3]), Integer.parseInt(line[3+j+4])));
            	}
            }
		}
        catch(FileNotFoundException e) {
            System.out.println("levels file not found");
        }
		
        addKeyListener(this);
        addMouseListener(new clickListener());
		setSize(1020,695);
	}
	
	public void addNotify(){
        super.addNotify();
        requestFocus();
    }
	
	public void move(){
		rocket.moveBullet();
		allLevels.get(currentLevel).moveTargets();
		if(keys[KeyEvent.VK_RIGHT] ){
			rocket.move(2);
		}
		if(keys[KeyEvent.VK_LEFT] ){
			rocket.move(-2);
		}
		if(keys[KeyEvent.VK_SPACE] && keys[KeyEvent.VK_LEFT]==false && keys[KeyEvent.VK_RIGHT]==false){
			if(allLevels.get(currentLevel).getNumBullets() > 0){
				rocket.shoot();
			}
		}
    }
    
    public void checkCollisions(){
    	if(rocket.getIsShooting()){
    		Rectangle bullet = new Rectangle(rocket.getBulletX(), rocket.getBulletY(), 1, 1);
    		for(int i=0; i<allLevels.get(currentLevel).getWalls().size(); i++){
    			if (bullet.intersects(allLevels.get(currentLevel).getWalls().get(i))){
    				rocket.setIsShooting(false);
    				allLevels.get(currentLevel).removeBullet();
    			}
    		}
    		for(int i=0; i<allLevels.get(currentLevel).getTargets().size(); i++){
    			Target target = allLevels.get(currentLevel).getTargets().get(i);
    			Rectangle tmp = new Rectangle(target.getX()-(target.getLength()/2), target.getY()-(target.getWidth()/2), target.getLength(), target.getWidth());
    			if (bullet.intersects(tmp) && target.getIsHit()==false){
    				rocket.setIsShooting(false);
    				allLevels.get(currentLevel).removeBullet();
    				allLevels.get(currentLevel).addWall(tmp);
    				allLevels.get(currentLevel).getTargets().get(i).setIsHit(true);
    			}
    		}
    	}
    }
    
    public void checkComplete(){
		if(allLevels.get(currentLevel).getComplete() && currentLevel<(allLevels.size()-1)){
			currentLevel++;
		}
		if(allLevels.get(currentLevel).getNumBullets() <= 0){
			numLives--;
		}
		if(numLives<=0){
			for(int i=0; i<allLevels.size(); i++){
				allLevels.get(i).restart();
			}
			numLives=3;
			currentLevel=0;
			restarting=true;
		}
		else if (allLevels.get(currentLevel).getNumBullets() <= 0 && numLives>0){
			allLevels.get(currentLevel).restart();
		}
	}
	
	public void paint(Graphics g){
		if(allLevels.get(currentLevel).getComplete() && currentLevel==(allLevels.size()-1)){
			g.drawImage(endScreen, 0, 0, null);
		}
		else{
			allLevels.get(currentLevel).draw(g);
			rocket.draw(g);
			for(int i=0; i<numLives; i++){
				g.drawImage(rocketPic, 700 + (i*60), 610, null);
			}
		}

	/*	if(restarting){
			g.drawImage(restartScreen, 0, 0, null);
			try {
 		    	Thread.sleep(5000);
			} 
			catch (InterruptedException ie) {
    			Thread.currentThread().interrupt();
			}
			restarting=false;
		}*/
    }
    
    public void keyTyped(KeyEvent e) {}
    
	public void keyPressed(KeyEvent e) {
	    keys[e.getKeyCode()] = true;
	}	
		    
	public void keyReleased(KeyEvent e) {
	    keys[e.getKeyCode()] = false;
	}
	
    class clickListener implements MouseListener{
	    public void mouseEntered(MouseEvent e) {}
	    public void mouseExited(MouseEvent e) {}
	    public void mouseReleased(MouseEvent e) {}
	    public void mouseClicked(MouseEvent e){}  
	    public void mousePressed(MouseEvent e){
	    	Point mouse = MouseInfo.getPointerInfo().getLocation();
			Point offset = getLocationOnScreen();
	    //	System.out.println((mouse.x-offset.x) + " " + (mouse.y-offset.y));
	    }
    }
		
}