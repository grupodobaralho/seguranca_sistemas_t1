package seguranca_sistemas_t1;

import java.util.ArrayList;
import java.util.List;

/**
 * Essa classe representa uma lista de chaves que ordena automaticamente da mais
 * provavel para a menos provavel
 * 
 * @author Israel Deorce Vieira Junior
 *
 */
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
}
