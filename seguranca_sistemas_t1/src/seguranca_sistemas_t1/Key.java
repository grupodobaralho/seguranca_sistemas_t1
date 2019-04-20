package seguranca_sistemas_t1;

/**
 * Classe que representa um possivel tamanho de chave. Armazena os valores de
 * indexe de coicidencia medio encontrado, compara com a media da lingua
 * portuguesa e atribui para futura ordenacao de chaves possiveis
 * 
 * @author Israel Deorce Vieira Junior
 *
 */
public class Key {

	private int keyLength;
	private double indexOfCoincidence;
	private double difIndexOfCoincidence;
	private static final double IOCpt = 0.0727;

	public Key(int keyLength, double indexOfCoincidence) {
		this.keyLength = keyLength;
		this.indexOfCoincidence = indexOfCoincidence;
		if (indexOfCoincidence >= IOCpt) {
			this.difIndexOfCoincidence = indexOfCoincidence - IOCpt;
		} else {
			this.difIndexOfCoincidence = IOCpt - indexOfCoincidence;
		}
	}

	public int getKeyLength() {
		return keyLength;
	}

	public double getDifIndexOfCoincidence() {
		return difIndexOfCoincidence;
	}

	public void setDifIndexOfCoincidence(double difIndexOfCoincidence) {
		this.difIndexOfCoincidence = difIndexOfCoincidence;
	}

	public static double getIocpt() {
		return IOCpt;
	}

	public void setKeyLength(int keyLength) {
		this.keyLength = keyLength;
	}

	public double getIndexOfCoincidence() {
		return indexOfCoincidence;
	}

	public void setIndexOfCoincidence(double indexOfCoincidence) {
		this.indexOfCoincidence = indexOfCoincidence;
	}

	public String toString() {
		return "<" + keyLength + ", " + indexOfCoincidence + ">";
	}
}
