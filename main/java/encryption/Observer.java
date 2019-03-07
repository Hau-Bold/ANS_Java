package encryption;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ans.IconButton;

public class Observer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel streamingPanel;

	protected JTextField streamingField;
	private Observer streamPartner;
	protected IconButton submitIconButton;

	public Observer(String title) {
		initComponent(title);
	}

	private void initComponent(String title) {

		this.setTitle(title);
		this.setSize(300, 100);

		/** orders when window is closing */
		this.addWindowListener(new StreamWindowAdapter(this));

		streamingPanel = new JPanel();
		streamingPanel.setLayout(null);

		streamingField = new JTextField();
		streamingField.setLocation(10, 10);
		streamingField.setSize(100, 25);

		submitIconButton = new IconButton("confirm.png", 110, 10);
		submitIconButton.setVisible(Boolean.FALSE);

		streamingPanel.add(streamingField);
		streamingPanel.add(submitIconButton);

		this.getContentPane().add(streamingPanel);

	}

	public void showFrame() {
		this.setVisible(true);
	}

	protected Observer getStreamPartner() {
		return streamPartner;
	}

	protected void setStreamPartner(Observer streamPartner) {
		this.streamPartner = streamPartner;
	}

	public JTextField getStreamingField() {
		return streamingField;
	}

}
