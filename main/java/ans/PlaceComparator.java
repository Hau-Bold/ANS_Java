package ans;

import java.util.Comparator;

public class PlaceComparator implements Comparator<SymbolCorrespondingToProbability> {

	public int compare(SymbolCorrespondingToProbability o, SymbolCorrespondingToProbability other) {

		return Integer.compare(o.getPlace(), other.getPlace());

	}

}
