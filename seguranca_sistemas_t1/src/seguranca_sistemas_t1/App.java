/**
 * 
 */
package seguranca_sistemas_t1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

	private Map<Character, Integer> letterFrequency;

	private Character[] alphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	private String text = "Empty Text";
	private int N;
	private Map<Integer, ArrayList<Character>> matrix;

	private ListOfKeys listOfKeys = new ListOfKeys();
	int count = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// app.readFile(new File(args[0]));
		app.readFile(new File("DemCifrado.txt"));
		// System.out.println(app.text);

		app.N = app.text.length();

		app.letterFrequency = new HashMap<>();
		app.letterFrequency.put('a', 0);
		app.letterFrequency.put('b', 0);
		app.letterFrequency.put('c', 0);
		app.letterFrequency.put('d', 0);
		app.letterFrequency.put('e', 0);
		app.letterFrequency.put('f', 0);
		app.letterFrequency.put('g', 0);
		app.letterFrequency.put('h', 0);
		app.letterFrequency.put('i', 0);
		app.letterFrequency.put('j', 0);
		app.letterFrequency.put('k', 0);
		app.letterFrequency.put('l', 0);
		app.letterFrequency.put('m', 0);
		app.letterFrequency.put('n', 0);
		app.letterFrequency.put('o', 0);
		app.letterFrequency.put('p', 0);
		app.letterFrequency.put('q', 0);
		app.letterFrequency.put('r', 0);
		app.letterFrequency.put('s', 0);
		app.letterFrequency.put('t', 0);
		app.letterFrequency.put('u', 0);
		app.letterFrequency.put('v', 0);
		app.letterFrequency.put('w', 0);
		app.letterFrequency.put('x', 0);
		app.letterFrequency.put('y', 0);
		app.letterFrequency.put('z', 0);

		app.fazTudo();

		System.out.println(app.listOfKeys);
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

		int nOfColumns = 1;

		// Para numeros diferentes de colunas até o tamanho do texto
		for (; nOfColumns < N; nOfColumns++) {

			// Separa o texto em linhas e colunas de acordo
			int nOfLines = buildMatrix(nOfColumns);

			// Para cada coluna, conta a frequencia de cada letra e armazena
			// Calcula o indexOfCoincidence() de cada coluna e adicona em um array

			List<Double> eachColumnsFrequency = new ArrayList<>();

			for (int i = 0; i < nOfColumns; i++) {
				// letterFrequency = new HashMap<>();
				for (int j = 0; j < nOfLines; j++) {
					try {
						matrix.get(j).get(i);
					} catch ( IndexOutOfBoundsException e ) {
					    break;
					}
					letterFrequency.put(matrix.get(j).get(i), letterFrequency.get(matrix.get(j).get(i)) + 1);

				}
				// Calcula IndexOfCoincidence da coluna e poe resultado no array de Key
				eachColumnsFrequency.add(indexOfCoincidence());
			}

			// Calcula a média do índice de frequencia das colunas e cria objeto Key
			double somaIOC = 0;
			for (Double IOC : eachColumnsFrequency) {
				somaIOC = +IOC;
			}
			double avg = somaIOC / eachColumnsFrequency.size();
			
			listOfKeys.add(new Key(nOfColumns, avg));
			//System.out.println("List of Keys n" + count++ + " " + listOfKeys);
		}
	}

	public double indexOfCoincidence() {
		int numerator = 0;
		for (int i = 0; i < alphabet.length; i++) {
			numerator += letterFrequency.get(alphabet[i]) * (letterFrequency.get(alphabet[i]) - 1);
			letterFrequency.put(alphabet[i], 0);
		}
		// Retorna o índice de coicidencia de uma coluna
		return numerator / ((double) N * (N - 1) / alphabet.length);
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

}
