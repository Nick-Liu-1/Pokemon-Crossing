import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

class Rocket{
	private int x, y, bulletX, bulletY;
	private boolean isShooting;
    private Image rocketPic, bulletPic;
    
    public Rocket(int x, int y){
    	this.x = x;
        this.y = y;
        bulletX = -1;
        bulletY = -1;
        rocketPic = new ImageIcon("Sprites/rocket.png").getImage();
        bulletPic = new ImageIcon("Sprites/bullet.png").getImage();
    }
    
    public int getX(){return x;}
    public int getY(){return y;}
	public int getBulletX(){return bulletX;}
    public int getBulletY(){return bulletY;}
    public boolean getIsShooting(){return isShooting;}
    public void setIsShooting(boolean x){isShooting=x;}

    public void move(int n){
    	if(x+n > 72 && x+n < 950){
    		x+=n;
    	}
    }
    
    public void shoot(){
    	if(isShooting==false){
    		isShooting=true;
    		bulletX=x;
    		bulletY=y;
    	}
    }
    
    public void moveBullet(){
    	bulletY-=7;
    }
    
    public void draw(Graphics g){
    	g.drawImage(rocketPic, x-50, y-29, null);
    	if(isShooting){
    		g.drawImage(bulletPic, bulletX-10, bulletY-20, null);
    	}
    }
}