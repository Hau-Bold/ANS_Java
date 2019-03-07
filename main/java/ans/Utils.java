package ans;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import encryption.EncryptionUtils;
import encryption.EncryptionUtils.RecommendateParameters;
import lookup.CommonModel;
import lookup.LookUpUtils;

public class Utils {

	/**
	 * generates the approximated probability
	 * 
	 * @param frequencyOfSymbol
	 *            - the frequency of the symbol
	 * @param sumOfFrequencys
	 *            - the sum of the frequencies
	 * @return the approximated probability
	 */
	public static double generateApproximatedProbability(double frequencyOfSymbol, double sumOfFrequencies) {

		double probability = frequencyOfSymbol / sumOfFrequencies;

		/** approximately log_2(1 / probability) */
		double potency = Math.log(1 / probability);
		potency /= Math.log(2);

		int ceiledPotency = (int) Math.ceil(potency);
		int flooredPotency = (int) Math.floor(potency);

		double resultCeil1 = Math.pow(2, ceiledPotency);
		double resultFloor1 = Math.pow(2, flooredPotency);

		double approximatedProbabilityCeiledVariant = resultCeil1 * probability;
		double approximatedProbabilityFlooredVariant = resultFloor1 * probability;

		double distanceToCeiled = Math.abs(1 - approximatedProbabilityCeiledVariant);
		double distanceToFloored = Math.abs(1 - approximatedProbabilityFlooredVariant);

		return distanceToCeiled > distanceToFloored ? 1.0 / resultFloor1 : 1.0 / resultCeil1;
	}

	public static void fillTableModel(CommonModel model, Integer lengthOfStream, String symbol, Integer frequency) {
		Vector<String> datarow = new Vector<String>();
		double probability = (double) frequency / (double) lengthOfStream;
		datarow.add(symbol);
		datarow.add(String.valueOf(frequency));
		datarow.add(String.valueOf(probability));
		datarow.add(String.valueOf(generateApproximatedProbability(frequency, lengthOfStream)));

		model.addDataRow(datarow);

	}

	public static void fillTableModel(CommonModel model, Integer lengthOfStream,
			List<SymbolCorrespondingToProbability> receiving) {

		Vector<String> datarow;

		for (SymbolCorrespondingToProbability item : receiving) {

			datarow = new Vector<String>();
			double probability = (double) item.getFrequency() / (double) lengthOfStream;
			datarow.add(item.getSymbol());
			datarow.add(String.valueOf(item.getFrequency()));
			datarow.add(String.valueOf(probability));
			datarow.add(String.valueOf(item.getProbability()));

			model.addDataRow(datarow);
		}

	}

	/**
	 * to clear the JTextFields
	 * 
	 * @param var
	 *            the JTextFields
	 */
	public static void clearTextFields(JTextField... var) {
		for (JTextField txtField : var) {
			txtField.setText("");
		}

	}

	/**
	 * to determine the visibility of several JLabels
	 * 
	 * @param isVisible
	 *            - a boolean the visibility depends on
	 * @param varargs
	 *            - the JLabels
	 */
	public static void setVisibilityOfLabels(boolean isVisible, JLabel... varargs) {
		for (JLabel label : varargs) {
			label.setVisible(isVisible);
		}

	}

	/**
	 * to determine the visibility of several JTextFields
	 * 
	 * @param isVisible
	 *            - a boolean the visibility depends on
	 * @param varargs
	 *            - the JTextFields
	 */
	public static void setVisibilityOfJTextFields(boolean isVisible, JTextField... varargs) {
		for (JTextField textfield : varargs) {
			textfield.setVisible(isVisible);
		}
	}

	/**
	 * to determine the visibility of several JButtons
	 * 
	 * @param isVisible
	 *            - a boolean the visibility depends on
	 * @param varargs
	 *            - the JButtons
	 */
	public static void setVisibilityOfJButtons(boolean isVisible, JButton... varargs) {
		for (JButton jButton : varargs) {
			jButton.setVisible(isVisible);
		}
	}

	public static String[] generateColumnHeader(int b, int L) {

		String[] response = null;

		response = new String[(b - 1) * L + 1];
		response[0] = "<html>s  \\ C(s,x)</html>";
		for (int i = L; i <= b * L - 1; i++) {
			response[i - L + 1] = String.valueOf(i);
		}

		return response;
	}

	/**
	 * checks whether the sum of the probabilities in receiving is one
	 * 
	 * @param listOfSymbolsWithApproxProbability
	 *            - the receiving
	 * @return
	 */
	public static boolean isSumOfProbabilitiesOne(
			List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability) {

		return Double.compare(getSumOfProbabilities(listOfSymbolsWithApproxProbability), 1.0) == 0 ? Boolean.TRUE
				: Boolean.FALSE;

	}

	private static double getSumOfProbabilities(
			List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability) {

		double sumOfProbabilities = .0;

		for (SymbolCorrespondingToProbability item : listOfSymbolsWithApproxProbability) {
			sumOfProbabilities += item.getProbability();
		}

		return sumOfProbabilities;

	}

	public static List<SymbolCorrespondingToProbability> balanceProbabilities(
			List<SymbolCorrespondingToProbability> receiving) {
		SymbolCorrespondingToProbability item;

		while (!isSumOfProbabilitiesOne(receiving)) {
			if (getSumOfProbabilities(receiving) < 1) {
				item = getSymbolWithMinimalCorrespondingProbability(receiving);

				item.setProbability(item.getProbability() * 2);
				receiving.add(item);
			}

			else {
				item = getSymbolWithMaximalCorrespondingProbability(receiving);

				item.setProbability(item.getProbability() * 0.5);
				receiving.add(item);
			}
		}

		return receiving;
	}

	private static SymbolCorrespondingToProbability getSymbolWithMinimalCorrespondingProbability(
			List<SymbolCorrespondingToProbability> receiving) {

		SymbolCorrespondingToProbability minimum = receiving.get(0);
		SymbolCorrespondingToProbability comparison;
		for (int i = 1; i < receiving.size(); i++) {
			comparison = receiving.get(i);

			if (comparison.getProbability() < minimum.getProbability()) {
				minimum = comparison;
			}

		}

		receiving.remove(minimum);

		return minimum;
	}

	private static SymbolCorrespondingToProbability getSymbolWithMaximalCorrespondingProbability(
			List<SymbolCorrespondingToProbability> receiving) {

		SymbolCorrespondingToProbability maximum = receiving.get(0);
		SymbolCorrespondingToProbability comparison;
		for (int i = 1; i < receiving.size(); i++) {
			comparison = receiving.get(i);

			if (comparison.getProbability() > maximum.getProbability()) {
				maximum = comparison;
			}

		}

		receiving.remove(maximum);

		return maximum;
	}

	public static List<SymbolCorrespondingToProbability> getSymbolsCorrespondingToProbabilityFromInput(
			String receiving) {
		List<String> candidates = new ArrayList<String>();
		List<SymbolCorrespondingToProbability> response = new ArrayList<SymbolCorrespondingToProbability>();

		for (int counter = 0; counter < receiving.length(); counter++) {
			String symbol = String.valueOf(receiving.charAt(counter));

			if (!candidates.contains(symbol)) {
				candidates.add(symbol);
				int frequency = 1;

				for (int i = counter + 1; i < receiving.length(); i++) {
					if (String.valueOf(receiving.charAt(i)).equals(symbol)) {
						frequency++;
					}
				}

				response.add(new SymbolCorrespondingToProbability(symbol, frequency));

			}
		}

		return response;
	}

	/**
	 * Yields the nearest power-of-2 to the length of the receiving, which consists
	 * of the occurring symbols and their frequencies.
	 * 
	 * @param symbolsCorrespondingToProbabilities
	 *            - the receiving
	 * @return nearest power of two
	 */
	public static int getNearestPowerOfTwo(List<SymbolCorrespondingToProbability> symbolsCorrespondingToProbabilities) {

		int binaryPotency = 0;
		int lowerBound = 1;
		int upperBound = 2;
		int result;

		for (SymbolCorrespondingToProbability item : symbolsCorrespondingToProbabilities) {
			binaryPotency += item.getFrequency();
		}

		while (binaryPotency > upperBound) {
			upperBound *= 2;
		}

		lowerBound = upperBound / 2;

		result = Math.abs(binaryPotency - lowerBound) > Math.abs(binaryPotency - upperBound) ? upperBound : lowerBound;

		return (int) (Math.log(result) / Math.log(2));
	}

	public static RecommendateParameters recommendateParameters(List<SymbolCorrespondingToProbability> receiving) {
		if (receiving != null) {

			/** copy */
			List<SymbolCorrespondingToProbability> temp = new ArrayList<SymbolCorrespondingToProbability>(receiving);

			/** objects: symbols with same probability */
			List<SymbolsCorrespondingToProbability> symbolsCorrespondingToProbability = LookUpUtils
					.getSymbolsCorrespondingToProbalities(temp);

			/** compute rests */
			List<SymbolsCorrespondingToProbability> compResult = LookUpUtils
					.computePossibleRests(symbolsCorrespondingToProbability);

			/** separating symbols */
			List<SymbolCorrespondingToProbability> response = LookUpUtils.transformTo(compResult);

			/** setting again frequencies: */
			for (SymbolCorrespondingToProbability item : response) {
				for (SymbolCorrespondingToProbability innerItem : receiving) {
					if (innerItem.getSymbol().equals(item.getSymbol())) {
						item.setFrequency(innerItem.getFrequency());
						item.setIndexSubset(innerItem.getIndexSubset());
						break;
					}

				}
			}
			int param_b = 2;
			int param_L = 16;

			SymbolCorrespondingToProbability itemWithMaximalPowerOfTwo = getItemWithMaximalPowerOfTwo(response);

			while (param_b * param_L - 1 < itemWithMaximalPowerOfTwo.getPowerOfTwo()
					+ itemWithMaximalPowerOfTwo.getModuloRest()) {

				param_L *= 2;
			}

			/** generate index subsets */
			for (SymbolCorrespondingToProbability item : response) {
				LookUpUtils.generateIndexSubset(item, param_b, param_L);
			}

			return new EncryptionUtils().new RecommendateParameters(param_L, 2, response);
		}

		else {
			return null;
		}

	}

	/**
	 * yields the SymbolCorrespondingToProbability with maximal power of Two from
	 * the receiving
	 * 
	 * @param receiving
	 *            - list of SymbolCorrespondingToProbability's
	 * @return the SymbolCorrespondingToProbability with maximal power of Two
	 */
	private static SymbolCorrespondingToProbability getItemWithMaximalPowerOfTwo(
			List<SymbolCorrespondingToProbability> receiving) {

		SymbolCorrespondingToProbability itemWithMaximalPowerOfTwo = receiving.get(0);

		for (int i = 1; i < receiving.size(); i++) {
			SymbolCorrespondingToProbability tmpItem = receiving.get(i);
			if (tmpItem.getPowerOfTwo() > itemWithMaximalPowerOfTwo.getPowerOfTwo()) {
				itemWithMaximalPowerOfTwo = tmpItem;
			}
		}

		return itemWithMaximalPowerOfTwo;
	}

	public static String getSymbolCorrespondingToPlace(List<SymbolCorrespondingToProbability> receiving, int place) {
		for (SymbolCorrespondingToProbability item : receiving) {

			if (Integer.compare(item.getPlace(), place) == 0) {
				return item.getSymbol();
			}

		}

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * removes all in table selected symbols from the receiving
	 * 
	 * @param rows
	 *            - the selected rows
	 * @param model
	 *            - the model belonging to the table
	 * @param receiving
	 *            - the list of SymbolCorrespondingToProbability's
	 * @return
	 */
	public static List<SymbolCorrespondingToProbability> removeSymbols(int[] rows, CommonModel model,
			List<SymbolCorrespondingToProbability> receiving) {

		ArrayList<SymbolCorrespondingToProbability> response = new ArrayList<SymbolCorrespondingToProbability>(
				receiving);

		Iterator<SymbolCorrespondingToProbability> iterator = null;

		for (int i = 0; i < rows.length; i++) {
			String symbol = (String) model.getValueAt(rows[i], 0);

			iterator = response.iterator();

			while (iterator.hasNext()) {
				SymbolCorrespondingToProbability currentSymbol = iterator.next();
				if (currentSymbol.getSymbol().equals(symbol)) {
					iterator.remove();
					break;
				}
			}

		}

		return response;
	}

	public static boolean isStringValid(String string) {
		return (string != null) && (!string.trim().equals(""));
	}

	public static long getSizeOfImage(BufferedImage bufferedImage) {
		DataBuffer dataBuffer = bufferedImage.getData().getDataBuffer();

		// Each bank element in the data buffer is a 32-bit integer

		long s = dataBuffer.getSize();
		long sizeBytes = s * 4;
		double sizeMB = sizeBytes / (1024 * 1024);

		return (long) sizeMB;

	}

	// public static List<Integer> transformToBits(Integer receiving) {
	//
	// List<Integer> response = new ArrayList<Integer>();
	//
	// while (receiving > 0) {
	// response.add(receiving % 2);
	// receiving /= 2;
	// }
	//
	// return response;
	// }

}
