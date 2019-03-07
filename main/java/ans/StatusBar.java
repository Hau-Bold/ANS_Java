package ans;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StatusBar extends JLabel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Dimensions of the StatusBar are fixed
	final int WIDTH = 150;
	final int HEIGHT = 20;

	/**
	 * Constructor. Sets the StatusBar at (xPos,yPos)
	 * 
	 * @param xPos
	 *            the relative position in x direction
	 * @param yPos
	 *            the relative Position in y direction
	 */
	public StatusBar() {
		this.setSize(WIDTH, HEIGHT);
		this.setFont(new Font("Helvetica", Font.ITALIC, 11));
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setForeground(Color.RED);
		this.setText(Constants.NOT_BASED_ON_INDEX_SUBSETS);
		this.setBorder(BorderFactory.createRaisedSoftBevelBorder());
	}
}
