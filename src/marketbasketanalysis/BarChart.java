/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package marketbasketanalysis;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JPanel;
import model.controller;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Panji
 */
public class BarChart extends JPanel {

    private JFreeChart barchart;
    private ChartPanel panel;
    private String prefix;
    controller c = new controller();

    public BarChart(String prefix) {
        this.prefix = prefix;
        panel = createChart(createData());
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    public BarChart() {
        this.prefix = prefix;
        panel = createChart(createData());
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private DefaultCategoryDataset createData() {
        DefaultCategoryDataset data = new DefaultCategoryDataset();
        double [][] hasil = c.getChart(prefix);

        for (int i = 0; i < hasil.length; i++) {
            double[] ds = hasil[i];
            data.addValue(ds[0]*100, "Support", String.valueOf(i+1));
            data.addValue(ds[1]*100, "Confidence", String.valueOf(i+1));
//            data.addValue(ds[2], "Lift", String.valueOf(i+1));
//            data.addValue(ds[3], "Conviction", String.valueOf(i+1));
            System.out.println(ds[0]);
        }


        return data;
    }

    private ChartPanel createChart(DefaultCategoryDataset data) {

        //barchart = ChartFactory.createBarChart3D("Association Rules", "Parameter", "Values (in %)", data, PlotOrientation.VERTICAL, true, true, false);
        barchart = ChartFactory.createBarChart("Association Rules", "Rules", "Values (in %)", data, PlotOrientation.HORIZONTAL, true, true, true);
        //barchart.setBackgroundPaint(new Color(135,206,250));

        CategoryPlot plot = (CategoryPlot) barchart.getPlot();
        plot.setNoDataMessage("Data Tidak ada");

        CategoryAxis axis = plot.getDomainAxis();
        axis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);

        CategoryItemRenderer renderer = plot.getRenderer();
        renderer.setBaseItemLabelsVisible(true);
        BarRenderer barRenderer = (BarRenderer) renderer;
        barRenderer.setItemMargin(0.1D);

        barRenderer.setDrawBarOutline(true);
        return new ChartPanel(barchart);
    }
}
