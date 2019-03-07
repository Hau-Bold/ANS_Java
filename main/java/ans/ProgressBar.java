package ans;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class ProgressBar extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4653956083050681816L;
	private JProgressBar progressBar;

	private int x;
	private int y;
	private JPanel panel;

	public ProgressBar(int x, int y) {
		this.x = x;
		this.y = y;
		initComponent();

	}

	private void initComponent() {
		this.setSize(300, 100);
		this.setTitle(Constants.INITIALIZING);
		this.setLocation(x, y);
		this.setLayout(new BorderLayout());

		panel = new JPanel();
		this.add(panel, BorderLayout.CENTER);

		progressBar = new JProgressBar();
		panel.add(progressBar);

		this.setVisible(true);
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}

}
