package contextmenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import ans.ANS;
import ans.Constants;

/**
 * the class ActionContextMenu
 *
 */
public class ActionContextMenu extends CommonContextMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	private IconMenuItem indexSubsetsBasedIconButton, notIndexSubsetsBasedIconButton, changeParamterIconButton;

	public ActionContextMenu(MouseEvent event, ANS ans) {
		super(event, ans);
		initComponent();
		showMenu();
	}

	private void initComponent() {

		this.setModal(Boolean.TRUE);

		if (ans.getIsProcedureBasedOnIndexSubsets()) {
			notIndexSubsetsBasedIconButton = new IconMenuItem(Constants.NOT_BASED_ON_INDEX_SUBSETS, "minus.png");
			notIndexSubsetsBasedIconButton.addActionListener(this);
			super.addIconMenuItem(notIndexSubsetsBasedIconButton);

		} else {

			indexSubsetsBasedIconButton = new IconMenuItem(Constants.BASED_ON_INDEX_SUBSETS, "plus.png");
			indexSubsetsBasedIconButton.addActionListener(this);
			super.addIconMenuItem(indexSubsetsBasedIconButton);
		}

		if (ANS.getIsParamBSet() && ANS.getIsParamLSet()) {
			changeParamterIconButton = new IconMenuItem(Constants.CHANGE_PARAMETER, "settings.png");
			changeParamterIconButton.addActionListener(this);
			super.addIconMenuItem(changeParamterIconButton);
		}
		super.activate();
	}

	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(notIndexSubsetsBasedIconButton)) {

			ans.setIsProcedureBasedOnIndexSubsets(Boolean.FALSE);
			ans.getStatusBar().setText(Constants.NOT_BASED_ON_INDEX_SUBSETS);

			this.dispose();
		}

		else if (o.equals(indexSubsetsBasedIconButton)) {
			ans.setIsProcedureBasedOnIndexSubsets(Boolean.TRUE);
			ans.getStatusBar().setText(Constants.BASED_ON_INDEX_SUBSETS);
			this.dispose();
		}

		else if (o.equals(changeParamterIconButton)) {
			ANS.getTxtParam_b().setEnabled(Boolean.TRUE);
			ANS.getSubmitParam_B().setVisible(Boolean.TRUE);
			ANS.setIsParamBSet(Boolean.FALSE);

			ANS.getTxtParam_L().setEnabled(Boolean.TRUE);
			ANS.getSubmitParam_L().setVisible(Boolean.TRUE);
			ANS.setIsParamLSet(Boolean.FALSE);

			this.dispose();
		}

	}

}
