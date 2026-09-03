import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Visualization {
    
    public static void showIntensityGraph(double[] intensity, double screenWidth, String title) {
        XYSeries series = new XYSeries("Интенсивность");
        int numPoints = intensity.length;
        double dx = screenWidth / numPoints;
        
        double maxIntensity = 0.0;
        for (int i = 0; i < numPoints; i++) {
            maxIntensity = Math.max(maxIntensity, intensity[i]);
        }
        
        for (int i = 0; i < numPoints; i++) {
            double x = (i - numPoints / 2.0) * dx;
            series.add(x, intensity[i] / maxIntensity);
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        
        JFreeChart chart = ChartFactory.createXYLineChart(
            title,
            "x (мм)",
            "Интенсивность (отн. ед.)",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        NumberAxis domainAxis = (NumberAxis) chart.getXYPlot().getDomainAxis();
        domainAxis.setRange(-screenWidth / 2, screenWidth / 2);
        domainAxis.setAutoRange(false);
        
        NumberAxis rangeAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        rangeAxis.setRange(0.0, 1.1);
        rangeAxis.setAutoRange(false);
        
        Font font = new Font("Arial", Font.PLAIN, 12);
        chart.getTitle().setFont(new Font("Arial", Font.BOLD, 14));
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        
        ChartFrame frame = new ChartFrame("Интерференция", chart);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void showColorMap(double[] intensity, double screenWidth, double L, String title) {
        int numPoints = intensity.length;
        double dx = screenWidth / numPoints;
        
        double maxIntensity = 0.0;
        for (int i = 0; i < numPoints; i++) {
            maxIntensity = Math.max(maxIntensity, intensity[i]);
        }
        
        int imageWidth = 800;
        int imageHeight = 200;
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        
        for (int x = 0; x < imageWidth; x++) {
            double xCoord = -screenWidth / 2 + (double) x / imageWidth * screenWidth;
            double theta = Math.atan(xCoord / L);

            int xIndex = (int) ((xCoord + screenWidth / 2) / screenWidth * numPoints);
            xIndex = Math.max(0, Math.min(numPoints - 1, xIndex));
            double intensityAtX = intensity[xIndex];

            int intensityValue = (int) (255 * Math.min(1.0, intensityAtX / maxIntensity));
            Color color = new Color(intensityValue, intensityValue, intensityValue);
            
            for (int y = 0; y < imageHeight; y++) {
                image.setRGB(x, y, color.getRGB());
            }
        }
        
        JLabel label = new JLabel(new ImageIcon(image));
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(label);
        frame.pack();
        frame.setVisible(true);
    }
}