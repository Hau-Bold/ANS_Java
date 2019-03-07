package ans;

import javax.swing.SwingWorker;

import compressImage.CompressImageDialog;

public class InfiniteProgress extends SwingWorker<Void, Void> {
	private int locX;
	private int locY;
	private CompressImageDialog compressImageDialog;
	private Boolean isModeCompressImage;

	public InfiniteProgress(CompressImageDialog compressImageDialog, int locX, int locY, Boolean isModeCompressImage) {
		this.compressImageDialog = compressImageDialog;
		this.locX = locX;
		this.locY = locY;
		this.isModeCompressImage = isModeCompressImage;
	}

	@Override
	protected Void doInBackground() throws Exception {

		ProgressBar Progress = new ProgressBar(locX, locY);
		Progress.getProgressBar().setIndeterminate(true);

		if (isModeCompressImage) {
			compressImageDialog.compressImage(Progress);
		} else {
			compressImageDialog.uncompressImage(Progress);

		}
		Progress.dispose();
		return null;
	}

	@Override
	protected void done() {
		// TODO Auto-generated method stub
		super.done();
	}

}
