package statistics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import ans.SymbolCorrespondingToProbability;

/** the class ShowStatisticsDialog */
public class ShowStatisticsDialog extends JFrame {

	private static final long serialVersionUID = 1L;

	public ShowStatisticsDialog(List<SymbolCorrespondingToProbability> listOfSymbolsWithApproxProbability) {

		JPanel chartPanel = createChartPanel(listOfSymbolsWithApproxProbability);
		add(chartPanel, BorderLayout.CENTER);

		setSize(1000, 800);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
	}

	private JPanel createChartPanel(List<SymbolCorrespondingToProbability> lstSymbolsWithApproxProbability) {
		String chartTitle = "Statistics";
		String xAxisLabel = "N";
		String yAxisLabel = "Frequency";

		XYDataset dataset = createDataset(lstSymbolsWithApproxProbability);

		boolean showLegend = false;
		boolean createURL = true;
		boolean createTooltip = false;

		JFreeChart chart = ChartFactory.createXYLineChart(chartTitle, xAxisLabel, yAxisLabel, dataset,
				PlotOrientation.VERTICAL, showLegend, createTooltip, createURL);
		XYSplineRenderer splineRenderer = new XYSplineRenderer(10);

		chart.getXYPlot().setRenderer(splineRenderer);
		chart.getXYPlot().getRenderer().setSeriesPaint(0, Color.RED);
		chart.getXYPlot().getRenderer().setSeriesShape(0, ShapeUtilities.createRegularCross((float) 0.0, (float) 0.0));

		return new ChartPanel(chart);
	}

	private XYDataset createDataset(List<SymbolCorrespondingToProbability> lstSymbolsWithApproxProbability) {

		List<SymbolCorrespondingToProbability> lstCopy = new ArrayList<SymbolCorrespondingToProbability>(
				lstSymbolsWithApproxProbability);

		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series1 = new XYSeries("1");

		for (int i = 0; i < lstCopy.size(); i++) {

			SymbolCorrespondingToProbability current = lstCopy.get(i);
			int currentSymbolAsInt = Integer.valueOf(current.getSymbol());

			series1.add(currentSymbolAsInt, lstCopy.get(i).getFrequency());

		}

		dataset.addSeries(series1);

		return dataset;
	}

}
