package listeners;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ans.IconButton;

public class ListenerForEmptyField implements DocumentListener {

	private IconButton iconButton;
	private JTextField textField;

	public ListenerForEmptyField(IconButton iconButton, JTextField textField) {

		this.iconButton = iconButton;
		this.textField = textField;
	}

	public void changedUpdate(DocumentEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void insertUpdate(DocumentEvent arg0) {

		String content = textField.getText();
		try {
			Integer contentAsInt = Integer.valueOf(content);

			if (contentAsInt != null) {
				iconButton.setVisible(true);
				textField.setBackground(Color.WHITE);
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null, "Wrong Format, Integer");
			// SwingUtilities.invokeLater(new TextFieldSetter(textField, false));
		}
	}

	public void removeUpdate(DocumentEvent arg0) {
		if (textField.getText().equals("")) {
			iconButton.setVisible(false);
			textField.setBackground(Color.GREEN);
		}

	}

}
