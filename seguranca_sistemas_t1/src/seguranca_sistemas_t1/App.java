/**
 * 
 */
package seguranca_sistemas_t1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Israel-PC Fontes: https://www.youtube.com/watch?v=Ge_mreVqVC4
 *         csilm.usu.edu/~securityninja/index.psp
 */
//Dado o a maior probabilidade, divide o texto em X Vigenere Columns, onde X é o tamanho da chave mais provável
public class App {

	private static App app = new App();
	private StringBuilder sb = new StringBuilder();

	//
	private Character[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	// Map que guarda a frequencia de cada letra do alfabeto
	private Map<Character, Integer> letterFrequency = new HashMap<>();;

	// Variavel que recebe o texto
	private String text = "Empty Text";

	// Tamanho do texto
	private int N;

	// Matriz de colunas diferentes que separam o texto cifrado
	private Map<Integer, ArrayList<Character>> matrix;

	// Lista final de possiveis tamanhos de chaves e frequencia
	private ListOfKeys listOfKeys = new ListOfKeys();

	// Matriz da cifra de Vigenere para conversao
	private Map<Character, ArrayList<Character>> vigenereChiperTable = new HashMap<>();

	private double[] PTLetterFrequency = { 0.1463, 0.0104, 0.0388, 0.0499, 0.1257, 0.0102, 0.0130, 0.0128, 0.0618,
			0.0040, 0.0002, 0.0278, 0.0474, 0.0505, 0.1073, 0.0252, 0.0120, 0.0653, 0.0781, 0.0434, 0.0463, 0.0167,
			0.0001, 0.0021, 0.0001, 0.0047 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// app.readFile(new File(args[0]));
		app.readFile(new File("DemCifrado6.txt"));

		app.N = app.text.length();

		for (int i = 0; i < app.alphabet.length; i++) {
			app.letterFrequency.put(app.alphabet[i], 0);
		}

		// System.out.println(app.decipher("avelino"));

//		PrintWriter writer;
//		try {
//			writer = new PrintWriter("decipheredText.txt", "UTF-8");
//			writer.println(app.decipher("avelino"));
//			writer.flush();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		app.fazTudo();

		int keyLength = app.listOfKeys.getKey(0).getKeyLength();
		System.out.println(keyLength);

		System.out.println("List of k: " + app.listOfKeys);

		/*
		 * For each iteration: 1- Iteratively divide text into columns of increasing
		 * size (period) 2- Calculate the following for each column 2.1- Where N is the
		 * lenght of the text and n1 through nc are the frequencies (as integers) of the
		 * c letters of the alphabet 3- Average the columns 4- The first period with an
		 * average near or above 1.73 is likely correct, others will be near 1.0 4.1 -
		 * Multiples of the correct period will also be high 5- Determine the shift for
		 * each column using frequency analysis and the correlation of frequency
		 */
	}

	public void fazTudo() {

		// Para numeros diferentes de colunas ateh o tamanho do texto
		for (int nOfColumns = 1; nOfColumns < N / 10; nOfColumns++) {

			// Para cada coluna, conta a frequencia de cada letra e armazena
			// Calcula o indexOfCoincidence() de cada coluna e adicona em um array
			List<Double> eachColumnsFrequency = new ArrayList<>();

			// Separa o texto em linhas e colunas de acordo
			int nOfLines = buildMatrix(nOfColumns);

			for (int i = 0; i < nOfColumns; i++) {
				for (int j = 0; j < nOfLines; j++) {
					try {
						matrix.get(j).get(i);
					} catch (IndexOutOfBoundsException e) {
						break;
					}
					letterFrequency.put(matrix.get(j).get(i), letterFrequency.get(matrix.get(j).get(i)) + 1);
				}
				// Calcula IndexOfCoincidence da coluna e poe resultado no array de Key
				eachColumnsFrequency.add(calcIC());
			}

			// Calcula a media do indice de frequencia das colunas e cria objeto Key
			double somaIOC = 0;
			for (Double IOC : eachColumnsFrequency) {
				somaIOC += IOC;
			}
			double avg = somaIOC / eachColumnsFrequency.size();

			if (avg > 0)
				listOfKeys.add(new Key(nOfColumns, avg));
		}
	}

	public String decipher(String key) {
		vigenereChiperTable();
		StringBuilder decipheredText = new StringBuilder();
		int keyPos = 0;
		for (int i = 0; i < text.length(); i++) {
			Character current = text.charAt(i);
			int charIndex = vigenereChiperTable.get(key.charAt(keyPos)).indexOf(current);
			decipheredText.append(alphabet[charIndex]);
			keyPos++;
			if (keyPos >= key.length())
				keyPos = 0;
		}
		return decipheredText.toString();
	}

	public void vigenereChiperTable() {
		int initPos = 0;
		for (int i = 0; i < alphabet.length; i++) {
			vigenereChiperTable.put(alphabet[i], new ArrayList<Character>());
			for (int j = initPos; vigenereChiperTable.get(alphabet[i]).size() < alphabet.length; j++) {
				if (j >= alphabet.length)
					j = 0;
				vigenereChiperTable.get(alphabet[i]).add(alphabet[j]);
			}
			initPos++;
		}
	}

	public double calcIC() {
		double ic = 0;
		int sum = 0;
		for (int i = 0; i < alphabet.length; i++) {
			sum += letterFrequency.get(alphabet[i]);
		}

		for (int i = 0; i < alphabet.length; i++) {
			double top = letterFrequency.get(alphabet[i]) * (letterFrequency.get(alphabet[i]) - 1);
			double bottom = sum * (sum - 1);
			ic += top / bottom;
			letterFrequency.put(alphabet[i], 0);
		}
		return ic;
	}

	public int buildMatrix(int nOfColumns) {
		matrix = new HashMap<>();
		int indexChar = 0;
		int nOfLines = (int) Math.ceil((double) N / nOfColumns);

		// Para cada linha da matriz com tamanho igual a quantidade de colunas
		for (int i = 0; i < nOfLines; i++) {
			matrix.put(i, new ArrayList<Character>());

			// Para cada coluna da linha, adiciona a letra
			for (int j = 0; j < nOfColumns; j++, indexChar++) {
				if (indexChar >= N)
					break;
				matrix.get(i).add(text.charAt(indexChar));
			}
		}

		return nOfLines;
	}

	public void printMatrix() {
		for (Map.Entry<Integer, ArrayList<Character>> entry : matrix.entrySet()) {
			for (Character c : entry.getValue()) {
				System.out.print(c + ", ");
			}
			System.out.println("\n");
		}
	}

	public void readFile(File file) {
		if (file.exists()) {
			System.out.println("Carregando arquivo " + file.getName() + "...\n");
		} else {
			System.out.println("ERRO: arquivo inexistente.\n");
			System.exit(1);
		}

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		text = sb.toString();
	}

//	public double indexOfCoincidence() {
//	int numerator = 0;
//	for (int i = 0; i < alphabet.length; i++) {
//		numerator += letterFrequency.get(alphabet[i]) * (letterFrequency.get(alphabet[i]) - 1);
//		letterFrequency.put(alphabet[i], 0);
//	}
//	// Retorna o índice de coicidencia de uma coluna
//	return numerator / ((double) N * (N - 1) / alphabet.length);
//}

//public double indexOfCoincidence() {
//	int numerator = 0;
//	for (int i = 0; i < alphabet.length; i++) {
//		numerator += letterFrequency.get(alphabet[i]) * (letterFrequency.get(alphabet[i]) - 1);
//		letterFrequency.put(alphabet[i], 0);
//	}
//
//	return numerator / ((double) N * (N - 1));
//}
}
