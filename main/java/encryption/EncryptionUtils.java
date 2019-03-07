package encryption;

import java.util.List;

import javax.swing.JOptionPane;

import ans.Constants;
import ans.SymbolCorrespondingToProbability;

public class EncryptionUtils {

	/**
	 * 
	 * the class RecommendateParameters
	 */
	public class RecommendateParameters {

		private int param_L;
		private int param_b;
		private List<SymbolCorrespondingToProbability> lstSymbolsPreparedForStreaming;

		/**
		 * Constructor.
		 * 
		 * @param param_L
		 *            - the parameter L
		 * @param param_b
		 *            - the parameter b
		 * @param lstSymbolsPreparedForStreaming
		 *            - the list of symbols prepared for streaming
		 */
		public RecommendateParameters(int param_L, int param_b,
				List<SymbolCorrespondingToProbability> lstSymbolsPreparedForStreaming) {
			super();
			this.param_L = param_L;
			this.param_b = param_b;
			this.lstSymbolsPreparedForStreaming = lstSymbolsPreparedForStreaming;
		}

		public int getParam_L() {
			return param_L;
		}

		public int getParam_b() {
			return param_b;
		}

		public List<SymbolCorrespondingToProbability> getLstSymbolsPreparedForStreaming() {
			return lstSymbolsPreparedForStreaming;
		}

	}

	/**
	 * yields the SymbolCorrespondingToProbability corresponding to the current
	 * state
	 * 
	 * @param value
	 *            - current state
	 * @param receiving
	 *            the list of SymbolCorrespondingToProbability's
	 * @return the SymbolCorrespondingToProbability corresponding to the current
	 *         state
	 */
	public static SymbolCorrespondingToProbability getCorrespondingSymbol(int value,
			List<SymbolCorrespondingToProbability> receiving) {
		for (SymbolCorrespondingToProbability item : receiving) {
			int moduloRest = item.getModuloRest();
			int binaryPotency = item.getPowerOfTwo();

			if ((value - moduloRest) % binaryPotency == 0) {
				return item;
			}
		}

		return null;
	}

	/**
	 * yields the SymbolCorrespondingToProbability corresponding to the received
	 * symbol
	 * 
	 * @param symbol
	 *            - the symbol
	 * @param receiving
	 *            the list of SymbolCorrespondingToProbability's
	 * @return the SymbolCorrespondingToProbability corresponding to the symbol
	 */
	public static SymbolCorrespondingToProbability getCorrespondingSymbol(String symbol,
			List<SymbolCorrespondingToProbability> receiving) {

		for (SymbolCorrespondingToProbability item : receiving) {
			if (item.getSymbol().equals(symbol)) {
				return item;
			}
		}

		JOptionPane.showMessageDialog(null, String.format(Constants.INVALID_SYMBOL, symbol));
		return null;
	}

	/**
	 * computes the sum of the frequencies occurred in receiving
	 * 
	 * @param lstSymbolCorrespondingToProbability
	 *            - the list of SymbolCorrespondingToProbability's
	 * @return the sum of the frequencies
	 */
	static int getSumOfFrequencies(List<SymbolCorrespondingToProbability> lstSymbolCorrespondingToProbability) {

		int response = 0;

		for (SymbolCorrespondingToProbability item : lstSymbolCorrespondingToProbability) {
			response += item.getFrequency();
		}

		return response;
	}

	/**
	 * computes the sum of the frequencies occurred in receiving from 0 to place-1
	 * 
	 * @param receiving
	 *            - the list of SymbolCorrespondingToProbability's
	 * @param place
	 *            - the place
	 * @return
	 */
	static int getSumOfFrequencies(List<SymbolCorrespondingToProbability> lstSymbolCorresondingToProbability,
			int place) {

		int response = 0;

		for (int i = 0; i < place; i++) {

			response += lstSymbolCorresondingToProbability.get(i).getFrequency();

		}

		return response;
	}

	public static int lookUp(int value, List<SymbolCorrespondingToProbability> receiving) {

		int currentSumOfFrequencies = 0;
		int s = 0;

		for (int i = 0; i < receiving.size(); i++) {
			if (currentSumOfFrequencies + receiving.get(i).getFrequency() <= value) {
				currentSumOfFrequencies += receiving.get(i).getFrequency();
				s++;
			} else {
				break;
			}
		}

		return s;
	}

	/**
	 * yields the EncodingResult corresponding to the received finalState
	 * 
	 * @param lstEncodingResult
	 *            - the list of EncodingResult's
	 * @param finalState
	 *            - the finalState
	 * @return the EncodingResult corresponding to the received finalState
	 */
	public static EncodingResult getEncodingResult(List<EncodingResult> lstEncodingResult, int finalState) {

		for (EncodingResult encodingResult : lstEncodingResult) {
			if (Integer.compare(encodingResult.getFinalState(), finalState) == 0) {
				return encodingResult;
			}
		}

		return null;
	}

}
