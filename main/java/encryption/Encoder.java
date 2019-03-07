package encryption;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import ans.SymbolCorrespondingToProbability;

/**
 * the class Encoder
 *
 */
public class Encoder implements Command {

	private List<SymbolCorrespondingToProbability> symbolCorrespondingToProbabilities;
	private Integer param_b;
	private Integer param_L;
	private Boolean isProcedureBasedOnIndexSubsets;

	/**
	 * Constructor.
	 * 
	 * @param symbolCorrespondingToProbabilities
	 * @param param_b
	 *            - the parameter b
	 * @param param_L
	 *            - the parameter L
	 * @param isProcedureBasedOnIndexSubsets
	 */
	public Encoder(List<SymbolCorrespondingToProbability> symbolCorrespondingToProbabilities, Integer param_b,
			Integer param_L, Boolean isProcedureBasedOnIndexSubsets) {
		this.symbolCorrespondingToProbabilities = symbolCorrespondingToProbabilities;
		this.param_b = param_b;
		this.param_L = param_L;
		this.isProcedureBasedOnIndexSubsets = isProcedureBasedOnIndexSubsets;

	}

	/**
	 * encodes a given text
	 * 
	 * @param textToEncode
	 *            - the text to encode
	 * @return the computed result consisting of a finalState and a BitMask
	 */
	private EncodingResult encode(String textToEncode) {

		BitMask bitMask = new BitMask();

		int M = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities);

		int currentState = param_L;

		/** start encoding */
		for (int i = textToEncode.length() - 1; i >= 0; i--) {

			String symbol = String.valueOf(textToEncode.charAt(i));

			/** compute Bs */
			SymbolCorrespondingToProbability refItem = EncryptionUtils.getCorrespondingSymbol(symbol,
					symbolCorrespondingToProbabilities);
			if (refItem != null) {

				int s = refItem.getPlace();

				int Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, s);
				int Fs = refItem.getFrequency();

				currentState = (int) (M * Math.floor((double) currentState / (double) Fs) + Bs + (currentState % Fs));

				/** normalize current state */
				ArrayList<Integer> lstOfModuloRest = new ArrayList<Integer>();
				while (currentState > param_b * param_L) {
					int rest = currentState % param_b;
					lstOfModuloRest.add(rest);

					currentState /= param_b;
				}

				bitMask.addBit(lstOfModuloRest);

			} else {
				break;
			}
		}

		return new EncodingResult(bitMask, currentState);

	}

	private EncodingResult encodeForIndexSubsets(String textToStream) {

		BitMaskForIndexSubsets bitMaskForIndexSubsets = new BitMaskForIndexSubsets();

		int currentState = param_L;

		/** start encoding */
		for (int i = textToStream.length() - 1; i >= 0; i--) {

			char symbol = textToStream.charAt(i);
			String symbolToEncode = String.valueOf(symbol);

			SymbolCorrespondingToProbability refItem = EncryptionUtils.getCorrespondingSymbol(symbolToEncode,
					symbolCorrespondingToProbabilities);

			if (refItem != null) {

				/** normalize current state */
				while (!refItem.getIndexSubset().contains(currentState)) {

					int rest = currentState % param_b;
					bitMaskForIndexSubsets.addBit(rest);

					currentState /= (param_b);
				}

				currentState = currentState * refItem.getPowerOfTwo() + refItem.getModuloRest();
			}

		}

		return new EncodingResult(bitMaskForIndexSubsets, currentState);

	}

	/**
	 * image encoding:procedure based on index subsets
	 * 
	 * @param x
	 *            - the width value of the buffered image
	 * @param bufferedImage
	 *            - the buffered image
	 * @return encoding result
	 */
	private EncodingResult encodeImageForIndexSubsets(int x, BufferedImage bufferedImage) {

		int currentState = param_L;

		int height = bufferedImage.getHeight();

		int red;
		int green;
		int blue;
		int alpha;

		SymbolCorrespondingToProbability refItemRed;
		SymbolCorrespondingToProbability refItemGreen;
		SymbolCorrespondingToProbability refItemBlue;
		SymbolCorrespondingToProbability refItemAlpha;
		BitMaskForIndexSubsets bitMaskForIndexSubsets = new BitMaskForIndexSubsets();

		for (int i = height - 1; i > -1; i--) {

			Color color = new Color(bufferedImage.getRGB(x, i));
			red = color.getRed();
			green = color.getGreen();
			blue = color.getBlue();
			alpha = color.getAlpha();

			refItemRed = EncryptionUtils.getCorrespondingSymbol(String.valueOf(red),
					symbolCorrespondingToProbabilities);
			refItemGreen = EncryptionUtils.getCorrespondingSymbol(String.valueOf(green),
					symbolCorrespondingToProbabilities);
			refItemBlue = EncryptionUtils.getCorrespondingSymbol(String.valueOf(blue),
					symbolCorrespondingToProbabilities);
			refItemAlpha = EncryptionUtils.getCorrespondingSymbol(String.valueOf(alpha),
					symbolCorrespondingToProbabilities);

			/** normalize current state for red value */
			while (!refItemRed.getIndexSubset().contains(currentState)) {

				int rest = currentState % param_b;
				bitMaskForIndexSubsets.addBit(rest);

				currentState /= (param_b);
			}

			currentState = currentState * refItemRed.getPowerOfTwo() + refItemRed.getModuloRest();

			/** normalize current state for green value */
			while (!refItemGreen.getIndexSubset().contains(currentState)) {

				int rest = currentState % param_b;
				bitMaskForIndexSubsets.addBit(rest);

				currentState /= (param_b);
			}

			currentState = currentState * refItemGreen.getPowerOfTwo() + refItemGreen.getModuloRest();

			/** normalize current state for blue value */
			while (!refItemBlue.getIndexSubset().contains(currentState)) {

				int rest = currentState % param_b;
				bitMaskForIndexSubsets.addBit(rest);

				currentState /= (param_b);
			}

			currentState = currentState * refItemBlue.getPowerOfTwo() + refItemBlue.getModuloRest();

			/** normalize current state for alpha value */
			while (!refItemAlpha.getIndexSubset().contains(currentState)) {

				int rest = currentState % param_b;
				bitMaskForIndexSubsets.addBit(rest);

				currentState /= (param_b);
			}

			currentState = currentState * refItemAlpha.getPowerOfTwo() + refItemAlpha.getModuloRest();

		}

		return new EncodingResult(bitMaskForIndexSubsets, currentState);
	}

	/**
	 * image encoding:procedure not based on index subsets
	 * 
	 * @param x
	 *            - the width value of the buffered image
	 * @param bufferedImage
	 *            - the buffered image
	 * @return encoding result
	 */
	private EncodingResult encodeImage(int x, BufferedImage bufferedImage) {

		int height = bufferedImage.getHeight();
		int red, green, blue, alpha;
		int M = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities);
		int s, Bs, Fs;
		ArrayList<Integer> lstOfModuloRest = null;

		SymbolCorrespondingToProbability refItemRed, refItemGreen, refItemBlue, refItemAlpha;

		/** the bitMask to save modulo rests */
		BitMask bitMask = new BitMask();

		int currentState = param_L;

		/** start encoding */
		for (int i = height - 1; i > -1; i--) {
			/** reading color at pixel(x,i) */
			Color color = new Color(bufferedImage.getRGB(x, i));
			red = color.getRed();
			green = color.getGreen();
			blue = color.getBlue();
			alpha = color.getAlpha();

			// RED
			/** computing corresponding red item */
			refItemRed = EncryptionUtils.getCorrespondingSymbol(String.valueOf(red),
					symbolCorrespondingToProbabilities);

			/** compute parameters */
			s = refItemRed.getPlace();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, s);
			Fs = refItemRed.getFrequency();

			/** new current state */
			currentState = (int) (M * Math.floor((double) currentState / (double) Fs) + Bs + (currentState % Fs));

			/** normalize current state for red value */
			lstOfModuloRest = new ArrayList<Integer>();
			while (currentState > param_b * param_L) {
				int rest = currentState % param_b;
				lstOfModuloRest.add(rest);

				currentState /= param_b;
			}
			bitMask.addBit(lstOfModuloRest);

			// GREEN
			/** computing corresponding green item */
			refItemGreen = EncryptionUtils.getCorrespondingSymbol(String.valueOf(green),
					symbolCorrespondingToProbabilities);

			/** compute parameters */
			s = refItemGreen.getPlace();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, s);
			Fs = refItemGreen.getFrequency();

			/** new current state */
			currentState = (int) (M * Math.floor((double) currentState / (double) Fs) + Bs + (currentState % Fs));

			/** normalize current state for green value */
			lstOfModuloRest = new ArrayList<Integer>();
			while (currentState > param_b * param_L) {
				int rest = currentState % param_b;
				lstOfModuloRest.add(rest);

				currentState /= param_b;
			}
			bitMask.addBit(lstOfModuloRest);

			// BLUE
			/** computing corresponding blue item */
			refItemBlue = EncryptionUtils.getCorrespondingSymbol(String.valueOf(blue),
					symbolCorrespondingToProbabilities);

			/** compute parameters */
			s = refItemBlue.getPlace();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, s);
			Fs = refItemBlue.getFrequency();

			/** new current state */
			currentState = (int) (M * Math.floor((double) currentState / (double) Fs) + Bs + (currentState % Fs));

			/** normalize current state for blue value */
			lstOfModuloRest = new ArrayList<Integer>();
			while (currentState > param_b * param_L) {
				int rest = currentState % param_b;
				lstOfModuloRest.add(rest);

				currentState /= param_b;
			}
			bitMask.addBit(lstOfModuloRest);

			// ALPHA
			/** computing corresponding alpha item */
			refItemAlpha = EncryptionUtils.getCorrespondingSymbol(String.valueOf(alpha),
					symbolCorrespondingToProbabilities);

			/** compute parameters */
			s = refItemAlpha.getPlace();
			Bs = EncryptionUtils.getSumOfFrequencies(symbolCorrespondingToProbabilities, s);
			Fs = refItemAlpha.getFrequency();

			/** new current state */
			currentState = (int) (M * Math.floor((double) currentState / (double) Fs) + Bs + (currentState % Fs));

			/** normalize current state for alpha value */
			lstOfModuloRest = new ArrayList<Integer>();
			while (currentState > param_b * param_L) {
				int rest = currentState % param_b;
				lstOfModuloRest.add(rest);

				currentState /= param_b;
			}
			bitMask.addBit(lstOfModuloRest);
		}

		return new EncodingResult(bitMask, currentState);

	}

	public EncodingResult execute(String textToStream) {

		if (isProcedureBasedOnIndexSubsets) {
			return encodeForIndexSubsets(textToStream);
		}

		return encode(textToStream);
	}

	public EncodingResult execute(int x, BufferedImage bufferedImage) {

		if (isProcedureBasedOnIndexSubsets) {
			return encodeImageForIndexSubsets(x, bufferedImage);
		}

		return encodeImage(x, bufferedImage);
	}

}
