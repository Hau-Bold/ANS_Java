package encryption;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import ans.SymbolCorrespondingToProbability;
import ans.Utils;

public class Decoder implements CommandDecode {

	private List<SymbolCorrespondingToProbability> symbolCorrespondingToProbabilities;
	private Integer param_b;
	private Integer param_L;
	private BitMask bitMask;
	private BitMaskForIndexSubsets bitMaskForIndexSubsets;
	private Boolean isProcedureBasedOnIndexSubsets;

	/**
	 * Constructor.
	 * 
	 * @param symbolCorrespondingToProbabilities
	 * @param param_b
	 * @param param_L
	 * @param isProcedureBasedOnIndexSubsets
	 */
	public Decoder(List<SymbolCorrespondingToProbability> symbolCorrespondingToProbabilities, Integer param_b,
			Integer param_L, BitMask bitMask, Boolean isProcedureBasedOnIndexSubsets) {
		this.symbolCorrespondingToProbabilities = symbolCorrespondingToProbabilities;
		this.param_b = param_b;
		this.param_L = param_L;
		this.bitMask = bitMask;
		this.isProcedureBasedOnIndexSubsets = isProcedureBasedOnIndexSubsets;

	}

	/**
	 * Constructor.
	 * 
	 * @param symbolCorrespondingToProbabilities
	 * @param param_b
	 * @param param_L
	 */
	public Decoder(List<SymbolCorrespondingToProbability> symbolCorrespondingToProbabilities, Integer param_b,
			Integer param_L, BitMaskForIndexSubsets bitMaskForIndexSubsets, Boolean isProcedureBasedOnIndexSubsets) {
		this.symbolCorrespondingToProbabilities = symbolCorrespondingToProbabilities;
		this.param_b = param_b;
		this.param_L = param_L;
		this.bitMaskForIndexSubsets = bitMaskForIndexSubsets;
		this.isProcedureBasedOnIndexSubsets = isProcedureBasedOnIndexSubsets;
	}

	/**
	 * decodes a finalState to a String
	 * 
	 * @param finalState
	 *            - the final state
	 * @return the decoded final state
	 */
	private String decode(int finalState) {
		int currentState = finalState;
		int M = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities);
		StringBuilder builder = new StringBuilder();

		int countOfRests = bitMask.getListOfRests().size() - 1;

		do {

			/** normalize current state */
			ArrayList<Integer> lstModuloRests = bitMask.getListOfRests().get(countOfRests);
			countOfRests--;
			for (int i = lstModuloRests.size() - 1; i >= 0; i--) {

				Integer rest = lstModuloRests.get(i);
				currentState = param_b * currentState + rest;
			}

			/** compute the symbol */
			int R = currentState % M;
			int place = EncryptionUtils.lookUp(R, symbolCorrespondingToProbabilities);

			builder.append(Utils.getSymbolCorrespondingToPlace(symbolCorrespondingToProbabilities, place));

			/** compute the next currentState */
			int Fs = symbolCorrespondingToProbabilities.get(place).getFrequency();
			int Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, place);

			currentState = (int) (Fs * Math.floor((double) currentState / (double) M) + R - Bs);

		} while (countOfRests > -1);

		return builder.toString();

	}

	private String decodeForIndexSubsets(int finalState) {
		int currentState = finalState;
		StringBuilder builder = new StringBuilder();

		/** For normalizing current state */
		List<Integer> lstModuloRests = bitMaskForIndexSubsets.getListOfRests();
		/** a pointer that signs on the current rest */
		int countOfRests = bitMaskForIndexSubsets.getListOfRests().size() - 1;

		do {

			while (currentState < param_L) {

				/** normalize current state */
				int read = lstModuloRests.get(countOfRests);
				currentState = param_b * currentState + read;
				countOfRests--;
			}

			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			SymbolCorrespondingToProbability refItem = EncryptionUtils.getCorrespondingSymbol(currentState,
					symbolCorrespondingToProbabilities);

			if (refItem != null) {
				builder.append(refItem.getSymbol());

				currentState = (currentState - refItem.getModuloRest()) / refItem.getPowerOfTwo();

			}

		} while (countOfRests > 0 && currentState != param_L);

		return builder.toString();
	}

	private List<Color> decodeImageForIndexSubsets(int finalState) {

		List<Color> response = new ArrayList<Color>();

		int alpha = 0;
		int blue = 0;
		int green = 0;
		int red = 0;

		int currentState = finalState;

		/** For normalizing current state */
		List<Integer> lstModuloRests = bitMaskForIndexSubsets.getListOfRests();
		/** a pointer that signs on the current rest */
		int countOfRests = bitMaskForIndexSubsets.getListOfRests().size() - 1;

		do {

			while (currentState < param_L) {

				/** normalize current state */
				int read = lstModuloRests.get(countOfRests);
				currentState = param_b * currentState + read;
				countOfRests--;
			}

			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			SymbolCorrespondingToProbability refItemAlpha = EncryptionUtils.getCorrespondingSymbol(currentState,
					symbolCorrespondingToProbabilities);

			if (refItemAlpha != null) {

				alpha = Integer.valueOf(refItemAlpha.getSymbol());

				currentState = (currentState - refItemAlpha.getModuloRest()) / refItemAlpha.getPowerOfTwo();

			}

			/** blue */
			while (currentState < param_L) {

				/** normalize current state */
				int read = lstModuloRests.get(countOfRests);
				currentState = param_b * currentState + read;
				countOfRests--;
			}

			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			SymbolCorrespondingToProbability refItemBlue = EncryptionUtils.getCorrespondingSymbol(currentState,
					symbolCorrespondingToProbabilities);

			if (refItemBlue != null) {

				blue = Integer.valueOf(refItemBlue.getSymbol());

				currentState = (currentState - refItemBlue.getModuloRest()) / refItemBlue.getPowerOfTwo();

			}

			/** green */
			while (currentState < param_L) {

				/** normalize current state */
				int read = lstModuloRests.get(countOfRests);
				currentState = param_b * currentState + read;
				countOfRests--;
			}

			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			SymbolCorrespondingToProbability refItemGreen = EncryptionUtils.getCorrespondingSymbol(currentState,
					symbolCorrespondingToProbabilities);

			if (refItemGreen != null) {

				green = Integer.valueOf(refItemGreen.getSymbol());

				currentState = (currentState - refItemGreen.getModuloRest()) / refItemGreen.getPowerOfTwo();

			}

			/** red */
			while (currentState < param_L) {

				/** normalize current state */
				int read = lstModuloRests.get(countOfRests);
				currentState = param_b * currentState + read;
				countOfRests--;
			}

			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			SymbolCorrespondingToProbability refItemRed = EncryptionUtils.getCorrespondingSymbol(currentState,
					symbolCorrespondingToProbabilities);

			if (refItemRed != null) {

				red = Integer.valueOf(refItemRed.getSymbol());

				currentState = (currentState - refItemRed.getModuloRest()) / refItemRed.getPowerOfTwo();

			}

			response.add(new Color(red, green, blue, alpha));

		} while (countOfRests > 0);

		return response;
	}

	private List<Color> decodeImage(int finalState) {

		int currentState = finalState;
		int M = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities);

		int countOfRests = bitMask.getListOfRests().size() - 1;
		int red, green, blue, alpha;
		int R, place, Fs, Bs;

		List<Color> response = new ArrayList<Color>();

		do {

			// ALPHA
			ArrayList<Integer> lstModuloAlpha = bitMask.getListOfRests().get(countOfRests);
			countOfRests--;

			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			/** normalize current state for alpha */
			for (int i = lstModuloAlpha.size() - 1; i >= 0; i--) {

				Integer rest = lstModuloAlpha.get(i);
				currentState = param_b * currentState + rest;
			}

			/** compute alpha */
			R = currentState % M;
			place = EncryptionUtils.lookUp(R, symbolCorrespondingToProbabilities);

			alpha = Integer.valueOf(Utils.getSymbolCorrespondingToPlace(symbolCorrespondingToProbabilities, place));

			/** compute the next currentState */
			Fs = symbolCorrespondingToProbabilities.get(place).getFrequency();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, place);
			currentState = (int) (Fs * Math.floor((double) currentState / (double) M) + R - Bs);

			// BLUE
			ArrayList<Integer> lstModuloBlue = bitMask.getListOfRests().get(countOfRests);
			countOfRests--;
			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			/** normalize current state for blue */
			for (int i = lstModuloBlue.size() - 1; i >= 0; i--) {

				Integer rest = lstModuloBlue.get(i);
				currentState = param_b * currentState + rest;
			}

			/** compute blue */
			R = currentState % M;
			place = EncryptionUtils.lookUp(R, symbolCorrespondingToProbabilities);

			blue = Integer.valueOf(Utils.getSymbolCorrespondingToPlace(symbolCorrespondingToProbabilities, place));

			/** compute the next currentState */
			Fs = symbolCorrespondingToProbabilities.get(place).getFrequency();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, place);
			currentState = (int) (Fs * Math.floor((double) currentState / (double) M) + R - Bs);

			// GREEN
			ArrayList<Integer> lstModuloGreen = bitMask.getListOfRests().get(countOfRests);
			countOfRests--;
			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			/** normalize current state for blue */
			for (int i = lstModuloGreen.size() - 1; i >= 0; i--) {

				Integer rest = lstModuloGreen.get(i);
				currentState = param_b * currentState + rest;
			}

			/** compute green */
			R = currentState % M;
			place = EncryptionUtils.lookUp(R, symbolCorrespondingToProbabilities);
			green = Integer.valueOf(Utils.getSymbolCorrespondingToPlace(symbolCorrespondingToProbabilities, place));

			/** compute the next currentState */
			Fs = symbolCorrespondingToProbabilities.get(place).getFrequency();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, place);
			currentState = (int) (Fs * Math.floor((double) currentState / (double) M) + R - Bs);

			// RED
			ArrayList<Integer> lstModuloRed = bitMask.getListOfRests().get(countOfRests);
			countOfRests--;
			if (Integer.compare(countOfRests, -1) == 0) {
				break;
			}

			/** normalize current state for blue */
			for (int i = lstModuloRed.size() - 1; i >= 0; i--) {

				Integer rest = lstModuloRed.get(i);
				currentState = param_b * currentState + rest;
			}

			/** compute green */
			R = currentState % M;
			place = EncryptionUtils.lookUp(R, symbolCorrespondingToProbabilities);
			red = Integer.valueOf(Utils.getSymbolCorrespondingToPlace(symbolCorrespondingToProbabilities, place));

			/** compute the next currentState */
			Fs = symbolCorrespondingToProbabilities.get(place).getFrequency();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, place);
			currentState = (int) (Fs * Math.floor((double) currentState / (double) M) + R - Bs);

			response.add(new Color(red, green, blue, alpha));

		} while (countOfRests > -1);

		return response;

	}

	/** command pattern for decoding *.text files */
	public String execute(int finalState) {
		if (isProcedureBasedOnIndexSubsets) {
			return decodeForIndexSubsets(finalState);
		}
		return decode(finalState);
	}

	/** command pattern for decoding *.text files to image */
	public List<Color> executeForImage(int finalState) {
		if (isProcedureBasedOnIndexSubsets) {
			return decodeImageForIndexSubsets(finalState);
		}
		return decodeImage(finalState);
	}

}
