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

	// Arquivo a ser lido
	private String filename = "DemCifrado.txt";

	// Alfabeto da lingua portuguesa
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

	private double[] PTLetterFrequency = { 0.14634, 0.01043, 0.03882, 0.04992, 0.12570, 0.01023, 0.01303, 0.00781, 0.06186,
			0.00397, 0.00015, 0.02779, 0.04738, 0.04446, 0.09735, 0.02523, 0.01204, 0.06530, 0.06805, 0.04336, 0.03639, 0.01575,
			0.00037, 0.00253, 0.00006, 0.00470 };

	private double[] USLetterFrequency = { 0.082, 0.014, 0.028, 0.038, 0.131, 0.029, 0.020, 0.053, 0.064, 0.001, 0.004,
			0.034, 0.025, 0.071, 0.080, 0.020, 0.001, 0.068, 0.061, 0.105, 0.025, 0.009, 0.015, 0.002, 0.020, 0.001 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// app.readFile(new File(args[0]));
		app.readFile(new File(app.filename));

		app.N = app.text.length();

		for (int i = 0; i < app.alphabet.length; i++) {
			app.letterFrequency.put(app.alphabet[i], 0);
		}

		app.fazTudo();
		int keyLength = app.listOfKeys.getKey(0).getKeyLength();
		app.buildMatrix(keyLength);
		StringBuilder column = new StringBuilder();
		app.vigenereChiperTable();

		StringBuilder key = new StringBuilder();
		for(int i=0; i<keyLength; i++) {
			column = new StringBuilder();
			for(int j=0; j< app.matrix.size(); j++) {				
				try {	
					column.append(app.matrix.get(j).get(i));
				} catch (IndexOutOfBoundsException e) {
					
				}
			}
			System.out.println("THE COLUMN: "+ column);
			//System.out.println(column.toString());
			//System.out.println(app.getKeyLetter(column.toString()));
			key.append(app.getKeyLetter(column.toString()));		
		}	
		System.out.println(key.toString());

		// Parte 3 *************************************************************************************************************************
		
		// Decifrar o texto utilizando a chave encontrada
		String decipheredText = app.decipher(key.toString());
		System.out.println(decipheredText);
		app.createFile(decipheredText);
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

	public String shiftLetters(Character c, String column) {
		StringBuilder newColumn = new StringBuilder();
		ArrayList<Character> ac = vigenereChiperTable.get(c);
		int indexAlphabet = 0;
		for(int i=0; i< column.length(); i++) {
			for(int j=0; j<ac.size(); j++) {
			if(ac.get(j)==column.charAt(i)) {
				indexAlphabet = j;
				break;
			}				
		}
			newColumn.append(alphabet[indexAlphabet]);			
		}
		System.out.println(c + " - "+ newColumn);
		return newColumn.toString();
		
	}

	public Character getKeyLetter(String column) {
		double sumValues = 0;
		String analizingColumn = column;
		Map<Character, Double> chiSquaredMap = new HashMap<>();	
		for (int i = 1; i < alphabet.length; i++) {
			countLetterFreq(analizingColumn);			
			for (int j=0; j<alphabet.length; j++) {
				double ci = letterFrequency.get(alphabet[j]);
				double ei = PTLetterFrequency[j] * analizingColumn.length();
				sumValues += Math.pow(ci - ei, 2) / ei;
			}
			chiSquaredMap.put(alphabet[i-1], sumValues);
			analizingColumn = shiftLetters(alphabet[i] , column);
			sumValues = 0;		
		}
		
		Character leastValueC = new Character('1');
		Double leastValue = new Double(Double.MAX_VALUE);
		for(Character key : chiSquaredMap.keySet()) {
			if(leastValue > chiSquaredMap.get(key)) {
				leastValueC = key;
				leastValue = chiSquaredMap.get(key);
			}
		}
		System.out.println("Char: " + leastValueC);
		System.out.println("Value: " + leastValue);
		return leastValueC;

	}


	public void countLetterFreq(String column) {
		for (int i = 0; i < alphabet.length; i++) {
			letterFrequency.put(alphabet[i], 0);
		}
		for (int i = 0; i < column.length(); i++) {
			letterFrequency.put(column.charAt(i), letterFrequency.get(column.charAt(i)) + 1);
		}
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
	
	public void createFile(String text) {
		PrintWriter writer;
		try {
			writer = new PrintWriter("decipheredText.txt", "UTF-8");
			writer.println(text);
			writer.flush();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * For each iteration: 1- Iteratively divide text into columns of increasing
	 * size (period) 2- Calculate the following for each column 2.1- Where N is the
	 * lenght of the text and n1 through nc are the frequencies (as integers) of the
	 * c letters of the alphabet 3- Average the columns 4- The first period with an
	 * average near or above 1.73 is likely correct, others will be near 1.0 4.1 -
	 * Multiples of the correct period will also be high 5- Determine the shift for
	 * each column using frequency analysis and the correlation of frequency 
	 */
}
