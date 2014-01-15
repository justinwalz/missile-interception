package project;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class Bullet extends Rectangle {
	
	Position p;
	Velocity v;
	Acceleration a;
	double rotation;
	
	ImageIcon image;
	long fireTime;
	
	public Bullet(){
		image = new ImageIcon("images/bullet.jpg");
		fireTime = System.currentTimeMillis();
		p = new Position(0,0);
		x = y = 0;
		width = height = 10;
	}
	public Position getPosition(){
		return p;
	}
	public Velocity getVelocity(){
		return v;
	}
	public Acceleration getAcceleration(){
		return a;
	}
	public double getRotation(){
		return rotation;
	}
	public Image getImage(){
		return image.getImage();
	}
	public long getFireTime(){
		return fireTime;
	}
	public void setPosition(Position pos){
		p = pos;
		x = (int)pos.getX();
		y = (int)pos.getY();
	}
	public void setVelocity(Velocity x){
		v = x;
	}
	public void setAcceleration(Acceleration x){
		a = x;
	}
	public void setRotation(double r){
		rotation = r;
	}
	
	
}
