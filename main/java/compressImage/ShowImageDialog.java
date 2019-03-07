package compressImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import listeners.ResizeListener;

public class ShowImageDialog extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedImage bufferedImage;
	public static JScrollPane scrollPane;
	private ImagePanel imagePanel;

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
	public ShowImageDialog(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		initComponent();
	}

	public void showFrame() {
		this.setVisible(true);

	}

	private void initComponent() {
		this.setTitle("the uncompressed");
		this.setBounds(50, 50, 500, 500);
		this.setLayout(new BorderLayout());
		this.setResizable(true);
		this.getContentPane().setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		/** save image in scrollPane */
		scrollPane = new JScrollPane();
		// Vertikales Scrollen
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// Horizontales Scrollen
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.imagePanel = new ImagePanel(bufferedImage);
		scrollPane.setColumnHeaderView(imagePanel);

		this.add(scrollPane);
		this.addComponentListener(new ResizeListener(this));
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

}
