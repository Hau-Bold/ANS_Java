package contextmenu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ans.ANS;

public class ANSMouseListener implements MouseListener {

	private ANS ans;
	private ActionContextMenu actionContextMenu;

	/**
	 * Constructor.
	 * 
	 * @param ans
	 *            - the ans
	 */
	public ANSMouseListener(ANS ans) {
		this.ans = ans;
		actionContextMenu = ans.getActionContextMenu();
	}

	public void mouseClicked(MouseEvent event) {

		if (event.getButton() == MouseEvent.BUTTON3) {

			if (!(actionContextMenu == null)) {
				actionContextMenu.dispose();
			}
			actionContextMenu = new ActionContextMenu(event, ans);
			ans.setActionContextMenu(actionContextMenu);
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mousePressed(MouseEvent arg0) {
		if (actionContextMenu != null) {
			actionContextMenu.dispose();
		}

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
