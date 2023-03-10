import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
/**
*
* @author Hajar Refaat
*/
public class SJF {
	int numberOfProcesses;
	static int currentTime;
	static float avgTurnaround;
	static float avgWaitingTime;
	static PriorityQueue<Process> processes;
	        
	static ArrayList<Process> run(ArrayList<Process> allProcesses) throws InterruptedException {
		//Copy to external ArrayList
		ArrayList<Process> includedProcesses = new ArrayList<Process>(allProcesses);
		
		//Set All Priorities to Zero 
		includedProcesses = nullPriority(includedProcesses);
		
		//Initialize Priority Queue - Add processes from ArrayList to Priority Queue
		processes = new PriorityQueue<Process>(allProcesses.size(),new ArrivalComparator());
				
		System.out.println("Process Name\tArrival Time\tBurst Time\tWaiting Time\tTurn Around Time\t");
		
		int i=1;
		while(i<=allProcesses.size()) { 
			//Add processes that have arrived to priority queue
			for(Process process : includedProcesses) {
			    if(process.arrivalTime<=currentTime )
			    	processes.add(process);
			}
			//Remove processes that have arrived 
			includedProcesses.removeIf(process -> process.arrivalTime<=currentTime);
			
			if(!processes.isEmpty()){
				System.out.print(processes.peek().processName + "\t\t");
				int processNum = allProcesses.indexOf(processes.peek());
				
				processes.peek().addInterval(currentTime); //Start
				currentTime += processes.peek().burstTime;
				processes.poll().addInterval(currentTime); //End
				
				//Calculate Waiting & Turnaround Time for Process
				Utilities.calcTime(currentTime, processNum, allProcesses);    
				
				if(i%15==0)
					agingFunc();
				i++;
			 }else { //No Process in Queue - Increment Time
			    currentTime++;
			}         
		}
		avgWaitingTime = Utilities.calcAvgWaitingTime(allProcesses);
		avgTurnaround = Utilities.calcAvgTurnaround(allProcesses);
		System.out.println("Average Waiting Time = " + avgWaitingTime + "\nAverage Turnaround Time = " + avgTurnaround+"\n");
		
		return allProcesses;		      
	}
	
	static class ArrivalComparator implements Comparator<Process> {
		//Priority of process 1 >= Priority of process 2, and Burst Time of process 1> Burst Time of process 2 
	    public int compare(Process firstProcess, Process secondProcess){
    		if (firstProcess.burstTime < secondProcess.burstTime && firstProcess.priorityNumber >= secondProcess.priorityNumber)
    			return -1;
	    	else
	            return 1;
	    }
	}
	
	// Set all priority numbers to zero
	static ArrayList<Process> nullPriority(ArrayList<Process> incl) {
		for(Process p : incl)
			p.nullifyPriority();
		return incl;
	}
	
	// Aging – Priority Increases as Time Progresses 
	static void agingFunc() {
		for(Process process : processes) {
			process.priorityNumAging(1);
		}
	}
	
 	
}
		    
		
