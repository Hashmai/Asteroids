/**
 * @author: Daniel Serrano Rodríguez
 */

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Game extends JFrame{
	//Variables de Game
	private JPanel contentPane;
	private int vidas;
	private int puntuacion;
	static boolean pantalla;
	//Arranque de la clase
	public static void main (String[] args){
		@SuppressWarnings("unused")
		Game frame = new Game();
	}
	//Constructor de la clase
	public Game(){
		//Propiedades del frame
		setIconImage (new ImageIcon(getClass().getResource("/images.jpg")).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(5, 5, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		this.setLocationRelativeTo(null);
		this.setTitle("Asteroids - Daniel Serrano Rodríguez");
		setContentPane(contentPane);
		setVisible(true);
		
		crearEspacio();
		
	}
	private void crearEspacio() {
		// TODO Auto-generated method stub
		vidas = 3;
		puntuacion = 0;
		Espacio s = new Espacio(new int[]{1024, 768}, vidas, puntuacion);
		contentPane.add(s);
		s.createBufferStrategy(2);
		s.toThread();
	}
}
