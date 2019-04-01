package seguranca_sistemas_t1;

import java.util.ArrayList;
import java.util.List;

public class ListOfKeys {
	private List<Key> listOfKeys = new ArrayList<>();
	
	public void add(Key key) {
		if(listOfKeys.isEmpty())
			listOfKeys.add(key);
		boolean foundIndex = false;
		for(int i=0; i<listOfKeys.size(); i++) {
			if(listOfKeys.get(i).getDifIndexOfCoincidence() > key.getDifIndexOfCoincidence()) {
				listOfKeys.add(i, key);
				foundIndex = true;
				break;
			}
		}
		if(!foundIndex)
			listOfKeys.add(key);
	}
	
	public String toString() {
		return listOfKeys.toString();
	}
}
