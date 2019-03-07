package listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import compressImage.CompressImageDialog;
import compressImage.ImagePanel;
import compressImage.ImageUtils;
import compressImage.ShowImageDialog;

public class ResizeListener implements ComponentListener {

	private BufferedImage bufferedImage;
	private Boolean useCompressDialog = Boolean.TRUE;
	private CompressImageDialog compressImageDialog;
	private ShowImageDialog showImageDialog;

	public ResizeListener(CompressImageDialog compressImageDialog) {

		this.compressImageDialog = compressImageDialog;
		this.bufferedImage = compressImageDialog.getBufferedImage();

	}

	public ResizeListener(ShowImageDialog showImageDialog) {
		this.showImageDialog = showImageDialog;
		this.bufferedImage = showImageDialog.getBufferedImage();
		useCompressDialog = Boolean.FALSE;
	}

	public void componentHidden(ComponentEvent arg0) {
		check();
	}

	public void componentMoved(ComponentEvent arg0) {
		check();
	}

	public void componentResized(ComponentEvent event) {

		check();

	}

	private void check() {

		if (useCompressDialog) {
			int imageWidth = compressImageDialog.getWidth();
			int imageHeight = compressImageDialog.getHeight();
			BufferedImage scaled = ImageUtils.resizeBufferedImage(bufferedImage, imageWidth, imageHeight);
			ImagePanel imagePanel = new ImagePanel(scaled);
			compressImageDialog.scrollPane.setColumnHeaderView(imagePanel);
		} else {
			int imageWidth = showImageDialog.getWidth();
			int imageHeight = showImageDialog.getHeight();
			BufferedImage scaled = ImageUtils.resizeBufferedImage(bufferedImage, imageWidth, imageHeight);
			ImagePanel imagePanel = new ImagePanel(scaled);
			showImageDialog.scrollPane.setColumnHeaderView(imagePanel);
		}
	}

	public void componentShown(ComponentEvent arg0) {
		check();
	}

}
