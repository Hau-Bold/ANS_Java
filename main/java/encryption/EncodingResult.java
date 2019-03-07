package encryption;

/**
 * the class EncodingResult
 *
 */
public class EncodingResult {

	private BitMask bitMask;
	private int finalState;
	private BitMaskForIndexSubsets bitMaskForIndexSubsets;

	/**
	 * Constructor
	 * 
	 * @param bitMask
	 *            - the bisMask
	 * @param finalState
	 *            - the finalState
	 */
	public EncodingResult(BitMask bitMask, int finalState) {
		this.bitMask = bitMask;
		this.finalState = finalState;
	}

	/**
	 * Constructor.
	 * 
	 * @param bitMaskForIndexSubsets
	 *            - the bitMaskForIndexSubsets
	 * @param finalState
	 *            - the finalState
	 */
	public EncodingResult(BitMaskForIndexSubsets bitMaskForIndexSubsets, int finalState) {
		this.bitMaskForIndexSubsets = bitMaskForIndexSubsets;
		this.finalState = finalState;
	}

	public BitMask getBitMask() {
		return bitMask;
	}

	public int getFinalState() {
		return finalState;
	}

	public BitMaskForIndexSubsets getBitMaskForIndexSubsets() {
		return bitMaskForIndexSubsets;
	}

}
