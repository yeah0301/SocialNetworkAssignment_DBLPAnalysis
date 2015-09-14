import javax.swing.JFrame;  

import java.awt.*;  
import java.util.HashMap;

import org.jfree.chart.*;  
import org.jfree.chart.axis.CategoryAxis;  
import org.jfree.chart.axis.NumberAxis;  
import org.jfree.chart.plot.*;  
import org.jfree.chart.renderer.category.BarRenderer;  
import org.jfree.data.category.CategoryDataset;  
import org.jfree.data.category.DefaultCategoryDataset;  
  
public class DegreeDistribution extends JFrame{  
      
    public DegreeDistribution(HashMap<Integer, Integer> rowdata) {  
        
        CategoryDataset dataset = setDataset(rowdata);  
        JFreeChart chart = createChart(dataset);  

        ChartPanel chartPanel = new ChartPanel(chart);  
        chartPanel.setPreferredSize(new Dimension(600, 500));  
        getContentPane().add(chartPanel);  
        pack();  
        setVisible(true);  
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    }  
      
    /**
     * Y[degree]=number of nodes;
     * @param degrees
     * @return
     */
    private CategoryDataset setDataset(HashMap<Integer, Integer> rowdata) {  
    	String series = "curve"; 

        DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
        
        for(int degree:rowdata.keySet())
        	dataset.addValue(rowdata.get(degree), series, String.valueOf(degree));  
        	
        		
        return dataset;  
    }  
  
    private JFreeChart createChart(final CategoryDataset dataset) {  
        JFreeChart chart = ChartFactory.createBarChart(  
                "Degree Distribution", // 圖表標題   
                "degree", // X 軸標題  
                "Number of nodes within k items", // Y 軸標題  
                dataset, // dataset 物件變數  
                PlotOrientation.VERTICAL, // Bar 方向   
                true,  // 加入圖例說明  
                true,  // 顯示提示資料  
                false);// URLs?  
        
        CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();  
        CategoryAxis domainAxis = categoryplot.getDomainAxis();  
        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();  
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());  
        BarRenderer barrenderer = (BarRenderer) categoryplot.getRenderer();  
        barrenderer.setBaseItemLabelFont(new Font("標楷體", Font.PLAIN, 12));  
        barrenderer.setSeriesItemLabelFont(1, new Font("標楷體", Font.PLAIN, 12));  
  
        /*------設置X軸座標上的文字-----------*/  
        domainAxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 15));  
  
        /*------設置X軸的標題文字------------*/  
        domainAxis.setLabelFont(new Font("標楷體", Font.PLAIN, 15));  
  
        /*------設置Y軸座標上的文字-----------*/  
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 15));  
  
        /*------設置Y軸的標題文字------------*/  
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN, 15));  
        chart.getLegend().setItemFont(new Font("標楷體", Font.PLAIN, 15));  
        chart.getTitle().setFont(new Font("黑体", Font.BOLD, 14));  
        return chart;  
    }  
      
    public static void main(String args[]) {  
        //new DegreeDistribution();  
    }  
}  