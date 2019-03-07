package lookup;

import java.util.Comparator;

import ans.SymbolsCorrespondingToProbability;

public class PowerOfTwoComparator implements Comparator<SymbolsCorrespondingToProbability> {

	public int compare(SymbolsCorrespondingToProbability o, SymbolsCorrespondingToProbability other) {

		return Double.compare(o.getPowerOfTwo(), other.getPowerOfTwo());
	}

}
