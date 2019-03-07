package lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import ans.ProbabilityComparator;
import ans.SymbolCorrespondingToProbability;
import ans.SymbolsCorrespondingToProbability;

public class LookUpUtils {

	static int counter = 0;

	public static void computeLookUpTable(CommonModel model, List<SymbolCorrespondingToProbability> receiving, int b,
			int L) {

		Vector<String> datarow = null;
		for (SymbolCorrespondingToProbability item : receiving) {
			datarow = new Vector<String>();
			datarow.add(String.valueOf(item.getSymbol()));
			double approxProbability = item.getProbability();
			int binaryPotency = (int) (1 / approxProbability);
			int moduloRest = item.getModuloRest();

			for (int i = L; i <= b * L - 1; i++) {
				Boolean aValueWasAdded = Boolean.FALSE;

				for (int k = 0; k < item.getIndexSubset().size(); k++) {

					Integer tmpValue = item.getIndexSubset().get(k) * binaryPotency + moduloRest;

					if (Integer.compare(i, tmpValue) == 0) {
						datarow.add(String.valueOf(item.getIndexSubset().get(k)));
						aValueWasAdded = Boolean.TRUE;
						break;
					}
				}

				if (!aValueWasAdded) {
					datarow.add("");
				}

			}
			model.addDataRow(datarow);
		}

	}

	/**
	 * prepares the symbols for being displayed in a lookUp table
	 * 
	 * @param receiving
	 *            - the list of SymbolCorrespondingToProbability's
	 * @param b
	 *            - the parameter b
	 * @param L
	 *            - the parameter L
	 * @return the prepared symbols:
	 */
	public static List<SymbolCorrespondingToProbability> prepareSymbols(
			List<SymbolCorrespondingToProbability> receiving, Integer b, Integer L) {

		/** copy */
		List<SymbolCorrespondingToProbability> temp = new ArrayList<SymbolCorrespondingToProbability>(receiving);

		/** initially the list has to be sorted by decreasing probability */
		ProbabilityComparator comparator = new ProbabilityComparator();
		Collections.sort(temp, comparator);

		/**
		 * yields all symbols with same probability in an object collected in a List
		 */
		List<SymbolsCorrespondingToProbability> lst = getSymbolsCorrespondingToProbalities(temp);

		/** compute rests */
		List<SymbolsCorrespondingToProbability> compResult = computePossibleRests(lst);

		/** separating symbols */
		List<SymbolCorrespondingToProbability> response = transformTo(compResult);

		/** computing the index subsets: */
		for (SymbolCorrespondingToProbability item : response) {
			generateIndexSubset(item, b, L);
		}

		/** setting again frequency */
		for (SymbolCorrespondingToProbability item : response) {
			for (SymbolCorrespondingToProbability correspondingToProbability : receiving) {
				if (item.getSymbol().equals(correspondingToProbability.getSymbol())) {
					item.setFrequency(correspondingToProbability.getFrequency());
				}
			}

		}

		return response;
	}

	/**
	 * Collects symbols with same probability from receiving
	 * 
	 * @param receiving
	 *            - the receiving
	 * @return - list of Object in which symbols with same probability are bundled.
	 */
	public static List<SymbolsCorrespondingToProbability> getSymbolsCorrespondingToProbalities(
			List<SymbolCorrespondingToProbability> receiving) {

		List<SymbolsCorrespondingToProbability> response = new ArrayList<SymbolsCorrespondingToProbability>();

		while (receiving.size() > 0) {

			SymbolCorrespondingToProbability item = receiving.get(0);
			receiving.remove(item);
			double currentProbability = item.getProbability();

			SymbolsCorrespondingToProbability correspondingToProbability = new SymbolsCorrespondingToProbability(
					currentProbability);
			correspondingToProbability.addSymbol(item.getSymbol());

			Iterator<SymbolCorrespondingToProbability> iterator = receiving.iterator();
			while (iterator.hasNext()) {

				SymbolCorrespondingToProbability currentItem = iterator.next();
				/** the current values */
				double innerCurrentProbabilty = currentItem.getProbability();
				String currentSymbol = currentItem.getSymbol();

				/** seek same probabilities */
				if (Double.compare(currentProbability, innerCurrentProbabilty) == 0) {
					correspondingToProbability.addSymbol(currentSymbol);
					iterator.remove();
				}
			}

			response.add(correspondingToProbability);
		}

		return response;
	}

	/**
	 * Computes the modulo rests for the items of the receiving , such that the
	 * arising index subsets are disjunct.
	 * 
	 * @param receiving
	 *            - the list of SymbolsCorrespondingToProbability
	 * @return items with possible index subsets
	 */
	public static List<SymbolsCorrespondingToProbability> computePossibleRests(
			List<SymbolsCorrespondingToProbability> receiving) {

		/** sort by powerOfTwo */
		Collections.sort(receiving, new PowerOfTwoComparator());

		/** the response */
		List<SymbolsCorrespondingToProbability> response = new ArrayList<SymbolsCorrespondingToProbability>();

		Iterator<SymbolsCorrespondingToProbability> iterator = receiving.iterator();

		while (iterator.hasNext()) {
			SymbolsCorrespondingToProbability item = iterator.next();

			int powerOfTwo = (int) item.getPowerOfTwo();

			List<Integer> candidate = new ArrayList<Integer>();
			for (int i = 0; i < item.getListOfSymbols().size(); i++) {
				candidate.add(item.getModuloRests().get(i));
			}
			item.setModuloRests(candidate);

			response.add(item);
			iterator.remove();

			Iterator<SymbolsCorrespondingToProbability> iterTMP = receiving.iterator();

			while (iterTMP.hasNext()) {
				SymbolsCorrespondingToProbability itemTMP = iterTMP.next();
				List<Integer> candidateIndices = new ArrayList<Integer>();
				List<Integer> candidateToRemoveRests = new ArrayList<Integer>();

				// check item:
				for (int i = 0; i < item.getModuloRests().size(); i++) {

					int restItem = item.getModuloRests().get(i);

					for (int k = 0; k < itemTMP.getModuloRests().size(); k++) {

						int restTMP = itemTMP.getModuloRests().get(k);

						if ((restTMP - restItem) % powerOfTwo == 0) {

							if (!candidateIndices.contains(k)) {

								/** Indices that has to be removed from itemTMP.getModuloRests(): */
								candidateIndices.add(k);
							}
						}
					}
				}

				/** remove indices */

				for (int i = 0; i < candidateIndices.size(); i++) {

					candidateToRemoveRests.add(itemTMP.getModuloRests().get(candidateIndices.get(i)));

				}

				for (int l = 0; l < candidateToRemoveRests.size(); l++) {

					int rest = candidateToRemoveRests.get(l);

					List<Integer> q = itemTMP.getModuloRests();

					for (int x = 0; x < q.size(); x++) {
						if (q.get(x) == rest) {
							q.remove(x);
							break;
						}
					}

				}

			}

		}

		return response;

	}

	/**
	 * transforms each SymbolsCorrespondingToProbability from the receiving to a
	 * SymbolCorrespondingToProbability
	 * 
	 * @param receiving
	 *            - the list of SymbolCorrespondingToProbability's
	 * @return the transformed receiving
	 */
	public static List<SymbolCorrespondingToProbability> transformTo(
			List<SymbolsCorrespondingToProbability> receiving) {

		List<SymbolCorrespondingToProbability> response = new ArrayList<SymbolCorrespondingToProbability>();

		for (SymbolsCorrespondingToProbability item : receiving) {

			for (int c = 0; c < item.getListOfSymbols().size(); c++) {
				SymbolCorrespondingToProbability symb = new SymbolCorrespondingToProbability(
						item.getListOfSymbols().get(c), item.getApproximatedProbability(), counter);

				symb.setModuloRest(item.getModuloRests().get(c));
				response.add(symb);
				counter++;
			}

		}
		counter = 0;

		return response;
	}

	/**
	 * 
	 * @param item
	 * @param b
	 *            - the parameter b
	 * @param L
	 *            - the parameter L
	 */
	public static void generateIndexSubset(SymbolCorrespondingToProbability item, Integer b, Integer L) {

		int powerOfTwo = (int) (1 / item.getProbability());
		int moduloRest = item.getModuloRest();

		for (int i = 1; i <= b * L - 1; i++) {

			int tmpValue = powerOfTwo * i + moduloRest;

			if (tmpValue > b * L - 1) {
				/** value greater than upper bound of normalized interval */
				break;

			} else if (tmpValue >= L && tmpValue <= b * L - 1) {
				/** value in normalized interval */
				item.addIndex(i);
			}

		}
	}

}
