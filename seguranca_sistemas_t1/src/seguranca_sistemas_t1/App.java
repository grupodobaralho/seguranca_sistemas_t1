/**
 * 
 */
package seguranca_sistemas_t1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
	private Map<String,Integer> letterFrequency;
	private List<Integer> iCArray;
	
	
	private char[] alphabet = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
			};
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
	
	//public int 

	public void indexOfCoincidence() {
		return ;
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
