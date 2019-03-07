package encryption;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * the class BitMask
 *
 */
public class BitMask {

	private List<ArrayList<Integer>> listOfRests = new ArrayList<ArrayList<Integer>>();

	public List<ArrayList<Integer>> getListOfRests() {
		return listOfRests;
	}

	public void setListOfRests(List<ArrayList<Integer>> listOfRests) {
		this.listOfRests = listOfRests;
	}

	public void addBit(ArrayList<Integer> bit) {
		listOfRests.add(bit);
	}

	public void clear() {
		listOfRests.clear();
	}

}
