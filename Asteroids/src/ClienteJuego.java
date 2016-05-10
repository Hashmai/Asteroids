/**
 * @author: Daniel Serrano Rodríguez
 */

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClienteJuego extends Thread{

	private static final int PORT = 1234;
	private static final String host = "localhost";
	
	public Socket socket;
	public PrintWriter pw;
    public BufferedReader br;
    public Espacio espacio;

	public ClienteJuego(Espacio espacio) {
		this.espacio = espacio;
	}
    
	public void run() {
		while (true) {
		    
		    crearConexion();
		    procesarCliente(br);
		  
		}
	}
	
	private void procesarCliente(BufferedReader br) {
		// TODO Auto-generated method stub
		String linea;
		
		try {
			while((linea=br.readLine())!= null){
				espacio.control(linea);
			}
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.setStackTrace(null);
		}
		
	}
	
	@SuppressWarnings("unused")
	private void crearConexion() {
		
		try {
        	while(socket==null){
        		
        		socket = new Socket(host, PORT);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                pw = new PrintWriter(socket.getOutputStream(), true);
        	}
            pw.println("juego");
        } 
        catch (Exception e) {
            System.out.println(e);
        }
    }
	
	public void notificarSalida (boolean direccion, String s){
		System.out.println(direccion);
		if(direccion == true){
			
			pw.println("naveSaleDer#" + s);

		}
		else if(direccion == false){
			
			pw.println("naveSaleIzq#" + s);
			
		}
		
	}

}
