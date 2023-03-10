import java.util.ArrayList;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

public class GanttChart extends JFrame {
	private static final long serialVersionUID = 1L;

	   public JFreeChart run(ArrayList<Process> processes) {
	      // Create dataset
	      GanttCategoryDataset dataset = getCategoryDataset(processes);
	    
	      // Create chart
	    final JFreeChart chart = ChartFactory.createGanttChart(
	            "", // Chart title
	            "Processes", // X-Axis Label
	            "Time", // Y-Axis Label
	            dataset,
	            true,
	            false,
	            false);

	      CategoryPlot plot = chart.getCategoryPlot();
	      CategoryItemRenderer renderer = plot.getRenderer();
	      for(int i=0; i<processes.size(); i++) {
	    	  renderer.setSeriesPaint(i, processes.get(i).processColor);
	      }
	      
	      NumberAxis xAxis = new NumberAxis();
	      xAxis.setTickUnit(new NumberTickUnit(1));
	      plot.setRangeAxis(xAxis);
	      
	      ChartPanel panel = new ChartPanel(chart);
	      setContentPane(panel);
		   
	      return chart;
	   }
	   
	   private GanttCategoryDataset getCategoryDataset(ArrayList<Process> processes) {
		  TaskSeriesCollection dataset = new TaskSeriesCollection();
		  
		  for(int i=0; i<processes.size(); i++) { //Original Processes
			  TaskSeries taskseries = new TaskSeries(processes.get(i).processName);
			  int startTime = processes.get(i).intervals.get(0);
			  int endTime = processes.get(i).intervals.get(processes.get(i).intervals.size()-1);
			  Task series = new Task(processes.get(i).processName, new SimpleTimePeriod(startTime,endTime));
			  
			  ArrayList<Integer> interval = processes.get(i).intervals;
			  for(int j=0; j<interval.size();j+=2) { 
					  startTime = interval.get(j);
					  endTime = interval.get(j+1);
					  series.addSubtask(new Task(Integer.toString(j), new SimpleTimePeriod(startTime, endTime)));
			  }
			  
			  taskseries.add(series);
		      dataset.add(taskseries);
		   } 
	      return dataset;
	   }
}
