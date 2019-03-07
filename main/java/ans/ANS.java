package ans;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import compressImage.CompressImageDialog;
import compressImage.ImageUtils;
import contextmenu.ANSMouseListener;
import contextmenu.ActionContextMenu;
import encryption.BitMask;
import encryption.BitMaskForIndexSubsets;
import encryption.Decoder;
import encryption.Encoder;
import encryption.EncodingResult;
import encryption.EncryptionUtils.RecommendateParameters;
import encryption.Observer;
import encryption.Receiver;
import listeners.ListenerForValus;
import lookup.CommonModel;
import lookup.LookUp;
import lookup.LookUpUtils;

public class ANS extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel panelMain, panelForDisplayingInput;
	private static JTextField txtLengthOfStream, txtFrequency, txtParam_L, txtParam_b, txtSymbol;
	private JLabel lblSymbol, lblFrequency, lblParam_B, lblParam_L, lblLengthOfStream, lblProcedure;
	private IconButton encodeIconButton, decodeIconButton, fixLengthOfStreamIconButton, showLookUpTableIconButton,
			determineByTextIconButton, compressSelectedFileIconButton, uncompressSelectedFileIconButton,
			submitIconButton, loadImageIconButton;
	private static IconButton clearIconButton, submitParam_B, submitParam_L;
	private CommonModel model;
	private JTable table;
	private static List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability = new ArrayList<SymbolCorrespondingToProbability>();
	private List<String> listOfSymbols = new ArrayList<String>();
	private static int symbolCounter;
	private Integer param_L, param_b;
	private static Integer lengthOfStream, currentSumOfStreams = 0;
	private List<SymbolCorrespondingToProbability> symbolsPreparedForStreaming = null;
	private static Boolean isSymbolSet = Boolean.FALSE, isLengthOfStreamSet = Boolean.FALSE,
			isFrequencySet = Boolean.FALSE, isParamBSet = Boolean.FALSE, isParamLSet = Boolean.FALSE;
	private Boolean isProcedureBasedOnIndexSubsets = Boolean.FALSE, fileIsCompressedIndexBased = Boolean.FALSE;
	private List<EncodingResult> lstEncodingResult = new ArrayList<EncodingResult>();

	private String directory, contentOfFile;
	private StatusBar statusBar;
	ActionContextMenu actionContextMenu = null;

	private BufferedImage bufferedImage;

	/***
	 * Constructor.
	 */
	public ANS() {
		initComponent();
	}

	private void initComponent() {

		/** Setting the Layout */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		} catch (InstantiationException e2) {
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			e2.printStackTrace();
		} catch (UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}

		/** Setting language for JOptionspane */
		UIManager.put("OptionPane.yesButtonText", "yes");
		UIManager.put("OptionPane.noButtonText", "no");
		UIManager.put("OptionPane.cancelButtonText", "cancel");

		this.setSize(500, 500);
		this.setLocation(5, 5);
		this.setTitle(Constants.TITLE);

		/** handling of closing the window */
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			};
		});

		panelMain = new JPanel();
		panelMain.setLayout(null);

		this.getContentPane().add(panelMain);

		lblLengthOfStream = new JLabel(Constants.LENGTH + ":");
		lblLengthOfStream.setLocation(10, 10);
		lblLengthOfStream.setSize(100, 25);

		txtLengthOfStream = new JTextField();
		txtLengthOfStream.setLocation(70, 10);
		txtLengthOfStream.setSize(40, 25);
		txtLengthOfStream.setBackground(Color.GREEN);
		txtLengthOfStream.getDocument()
				.addDocumentListener(new ListenerForValus(txtLengthOfStream, Formats.UNSIGNEDINTEGER));

		lblSymbol = new JLabel(Constants.SYMBOL + ":");
		lblSymbol.setLocation(10, 40);
		lblSymbol.setSize(100, 25);
		lblSymbol.setVisible(false);

		txtSymbol = new JTextField();
		txtSymbol.setLocation(70, 40);
		txtSymbol.setSize(40, 25);
		txtSymbol.setBackground(Color.GREEN);
		txtSymbol.getDocument().addDocumentListener(new ListenerForValus(txtSymbol, Formats.STRING));
		txtSymbol.setVisible(false);

		lblFrequency = new JLabel(Constants.FREQUENCY + ":");
		lblFrequency.setLocation(10, 70);
		lblFrequency.setSize(100, 25);
		lblFrequency.setVisible(false);

		txtFrequency = new JTextField();
		txtFrequency.setLocation(70, 70);
		txtFrequency.setSize(40, 25);
		txtFrequency.setBackground(Color.GREEN);
		txtFrequency.getDocument().addDocumentListener(new ListenerForValus(txtFrequency, Formats.UNSIGNEDINTEGER));
		txtFrequency.setVisible(false);

		lblParam_L = new JLabel(Constants.L);
		lblParam_L.setLocation(220, 20);
		lblParam_L.setSize(20, 25);
		lblParam_L.setVisible(false);

		txtParam_L = new JTextField();
		txtParam_L.setSize(100, 25);
		txtParam_L.setLocation(255, 20);
		txtParam_L.setBackground(Color.GREEN);
		txtParam_L.getDocument().addDocumentListener(new ListenerForValus(txtParam_L, Formats.UNSIGNEDINTEGER));
		txtParam_L.setVisible(false);

		submitParam_L = new IconButton("confirm.png", 355, 20);
		submitParam_L.setVisible(Boolean.FALSE);
		submitParam_L.addActionListener(this);

		lblParam_B = new JLabel(Constants.b);
		lblParam_B.setLocation(220, 50);
		lblParam_B.setSize(20, 25);
		lblParam_B.setVisible(false);

		txtParam_b = new JTextField();
		txtParam_b.setSize(100, 25);
		txtParam_b.setLocation(255, 50);
		txtParam_b.setBackground(Color.GREEN);
		txtParam_b.getDocument().addDocumentListener(new ListenerForValus(txtParam_b, Formats.UNSIGNEDINTEGER));
		txtParam_b.setVisible(false);

		submitParam_B = new IconButton("confirm.png", 355, 50);
		submitParam_B.setVisible(Boolean.FALSE);
		submitParam_B.addActionListener(this);

		/** display input in a JSCROLLPANE. */
		model = new CommonModel(Constants.NAME_OF_COLUMNS);
		table = new JTable(model);
		table.setPreferredScrollableViewportSize(new Dimension(500, 400));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getTableHeader().setReorderingAllowed(false);
		table.setFillsViewportHeight(true);
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setFont(new Font("Helvetica", Font.BOLD, 11));

		this.addMouseListener(new ANSMouseListener(this));

		panelForDisplayingInput = new JPanel();
		panelForDisplayingInput.setLayout(new BorderLayout());
		panelForDisplayingInput.setSize(500, 300);
		panelForDisplayingInput.setLocation(0, 150);
		panelForDisplayingInput.setVisible(false);
		panelForDisplayingInput.add(new JScrollPane(table));

		submitIconButton = new IconButton("confirm.png", 10, 120);
		submitIconButton.addActionListener(this);
		submitIconButton.setMnemonic(KeyEvent.VK_C);
		submitIconButton.setToolTipText(String.format(Constants.CONFIRM_ACTION, "C"));
		submitIconButton.setVisible(false);

		encodeIconButton = new IconButton("encode.png", 30, 120);
		encodeIconButton.addActionListener(this);
		encodeIconButton.setVisible(false);

		decodeIconButton = new IconButton("decode.png", 70, 120);
		decodeIconButton.addActionListener(this);
		decodeIconButton.setVisible(false);

		fixLengthOfStreamIconButton = new IconButton("confirm.png", 110, 10);
		fixLengthOfStreamIconButton.addActionListener(this);

		showLookUpTableIconButton = new IconButton("showIcon.png", 50, 120);
		showLookUpTableIconButton.addActionListener(this);
		showLookUpTableIconButton.setVisible(Boolean.FALSE);

		determineByTextIconButton = new IconButton("document.png", 90, 120);
		determineByTextIconButton.addActionListener(this);

		compressSelectedFileIconButton = new IconButton("compress.jpg", 110, 120);
		compressSelectedFileIconButton.setVisible(Boolean.FALSE);
		compressSelectedFileIconButton.addActionListener(this);

		uncompressSelectedFileIconButton = new IconButton("decompress.png", 130, 120);
		uncompressSelectedFileIconButton.setVisible(Boolean.FALSE);
		uncompressSelectedFileIconButton.addActionListener(this);

		clearIconButton = new IconButton("clear.png", 150, 120);
		clearIconButton.addActionListener(this);
		clearIconButton.setVisible(Boolean.FALSE);
		clearIconButton.setMnemonic(KeyEvent.VK_D);
		clearIconButton.setToolTipText(String.format(Constants.DELETE_ACTION, "D"));

		loadImageIconButton = new IconButton("loadImage.jpg", 170, 120);
		loadImageIconButton.addActionListener(this);
		loadImageIconButton.setVisible(Boolean.TRUE);
		loadImageIconButton.setMnemonic(KeyEvent.VK_L);
		loadImageIconButton.setToolTipText(String.format(Constants.LOAD_IMAGE_ACTION, "L"));

		lblProcedure = new JLabel(Constants.PROCEDURE);
		lblProcedure.setSize(60, 20);
		lblProcedure.setLocation(280, 0);

		/** Configuration of Statusbar */
		statusBar = new StatusBar();
		statusBar.setLocation(340, 0);

		/** addding labels */
		panelMain.add(lblSymbol);
		panelMain.add(lblFrequency);
		panelMain.add(lblLengthOfStream);
		panelMain.add(lblParam_L);
		panelMain.add(lblParam_B);
		panelMain.add(statusBar);
		panelMain.add(lblProcedure);

		/** addding textfields */
		panelMain.add(txtSymbol);
		panelMain.add(txtLengthOfStream);
		panelMain.add(txtFrequency);
		panelMain.add(txtParam_L);
		panelMain.add(txtParam_b);

		/** addding panels */
		panelMain.add(panelForDisplayingInput);

		/** addding icons */
		panelMain.add(submitIconButton);
		panelMain.add(clearIconButton);
		panelMain.add(submitParam_L);
		panelMain.add(submitParam_B);
		panelMain.add(encodeIconButton);
		panelMain.add(decodeIconButton);
		panelMain.add(fixLengthOfStreamIconButton);
		panelMain.add(determineByTextIconButton);
		panelMain.add(showLookUpTableIconButton);
		panelMain.add(compressSelectedFileIconButton);
		panelMain.add(uncompressSelectedFileIconButton);
		panelMain.add(loadImageIconButton);

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		Object o = event.getSource();

		if (o.equals(submitIconButton)) {

			if (getIsSymbolSet() && getIsFrequencySet() && getIsLengthOfStreamSet()) {

				/** reading the input */
				String symbol = txtSymbol.getText();
				Integer frequency = Integer.valueOf(txtFrequency.getText());
				currentSumOfStreams += frequency;

				if (!listOfSymbols.contains(symbol)) {
					listOfSymbols.add(symbol);

					Utils.fillTableModel(model, lengthOfStream, symbol, frequency);
					listOfSymbolsWithApproxProbability.add(new SymbolCorrespondingToProbability(symbol, frequency,
							Utils.generateApproximatedProbability(frequency, lengthOfStream), symbolCounter));

					if (!panelForDisplayingInput.isVisible()) {
						panelForDisplayingInput.setVisible(true);
					}

					if (Integer.compare(currentSumOfStreams, lengthOfStream) == 0) {

						/** all symbols are entered */
						Utils.setVisibilityOfJTextFields(Boolean.TRUE, txtParam_L, txtParam_b);
						txtParam_L.requestFocus();
						Utils.setVisibilityOfJButtons(Boolean.TRUE, getClearIconButton(), showLookUpTableIconButton,
								submitParam_B, submitParam_L, encodeIconButton, decodeIconButton);
						Utils.setVisibilityOfLabels(Boolean.TRUE, lblParam_L, lblParam_B);
						Utils.setVisibilityOfJButtons(Boolean.FALSE, submitIconButton);

						boolean check = Utils.isSumOfProbabilitiesOne(listOfSymbolsWithApproxProbability);

						if (!check) {

							JOptionPane.showMessageDialog(null,
									"sum of probabilities is not one, probabilities will be approximated such that their sum is one",
									Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE);

							listOfSymbolsWithApproxProbability = Utils
									.balanceProbabilities(listOfSymbolsWithApproxProbability);
							model.clear();
							Utils.fillTableModel(model, lengthOfStream, listOfSymbolsWithApproxProbability);

						}

						Utils.setVisibilityOfLabels(Boolean.FALSE, lblSymbol, lblFrequency);
						Utils.setVisibilityOfJTextFields(Boolean.FALSE, txtSymbol, txtFrequency);

					}
					symbolCounter++;

					setIsSymbolSet(Boolean.FALSE);
					setIsFrequencySet(Boolean.FALSE);
					Utils.clearTextFields(txtSymbol, txtFrequency);
					txtSymbol.requestFocus();
				} else {
					JOptionPane.showConfirmDialog(null, String.format("Symbol %s is already in use", symbol),
							Constants.WARNING, JOptionPane.WARNING_MESSAGE);
				}

			}

		}

		/** encoding */
		else if (o.equals(encodeIconButton)) {

			if (getIsParamBSet() && getIsParamLSet()) {

				if (isProcedureBasedOnIndexSubsets) {

					symbolsPreparedForStreaming = LookUpUtils.prepareSymbols(listOfSymbolsWithApproxProbability,
							param_b, param_L);
				}

				else {

					Collections.sort(listOfSymbolsWithApproxProbability, new PlaceComparator());
					symbolsPreparedForStreaming = listOfSymbolsWithApproxProbability;
				}

				Receiver receiver = new Receiver(this, param_b, param_L, symbolsPreparedForStreaming, Boolean.TRUE,
						isProcedureBasedOnIndexSubsets);
				receiver.setLocation(10, 10);

				Observer observer = new Observer(Constants.OUTPUT);
				observer.setLocation(300, 10);

				receiver.addObserver(observer);

				observer.showFrame();
				receiver.showFrame();

			} else {
				JOptionPane.showMessageDialog(null, "please check settings of parameters to proceed", Constants.WARNING,
						JOptionPane.WARNING_MESSAGE);
			}

		}
		/** decoding */
		else if (o.equals(decodeIconButton)) {

			if (!lstEncodingResult.isEmpty()) {

				Receiver receiver = new Receiver(this, param_b, param_L, symbolsPreparedForStreaming, Boolean.FALSE,
						isProcedureBasedOnIndexSubsets);
				receiver.setLocation(10, 10);
				receiver.showFrame();

				Observer observer = new Observer(Constants.OUTPUT);
				observer.setLocation(300, 10);
				observer.showFrame();

				receiver.addObserver(observer);

			} else {
				JOptionPane.showMessageDialog(null, "There was nothing encoded before", Constants.INFORMATION,
						JOptionPane.INFORMATION_MESSAGE);
			}

		}

		/** setting lenth of stream */
		else if (o.equals(fixLengthOfStreamIconButton)) {

			String content = txtLengthOfStream.getText();
			if (!content.equals("")) {
				lengthOfStream = (int) Math.pow(2, Integer.valueOf(content));

				if (lengthOfStream > 0) {
					txtLengthOfStream.setEnabled(Boolean.FALSE);
					fixLengthOfStreamIconButton.setVisible(Boolean.FALSE);
					Utils.setVisibilityOfLabels(Boolean.TRUE, lblSymbol, lblFrequency);
					Utils.setVisibilityOfJTextFields(Boolean.TRUE, txtSymbol, txtFrequency);
					Utils.setVisibilityOfJButtons(Boolean.TRUE, submitIconButton);
				}
			}
		}

		/** submit PARAM_L */
		else if (o.equals(submitParam_L)) {
			txtParam_L.setEnabled(Boolean.FALSE);
			param_L = Integer.valueOf(txtParam_L.getText());
			setIsParamLSet(Boolean.TRUE);
			submitParam_L.setVisible(Boolean.FALSE);
		}

		/** submit PARAM_B */
		else if (o.equals(submitParam_B)) {
			param_b = Integer.valueOf(txtParam_b.getText());
			txtParam_b.setEnabled(Boolean.FALSE);
			setIsParamBSet(Boolean.TRUE);
			submitParam_B.setVisible(Boolean.FALSE);
		}

		/** show look up table */
		else if (o.equals(showLookUpTableIconButton) && getIsParamBSet() && getIsParamLSet()
				&& isProcedureBasedOnIndexSubsets) {

			/** generate subsets for each symbol received from the computed list */
			// if (symbolsPreparedForStreaming == null) {
			symbolsPreparedForStreaming = LookUpUtils.prepareSymbols(listOfSymbolsWithApproxProbability, param_b,
					param_L);
			// }

			LookUp lookUp = new LookUp(symbolsPreparedForStreaming, param_b, param_L);
			lookUp.showFrame();

		}

		/** clear row(s) */
		else if (o.equals(getClearIconButton())) {

			int row[] = table.getSelectedRows();
			if (row.length > 0) {

				JOptionPane.showMessageDialog(null,
						"Selected Symbols will be removed, sum of probabilities will be balanced again",
						Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE);

				List<SymbolCorrespondingToProbability> lsttmp = Utils.removeSymbols(row, model,
						listOfSymbolsWithApproxProbability);

				/** nearest power of two to length of input */
				int content = Utils.getNearestPowerOfTwo(lsttmp);

				/** setting values */
				txtLengthOfStream.setText(String.valueOf(content));
				txtLengthOfStream.setEnabled(Boolean.FALSE);

				lengthOfStream = (int) Math.pow(2, Integer.valueOf(content));

				symbolCounter = 0;

				model.clear();

				listOfSymbolsWithApproxProbability.clear();

				for (SymbolCorrespondingToProbability item : lsttmp) {

					listOfSymbolsWithApproxProbability.add(new SymbolCorrespondingToProbability(item.getSymbol(),
							item.getFrequency(),
							Utils.generateApproximatedProbability(item.getFrequency(), lengthOfStream), symbolCounter));
					symbolCounter++;
				}

				listOfSymbolsWithApproxProbability = Utils.balanceProbabilities(listOfSymbolsWithApproxProbability);

				Utils.fillTableModel(model, lengthOfStream, listOfSymbolsWithApproxProbability);

				symbolsPreparedForStreaming = LookUpUtils.prepareSymbols(listOfSymbolsWithApproxProbability, param_b,
						param_L);

			} else {

				int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all symbols?");

				if (Integer.compare(choice, 0) == 0) {
					currentSumOfStreams = 0;
					listOfSymbols.clear();
					symbolsPreparedForStreaming = null;
					listOfSymbolsWithApproxProbability.clear();
					model.clear();
					submitParam_B.setEnabled(Boolean.TRUE);
					submitParam_L.setEnabled(Boolean.TRUE);
					Utils.clearTextFields(txtParam_b, txtParam_L, txtLengthOfStream);
					setIsParamBSet(Boolean.FALSE);
					setIsParamLSet(Boolean.FALSE);
					Utils.setVisibilityOfJTextFields(Boolean.FALSE, txtParam_b, txtParam_L);
					Utils.setVisibilityOfLabels(Boolean.FALSE, lblParam_B, lblParam_L);

					Utils.setVisibilityOfJButtons(Boolean.TRUE, fixLengthOfStreamIconButton, submitIconButton);
					Utils.setVisibilityOfJButtons(Boolean.FALSE, submitParam_B, submitParam_L, getClearIconButton(),
							decodeIconButton, encodeIconButton, showLookUpTableIconButton);
					txtLengthOfStream.setEnabled(Boolean.TRUE);
					txtParam_L.setEnabled(Boolean.TRUE);
					submitParam_L.setVisible(Boolean.FALSE);
					txtParam_b.setEnabled(Boolean.TRUE);
					submitParam_B.setVisible(Boolean.FALSE);
					txtLengthOfStream.requestFocus();
				} else {
					return;
				}
			}
		}

		/** determine symbols from file */
		else if (o.equals(determineByTextIconButton)) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogType(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter markUpFilter = new FileNameExtensionFilter("Markup: xml, htm, html,txt", "xml",
					"html", "htm", "txt");
			chooser.setFileFilter(markUpFilter);

			chooser.setSelectedFile(new File(Constants.PATH_TO_DESKTOP));
			chooser.setBackground(Color.WHITE);
			int response = chooser.showDialog(null, null);

			if (response == JFileChooser.APPROVE_OPTION) {
				model.clear();
				directory = chooser.getSelectedFile().toString();
				if (directory != null) {
					File file = new File(directory);
					BufferedReader bufferedReader = null;
					try {
						bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
					} catch (FileNotFoundException e) {
						System.err.println(String.format("was not able to open document < %s >", file));
						e.printStackTrace();
					}

					/** reading file */
					if (bufferedReader != null) {
						String line = null;
						StringBuilder builder = new StringBuilder();
						try {
							while ((line = bufferedReader.readLine()) != null) {
								builder.append(line);
							}
						} catch (IOException e) {
							System.err.println(String.format("was not able to read document < %s >", file));
							e.printStackTrace();
						}
						/** file is read */
						contentOfFile = builder.toString();

						List<SymbolCorrespondingToProbability> symbolsCorrespondingToProbabilities = Utils
								.getSymbolsCorrespondingToProbabilityFromInput(contentOfFile);

						/** nearest power of two to length of input */
						int content = Utils.getNearestPowerOfTwo(symbolsCorrespondingToProbabilities);

						/** setting values */
						txtLengthOfStream.setText(String.valueOf(content));
						txtLengthOfStream.setEnabled(Boolean.FALSE);

						lengthOfStream = (int) Math.pow(2, Integer.valueOf(content));

						if (lengthOfStream > 0) {
							txtLengthOfStream.setEnabled(Boolean.FALSE);
							fixLengthOfStreamIconButton.setVisible(Boolean.FALSE);
							Utils.setVisibilityOfLabels(Boolean.FALSE, lblSymbol, lblFrequency);
							Utils.setVisibilityOfJTextFields(Boolean.FALSE, txtSymbol, txtFrequency);
							Utils.setVisibilityOfJButtons(Boolean.FALSE, submitIconButton);
						}

						listOfSymbolsWithApproxProbability.clear();
						symbolCounter = 0;

						for (SymbolCorrespondingToProbability item : symbolsCorrespondingToProbabilities) {

							Utils.fillTableModel(model, lengthOfStream, item.getSymbol(), item.getFrequency());
							listOfSymbolsWithApproxProbability
									.add(new SymbolCorrespondingToProbability(item.getSymbol(), item.getFrequency(),
											Utils.generateApproximatedProbability(item.getFrequency(), lengthOfStream),
											symbolCounter));
							symbolCounter++;
						}

						if (!panelForDisplayingInput.isVisible()) {
							panelForDisplayingInput.setVisible(true);
						}

						Utils.setVisibilityOfJTextFields(Boolean.TRUE, txtParam_L, txtParam_b);
						Utils.setVisibilityOfJButtons(Boolean.TRUE, showLookUpTableIconButton, submitParam_B,
								submitParam_L, encodeIconButton, decodeIconButton, getClearIconButton());
						Utils.setVisibilityOfLabels(Boolean.TRUE, lblParam_L, lblParam_B);

						boolean check = Utils.isSumOfProbabilitiesOne(listOfSymbolsWithApproxProbability);

						if (!check) {

							JOptionPane.showMessageDialog(null,
									"sum of probabilities is not one, probabilities will be approximated such that their sum is one",
									Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE);

							listOfSymbolsWithApproxProbability = Utils
									.balanceProbabilities(listOfSymbolsWithApproxProbability);
							model.clear();

							Utils.fillTableModel(model, lengthOfStream, listOfSymbolsWithApproxProbability);

							int choiceForRecommendateParameters = JOptionPane.showConfirmDialog(null, null,
									"do you wish to get a recommendation for b and L?", JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
							if (Integer.compare(choiceForRecommendateParameters, 0) == 0) {
								RecommendateParameters recommendateParameters = Utils
										.recommendateParameters(listOfSymbolsWithApproxProbability);
								// FIX ME
								// symbolsPreparedForStreaming = recommendateParameters
								// .getLstSymbolsPreparedForStreaming();
								if (recommendateParameters != null) {
									txtParam_L.setText(String.valueOf(recommendateParameters.getParam_L()));
									param_L = recommendateParameters.getParam_L();
									param_b = recommendateParameters.getParam_b();
									txtParam_b.setText(String.valueOf(recommendateParameters.getParam_b()));
									txtParam_L.setEnabled(Boolean.FALSE);
									txtParam_b.setEnabled(Boolean.FALSE);
									setIsParamBSet(Boolean.TRUE);
									setIsParamLSet(Boolean.TRUE);
									submitParam_B.setVisible(Boolean.FALSE);
									submitParam_L.setVisible(Boolean.FALSE);
								}

							}

						}

					}

				}
			}
			compressSelectedFileIconButton.setVisible(Boolean.TRUE);

		}

		/** compress selected file */
		else if (o.equals(compressSelectedFileIconButton)) {

			if (getIsParamBSet() && getIsParamLSet()) {

				String path = null;

				int indexOfFileSource = directory.indexOf(".");
				path = directory.substring(0, indexOfFileSource);
				path = path.concat("_ans.txt");

				File file = new File(path);

				BufferedWriter bufferedWriter = null;
				try {
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				} catch (FileNotFoundException e) {
					System.err.println(String.format("was not able to create document < %s >", file));
					e.printStackTrace();
				}

				if (!isProcedureBasedOnIndexSubsets) {
					/** procedure not based on index subsets */
					PlaceComparator placeComparator = new PlaceComparator();

					Collections.sort(listOfSymbolsWithApproxProbability, placeComparator);

					symbolsPreparedForStreaming = listOfSymbolsWithApproxProbability;
					Encoder encoder = new Encoder(symbolsPreparedForStreaming, param_b, param_L,
							isProcedureBasedOnIndexSubsets);

					EncodingResult encodingResult = encoder.execute(contentOfFile);

					BitMask bitMask = encodingResult.getBitMask();
					int finalState = encodingResult.getFinalState();

					try {

						for (List<Integer> lst : bitMask.getListOfRests()) {

							for (int i = 0; i < lst.size(); i++) {

								bufferedWriter.write(String.valueOf(lst.get(i)));
							}
							bufferedWriter.newLine();
						}
						bufferedWriter.write(String.valueOf(finalState));

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					fileIsCompressedIndexBased = Boolean.FALSE;

				} else {

					/** procedure based on index subsets */
					symbolsPreparedForStreaming = LookUpUtils.prepareSymbols(listOfSymbolsWithApproxProbability,
							param_b, param_L);

					Encoder encoder = new Encoder(symbolsPreparedForStreaming, param_b, param_L,
							isProcedureBasedOnIndexSubsets);

					EncodingResult encodingResult = encoder.execute(contentOfFile);

					BitMaskForIndexSubsets bitMask = encodingResult.getBitMaskForIndexSubsets();
					int finalState = encodingResult.getFinalState();

					try {

						for (Integer rest : bitMask.getListOfRests()) {

							bufferedWriter.write(String.valueOf(rest));
						}

						bufferedWriter.write(String.valueOf(finalState));

					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							bufferedWriter.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

				}

				fileIsCompressedIndexBased = isProcedureBasedOnIndexSubsets;
				compressSelectedFileIconButton.setVisible(Boolean.FALSE);
				uncompressSelectedFileIconButton.setVisible(Boolean.TRUE);
			} else {
				JOptionPane.showMessageDialog(null, "Please check settings of parameters");
			}
		}

		/** uncompress selected file */
		else if (o.equals(uncompressSelectedFileIconButton)) {

			if (isProcedureBasedOnIndexSubsets != fileIsCompressedIndexBased) {
				JOptionPane.showMessageDialog(null, "It is not possible to change procedure for uncrompressing file!",
						Constants.ERROR, JOptionPane.ERROR_MESSAGE);
				return;
			}

			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(Constants.UNCOMPRESS_FILE);
			chooser.setDialogType(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter markUpFilter = new FileNameExtensionFilter("Markup: txt", "txt");
			chooser.setFileFilter(markUpFilter);

			chooser.setSelectedFile(new File(Constants.PATH_TO_DESKTOP));
			chooser.setBackground(Color.WHITE);
			int response = chooser.showDialog(null, null);

			if (response == JFileChooser.APPROVE_OPTION) {

				File selectedFile = chooser.getSelectedFile();

				/** preparing File to read */
				BufferedReader bufferedReader = null;

				try {
					bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(selectedFile)));
					try {
						bufferedReader.close();
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					System.err.println(
							String.format("was not able to read document < %s >", selectedFile.getAbsolutePath()));
					e.printStackTrace();
				}

				/** reading file */
				String line;
				List<String> restsAndFinalStateAsList = new ArrayList<String>();

				try {
					while ((line = bufferedReader.readLine()) != null) {

						restsAndFinalStateAsList.add(line);

					}
				} catch (IOException e) {
					System.err.println(
							String.format("was not able to read document < %s >", selectedFile.getAbsolutePath()));
					e.printStackTrace();
				} finally {
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				/** the decoded result */
				String result = null;

				/** procedures are starting */
				if (!isProcedureBasedOnIndexSubsets) {

					/** procedure not based on index subsets */

					int countOfLines = restsAndFinalStateAsList.size();

					if (countOfLines > 0) {

						/** Components of BitMask */
						BitMask bitMask = new BitMask();
						ArrayList<Integer> lstModuloRest;
						int finalState = 0;

						/** the final State */
						String finalStateAsString = restsAndFinalStateAsList.get(countOfLines - 1);
						finalState = Integer.valueOf(finalStateAsString);

						/** the modulo rests */
						for (int i = 0; i < countOfLines - 1; i++) {

							lstModuloRest = new ArrayList<Integer>();
							String moduloRestsAsString = restsAndFinalStateAsList.get(i);

							for (int j = 0; j < moduloRestsAsString.length(); j++) {
								String restAsString = String.valueOf(moduloRestsAsString.charAt(j));

								int restAsInt = Integer.valueOf(restAsString);
								lstModuloRest.add(restAsInt);
							}

							bitMask.addBit(lstModuloRest);

						}

						/** the decoding */
						Decoder decoder = new Decoder(symbolsPreparedForStreaming, param_b, param_L, bitMask,
								isProcedureBasedOnIndexSubsets);
						result = decoder.execute(finalState);

					}

				} else {

					/** procedure based on index subsets */
					String content = restsAndFinalStateAsList.get(0);
					int length = content.length();

					int symbolPointer = length - 1;
					int potency = 0;
					String resultToCheckAsString = String.valueOf(content.charAt(symbolPointer));

					/** computing final state */

					int finalState = (int) (Integer.valueOf(resultToCheckAsString) * Math.pow(10.0, potency));

					while (finalState < param_L) {

						symbolPointer--;
						potency++;

						int tmpPotency = potency;
						finalState = 0;
						for (int i = symbolPointer; i < length; i++) {
							finalState += Integer.valueOf(String.valueOf(content.charAt(i)))
									* Math.pow(10.0, tmpPotency);
							tmpPotency--;
						}

					}

					/** final state is calculated */

					BitMaskForIndexSubsets bitMaskForIndexSubsets = new BitMaskForIndexSubsets();

					for (int i = 0; i < symbolPointer; i++) {
						bitMaskForIndexSubsets.addBit(Integer.valueOf(String.valueOf(content.charAt(i))));
					}

					Decoder decoder = new Decoder(symbolsPreparedForStreaming, param_b, param_L, bitMaskForIndexSubsets,
							isProcedureBasedOnIndexSubsets);

					result = decoder.execute(finalState);

				}

				/** writing the file */

				/** writing result */
				int indexOfFileSource = directory.indexOf(".");
				String path = directory.substring(0, indexOfFileSource);
				path = path.concat("_unans.txt");

				File file = new File(path);
				BufferedWriter bufferedWriter = null;
				try {
					bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				} catch (FileNotFoundException e) {
					System.err.println(String.format("was not able to create document < %s >", file));
					e.printStackTrace();
				}

				try {
					bufferedWriter.write(result);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						bufferedWriter.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		}
		/** load image */
		if (o.equals(loadImageIconButton)) {
			JFileChooser chooser = new JFileChooser();
			chooser.setDialogTitle(Constants.SELECT_IMAGE);
			chooser.setDialogType(JFileChooser.FILES_ONLY);
			FileNameExtensionFilter markUpFilter = new FileNameExtensionFilter("Markup: jpg,png", "jpg", "png");
			chooser.setFileFilter(markUpFilter);
			chooser.setBackground(Color.WHITE);

			/** set path to documents */
			chooser.setSelectedFile(FileSystemView.getFileSystemView().getHomeDirectory());

			int response = chooser.showDialog(null, null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File selectedFile = chooser.getSelectedFile();

				String path = chooser.getSelectedFile().getAbsolutePath();
				long val = 0;
				try {
					bufferedImage = ImageIO.read(new FileImageInputStream(selectedFile));
					val = Utils.getSizeOfImage(bufferedImage);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// getting symbols with frequencies:
				List<SymbolCorrespondingToProbability> symbolsCorrespondingToProbabilities = ImageUtils
						.getSymbolsCorrespondingToProbabilityFromImage(bufferedImage);

				/** nearest power of two to length of input */
				int content = Utils.getNearestPowerOfTwo(symbolsCorrespondingToProbabilities);

				/** setting values */
				txtLengthOfStream.setText(String.valueOf(content));
				txtLengthOfStream.setEnabled(Boolean.FALSE);

				// lengthOfStream = (int) Math.pow(2, Integer.valueOf(content));
				lengthOfStream = 1 << Integer.valueOf(content);

				if (lengthOfStream > 0) {
					txtLengthOfStream.setEnabled(Boolean.FALSE);
					fixLengthOfStreamIconButton.setVisible(Boolean.FALSE);
					Utils.setVisibilityOfLabels(Boolean.FALSE, lblSymbol, lblFrequency);
					Utils.setVisibilityOfJTextFields(Boolean.FALSE, txtSymbol, txtFrequency);
					Utils.setVisibilityOfJButtons(Boolean.FALSE, submitIconButton);
				}

				listOfSymbolsWithApproxProbability.clear();
				symbolCounter = 0;

				for (SymbolCorrespondingToProbability item : symbolsCorrespondingToProbabilities) {

					Utils.fillTableModel(model, lengthOfStream, item.getSymbol(), item.getFrequency());
					listOfSymbolsWithApproxProbability.add(new SymbolCorrespondingToProbability(item.getSymbol(),
							item.getFrequency(),
							Utils.generateApproximatedProbability(item.getFrequency(), lengthOfStream), symbolCounter));
					symbolCounter++;
				}

				if (!panelForDisplayingInput.isVisible()) {
					panelForDisplayingInput.setVisible(true);
				}

				Utils.setVisibilityOfJTextFields(Boolean.TRUE, txtParam_L, txtParam_b);
				Utils.setVisibilityOfJButtons(Boolean.TRUE, showLookUpTableIconButton, submitParam_B, submitParam_L,
						encodeIconButton, decodeIconButton, getClearIconButton());
				Utils.setVisibilityOfLabels(Boolean.TRUE, lblParam_L, lblParam_B);

				boolean check = Utils.isSumOfProbabilitiesOne(listOfSymbolsWithApproxProbability);

				if (!check) {

					JOptionPane.showMessageDialog(null,
							"sum of probabilities is not one, probabilities will be approximated such that their sum is one",
							Constants.INFORMATION, JOptionPane.INFORMATION_MESSAGE);

					listOfSymbolsWithApproxProbability = Utils.balanceProbabilities(listOfSymbolsWithApproxProbability);
					model.clear();

					Utils.fillTableModel(model, lengthOfStream, listOfSymbolsWithApproxProbability);

					int choiceForRecommendateParameters = JOptionPane.showConfirmDialog(null, null,
							"do you wish to get a recommendation for b and L?", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE);
					if (Integer.compare(choiceForRecommendateParameters, 0) == 0) {
						RecommendateParameters recommendateParameters = Utils
								.recommendateParameters(listOfSymbolsWithApproxProbability);

						if (recommendateParameters != null) {
							txtParam_L.setText(String.valueOf(recommendateParameters.getParam_L()));
							param_L = recommendateParameters.getParam_L();
							param_b = recommendateParameters.getParam_b();
							txtParam_b.setText(String.valueOf(recommendateParameters.getParam_b()));
							txtParam_L.setEnabled(Boolean.FALSE);
							txtParam_b.setEnabled(Boolean.FALSE);
							setIsParamBSet(Boolean.TRUE);
							setIsParamLSet(Boolean.TRUE);
							submitParam_B.setVisible(Boolean.FALSE);
							submitParam_L.setVisible(Boolean.FALSE);
						}

					}

				}

				/** procedure based on index subsets */
				symbolsPreparedForStreaming = LookUpUtils.prepareSymbols(listOfSymbolsWithApproxProbability, param_b,
						param_L);

				CompressImageDialog compressImageDialog = new CompressImageDialog(path, bufferedImage,
						isProcedureBasedOnIndexSubsets, symbolsPreparedForStreaming, param_b, param_L);
				compressImageDialog.setVisible(Boolean.TRUE);
			}
		}

	}

	public void showFrame() {
		this.setVisible(true);
	}

	// get & set follows below here
	public static JTextField getTxtProbability() {
		return txtLengthOfStream;
	}

	public static JTextField getTxtParam_L() {
		return txtParam_L;
	}

	public static JTextField getTxtParam_b() {
		return txtParam_b;
	}

	public static JTextField getTxtSymbol() {
		return txtSymbol;
	}

	public static JTextField getTxtFrequency() {
		return txtFrequency;
	}

	public static JTextField getTxtLengthOfStream() {
		return txtLengthOfStream;
	}

	public static Integer getLengthOfStream() {
		return lengthOfStream;
	}

	public static Integer getCurrentSumOfStreams() {
		return currentSumOfStreams;
	}

	public List<EncodingResult> getLstEncodingResult() {
		return lstEncodingResult;
	}

	public void setLstEncodingResult(List<EncodingResult> lstEncodingResult) {
		this.lstEncodingResult = lstEncodingResult;
	}

	public static List<SymbolCorrespondingToProbability> getListOfSymbolsWithApproxProbability() {
		return listOfSymbolsWithApproxProbability;
	}

	public Boolean getIsProcedureBasedOnIndexSubsets() {
		return isProcedureBasedOnIndexSubsets;
	}

	public void setIsProcedureBasedOnIndexSubsets(Boolean isProcedureBasedOnIndexSubsets) {
		this.isProcedureBasedOnIndexSubsets = isProcedureBasedOnIndexSubsets;
	}

	public StatusBar getStatusBar() {
		return statusBar;
	}

	public void setStatusBar(StatusBar statusBar) {
		this.statusBar = statusBar;
	}

	public static Boolean getIsParamBSet() {
		return isParamBSet;
	}

	public static void setIsParamBSet(Boolean isParamBSet) {
		ANS.isParamBSet = isParamBSet;
	}

	public static Boolean getIsParamLSet() {
		return isParamLSet;
	}

	public static void setIsParamLSet(Boolean isParamLSet) {
		ANS.isParamLSet = isParamLSet;
	}

	public static Boolean getIsSymbolSet() {
		return isSymbolSet;
	}

	public static void setIsSymbolSet(Boolean isSymbolSet) {
		ANS.isSymbolSet = isSymbolSet;
	}

	public static IconButton getClearIconButton() {
		return clearIconButton;
	}

	public static void setClearIconButton(IconButton clearIconButton) {
		ANS.clearIconButton = clearIconButton;
	}

	public static Boolean getIsFrequencySet() {
		return isFrequencySet;
	}

	public static void setIsFrequencySet(Boolean isFrequencySet) {
		ANS.isFrequencySet = isFrequencySet;
	}

	public static Boolean getIsLengthOfStreamSet() {
		return isLengthOfStreamSet;
	}

	public static void setIsLengthOfStreamSet(Boolean isLengthOfStreamSet) {
		ANS.isLengthOfStreamSet = isLengthOfStreamSet;
	}

	public ActionContextMenu getActionContextMenu() {
		return actionContextMenu;
	}

	public void setActionContextMenu(ActionContextMenu actionContextMenu) {
		this.actionContextMenu = actionContextMenu;
	}

	public static IconButton getSubmitParam_B() {
		return submitParam_B;
	}

	public static IconButton getSubmitParam_L() {
		return submitParam_L;
	}

}
