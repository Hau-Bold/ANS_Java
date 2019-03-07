package compressImage;

import java.util.concurrent.Callable;

import encryption.Decoder;

public class DecodeImageStripe implements Callable<Object> {

	private Decoder decoder;
	private int finalState;

	public DecodeImageStripe(Decoder decoder, int finalState) {
		this.decoder = decoder;
		this.finalState = finalState;
	}

	public Object call() throws Exception {
		return decoder.executeForImage(finalState);
	}

}
