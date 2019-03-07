package compressImage;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

import ans.SymbolCorrespondingToProbability;

public class ImageUtils {

	public static BufferedImage buildImage(List<Object> lstColor, int width, int height) {

		BufferedImage response = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster writableRaster = response.getRaster();
		ColorModel colorModel = response.getColorModel();

		for (int x = 0; x < lstColor.size(); x++) {
			@SuppressWarnings("unchecked")
			List<Color> lstColorsAtPixelInRow = (List<Color>) lstColor.get(x);

			for (int y = 0; y < lstColorsAtPixelInRow.size(); y++) {
				Color colorAtPixel = lstColorsAtPixelInRow.get(y);
				Color transformedColor = new Color(colorAtPixel.getRed(), colorAtPixel.getGreen(),
						colorAtPixel.getBlue(), colorAtPixel.getAlpha());
				int rgb = transformedColor.getRGB();
				Object dataAtPixel = colorModel.getDataElements(rgb, null);
				writableRaster.setDataElements(x, y, dataAtPixel);
			}
		}
		return response;
	}

	private void makeImage() {
		int width = 200, height = 200;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		WritableRaster raster = image.getRaster();
		ColorModel model = image.getColorModel();

		Color c1 = Color.RED;
		int argb1 = c1.getRGB();
		Object data1 = model.getDataElements(argb1, null);

		Color c2 = Color.GRAY;
		int argb2 = c2.getRGB();
		Object data2 = model.getDataElements(argb2, null);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				raster.setDataElements(i, j, data1);
				if (i > 50 && i < 150 && j > 50 && j < 150) {
					raster.setDataElements(i, j, data2);
				}
			}
		}
	}

	/** counts the frequencies of each rgb values: */
	public static List<SymbolCorrespondingToProbability> getSymbolsCorrespondingToProbabilityFromImage(
			BufferedImage bufferedImage) {

		int[] numbers = new int[256];

		List<SymbolCorrespondingToProbability> response = new ArrayList<SymbolCorrespondingToProbability>();

		for (int x = 0; x < bufferedImage.getWidth(); x++) {
			for (int y = 0; y < bufferedImage.getHeight(); y++) {

				// getting the color at each pixel:
				Color color = new Color(bufferedImage.getRGB(x, y));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				int alpha = color.getAlpha();

				// incrementing corresponding value symbol
				numbers[red]++;
				numbers[green]++;
				numbers[blue]++;
				numbers[alpha]++;
			}
		}

		for (int i = 0; i < 256; i++) {

			int frequency = numbers[i];
			if (Integer.compare(frequency, 0) != 0) {
				response.add(new SymbolCorrespondingToProbability(String.valueOf(i), frequency));
			}
		}

		return response;
	}

	/**
	 * scales a given buffered image
	 * 
	 * @param bufferedImage
	 *            - the buffered image
	 * @param width
	 *            - the new width
	 * @param height
	 *            - the new height
	 * @return - the scaled buffered image
	 */
	public static BufferedImage resizeBufferedImage(BufferedImage bufferedImage, int width, int height) {
		BufferedImage scaled = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaled.createGraphics();
		g.drawImage(bufferedImage, 0, 0, width, height, null);
		g.dispose();

		return scaled;
	}

	public static BufferedImage processImage(File file) {
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(new FileImageInputStream(file));
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		ColorModel model = bufferedImage.getColorModel();

		WritableRaster raster = bufferedImage.getRaster();
		for (int i = 0; i < bufferedImage.getWidth(); i++) {
			for (int j = 0; j < bufferedImage.getHeight(); j++) {

				Object dataAlt = raster.getDataElements(i, j, null);
				int argbAlt = model.getRGB(dataAlt);

				Color c = new Color(argbAlt, true);

				int limit = 150, wert = 50;

				int r = (c.getRed() + limit) < 256 ? c.getRed() + wert : 255;
				int g = (c.getGreen() + limit) < 256 ? c.getGreen() + wert : 255;
				int b = (c.getBlue() + limit) < 256 ? c.getBlue() + wert : 255;

				Color cNeu = new Color(r, g, b);

				int argbNeu = cNeu.getRGB();

				Object dataNeu = model.getDataElements(argbNeu, null);

				raster.setDataElements(i, j, dataNeu);
			}
		}
		return bufferedImage;
	}

}
