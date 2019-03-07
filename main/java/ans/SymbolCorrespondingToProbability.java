package ans;

import java.util.ArrayList;
import java.util.List;

public class SymbolCorrespondingToProbability {

	private String symbol;
	private double probability;
	private List<Integer> indexSubset = new ArrayList<Integer>();

	private int frequency;

	private int moduloRest;
	private int powerOfTwo;
	private int place;

	/**
	 * Constructor
	 * 
	 * @param symbol
	 *            - the symbol
	 * @param frequency
	 *            - the frequency
	 * @param probability
	 *            - the approximated probability
	 * @param symbolCounter
	 */
	public SymbolCorrespondingToProbability(String symbol, int frequency, double probability, int s) {

		this(symbol, probability, s);
		this.frequency = frequency;
		setPowerOfTwo((int) (1.0 / probability));

	}

	/**
	 * Constructor
	 * 
	 * @param symbol
	 *            - the symbol
	 * @param probability
	 *            - the approximated probability
	 */
	public SymbolCorrespondingToProbability(String symbol, double probability, int s) {
		this.symbol = symbol;
		this.probability = probability;
		this.place = s;
		setPowerOfTwo((int) (1.0 / probability));
	}

	/**
	 * default constructor.
	 */
	public SymbolCorrespondingToProbability(String symbol, int frequency) {
		this.symbol = symbol;
		this.frequency = frequency;

		setProbability(1.0 / frequency);
	}

	/**
	 * adds an index to the subset cooresponding to symbol
	 * 
	 * @param index
	 *            - the index
	 */
	public void addIndex(int index) {
		indexSubset.add(index);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setProbability(double approxProbability) {
		this.probability = approxProbability;
	}

	public double getProbability() {
		return probability;
	}

	public void getSubsets() {
		for (int i = 0; i < indexSubset.size(); i++) {
			System.out.print(indexSubset.get(i));
		}
	}

	public void clearSubset() {
		indexSubset.clear();
	}

	public List<Integer> getIndexSubset() {
		return indexSubset;
	}

	public void setIndexSubset(List<Integer> indexSubset) {
		this.indexSubset = indexSubset;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getFrequency() {
		return frequency;
	}

	public int getModuloRest() {
		return moduloRest;
	}

	public void setModuloRest(int moduloRest) {
		this.moduloRest = moduloRest;
	}

	public int getPowerOfTwo() {
		return powerOfTwo;
	}

	public void setPowerOfTwo(int binaryPotency) {
		this.powerOfTwo = binaryPotency;
	}

	public int getPlace() {
		return place;
	}

	public void setPlace(int place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "[Symbol: " + symbol + " approxProbability: " + probability + " frequency: " + frequency + " place: "
				+ place + " ]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + powerOfTwo;
		result = prime * result + frequency;
		result = prime * result + ((indexSubset == null) ? 0 : indexSubset.hashCode());
		result = prime * result + moduloRest;
		long temp;
		temp = Double.doubleToLongBits(probability);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SymbolCorrespondingToProbability other = (SymbolCorrespondingToProbability) obj;
		if (powerOfTwo != other.powerOfTwo)
			return false;
		if (frequency != other.frequency)
			return false;
		if (indexSubset == null) {
			if (other.indexSubset != null)
				return false;
		} else if (!indexSubset.equals(other.indexSubset))
			return false;
		if (moduloRest != other.moduloRest)
			return false;
		if (Double.doubleToLongBits(probability) != Double.doubleToLongBits(other.probability))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

}
