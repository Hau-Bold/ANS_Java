package listeners;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ans.ANS;
import ans.Formats;
import ans.TextFieldSetter;

public class ListenerForValus implements DocumentListener {
	private JTextField textField;
	private Formats format;

	public ListenerForValus(JTextField textField, Formats format) {
		this.textField = textField;
		this.format = format;
	}

	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void insertUpdate(DocumentEvent arg0) {

		String content = textField.getText();

		switch (format) {

		case DOUBLE:

			if (!content.equals("0.")) {
				try {
					Double.valueOf(content);
					modifyTextFieldAndANSConfig();
				} catch (NumberFormatException ex) {
					callMessage();
				}
			}

			break;

		case UNSIGNEDINTEGER:

			try {
				if (!content.equals("")) {
					modifyTextFieldAndANSConfig();
				}
			} catch (NumberFormatException ex) {
				callMessage();
			}

			break;

		case STRING:
		default:
			modifyTextFieldAndANSConfig();
			break;
		// do nothing
		}

	}

	private void modifyTextFieldAndANSConfig() {
		textField.setBackground(Color.WHITE);

		if (textField.equals(ANS.getTxtParam_b())) {
			/** parameter b */
			ANS.setIsParamBSet(Boolean.TRUE);

		} else if (textField.equals(ANS.getTxtParam_L())) {

			/** parameter L */
			ANS.setIsParamLSet(Boolean.TRUE);

		} else if (textField.equals(ANS.getTxtSymbol())) {

			/** symbol */
			ANS.setIsSymbolSet(Boolean.TRUE);

			if (ANS.getClearIconButton().isVisible() == Boolean.FALSE) {
				ANS.getClearIconButton().setVisible(Boolean.TRUE);
			}
		} else if (textField.equals(ANS.getTxtFrequency())) {

			Integer frequency = Integer.valueOf(textField.getText());
			if (frequency > ANS.getLengthOfStream()
					|| frequency + ANS.getCurrentSumOfStreams() > ANS.getLengthOfStream()) {
				JOptionPane.showMessageDialog(null, "frequency is invalid");
				SwingUtilities.invokeLater(new TextFieldSetter(textField, format));
			} else {

				/** frequency */
				ANS.setIsFrequencySet(Boolean.TRUE);
			}
		} else if (textField.equals(ANS.getTxtLengthOfStream())) {

			/** length of stream */
			ANS.setIsLengthOfStreamSet(Boolean.TRUE);
		}
	}

	public void removeUpdate(DocumentEvent event) {

		/** resetting Boolean values and content of corresponding textField */
		if (textField.equals(ANS.getTxtParam_b())) {
			ANS.setIsParamBSet(Boolean.FALSE);
		} else if (textField.equals(ANS.getTxtParam_L())) {

			ANS.setIsParamLSet(Boolean.FALSE);

		}

		else if (textField.equals(ANS.getTxtSymbol())) {
			/** symbol */
			ANS.setIsSymbolSet(Boolean.FALSE);
			if (ANS.getListOfSymbolsWithApproxProbability().size() == 0) {
				ANS.getClearIconButton().setVisible(Boolean.FALSE);
			}
		}

		textField.setBackground(Color.GREEN);
	}

	/**
	 * Generates a message for the client in case of using incorrect formats
	 */
	private void callMessage() {
		JOptionPane.showMessageDialog(null, String.format("Wrong Format, use %s", format));
		SwingUtilities.invokeLater(new TextFieldSetter(textField, format));
	}

}
