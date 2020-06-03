import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Target{
	private int x, y, size, length, width;
	private ArrayList<Point> pos = new ArrayList<Point>();
	private int nextPos;
    private Image targetPic, targetPicHit;
    private boolean isHit =false;
    private int speed;
    public static final int FORWARD = 1, BACKWARD = -1;
    private int dir;
    
    public Target(int size, int x, int y, int nextPos, int dir){
    	this.x = x;
        this.y = y;
        this.size = size;
        this.nextPos = nextPos;
        this.dir = dir;
        targetPic = new ImageIcon("Sprites/target"+size+".png").getImage();
        targetPicHit = new ImageIcon("Sprites/target"+size+" hit.png").getImage();
        if(size==0){
        	length = 50;
        	width=50;
        	speed = 6;
        }
        else if(size==1){
        	length=75;
        	width=75;
        	speed = 5;
        }
        else if(size==2){
        	length=120;
        	width=80;
        	speed = 4;
        }
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
    public int getSize(){return size;}
    public int getLength(){return length;}
    public int getWidth(){return width;}
   	public ArrayList<Point> getPoints(){return pos;}
   	public boolean getIsHit(){return isHit;}
   	public void setIsHit(boolean x){isHit=x;}
    
    public void addPos(Point newPos){
    	pos.add(newPos);
    }
    
    public void move(){
    	if(isHit == false){			
			if(nextPos<0){
	    		dir = FORWARD;
	    		nextPos = 1;    		
	    	}
	    	else if(nextPos>=pos.size()){
	    		dir = BACKWARD;    
	    		nextPos = pos.size()-2;
	    	}
			
	    	x += pos.get(nextPos).getX() > x ? speed:0;
	    	y += pos.get(nextPos).getY() > y ? speed:0;
	    	x -= pos.get(nextPos).getX() < x ? speed:0;
	    	y -= pos.get(nextPos).getY() < y ? speed:0;
	    	
	    	if(Math.abs(pos.get(nextPos).getX()-x)<=speed && Math.abs(pos.get(nextPos).getY()-y)<=speed){
	    		nextPos += dir;
	    	}
    	}
    }
    
    public void draw(Graphics g){
    	if(isHit){
    		g.drawImage(targetPicHit, x-(length/2), y-(width/2), null);
    	}
    	else{
    		g.drawImage(targetPic, x-(length/2), y-(width/2), null);
    	}
    }
}