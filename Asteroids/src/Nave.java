/**
 * @author: Daniel Serrano Rodríguez
 */

import java.awt.*;

public class Nave {

	//Variable de nave
	final double[] origenNaveX={15,-20,-15,-15,-20},origenNaveY={0,-12,-10,10,12},interiorNaveX={14,-14,-14},interiorNaveY={0,-9,9},origenLlamaX={-15,-30,-15},origenLlamaY={-6,0,6};  
	final int radio=12;  
	double x, y, angulo, velocidadX, velocidadY, aceleracion, reduccionVel, velocidadRotacional;  
	boolean girarIzquierda, girarDerecha, acelerador, parpadeo = true; 
	int[] actualNaveX, actualNaveY, actualInteriorX, actualInteriorY, actualLlamaX, actualLlamaY; 
	int disparoRetraso, disparoRetrasoIzquierda;
	int vida;
	Espacio esp;
	//Constructor de nave
	public Nave(double x, double y, double angulo, double aceleracion,  double reduccionVel, double velocidadRotacional, int disparoRetraso, Espacio esp){
		
		this.x=x;   
		this.y=y;   
		
		this.angulo=angulo;   
		
		this.aceleracion=aceleracion;   
		this.reduccionVel=reduccionVel;   
		this.velocidadRotacional=velocidadRotacional;   
		
		velocidadX=0;   
		velocidadY=0;   
		
		girarIzquierda=false;  
		girarDerecha=false;   
		
		acelerador=false;   
		
		actualNaveX=new int[5];    
		actualNaveY=new int[5];   
		actualInteriorX=new int[3];
		actualInteriorY=new int[3];
		actualLlamaX=new int[3];   
		actualLlamaY=new int[3];   
		
		vida = 3;
		
		this.disparoRetraso=disparoRetraso;   
		disparoRetrasoIzquierda=0;  
		
		this.esp = esp;
	}
	//Usa los datos de movimiento actualizado para repintar y crear el movimiento
	public void paint (Graphics2D g){
		
		if(acelerador){ 
			for(int i=0;i<3;i++){
				actualLlamaX[i]=(int)(origenLlamaX[i] * Math.cos(angulo) - origenLlamaY[i] * Math.sin(angulo) + x +0.5);
				
				actualLlamaY[i]=(int)(origenLlamaX[i] * Math.sin(angulo) + origenLlamaY[i] * Math.cos(angulo) + y +0.5);
			}
			if(parpadeo==true){
				g.setColor(Color.WHITE);
				parpadeo = false;
			}
			else{
				g.setColor(Color.BLACK);
				parpadeo = true;
			}
			
			g.drawPolygon(actualLlamaX,actualLlamaY,3);
		}
		
		for(int i=0;i<3;i++){
			actualInteriorX[i]=(int)(interiorNaveX[i] * Math.cos(angulo) - interiorNaveY[i] * Math.sin(angulo) + x + 0.5); 
		
			actualInteriorY[i]=(int)(interiorNaveX[i] * Math.sin(angulo) + interiorNaveY[i] * Math.cos(angulo) + y + 0.5); 
		}
		
		g.setColor(Color.BLACK);
		g.fillPolygon(actualInteriorX,actualInteriorY,3);
		
		for(int i=0;i<5;i++){
			actualNaveX[i]=(int)(origenNaveX[i] * Math.cos(angulo) - origenNaveY[i] * Math.sin(angulo) + x + 0.5); 
		
			actualNaveY[i]=(int)(origenNaveX[i] * Math.sin(angulo) + origenNaveY[i] * Math.cos(angulo) + y + 0.5); 
		}
		
		g.setColor(Color.WHITE);
		g.drawPolygon(actualNaveX,actualNaveY,5);
	}

	//Actualiza el movimiento de la nave
	public void mover(int anchoPantalla, int altoPantalla){
		
		if(disparoRetrasoIzquierda>0){ 
			disparoRetrasoIzquierda--; 
		}
		
		if(girarIzquierda){ 
			angulo-=velocidadRotacional; 
		}
		
		if(girarDerecha){ 
			angulo+=velocidadRotacional; 
		}
		
		if(angulo>(2*Math.PI)){ 
			angulo-=(2*Math.PI);
		}		
		
		else if(angulo<0){
			angulo+=(2*Math.PI);
		}
		
		if(acelerador){ 
			
			velocidadX+=aceleracion*Math.cos(angulo);
			velocidadY+=aceleracion*Math.sin(angulo);
		}
		
		x+=velocidadX; 
		y+=velocidadY;
		
		velocidadX*=reduccionVel; 
		velocidadY*=reduccionVel; 
		
		if(x<0){ 
//			x+=anchoPantalla; 
			String s = "exitLeft#posX=760#posY=" + this.y + "#velX=" + this.velocidadX + "#velY=" + this.velocidadY + "#angle=" + this.angulo;
			esp.notificarSalida(false, s);
		}
		else if(x>anchoPantalla){
//			x-=anchoPantalla;
			String s = "exitRight#posX=0#posY=" + this.y + "#velX=" + this.velocidadX + "#velY=" + this.velocidadY + "#angle=" + this.angulo;
			esp.notificarSalida(true, s);
		}
		if(y<0){
			y+=altoPantalla;
			
		}
		else if(y>altoPantalla){
			y-=altoPantalla;
		}
	}
	
	public void setAcelerador(boolean acelerador){
		this.acelerador=acelerador; 
	}
	
	public void setGirarIzquierda(boolean girarIzquierda){
		this.girarIzquierda=girarIzquierda; 
	}
		
	public void setGirarDerecha(boolean girarDerecha){
		this.girarDerecha=girarDerecha;
	}
		
	public double getX(){
		return x; 
	}
		
	public double getY(){
		return y;
	}
		
	public double getRadio(){
		return radio; 
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setVelocidadX(double velocidadX) {
		this.velocidadX = velocidadX;
	}

	public void setVelocidadY(double velocidadY) {
		this.velocidadY = velocidadY;
	}
	//Libera los disparos al pulsar la barra de espacio
	public boolean puedeDisparar(){
		
		if(disparoRetrasoIzquierda>0){ 
			return false; 
		}
		else{
			return true;
		}
	}
	//Crea los disparos que lanza
	public Disparo dispara(){
		disparoRetrasoIzquierda=disparoRetraso; 
	
		return new Disparo(x,y,angulo,velocidadX,velocidadY,40);	
	}
	//Reconoce la posicion de los disparos enemigos al colisionar
	public boolean recibirDisparo(Disparo disparo){

		if(Math.pow(radio,2) > Math.pow(disparo.getX()-x,2)+Math.pow(disparo.getY()-y,2)){
				return true;
		}
		return false;
	}
	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}
	public void setDisparando(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
} 