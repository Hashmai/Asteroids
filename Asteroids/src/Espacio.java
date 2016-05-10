/**
 * @author: Daniel Serrano Rodríguez
 */

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

@SuppressWarnings("serial")
public class Espacio extends Canvas implements KeyListener, Runnable{
	//Variables de espacio
	private Game game;
	private int[] medidas;
	private Nave nave;
	private boolean pausa;
	private Disparo[] disparos;
	private Disparo[] disparosE;
	private int numDisparos, numDisparosE;
	private int contadorE;
	private boolean disparando;
	private int nivel;
	private int contador;
	private Asteroide[] asteroides;
	private Enemigo enemigo;
	private int numAsteroides; 
	private double astRadio, minAstVel,maxAstVel; 
	private int astNumDisparos,astNumDivision;
	private int vidas;
	private int puntuacion;
	private int cuentaAtras;
	private boolean reiniciar;
	private Asteroide[] asteroidesIntro;
	private int numAsteroidesIntro;
    private ClienteJuego client;
    private boolean naveCreada;
	
	//Cnstructor de espacio
	public Espacio(int[] medidas, int vidas, int puntuacion) {

		this.medidas = medidas;
		this.vidas = vidas;
		this.puntuacion = puntuacion;

		setBounds(0, 0, medidas[0], medidas[1]);
		
		addKeyListener(this);
        setFocusable(true);
        
        cuentaAtras = 10;
        nivel = 0;
        reiniciar = false;
        
		disparos = new Disparo [41];
		numDisparos= 0;
		
		disparosE = new Disparo [41];
		numDisparosE= 0;
		
		numAsteroides = 0;
		astRadio = 85;
		minAstVel = 2;
		maxAstVel = 4;
		astNumDisparos = 3;
		astNumDivision = 2;
		
		asteroidesIntro =new Asteroide [12];
		numAsteroidesIntro = 12;
		
		
		for(int i=0;i<numAsteroidesIntro;i++){
			asteroidesIntro[i]=new Asteroide(this, this.medidas[0], this.medidas[1], astRadio, 
					minAstVel, maxAstVel, astNumDisparos, astNumDivision, 1);
		}
		naveCreada = false;
//		nave = new Nave (this.getWidth()/2, this.getHeight()/2, 29.85, 0.2, 0.98, 0.05, 20);
		enemigo = new Enemigo (this, 10, 3.1415, 1, 2, false, 0, 40, 1);
	
		client = new ClienteJuego (this);
		
		setVisible(true);
	}
	//Reinicia el nivel al destruir todos los asteroides
	public void siguienteNivel(){ 
		nivel++;
		contador = 0;
		contadorE = 0;
		reiniciar = false;
		
		numDisparos=0; 
		pausa=false;
		disparando=false;
		
		if(nivel<9){
			asteroides =new Asteroide [(2+(nivel*2)) *(int)Math.pow(astNumDivision,astNumDisparos-1)+1];
			numAsteroides = 2+(nivel*2);
		}
		else{
			asteroides =new Asteroide [12 *(int)Math.pow(astNumDivision,astNumDisparos-1)+1];
			numAsteroides = 12;
		}
		
		for(int i=0;i<numAsteroides;i++){
			asteroides[i]=new Asteroide(this, Math.random()*this.getWidth(), Math.random()*this.getHeight(), 
					astRadio,minAstVel, maxAstVel, astNumDisparos, astNumDivision, 1);
		}
	}
	//Arranca el thread de espacio
	public void toThread() {
		// TODO Auto-generated method stub
		new Thread(this).start();
		client.start();
	}

	public int[] getMedidas() {
		return medidas;
	}

	public void setMedidas(int[] medidas) {
		this.medidas = medidas;
	}
		
	public int getVidas() {
		return vidas;
	}

	public void quitarVida() {
		this.vidas--;
		nave.setX(this.getWidth()/2);
		nave.setY(this.getHeight()/2);
		nave.setVelocidadX(0);
    	nave.setVelocidadY(0);
    	nave.setAngulo(29.85);
		if(this.vidas == 0){
			reiniciar=true;
		}
	}
	
	public boolean isNaveCreada() {
		return naveCreada;
	}
	public void setNaveCreada(boolean naveCreada) {
		this.naveCreada = naveCreada;
	}
	//Crea los graficos del canvas y conecta con los objetos a pintar
	private synchronized void paint() {
		// TODO Auto-generated method stub
		
		BufferStrategy bs;
		bs = this.getBufferStrategy();
		
		Graphics2D g = (Graphics2D) bs.getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		if(this.reiniciar == true){
			String ca = "" + cuentaAtras;
			g.setColor(Color.WHITE); 
			g.setFont(new Font ("Consolas", Font.BOLD, 80));
	        g.drawString("Puntuación: " + puntuacion, 100, 200);
	        
	        g.setColor(Color.WHITE); 
			g.setFont(new Font ("Consolas", Font.BOLD, 300));
	        g.drawString(ca, 400, 500);
	        
	        g.setColor(Color.WHITE); 
			g.setFont(new Font ("Consolas", Font.BOLD, 40));
			g.drawString("Pulsa enter para volver a jugar", 150, 600);
		}
		
		else if(this.reiniciar == false){
			for(int i=0;i<numDisparos;i++){ 
				 disparos[i].paint(g);
			}
			for(int i=0;i<numAsteroides;i++){
				 asteroides[i].paint(g);
			}
			
			if(this.isNaveCreada()==true && nave!=null){
				nave.paint(g);
			}
			
			if(enemigo.isVivo()==true){
				enemigo.paint(g);
				
				for(int i=0;i<numDisparosE;i++){ 
					 disparosE[i].paint(g);
				}
			}
			
			
			g.setColor(Color.WHITE); 
			g.drawString("Puntuacion " + puntuacion,20,20); 
			
			g.setColor(Color.WHITE); 
			g.drawString("Vida " + this.getVidas(),20,40);
		}
		
		g.dispose();
		bs.show();
	} 
	//Thread de espacio
	@Override
	public void run() {

		while(true){
			
			if(this.reiniciar==true){
				while(this.reiniciar == true && cuentaAtras>0){
						
						cuentaAtras--;
						this.paint();
					
					try {
						Thread.sleep(1000);
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(cuentaAtras == 0 && reiniciar == true){
					System.exit(0);
				}
			}
			else if(this.reiniciar==false){
				contador++;
			
				if (contador==2000){
					añadirEnemigo();
					contador = 0;
				}
				if(numAsteroides<=0){
					siguienteNivel();
				}
				
				if(pausa == false){
					
					this.paint();
					
					if(this.isNaveCreada()==true && nave!=null){
						nave.mover(this.getWidth(), this.getHeight());
					}
					
					if(enemigo.isVivo()==true){
						actualizarEnemigo();
					}
					
					for(int i=0;i<numDisparos;i++){
						
						disparos[i].mover(this.getWidth(), this.getHeight());
						 
						if(disparos[i].getVidaDisparo()<=0){
						 
							eliminarDisparos(i); 
							i--; 
						}
					}
					
					actualizarAsteroides();
					
					
					if(disparando && nave.puedeDisparar()){
						
						disparos[numDisparos]=nave.dispara();
						numDisparos++;
					}
					
				}
				
				try{
					Thread.sleep(25);
				}
				catch(InterruptedException e) {
					
					e.printStackTrace();
				}
			}
		}	
	}
	//Crea la posibilidad de la aparicion de un enemigo cada cierto tiempo
	private void añadirEnemigo() {
		
		if(enemigo.isVivo()==false){
			int k;
			k = (int)(Math.random()*1000)+1;
			if(k<=200){
				if(this.puntuacion<10000){
					enemigo.setTipo(0);
					enemigo.setVivo(true);
				}
				else{
					enemigo.setTipo(1);
					enemigo.setVivo(true);
				}
			}
		}
	}
	//Elimina los disparos al colisionar o alcanzar la distancia maxima
	private void eliminarDisparos(int num){
		
		numDisparos--;
		for(int i=num;i<numDisparos;i++)
			disparos[i]=disparos[i+1];
			disparos[numDisparos]=null;
	} 
	//Elimina asteroide a colisionar
	private void eliminarAsteroide(int num){
		
		numAsteroides--;
		
		for(int i=num;i<numAsteroides;i++){
			asteroides[i]=asteroides[i+1];
		}
		asteroides[numAsteroides]=null;
	}
	//Añade asteroides al colisionar
	private void añadirAsteroide(Asteroide ast){
		
		asteroides[numAsteroides]=ast;
		numAsteroides++;
	}
	/*Actualiza los movimientos de los enemigos, sus colisiones con los distintos objetos y 
	hace que dispare cada cierto tiempo*/
	private synchronized void actualizarEnemigo(){
		contadorE++;
		enemigo.mover(this.getWidth(), this.getHeight());
		
		try{
			if(contadorE==100){
				enemigo.setAngulo(Math.random()*(2 * Math.PI));
				disparosE[numDisparosE]=enemigo.dispara();
				numDisparosE++;
				contadorE=0;
			}
			
			for(int i=0;i<numDisparosE;i++){
				 disparosE[i].mover(this.getWidth(), this.getHeight());
				 
				 if(disparosE[i].getVidaDisparo()<=0){
					 eliminarDisparosE(i); 
					 i--; 
				 }
			}
			
			if(enemigo.naveColisionE(nave)){
				anadirPuntos(enemigo.getPuntuacion());
				quitarVida();
			}
			
			for(int j=0;j<numDisparos;j++){
				if(enemigo.disparoColisionE(disparos[j])){
					
					if(disparos[j]!=null){
						eliminarDisparos(j);
					}
					anadirPuntos(enemigo.getPuntuacion());
					enemigo.setVivo(false);
					j=numDisparos;
				
					j--;
				}
			}
			for(int j=0;j<numDisparosE;j++){
				if(nave.recibirDisparo(disparosE[j])){
					if(disparos[j]!=null){
						eliminarDisparos(j);
					}
					
					j=numDisparosE; 
					j--;
				
					quitarVida();
				}
			}
		}
		catch(Exception e) {}
	}
	//Elimina los disparos del enemigo
	private void eliminarDisparosE(int num) {
		
		numDisparosE--;
		for(int i=num;i<numDisparosE;i++)
			disparosE[i]=disparosE[i+1];
			disparosE[numDisparosE]=null;

	}
	
	//Actualiza los movimientos de los asteroides y registra las colisiones
	private synchronized void actualizarAsteroides(){
		for(int i=0;i<numAsteroides;i++){
			
			asteroides[i].mover(this.getWidth(), this.getHeight());
		
			try{
			
				if(asteroides[i].naveColision(nave) && nave!=null){
					anadirPuntos(asteroides[i].getPuntuacion());
					if(asteroides[i].getNumDisparos()>1 && nave!=null){
						for(int k=0;k<asteroides[i].getNumDivision(); k++){
							añadirAsteroide(asteroides[i].crearDivisionAsteroide(minAstVel,maxAstVel));
						}
					}
					eliminarAsteroide(i);
					i--;
					
					quitarVida();
				}
				
				if(enemigo.isVivo()==true && asteroides[i].enemigoColision(enemigo) && enemigo.isVivo() && nave!=null){
					if(asteroides[i].getNumDisparos()>1 && nave!=null){
						for(int k=0;k<asteroides[i].getNumDivision(); k++){
							añadirAsteroide(asteroides[i].crearDivisionAsteroide(minAstVel,maxAstVel));
						}
					}
					eliminarAsteroide(i);
					enemigo.setVivo(false);
					i--;
				}
				
				for(int j=0;j<numDisparos;j++){
					if(asteroides[i].disparoColision(disparos[j]) && nave!=null){
						
						if(disparos[j]!=null){
							eliminarDisparos(j);
						}
						anadirPuntos(asteroides[i].getPuntuacion());
						
						if(asteroides[i].getNumDisparos()>1 && nave!=null){
							for(int k=0;k<asteroides[i].getNumDivision(); k++){
								añadirAsteroide(asteroides[i].crearDivisionAsteroide(minAstVel,maxAstVel));
							}
						}

						eliminarAsteroide(i);
						j=numDisparos; 
						
						i--;
						j--;
					}
				}
				
				for(int j=0;j<numDisparosE;j++){
					if(asteroides[i].disparoColision(disparosE[j]) && !enemigo.isVivo()){
						
						if(disparosE[j]!=null && nave!=null){
							eliminarDisparosE(j);
						}
						
						if(asteroides[i].getNumDisparos()>1 && nave!=null){
							for(int k=0;k<asteroides[i].getNumDivision(); k++){
								añadirAsteroide(asteroides[i].crearDivisionAsteroide(minAstVel,maxAstVel));
							}
						}
						
						eliminarAsteroide(i);
						j=numDisparosE;
					
						i--;
						j--;
					}
				}
			}
			catch(Exception e) {}
		}
	}
	//Añade puntos despues de destruir un enemigo o un asteroide
	private void anadirPuntos(int puntos) {
		// TODO Auto-generated method stub
		this.puntuacion = this.puntuacion + puntos;
	}
	//Registra las pulsaciones de las teclas habilitadas y produce una accion asignada
	public void keyPressed(KeyEvent e) {

		int tecla = e.getKeyCode();
		
		if(nave!=null){
		
	        if (tecla == KeyEvent.VK_UP) {
	            nave.acelerador = true;
	        }
	
	        else if (tecla == KeyEvent.VK_RIGHT) {
	            nave.girarDerecha = true;
	        }
	        
	        else if (tecla == KeyEvent.VK_LEFT) {
	            nave.girarIzquierda = true;
	        }
	        
	        else if (tecla == KeyEvent.VK_P){
	        	if(pausa==false){
	        		pausa = true;
	        	}
	        	else if(pausa==true){
	        		pausa = false;
	        	}	
	        }
	        
	        else if (tecla == KeyEvent.VK_SPACE){
	        	this.disparando = true;
	        }
	        
	        else if (tecla == KeyEvent.VK_T){
	        	nave.setX(Math.random()*this.getWidth());
	        	nave.setY(Math.random()*this.getHeight());
	        }
	        else if(tecla == KeyEvent.VK_ENTER){
	        	
	        	if(this.reiniciar==true){
	        		this.vidas = 3;
		        	this.puntuacion = 0;
		        	this.nivel = 0;
		        	this.cuentaAtras = 10;
		        	
		        	reiniciar = false;
		        	
		        	siguienteNivel();
	        	}
	        }
	        else if(tecla == KeyEvent.VK_ESCAPE){
				System.exit(0);
			}
		}
    }
    //Registra las acciones a realizar despues de dejar de pulsar una tecla asignada
    public void keyReleased(KeyEvent e) {
        
    	if(nave!=null){
    		
    		int tecla = e.getKeyCode();
        
	        if (tecla == KeyEvent.VK_UP) {
	            nave.acelerador = false;
	        }
	        else if (tecla == KeyEvent.VK_RIGHT) {
	            nave.girarDerecha = false;
	        }
	        else if (tecla == KeyEvent.VK_LEFT) {
	            nave.girarIzquierda = false;
	        }
	        else if (tecla == KeyEvent.VK_SPACE){
	        	this.disparando = false;
	        }
    		
    	}
        
    }

	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}
	
	public void control (String accion) {
        System.out.println("Accion mando " + accion);
        
        if(nave!=null){
	        if (accion.equals("acelera")) {
	            nave.setAcelerador(true); 
	        }
	        if (accion.equals("izquierda")) {
	        	nave.setGirarIzquierda(true);
	        }
	        if (accion.equals("derecha")) {
	        	nave.setGirarDerecha(true);
	        }
	        if (accion.equals("disparar")) {
	        	this.disparando = true;
	        }
	        if (accion.equals("teletransporte")) {
	        	if(this.reiniciar==false){
		        	nave.setX(Math.random()*this.getWidth());
		        	nave.setY(Math.random()*this.getHeight());
	        	}
	        }
	        if(accion.equals("pausa")){
	        	if(pausa==false){
	        		pausa = true;
	        	}
	        	else if(pausa==true){
	        		pausa = false;
	        	}
	        }
	        if(accion.equals("start")){
	        	if(this.reiniciar==true){
	        		this.vidas = 3;
		        	this.puntuacion = 0;
		        	this.nivel = 0;
		        	this.cuentaAtras = 10;
		        	
		        	reiniciar = false;
		        	
		        	siguienteNivel();
	        	}
	        }
	        if(accion.equals("salir")){
	        	System.exit(0);
	        }
	        if (accion.equals("noAcelera")) {
	            nave.setAcelerador(false);
	        }
	        if (accion.equals("noIzquierda")) {
	            nave.setGirarIzquierda(false);
	        }
	        if (accion.equals("noDerecha")) {
	            nave.setGirarDerecha(false);
	        }
	        if (accion.equals("noDisparar")) {
	            this.disparando = false;
	        }
        }
        
        if (accion.equals("Crear nave")){
        	nave = new Nave (this.getWidth()/2, this.getHeight()/2, 29.85, 0.2, 0.98, 0.05, 20, this);
        	this.setNaveCreada(true);
        }
        if(accion.length()>11 && accion.substring(0,12).equals("crearJugador")){
        	
        	crearNave(accion);
        	
        }
        
    }
	
	private void crearNave(String accion) {
		
		// TODO Auto-generated method stub
		this.naveCreada=true;
		pausa = false;
		
		String [] info = accion.split("#");
		String [] xA = info[3].split("=");
		String [] yA = info[4].split("=");
		String [] vxA = info[5].split("=");
		String [] vyA = info[6].split("=");
		String [] anA = info[7].split("=");
		
		double x = Double.valueOf(xA[1]);
		double y = Double.valueOf(yA[1]);
		double velocidadx = Double.valueOf(vxA[1]);
		double velocidady = Double.valueOf(vyA[1]);
		double an = Double.valueOf(anA[1]);
		
		this.nave = new Nave (x, y, an, 0.2, 0.98, 0.05, 20, this);
		nave.setVelocidadX(velocidadx);
		nave.setVelocidadY(velocidady);
	}
	
	public void notificarSalida (boolean direccion, String s){
		
		client.notificarSalida(direccion, s);
		nave = null;		
		
	}
	

}