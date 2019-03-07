package compressImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import ans.Constants;
import ans.IconButton;
import ans.InfiniteProgress;
import ans.ProgressBar;
import ans.SymbolCorrespondingToProbability;
import encryption.BitMask;
import encryption.BitMaskForIndexSubsets;
import encryption.Decoder;
import encryption.EncodeImageStripe;
import encryption.EncodingResult;
import listeners.ResizeListener;
import statistics.ShowStatisticsDialog;

public class CompressImageDialog extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedImage bufferedImage;
	public static JScrollPane scrollPane;
	private ImagePanel imagePanel;
	private IconButton compressSelectedImageIconButton, uncompressImageIconButton, showStatisticsOfImageIconButton;
	private JToolBar toolBarForButtons;
	private ExecutorService executorService;
	private Boolean isProcedureBasedOnIndexSubsets;

	private List<Object> lstEncodingResult = new ArrayList<Object>();
	private List<Object> lstDecodingResult = new ArrayList<Object>();
	private List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability;
	private Integer param_b;
	private Integer param_L;
	private String path;
	private int imageWidth;
	private int imageHeight;

	/**
	 * Constructor.
	 * 
	 * @param path
	 * 
	 * @param isProcedureBasedOnIndexSubsets
	 * @param listOfSymbolsWithApproxProbability
	 * @param param_L
	 * @param param_b
	 * 
	 * @param byteArray
	 *            the image as Byte Array
	 */
	public CompressImageDialog(String path, BufferedImage bufferedImage, Boolean isProcedureBasedOnIndexSubsets,
			List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability, Integer param_b,
			Integer param_L) {
		this.bufferedImage = bufferedImage;
		this.isProcedureBasedOnIndexSubsets = isProcedureBasedOnIndexSubsets;
		this.listOfSymbolsWithApproxProbability = listOfSymbolsWithApproxProbability;
		this.param_b = param_b;
		this.param_L = param_L;
		this.path = path;
		this.imageWidth = bufferedImage.getWidth();
		this.imageHeight = bufferedImage.getHeight();
		initComponent();
	}

	public void showFrame() {
		this.setVisible(true);

	}

	private void initComponent() {
		this.setBounds(50, 50, 500, 500);
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		compressSelectedImageIconButton = new IconButton("compress.jpg", 0, 0);
		compressSelectedImageIconButton.addActionListener(this);
		compressSelectedImageIconButton.setMnemonic(KeyEvent.VK_C);

		uncompressImageIconButton = new IconButton("uncompressImage.png", 20, 0);
		uncompressImageIconButton.setVisible(Boolean.FALSE);
		uncompressImageIconButton.addActionListener(this);
		uncompressImageIconButton.setMnemonic(KeyEvent.VK_U);

		showStatisticsOfImageIconButton = new IconButton("statistics.jpg", 40, 0);
		showStatisticsOfImageIconButton.addActionListener(this);
		showStatisticsOfImageIconButton.setMnemonic(KeyEvent.VK_S);

		toolBarForButtons = new JToolBar();
		toolBarForButtons.add(compressSelectedImageIconButton);
		toolBarForButtons.add(uncompressImageIconButton);
		toolBarForButtons.add(showStatisticsOfImageIconButton);

		this.add(toolBarForButtons, BorderLayout.SOUTH);

		/** save image in scrollPane */
		scrollPane = new JScrollPane();
		// scroll vertically
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scroll horizontly
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.imagePanel = new ImagePanel(bufferedImage);
		scrollPane.setColumnHeaderView(imagePanel);

		this.add(scrollPane);
		this.addComponentListener(new ResizeListener(this));
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Object o = event.getSource();

		if (o.equals(compressSelectedImageIconButton)) {

			new InfiniteProgress(this, this.getX(), this.getY(), Boolean.TRUE).execute();

		}

		else if (o.equals(uncompressImageIconButton)) {

			new InfiniteProgress(this, this.getX(), this.getY(), Boolean.FALSE).execute();

		}

		else if (o.equals(showStatisticsOfImageIconButton)) {

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					new ShowStatisticsDialog(listOfSymbolsWithApproxProbability).setVisible(true);
				}
			});
		}

	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void compressImage(ProgressBar progress) {

		executorService = Executors.newFixedThreadPool(imageWidth);

		for (int i = 0; i < bufferedImage.getWidth(); i++) {

			try {
				lstEncodingResult.add(executorService.submit(new EncodeImageStripe(i, bufferedImage,
						isProcedureBasedOnIndexSubsets, listOfSymbolsWithApproxProbability, param_b, param_L)).get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}

		progress.setTitle(Constants.PROCESSING);

		// write it:

		int indexOfFileSource = path.indexOf(".");
		path = path.substring(0, indexOfFileSource);
		path = path.concat("_ans.txt");

		File file = new File(path);

		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println(String.format("was not able to create document < %s >", file));
			e.printStackTrace();
		}

		for (int i = 0; i < lstEncodingResult.size(); i++) {

			EncodingResult tmp = (EncodingResult) lstEncodingResult.get(i);

			if (isProcedureBasedOnIndexSubsets) {
				/** procedure is based on index subsets */
				try {
					List<Integer> tmplst = tmp.getBitMaskForIndexSubsets().getListOfRests();
					for (int j = 0; j < tmplst.size(); j++) {
						bufferedWriter.write(String.valueOf(tmplst.get(j)));

					}
					bufferedWriter.write(String.valueOf(tmp.getFinalState()));
					bufferedWriter.newLine();
					// drain Stream
					bufferedWriter.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				/** procedure is not based on index subsets */

				List<ArrayList<Integer>> tmplst = tmp.getBitMask().getListOfRests();
				int finalState = tmp.getFinalState();
				Boolean isLastListToWrite = Boolean.FALSE;
				try {
					for (int k = 0; k < tmplst.size(); k++) {

						if (Integer.compare(tmplst.size() - 1, k) == 0) {
							isLastListToWrite = Boolean.TRUE;
						}

						ArrayList<Integer> currentModuloRests = tmplst.get(k);

						for (int j = 0; j < currentModuloRests.size(); j++) {
							bufferedWriter.write(String.valueOf(currentModuloRests.get(j)));
						}
						/** one list is written */
						if (!isLastListToWrite) {
							bufferedWriter.write(" ");
						}
					}
					/** all lists belonging to the BitMask are written */
					bufferedWriter.write(String.valueOf(finalState));
					bufferedWriter.newLine();
					// drain Stream
					bufferedWriter.flush();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		compressSelectedImageIconButton.setVisible(Boolean.FALSE);
		uncompressImageIconButton.setVisible(Boolean.TRUE);

	}

	public void uncompressImage(ProgressBar progress) {

		executorService = Executors.newFixedThreadPool(imageWidth);

		File file = new File(path);
		int indexOfEnding = path.indexOf("_");
		path = path.substring(0, indexOfEnding);
		path = path.concat("_uans.txt");

		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			System.err.println(String.format("was not able to read document < %s >", file));
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
			System.err.println(String.format("was not able to read document < %s >", file.getAbsolutePath()));
			e.printStackTrace();
		}

		progress.setTitle(Constants.PROCESSING);

		for (int counter = 0; counter < restsAndFinalStateAsList.size(); counter++) {
			String content = restsAndFinalStateAsList.get(counter);
			int length = content.length();

			int symbolPointer = length - 1;
			int potency = 0;
			String resultToCheckAsString = String.valueOf(content.charAt(symbolPointer));
			int finalState = 0;

			/** procedure based on index subsets */

			/** computing final state */
			finalState = (int) (Integer.valueOf(resultToCheckAsString) * Math.pow(10.0, potency));

			while (finalState < param_L) {

				symbolPointer--;
				potency++;

				int tmpPotency = potency;
				finalState = 0;
				for (int i = symbolPointer; i < length; i++) {
					try {
						finalState += Integer.valueOf(String.valueOf(content.charAt(i))) * Math.pow(10.0, tmpPotency);
						tmpPotency--;
					} catch (NumberFormatException ex) {
						System.out.println(counter);
						ex.printStackTrace();
					}
				}

			}
			/** final state is calculated */

			if (isProcedureBasedOnIndexSubsets) {
				BitMaskForIndexSubsets bitMaskForIndexSubsets = new BitMaskForIndexSubsets();

				for (int i = 0; i < symbolPointer; i++) {
					bitMaskForIndexSubsets.addBit(Integer.valueOf(String.valueOf(content.charAt(i))));
				}

				try {
					lstDecodingResult.add(executorService
							.submit(new DecodeImageStripe(new Decoder(listOfSymbolsWithApproxProbability, param_b,
									param_L, bitMaskForIndexSubsets, isProcedureBasedOnIndexSubsets), finalState))
							.get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {

				BitMask bitMask = new BitMask();
				ArrayList<Integer> lstModuloRests = new ArrayList<Integer>();

				for (int i = 0; i < symbolPointer; i++) {

					String currentRest = String.valueOf(content.charAt(i));

					if (!currentRest.equals(" ")) {
						lstModuloRests.add(Integer.valueOf(currentRest));
					} else {
						ArrayList<Integer> lstTmpModuloRests = new ArrayList<Integer>(lstModuloRests);
						bitMask.addBit(lstTmpModuloRests);
						lstModuloRests.clear();
					}
				}

				try {
					lstDecodingResult
							.add(executorService
									.submit(new DecodeImageStripe(new Decoder(listOfSymbolsWithApproxProbability,
											param_b, param_L, bitMask, isProcedureBasedOnIndexSubsets), finalState))
									.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}

			}
			/** up to next line */

		}

		BufferedImage bufferedImage = ImageUtils.buildImage(lstDecodingResult, imageWidth, imageHeight);

		ShowImageDialog showImageDialog = new ShowImageDialog(bufferedImage);
		showImageDialog.setVisible(Boolean.TRUE);

	}

}
