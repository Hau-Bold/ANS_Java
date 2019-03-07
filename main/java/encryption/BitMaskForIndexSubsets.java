package encryption;

import java.util.ArrayList;
import java.util.List;

public class BitMaskForIndexSubsets {

	private List<Integer> listOfRests = new ArrayList<Integer>();

	public List<Integer> getListOfRests() {
		return listOfRests;
	}

	public void setListOfRests(List<Integer> listOfRests) {
		this.listOfRests = listOfRests;
	}

	public void addBit(Integer bit) {
		listOfRests.add(bit);
	}

	public void clear() {
		listOfRests.clear();
	}

	public Integer get(int receiving) {

		return listOfRests.get(receiving);
	}

}
