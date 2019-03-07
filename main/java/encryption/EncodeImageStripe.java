package encryption;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.Callable;

import ans.SymbolCorrespondingToProbability;

public class EncodeImageStripe implements Callable<Object> {

	private BufferedImage bufferedImage;
	private int x;
	private Encoder encoder;

	public EncodeImageStripe(int x, BufferedImage bufferedImage, Boolean isProcedureBasedOnIndexSubsets,
			List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability, Integer param_b,
			Integer param_L) {
		this.x = x;
		this.bufferedImage = bufferedImage;
		encoder = new Encoder(listOfSymbolsWithApproxProbability, param_b, param_L, isProcedureBasedOnIndexSubsets);

		// TODO Auto-generated method stub

	}

	public Object call() throws Exception {

		return encoder.execute(x, bufferedImage);
	}

}
