import java.awt.EventQueue;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import org.jfree.chart.ChartPanel;
import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Font;
import javax.swing.JTabbedPane;

public class GUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	private static JTable table;
	static ArrayList<JPanel> panels = new ArrayList<JPanel>();
	
	//Non-Preemptive Case
	public static void run() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	static protected void createPanel(String name, ArrayList<Process> processes, float avgTA, float avgWA) {
		JPanel newPanel = new JPanel();
		newPanel.setLayout(null);
		
        ChartPanel panel = new ChartPanel(null);
        panel.setBackground(Color.WHITE);
        panel.setBounds(10, 43, 720, 387);
        newPanel.add(panel);
        panel.setChart(new GanttChart().run(processes));
        
        JLabel lblNewLabel = new JLabel("CPU Scheduling Graph");
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        lblNewLabel.setForeground(Color.BLUE);
        lblNewLabel.setBounds(10, 10, 225, 29);
        newPanel.add(lblNewLabel);
        
        JLabel lblProcessesInformation = new JLabel("Processes Information");
        lblProcessesInformation.setForeground(Color.BLUE);
        lblProcessesInformation.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        lblProcessesInformation.setBounds(739, 10, 376, 29);
        newPanel.add(lblProcessesInformation);
        
        JLabel lblStatistics = new JLabel("<HTML><U>Statistics</U></HTML>");
        lblStatistics.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblStatistics.setForeground(Color.BLUE);
        lblStatistics.setFont(new Font("Times New Roman", Font.PLAIN, 24));
        lblStatistics.setBounds(15, 430, 376, 29);
        newPanel.add(lblStatistics);
        
        JLabel lblNewLabel_1 = new JLabel("Scheduling Name: " + name);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1.setForeground(Color.BLACK);
        lblNewLabel_1.setBounds(15, 450, 390, 36);
        newPanel.add(lblNewLabel_1);
        
        JLabel lblNewLabel_1_1 = new JLabel("Average Waiting Time: " + avgWA);
        lblNewLabel_1_1.setForeground(Color.BLACK);
        lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1_1.setBounds(15, 475, 360, 36);
        newPanel.add(lblNewLabel_1_1);
        
        JLabel lblNewLabel_1_1_1 = new JLabel("Average Turnaround Time: " + avgTA);
        lblNewLabel_1_1_1.setForeground(Color.BLACK);
        lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_1_1_1.setBounds(15, 500, 360, 36);
        newPanel.add(lblNewLabel_1_1_1);
        
        String[] columnNames = { "Process Name", "Arrival Time", "Burst Time", "Waiting Time", "Turnaround Time", "Priority Number"};
        String[][] data = new String[processes.size()][6];
        for (int i = 0; i<processes.size(); i++) {
        	Process process = processes.get(i);
        	for(int j=0; j<6; j++) {
        		switch (j) {
        		case 0:
        			data[i][j]=process.processName;
        			break;
        		case 1:
        			data[i][j]=Integer.toString(process.arrivalTime);
        			break;
        		case 2:
        			data[i][j]=Integer.toString(process.burstTime);
        			break;
        		case 3:
        			data[i][j]=Integer.toString(process.processWaitingTime);
        			break;
        		case 4:
        			data[i][j]=Integer.toString(process.processTurnaround);
        			break;
        		case 5:
        			data[i][j]=Integer.toString(process.priorityNumber);
        		}
        	}
        }
        table = new JTable(data, columnNames);
        
      
        
        table.setEnabled(false);
        table.setBorder(null);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setForeground(Color.BLACK);
        table.setBackground(Color.WHITE);
        table.setBounds(740, 43, 301, 387);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setEnabled(false);
        scrollPane.setBounds(740, 43, 315, 387);
        scrollPane.getViewport().setBackground(Color.WHITE);
        newPanel.add(scrollPane);
        
        panels.add(newPanel);
	}
	
	private void initialize() {
		frame = new JFrame("CPU Scheduling");
		frame.setAlwaysOnTop(true);
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 1079, 631);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
        frame.getContentPane().setLayout(null);
  
        JTabbedPane tp=new JTabbedPane(); 
        tp.setBackground(Color.LIGHT_GRAY);
        for(int i=0; i<panels.size(); i++) {
        	tp.add(Integer.toString(i+1), panels.get(i));
        }
	      
        tp.setBounds(0, 0, 1065, 594); 
        frame.add(tp);
        
        
	}
}
