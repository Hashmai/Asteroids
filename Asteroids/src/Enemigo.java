/**
 * @author: Daniel Serrano Rodríguez
 */

import java.awt.Color;
import java.awt.Graphics2D;

public class Enemigo {
	//Variables de enemigo
	private double[] origenEnemyX={-3,3,1,-1,-3,-9,-4,4,9,3,9,-9},origenEnemyY={5,5,8,8,5,0,-4,-4,-0,5,0,0};
	private double x, y, angulo, xVelocidad, yVelocidad, radio;
	private int numDisparos, tipo;
	private int[] actualEnemyX, actualEnemyY;
	private double[] multiplicadorTamaño = {3, 1};
	private boolean vivo;
	private Espacio s;
	int disparoRetraso, disparoRetrasoIzquierda;
	private int[] multiplicadorPuntos = {200, 1000};
	private int puntuacion;
	//Constructor de enemigo
	public Enemigo(Espacio s, double radio, double angulo, double minVelocidad, double maxVelocidad, boolean vivo, int tipo, int disparoRetraso, int puntuacion) {

		this.s=s;
		//Calcula la posicion en la que saldra el enemigo
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
		this.radio = radio;
		this.angulo = angulo;
		//Cambia el tamaño segun el tipo de enmigo
		for(int i = 0; i<12; i++){
			origenEnemyX[i] = origenEnemyX[i] * multiplicadorTamaño[tipo];
			origenEnemyY[i] = origenEnemyY[i] * multiplicadorTamaño[tipo];
		}
		
		this.actualEnemyX = new int[12];
		this.actualEnemyY = new int[12];
		//Calcula la velocidad y direccion del enemigo
		double vel=minVelocidad + Math.random()*(maxVelocidad-minVelocidad),dir=2*Math.PI*Math.random(); 
		xVelocidad=vel*Math.cos(dir);
		yVelocidad=vel*Math.sin(dir);
				
		this.vivo = vivo;
		this.tipo = tipo;
		
		this.disparoRetraso=disparoRetraso; 
		disparoRetrasoIzquierda=0;
		//Marca la cantidad de puntos a recibir despues de destruirlo dependiendo del tipo de enemigo
		this.puntuacion = puntuacion * multiplicadorPuntos[tipo];
	}
	//Actualiza el movimiento de los enemigos
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
		
		for(int i=0;i<12;i++){
			actualEnemyX[i]=(int)(origenEnemyX[i] * Math.cos(3.1415) - origenEnemyY[i] * Math.sin(3.1415) + x + 0.5); 
		
			actualEnemyY[i]=(int)(origenEnemyX[i] * Math.sin(3.1415) + origenEnemyY[i] * Math.cos(3.1415) + y + 0.5);
		}
		
		g.setColor(Color.WHITE);
		g.drawPolygon(actualEnemyX,actualEnemyY,12);
	}
	//Calcula una colision reconociendo la posicion de la nave
	public boolean naveColisionE(Nave nave){
		
		if(Math.pow(radio+nave.getRadio(),2) > Math.pow(nave.getX()-x,2) + Math.pow(nave.getY()-y,2)){
			return true;
		}
		return false;
	}
	//Calcula una colision reconociendo la posicion de un disparo
	public boolean disparoColisionE(Disparo disparo){

		if(Math.pow(radio,2) > Math.pow(disparo.getX()-x,2)+Math.pow(disparo.getY()-y,2)){
				return true;
		}
		return false;
	}
	//Crea disparos
	public Disparo dispara(){
		disparoRetrasoIzquierda=disparoRetraso; 
		return new Disparo(x,y,angulo,xVelocidad,yVelocidad,40);	
	}

	public double[] getOrigenEnemyX() {
		return origenEnemyX;
	}

	public double[] getOrigenEnemyY() {
		return origenEnemyY;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAngulo() {
		return angulo;
	}

	public double getxVelocidad() {
		return xVelocidad;
	}

	public double getyVelocidad() {
		return yVelocidad;
	}

	public double getRadio() {
		return radio;
	}

	public int getNumDisparos() {
		return numDisparos;
	}

	public int[] getActualEnemyX() {
		return actualEnemyX;
	}

	public int[] getActualEnemyY() {
		return actualEnemyY;
	}

	public double[] getMultiplicadorTamaño() {
		return multiplicadorTamaño;
	}

	public boolean isVivo() {
		return vivo;
	}

	public Espacio getS() {
		return s;
	}

	public void setOrigenEnemyX(double[] origenEnemyX) {
		this.origenEnemyX = origenEnemyX;
	}

	public void setOrigenEnemyY(double[] origenEnemyY) {
		this.origenEnemyY = origenEnemyY;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}

	public void setxVelocidad(double xVelocidad) {
		this.xVelocidad = xVelocidad;
	}

	public void setyVelocidad(double yVelocidad) {
		this.yVelocidad = yVelocidad;
	}

	public void setRadio(double radio) {
		this.radio = radio;
	}

	public void setNumDisparos(int numDisparos) {
		this.numDisparos = numDisparos;
	}

	public void setActualEnemyX(int[] actualEnemyX) {
		this.actualEnemyX = actualEnemyX;
	}

	public void setActualEnemyY(int[] actualEnemyY) {
		this.actualEnemyY = actualEnemyY;
	}

	public void setMultiplicadorTamaño(double[] multiplicadorTamaño) {
		this.multiplicadorTamaño = multiplicadorTamaño;
	}

	public void setVivo(boolean vivo) {
		this.vivo = vivo;
	}

	public void setS(Espacio s) {
		this.s = s;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	
}
