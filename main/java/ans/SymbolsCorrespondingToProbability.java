package ans;

import java.util.ArrayList;
import java.util.List;

/** the class SymbolsCorrespondingToProbability */

public class SymbolsCorrespondingToProbability {

	private double approximatedProbability;
	private List<String> listOfSymbols = new ArrayList<String>();

	/** Object knows its modulo rests **/
	private List<Integer> moduloRests = new ArrayList<Integer>();

	private double powerOfTwo;

	/***
	 * Constructor.
	 * 
	 * @param approximatedProbability
	 *            - the approximated probability
	 */
	public SymbolsCorrespondingToProbability(double approximatedProbability) {
		this.approximatedProbability = approximatedProbability;
		this.powerOfTwo = (int) 1.0 / approximatedProbability;
		generateRests(powerOfTwo);
	}

	private void generateRests(double powerOfTwo) {

		for (int i = 0; i < powerOfTwo; i++) {
			moduloRests.add(i);
		}

	}

	/**
	 * adds a symbol to the list
	 * 
	 * @param symbol
	 *            - the symbol
	 */
	public void addSymbol(String symbol) {
		listOfSymbols.add(symbol);
	}

	public double getApproximatedProbability() {
		return approximatedProbability;
	}

	public List<String> getListOfSymbols() {
		return listOfSymbols;
	}

	public List<Integer> getModuloRests() {
		return moduloRests;
	}

	public void setModuloRests(List<Integer> moduloRests) {
		this.moduloRests = moduloRests;
	}

	public double getPowerOfTwo() {
		return powerOfTwo;
	}

	public void setPowerOfTwo(double powerOfTwo) {
		this.powerOfTwo = powerOfTwo;
	}

}
