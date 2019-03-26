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

	private String text = "Empty Text";
	private Map<String, Integer> letterFrequency;
	private List<Integer> iCArray;

	private String[] alphabet = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q",
			"r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private int[] frequency = new int[alphabet.length];

	private int N;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// app.readFile(new File(args[0]));
		app.readFile(new File("DemCifrado.txt"));
		System.out.println(app.text);

		app.N = app.text.length();

		for (int i = 0; i < app.text.length(); i++) {
			char c = app.text.charAt(i);
		}

		app.letterFrequency = new HashMap<>();
		app.letterFrequency.put("a", 0);
		app.letterFrequency.put("b", 0);
		app.letterFrequency.put("c", 0);
		app.letterFrequency.put("d", 0);
		app.letterFrequency.put("e", 0);
		app.letterFrequency.put("f", 0);
		app.letterFrequency.put("g", 0);
		app.letterFrequency.put("h", 0);
		app.letterFrequency.put("i", 0);
		app.letterFrequency.put("j", 0);
		app.letterFrequency.put("k", 0);
		app.letterFrequency.put("l", 0);
		app.letterFrequency.put("m", 0);
		app.letterFrequency.put("n", 0);
		app.letterFrequency.put("o", 0);
		app.letterFrequency.put("p", 0);
		app.letterFrequency.put("q", 0);
		app.letterFrequency.put("r", 0);
		app.letterFrequency.put("s", 0);
		app.letterFrequency.put("t", 0);
		app.letterFrequency.put("u", 0);
		app.letterFrequency.put("v", 0);
		app.letterFrequency.put("w", 0);
		app.letterFrequency.put("x", 0);
		app.letterFrequency.put("y", 0);
		app.letterFrequency.put("z", 0);

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

	// public int

	public int indexOfCoincidence(String[] column) {
		int numerator = 0;
		for (int i = 0; i < column.length; i++) {
			letterFrequency.put(column[i], letterFrequency.get(column[i])+1);
		}
		for (int i = 0; i < alphabet.length; i++) {
			numerator += letterFrequency.get(alphabet[i]) * (letterFrequency.get(alphabet[i]) - 1);
			letterFrequency.put(alphabet[i], 0);
		}
		// Retorna o índice de coicidencia de uma coluna
		return numerator / (N * (N - 1) / alphabet.length);
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
