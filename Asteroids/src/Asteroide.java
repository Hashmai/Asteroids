/**
 * @author: Daniel Serrano Rodríguez
 */

import java.awt.*;

public class Asteroide extends Thread {
	//Variables asteroide
	double[] origenAstX={0, 30, 42.5, 30, 0, -30, -42.5, -30},origenAstY={42.5, 30, 0, -30, -42.5, -30, 0, 30};
	double x, y, xVelocidad, yVelocidad, radio;
	int numDisparos, numDivision;
	int[] actualAstX, actualAstY;
	private double[] multiplicadorTamaño = {0.5, 0.75, 1};
	private int[] multiplicadorPuntos = {100, 50, 10};
	private int puntuacion;
	private Espacio s;
	//Constructor asteroide para usar en la clase espacio
	public Asteroide(Espacio s, double x, double y, double radio,double minVelocidad, 
			double maxVelocidad,int numDisparos,int numDivision, int puntuacion){
		this.s=s;
		//Cambia el radio del asteroide dependiendo de cuantas veces se ha partido
		this.radio=radio * multiplicadorTamaño[numDisparos-1];
		//Hace que los asteroides aparezcan de los bordes de la ventana.
		if(numDisparos==3){
			int k;
			k = (int) (Math.random()*2)+1;
			if(k == 1){
				this.x=x;
				y = 0;
			}
			else{
				x = 0;
				this.y=y;
			}
		}
		else{
			this.x = x;
			this.y = y;
		}
		int k;
		k = (int) (Math.random()+1);
		if(k == 0){
			x = s.getWidth();
			y = 0;
		}
		else{
			x = 0;
			y = s.getHeight();
		}
		
		this.numDisparos=numDisparos; 
		this.numDivision=numDivision; 
		//Cambia el tamaño del dibujo del asteoide dependiendo las veces que se haya partido antes
		for(int i = 0; i<8; i++){
			origenAstX[i] = origenAstX[i] * multiplicadorTamaño[numDisparos-1];
			origenAstY[i] = origenAstY[i] * multiplicadorTamaño[numDisparos-1];
		}
		
		actualAstX = new int[8];
		actualAstY = new int[8];
		//Calcula una velocidad y una direccion aleatoria
		double vel=minVelocidad + Math.random()*(maxVelocidad-minVelocidad),
		dir=2*Math.PI*Math.random();
		xVelocidad=vel*Math.cos(dir);
		yVelocidad=vel*Math.sin(dir);
		//Marca los puntos que valdra cada asteroide segun su tamaño
		this.puntuacion = puntuacion * multiplicadorPuntos[numDisparos-1];
		
	}
	//Actualiza el movimiento de los asteroides
	public void mover(int anchoPantalla, int altoPantalla){
		x+=xVelocidad;
		y+=yVelocidad;
		
		if(x<0-radio)
		x+=anchoPantalla+2*radio;
		else if(x>anchoPantalla+radio)
		x-=anchoPantalla+2*radio;
		if(y<0-radio)
		y+=altoPantalla+2*radio;
		else if(y>altoPantalla+radio)
		y-=altoPantalla+2*radio;
	}
	//Usa los datos de movimiento actualizado para repintar y crear el movimiento
	public void paint(Graphics2D g){
		
		for(int i=0;i<8;i++){
			actualAstX[i]=(int)(origenAstX[i] - origenAstY[i] + x + 0.5); 
		
			actualAstY[i]=(int)(origenAstX[i] + origenAstY[i] + y + 0.5);
		}
		
		g.setColor(Color.WHITE);
		g.drawPolygon(actualAstX,actualAstY,8);
	}
	//Da variables nuevas a los trozos de asteroide despues de colisionar
	public Asteroide crearDivisionAsteroide(double minVelocidad, double maxVelocidad){
		
		return new Asteroide(s,x,y,radio/Math.sqrt(numDivision),
		minVelocidad,maxVelocidad,numDisparos-1,numDivision,1);
	}
	//Reconoce la posicion al colisionar con la nave
	public boolean naveColision(Nave nave){
		
		if(Math.pow(radio+nave.getRadio(),2) > Math.pow(nave.getX()-x,2) + Math.pow(nave.getY()-y,2)){
			return true;
		}
		return false;
	}
	//Reconoce la posicion al colisionar con la nave enemiga
	public boolean enemigoColision(Enemigo enemigo) {
		// TODO Auto-generated method stub
		if(Math.pow(radio+enemigo.getRadio(),2) > Math.pow(enemigo.getX()-x,2) + Math.pow(enemigo.getY()-y,2)){
			return true;
		}
		return false;
	}
	//Reconoce la posicion al colisionar con un disparo de la nave o del enemigo
	public boolean disparoColision(Disparo disparo){
		
		if(Math.pow(radio,2) > Math.pow(disparo.getX()-x,2)+Math.pow(disparo.getY()-y,2)){
			return true;
		}
		return false;
	}
	
	public int getNumDisparos(){
		
		return numDisparos;
	}
	
	public int getNumDivision(){
		return numDivision;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
}