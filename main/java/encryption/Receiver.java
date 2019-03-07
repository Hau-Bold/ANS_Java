package encryption;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import ans.ANS;
import ans.Constants;
import ans.SymbolCorrespondingToProbability;

public class Receiver extends Observer implements Notify, ActionListener {

	private static final long serialVersionUID = 1L;
	private Integer param_b;
	private Integer param_L;
	private List<SymbolCorrespondingToProbability> symbolsPreparedForStreaming;
	private Boolean mode;
	private String result;
	private ANS ans;
	private EncodingResult encodingResult;
	private Boolean isProcedureBasedOnIndexSubsets;

	/**
	 * Constructor.
	 * 
	 * @param title
	 * @param param_b
	 * @param param_L
	 * @param symbolsPreparedForStreaming
	 * @param showButton
	 * @param mode
	 * @param isProcedureBasedOnIndexSubsets
	 */
	public Receiver(ANS ans, Integer param_b, Integer param_L,
			List<SymbolCorrespondingToProbability> symbolsPreparedForStreaming, Boolean mode,
			Boolean isProcedureBasedOnIndexSubsets) {
		super(Constants.INPUT);
		this.ans = ans;
		this.param_b = param_b;
		this.param_L = param_L;
		this.symbolsPreparedForStreaming = symbolsPreparedForStreaming;
		this.mode = mode;
		this.isProcedureBasedOnIndexSubsets = isProcedureBasedOnIndexSubsets;

		submitIconButton.setVisible(true);
		submitIconButton.addActionListener(this);

	}

	/**
	 * notifies the streaming partner
	 */
	public void notifyObserver() {
		this.getStreamPartner().getStreamingField().setText(result);
	}

	public void addObserver(Observer observer) {

		this.setStreamPartner(observer);

	}

	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(submitIconButton)) {

			if (mode) {

				String textToStream = streamingField.getText().trim();
				Encoder encoder = new Encoder(symbolsPreparedForStreaming, param_b, param_L,
						isProcedureBasedOnIndexSubsets);

				encodingResult = encoder.execute(textToStream);
				ans.getLstEncodingResult().clear();
				ans.getLstEncodingResult().add(encodingResult);
				result = String.valueOf(encodingResult.getFinalState());
				notifyObserver();
			} else {

				int finalState = Integer.valueOf(streamingField.getText().trim());

				encodingResult = EncryptionUtils.getEncodingResult(ans.getLstEncodingResult(), finalState);

				Decoder decoder;
				if (!isProcedureBasedOnIndexSubsets) {
					decoder = new Decoder(symbolsPreparedForStreaming, param_b, param_L, encodingResult.getBitMask(),
							isProcedureBasedOnIndexSubsets);
				} else {
					decoder = new Decoder(symbolsPreparedForStreaming, param_b, param_L,
							encodingResult.getBitMaskForIndexSubsets(), isProcedureBasedOnIndexSubsets);
				}
				result = decoder.execute(finalState);

				notifyObserver();
			}

		}

	}
}
