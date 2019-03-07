package contextmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ans.ANS;

public class CommonContextMenu extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2796536710016330518L;
	private final int SIZEOFITEM = 20;
	private final int WIDTH = 180;
	private List<JMenuItem> iconMenuItems = new ArrayList<JMenuItem>();
	private JPanel panel;
	protected ANS ans;

	/**
	 * Constructor.
	 * 
	 * @param event
	 *            - the event
	 * @param ans
	 * @param iconMenuItems
	 *            - list of IconMenuItems
	 */
	CommonContextMenu(MouseEvent event, ANS ans) {
		this.ans = ans;
		initComponent(event);
	}

	private void initComponent(MouseEvent event) {

		this.setUndecorated(true);
		this.setLocation(ans.getX() + event.getX(), ans.getY() + event.getY());
		this.setLayout(new BorderLayout());

	}

	protected void showMenu() {
		this.setVisible(true);
	}

	/**
	 * moves the items to the context menu
	 */
	protected void activate() {
		int countOfItem = iconMenuItems.size();
		this.setSize(WIDTH, countOfItem * SIZEOFITEM);
		panel = new JPanel(new GridLayout(countOfItem, 1));
		panel.setSize(WIDTH, countOfItem * SIZEOFITEM);

		this.add(panel, BorderLayout.CENTER);

		for (JMenuItem item : iconMenuItems) {

			panel.add(getPanelWithItem(item));
		}
	}

	private JPanel getPanelWithItem(JMenuItem item) {

		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(WIDTH, SIZEOFITEM);
		panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panel.add(item, BorderLayout.CENTER);

		return panel;
	}

	protected void addIconMenuItem(JMenuItem... varargs) {
		for (JMenuItem item : varargs) {
			iconMenuItems.add(item);
		}
	}

}
