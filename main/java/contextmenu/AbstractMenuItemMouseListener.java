package contextmenu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;

public class AbstractMenuItemMouseListener implements MouseListener {

	private JMenuItem iconMenuItem;

	public AbstractMenuItemMouseListener(JMenuItem iconMenuItem) {
		this.iconMenuItem = iconMenuItem;
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		iconMenuItem.setBackground(Color.LIGHT_GRAY);

	}

	public void mouseExited(MouseEvent e) {
		iconMenuItem.setBackground(Color.WHITE);

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
