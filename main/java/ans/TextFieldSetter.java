package ans;

import javax.swing.JTextField;

public class TextFieldSetter implements Runnable {

	private JTextField textField;
	private Formats format;

	public TextFieldSetter(JTextField textField, Formats format) {
		this.textField = textField;
		this.format = format;
	}

	public void run() {

		textField.setText(format == Formats.DOUBLE ? "0." : "");
	}

}
