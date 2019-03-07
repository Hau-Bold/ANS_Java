package contextmenu;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;

import ans.Utils;

public class IconMenuItem extends JMenuItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ImageIcon icon = null;

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            - the text that should be displayed.
	 */
	IconMenuItem(String text, String pathOfIcon) {

		if (Utils.isStringValid(pathOfIcon)) {
			this.icon = new ImageIcon(getClass().getResource("../images/" + pathOfIcon));
			this.icon.setImage(icon.getImage().getScaledInstance(15, 15, Image.SCALE_SMOOTH));
			this.setIcon(icon);
		}

		this.setSize(25, 180);
		this.setText(text);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);

		this.addMouseListener(new MenuItemMouseListener(this));
	}

	public IconMenuItem(String pathOfIcon) {
		this(pathOfIcon, null);
	}

}
