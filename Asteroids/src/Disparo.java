/**
 * @author: Daniel Serrano Rodríguez
 */

import java.awt.*;

public class Disparo {
	//Variables de disparo
	final double velocidadDisparo=12; 
	double x,y,xVelocidad,yVelocidad; 
	int vidaDisparo; 
	//Constructor de disparo
	public Disparo(double x, double y, double angulo, double naveXVel, 
			double naveYVel, int vidaDisparo){
		 
		this.x=x;
		this.y=y;
		//Calcula la direccion de disparo
		xVelocidad=velocidadDisparo*Math.cos(angulo)+naveXVel;
		yVelocidad=velocidadDisparo*Math.sin(angulo)+naveYVel;
		
		this.vidaDisparo=vidaDisparo;
	}
	//Actualiza el movimiento de los disparos
	public void mover(int anchoPantalla, int altoPantalla){
		 
		vidaDisparo--; 
		x+=xVelocidad; 
		y+=yVelocidad;
		
		if(x<0){ 
			x+=anchoPantalla; 
		}
		else if(x>anchoPantalla){
			x-=anchoPantalla;
		}
		if(y<0){
			y+=altoPantalla;
		}
		else if(y>altoPantalla){
			y-=altoPantalla;
		}
	}
	//Usa los datos de movimiento actualizado para repintar y crear el movimiento
	public void paint(Graphics2D g){
		g.setColor(Color.WHITE); 
		g.fillOval((int)(x-.5), (int)(y-.5), 3, 3);
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public int getVidaDisparo(){
		return vidaDisparo;
	}
	
}