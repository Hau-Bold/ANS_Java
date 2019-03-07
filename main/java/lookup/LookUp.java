package lookup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import ans.Constants;
import ans.SymbolCorrespondingToProbability;
import ans.Utils;

public class LookUp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static LookUp instance = null;
	private JTable table;
	private JPanel panelTableNorth;
	private Container panelTable;
	private CommonModel model;
	private Integer b;
	private Integer L;
	private List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability;

	// /**
	// * to create an instance of this class
	// *
	// * @param l
	// * @param b2
	// *
	// * @return
	// */
	// public static LookUp getInstance(List<SymbolCorrespondingToProbability>
	// listOfSymbolsWithApproxProbability,
	// Integer b, Integer L) {
	// if (instance == null) {
	// instance = new LookUp(listOfSymbolsWithApproxProbability, b, L);
	// }
	// return instance;
	// }

	/**
	 * Constructor.
	 * 
	 * @param overViewEntries
	 * @param routenplaner
	 * @param overViewContextMenu
	 */
	public LookUp(List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability, Integer b, Integer L) {
		this.listOfSymbolsWithApproxProbability = listOfSymbolsWithApproxProbability;
		this.b = b;
		this.L = L;
		initComponent();
	}

	private void initComponent() {
		// the main frame
		this.setBounds(50, 50, 500, 500);

		StringBuilder builder = new StringBuilder();

		builder.append(Constants.b + ":" + b + ", " + Constants.L + ":" + L);

		this.setTitle(builder.toString());
		this.setResizable(true);
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addWindowListener(new MyWindowListener(this));

		panelTable = new JPanel(new GridLayout(1, 1));
		this.add(panelTable);

		panelTableNorth = new JPanel(new BorderLayout());
		panelTableNorth.setPreferredSize(new Dimension(500, 500));
		panelTable.add(panelTableNorth);

		model = new CommonModel(Utils.generateColumnHeader(b, L));
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setGridColor(Color.BLUE);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setAutoscrolls(Boolean.FALSE);
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));
		table.setDefaultRenderer(Object.class, new LookUpRenderer());
		LookUpUtils.computeLookUpTable(model, listOfSymbolsWithApproxProbability, b, L);
		model.fireTableDataChanged();

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		panelTableNorth.add(scrollPane);

	}

	public void showFrame() {
		this.setVisible(true);
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public CommonModel getModel() {
		return model;
	}

	public void setModel(CommonModel model) {
		this.model = model;
	}

}
