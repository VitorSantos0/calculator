package visao;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Calculadora extends JFrame{


	public Calculadora() {
		organizarLayout();
		//nao maximizar
		setResizable(false);
		setSize(232,322);
		//encerrar ao fechar
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//centralizar janela
		setLocationRelativeTo(null);
		//visualizar a tela
		setVisible(true);
		
	}
	

	public static void main(String[] args) {
		new Calculadora();
	}

	private void organizarLayout() {
		//incluindo o layout
		setLayout(new BorderLayout());
		
		//instanciando o display e o teclado
		
		Display display = new Display();
		//definindo o tamanho do display
		display.setPreferredSize(new Dimension(233, 60));
		//adicionando e posicionando 
		add(display, BorderLayout.NORTH);
		
		Teclado teclado = new Teclado();
		add(teclado, BorderLayout.CENTER);
	}
	
}
