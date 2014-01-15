package project;

public class Position {
	
	double x,y;
	
	@SuppressWarnings("unused")
	private Position(){};
	
	public Position(double a, double b){
		x = a;
		y = b;
	}
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public void setX(double a){
		x = a;
	}
	public void setY(double b){
		y = b;
	}
	public String toString(){
		return "(" + x + "," + y + ")";
	}
}
