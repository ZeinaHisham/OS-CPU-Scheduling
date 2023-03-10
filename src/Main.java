import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		ArrayList<Process> allProcesses = new ArrayList<Process>();
		
		try (Scanner scan = new Scanner(System.in)) {
			System.out.println("Enter the number of processes: ");
			int numProcess = Integer.parseInt(scan.next());
			
			System.out.println("Enter Context Switch Time: ");
			int contextSwitch = Integer.parseInt(scan.next());
			
			System.out.println("Enter in the following order: Name, Color, Arrival Time, Burst Time, Priority Number, Quantum");
			for(int i=1; i<=numProcess;i++) {
				String name = scan.next();
				String color = scan.next();
				int arrivalTime = Integer.parseInt(scan.next());
				int burstTime = Integer.parseInt(scan.next());
				int priorityNumber = Integer.parseInt(scan.next());
				int quantum = Integer.parseInt(scan.next());
				
				allProcesses.add(new Process(name, color, arrivalTime, burstTime, priorityNumber, quantum));
			}
			
			for(int i=0; i<4; i++) {
				ArrayList<Process> processes = Utilities.cloneProcesses(allProcesses);
				switch (i) { 
				case 0:
					System.out.println("------------------------Priority Scheduling------------------------");
					ArrayList<Process> prioritySched = PriorityScheduling.run(processes, contextSwitch);
					GUI.createPanel("Priority Scheduling", prioritySched, PriorityScheduling.avgTurnaround, PriorityScheduling.avgWaitingTime);
					break;
				case 1:
					System.out.println("-------------------Shortest-Job First Scheduling-------------------");
					ArrayList<Process> sjf = SJF.run(processes);
					GUI.createPanel("Shortest-Job First Scheduling", sjf, SJF.avgTurnaround, SJF.avgWaitingTime);
					break;
				case 2:
					System.out.println("--------------Shortest-Remaining Time First Scheduling--------------");
					ArrayList<Process> srtf = SRTF.run(processes, contextSwitch);
					GUI.createPanel("Shortest-Remaining Time First Scheduling", srtf, SRTF.avgTurnaround, SRTF.avgWaitingTime);
					break;
				case 3:
					System.out.println("--------------------------AGAT Scheduling---------------------------");
					ArrayList<Process> agatSched = AGATScheduling.run(processes);
					GUI.createPanel("AGAT Scheduling",agatSched, AGATScheduling.avgTurnaround, AGATScheduling.avgWaitingTime);
					break;
				}
			}
			GUI.run();
		}
	}	
}
