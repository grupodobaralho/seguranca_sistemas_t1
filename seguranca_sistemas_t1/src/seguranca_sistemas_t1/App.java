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

	private double[] USLetterFrequency = { 0.082, 0.014, 0.028, 0.038, 0.131, 0.029, 0.020, 0.053, 0.064, 0.001, 0.004,
			0.034, 0.025, 0.071, 0.080, 0.020, 0.001, 0.068, 0.061, 0.105, 0.025, 0.009, 0.015, 0.002, 0.020, 0.001 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// app.readFile(new File(args[0]));
		app.readFile(new File("DemCifrado2.txt"));

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
		// System.out.println(keyLength);

		int nOfLines = app.buildMatrix(keyLength);

		//app.buildShiftMatrix(keyLength);		
		
		StringBuilder column = new StringBuilder();
		for(int i=0; i<keyLength; i++) {
			for(int j=0; j< app.matrix.size(); j++) {
				System.out.println(column.append(app.matrix.get(j)));
				try {					
				} catch (IndexOutOfBoundsException e) {
					
				}
			}
			System.out.println(" se liga "+column.toString());
		}
		for (Map.Entry<Integer, ArrayList<Character>> entry : app.matrix.entrySet()) {
			for (Character c : entry.getValue()) {
				System.out.print(c + ", ");
			}
			System.out.println("\n");
		}
		
		
		for (int i = 0; i < keyLength; i++) {
			app.letterFrequency = new HashMap<>();
			
			for (int j = 0; j < app.alphabet.length; j++) {
				app.letterFrequency.put(app.alphabet[j], 0);
			}
			
			app.calculateFrequency(nOfLines, i);
			System.out.println(app.letterFrequency);
			//System.out.println(app.calculateXSquare2());
		}

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

	public void calculateFrequency(int nOfLines, int column) {

		for (int j = 0; j < nOfLines; j++) {
			try {
				app.matrix.get(j).get(column);
			} catch (IndexOutOfBoundsException e) {
				break;
			}
			app.letterFrequency.put(app.matrix.get(j).get(column),
					app.letterFrequency.get(app.matrix.get(j).get(column)) + 1);
		}
		//System.out.println(app.letterFrequency);
		// System.out.println(app.calculateXSquare2());

	}

	public void fazTudo() {

		// Para numeros diferentes de colunas ateh o tamanho do texto
		for (int nOfColumns = 1; nOfColumns < N; nOfColumns++) {

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

	public void buildShiftMatrix(int keyLength) {
		// NDaColuna por valores de X^2 para cada letra do alfabeto
		Map<Integer, ArrayList<Double>> shiftMatrix = new HashMap<>();

		for (int i = 0; i < keyLength; i++) {
			shiftMatrix.put(i, new ArrayList<Double>());
			for (int j = 0; j < alphabet.length; j++) {

			}
		}
	}

//	public double calculateXSquare() {
//		double fi = 0;
//		double Fi = 0;
//		double sum = 0;
//		for (int i = 0; i < alphabet.length; i++) {
//			if (letterFrequency.get(alphabet[i]) > 0) {
//				if(i != 2) {
//
//				fi = letterFrequency.get(alphabet[i]) / 10.0;
//				System.out.println(fi);
//				Fi = USLetterFrequency[i];
//				double resp = Math.pow((fi - Fi), 2) / Fi;
//				sum += Math.pow((fi - Fi), 2) / Fi;
//				}
//				System.out.println("(#fi:# " + fi + " - #Fi:# " + Fi + ") / " + Fi);
//				//System.out.println("resp: " + resp);
//				System.out.println("sum: " + sum);
//				letterFrequency.put(alphabet[i], 0);
//			}
//		}
//		return sum;
//	}

	public double calculateXSquare2() {
		for (int j = 0; j < alphabet.length; j++) {
			double sum = 0;
			double fi = 0;
			for (int i = 0; i < text.length(); i++) {
				if (text.charAt(i) == alphabet[j])
					fi++;
			}
			System.out.println(alphabet[j] + " fi: " + fi);

			double Fi = text.length() * USLetterFrequency[j];

			System.out.println(alphabet[j] + " Fi: " + Fi + " UFLF: " + USLetterFrequency[j]);
			sum += Math.pow(fi - Fi, 2) / Fi;
			System.out.println(j + "" + sum);
		}
		return 0;

//		private static char setMostCommonLetter(String column) {
//	        cypher.clear();
//	        int higherFreq = 0;
//	        String commonLetter = "";
//	        char keyLetter = ' ';
//	        double lowerChiSquad = Double.POSITIVE_INFINITY;
//	        int count = column.length();
//
//	        HashMap<Character, Integer> frequency = countLetterFreq(column);
//	        HashMap<Character, Double> chiSquaredList = new HashMap<>();
//
//	        for (int i = 0; i < alphabet.length; i++) {
//	            double chiSquared = 0;
//	            for (String letter: MutualIndex.get().keySet()) {
//	                char letterChar = letter.toCharArray()[0];
//	                int letterIndex = Alphabet.get().indexOf(letterChar) + i;
//	                if (letterIndex >= Alphabet.get().length()) {
//	                    letterIndex = letterIndex - Alphabet.get().length();
//	                }
//	                if (frequency.containsKey(Alphabet.get().charAt(letterIndex))) {
//	                    int letterFreq = frequency.get(Alphabet.get().charAt(letterIndex));
//	                    double mtFreq = MutualIndex.get().get(letter) * column.length();
//	                    chiSquared += Math.pow((letterFreq - mtFreq), 2) / mtFreq;
//	                }
//	            }
//	            chiSquaredList.put(Alphabet.get().charAt(i), chiSquared);
//	        }
//
//	        for (char letter: chiSquaredList.keySet()) {
//	            if (chiSquaredList.get(letter) < lowerChiSquad ) {
//	                lowerChiSquad = chiSquaredList.get(letter);
//	                keyLetter = letter;
//	            }
//	        }
//
//	        return keyLetter;
//	    }

//		for (int i = 0; i < alphabet.length; i++) {
//			System.out.println(letterFrequency.get(alphabet[i]));
//			fi = letterFrequency.get(alphabet[i]) / 10.0;
//			Fi = USLetterFrequency[i] * 10;
//			double resp = Math.pow((fi - Fi), 2) / Fi;
//			sum += Math.pow((fi - Fi), 2) / Fi;
//			letterFrequency.put(alphabet[i], 0);
//		}
//		return sum;
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
