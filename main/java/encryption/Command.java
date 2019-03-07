package encryption;

import java.awt.image.BufferedImage;

public interface Command {

	public EncodingResult execute(String textToStream);

	public EncodingResult execute(int x, BufferedImage bufferedImage);

}
