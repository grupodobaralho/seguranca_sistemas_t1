package seguranca_sistemas_t1;

import java.util.ArrayList;
import java.util.List;

public class ListOfKeys {
	private List<Key> listOfKeys = new ArrayList<>();
	boolean foundFirstHigh = false;

	public void add(Key key) {
		boolean foundIndex = false;
		if (key.getKeyLength() != 1) {
			for (int i = 0; i < listOfKeys.size(); i++) {
				if (listOfKeys.get(i).getDifIndexOfCoincidence() > key.getDifIndexOfCoincidence()
						&& (key.getKeyLength() % listOfKeys.get(i).getKeyLength() != 0)) {
					listOfKeys.add(i, key);
					foundIndex = true;
					break;
				}
			}
			if (!foundIndex)
				listOfKeys.add(key);
		}
	}
	
	public Key getKey(int index) {
		return listOfKeys.get(index);
	}

	public String toString() {
		return listOfKeys.toString();
	}
	
//	private static char setMostCommonLetter(String column) {
//        cypher.clear();
//        int higherFreq = 0;
//        String commonLetter = "";
//        char keyLetter = ' ';
//        double lowerChiSquad = Double.POSITIVE_INFINITY;
//        int count = column.length();
//
//        HashMap<Character, Integer> frequency = countLetterFreq(column);
//        HashMap<Character, Double> chiSquaredList = new HashMap<>();
//
//        for (int i = 0; i < Alphabet.get().length(); i++) {
//            double chiSquared = 0;
//            for (String letter: MutualIndex.get().keySet()) {
//                char letterChar = letter.toCharArray()[0];
//                int letterIndex = Alphabet.get().indexOf(letterChar) + i;
//                if (letterIndex >= Alphabet.get().length()) {
//                    letterIndex = letterIndex - Alphabet.get().length();
//                }
//                if (frequency.containsKey(Alphabet.get().charAt(letterIndex))) {
//                    int letterFreq = frequency.get(Alphabet.get().charAt(letterIndex));
//                    double mtFreq = MutualIndex.get().get(letter) * column.length();
//                    chiSquared += Math.pow((letterFreq - mtFreq), 2) / mtFreq;
//                }
//            }
//            chiSquaredList.put(Alphabet.get().charAt(i), chiSquared);
//        }
//
//        for (char letter: chiSquaredList.keySet()) {
//            if (chiSquaredList.get(letter) < lowerChiSquad ) {
//                lowerChiSquad = chiSquaredList.get(letter);
//                keyLetter = letter;
//            }
//        }
//
//        return keyLetter;
//    }
}
