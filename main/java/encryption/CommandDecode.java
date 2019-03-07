package encryption;

import java.awt.Color;
import java.util.List;

public interface CommandDecode {

	public String execute(int finalState);

	public List<Color> executeForImage(int finalState);

}
