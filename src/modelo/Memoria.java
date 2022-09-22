package modelo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Memoria {

	private enum TipoComando {
		ZERAR, SINAL, NUMERO, SOMA, SUB, DIV, MULT, IGUAL, VIRGULA;
	}
	
	//utilizando o singleton 
	private static final Memoria instancia = new Memoria();

	private final List<MemoriaObservador> observadores = new ArrayList<MemoriaObservador>();

	//variaves logica
	private TipoComando ultimaOperacao = null;
	private boolean substituir = false;
	private String textoAtual = "";
	private String textoBuffer = "";

	private Memoria() {}

	public void adicionarObservador(MemoriaObservador observador) {
		observadores.add(observador);
	}

	//definindo metodo como atribudo da classe 
	public static Memoria getInstancia() {
		return instancia;
	}

	public String getTextoAtual() {
		//se o texto estiver vazio...
		return textoAtual.isEmpty() ? "0" : textoAtual;
	}

	public void processarComando(String texto) {

		TipoComando tipoComando = detectarTipoComando(texto);

		if (tipoComando == null) {
			return;
		} else if (tipoComando == TipoComando.ZERAR) {
			textoAtual = "";
			textoBuffer = "";
			substituir = false;
			ultimaOperacao = null;
		} else if (tipoComando == TipoComando.SINAL && textoAtual.contains("-")) {
			textoAtual = textoAtual.substring(1);
		} else if (tipoComando == TipoComando.SINAL && !textoAtual.contains("-")) {
			textoAtual = "-" + textoAtual;
		} else if (tipoComando == TipoComando.NUMERO) {
			textoAtual = substituir ? texto : textoAtual + texto;
			substituir = false;
		} else if (tipoComando == TipoComando.VIRGULA) {
			textoAtual = getTextoAtual() + texto;
			substituir = false;
		}else {
			substituir = true;
			textoAtual = obterResultadoOperacao();
			textoBuffer = getTextoAtual();
			ultimaOperacao = tipoComando;
		}

		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));
	}

	private String obterResultadoOperacao() {
		if (ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}

		double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));

		double resultado = 0;

		switch (ultimaOperacao) {
		case SOMA:
			resultado = numeroBuffer + numeroAtual;
			break;
		case SUB:
			resultado = numeroBuffer - numeroAtual;
			break;
		case MULT:			
			resultado = numeroBuffer * numeroAtual;
			break;
		case DIV:
			if(numeroAtual == 0) {
				JOptionPane.showMessageDialog(null, "Não é possível dividir por zero");
				break;
			}
			resultado = numeroBuffer / numeroAtual;
			break;
		default:
			resultado = 0;
			break;
		}

		String resultadoString = Double.toString(resultado).replace(".", ",");
		boolean inteiro = resultadoString.endsWith(",0");
		return inteiro ? resultadoString.replace(",0", "") : resultadoString;
	}

	private TipoComando detectarTipoComando(String texto) {
		if (textoAtual.isEmpty() && texto == "0") {
			return null;
		}

		try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			// quando n for numero
			switch (texto) {
			case "C":
				return TipoComando.ZERAR;
			case "/":
				return TipoComando.DIV;
			case "*":
				return TipoComando.MULT;
			case "+":
				return TipoComando.SOMA;
			case "-":
				return TipoComando.SUB;
			case "=":
				return TipoComando.IGUAL;
			case "±":
				return TipoComando.SINAL;
			case ",":
				if (textoAtual.contains(",")) {
					return null;
				}
				return TipoComando.VIRGULA;
			default:
				return null;
			}
		}

	}

}
