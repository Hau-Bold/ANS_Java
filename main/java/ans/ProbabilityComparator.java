package ans;

import java.util.Comparator;

public class ProbabilityComparator implements Comparator<SymbolCorrespondingToProbability> {

	public int compare(SymbolCorrespondingToProbability o, SymbolCorrespondingToProbability other) {

		return -Double.compare(o.getProbability(), other.getProbability());

	}

}
