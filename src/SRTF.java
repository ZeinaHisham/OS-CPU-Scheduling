import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class SRTF {
	int numberOfProcesses;
	static int currentTime;
	static float avgTurnaround;
	static float avgWaitingTime;
	static PriorityQueue<Process> processes;
	        
	//Add processes that have arrived to ready queue and remove from array list
	static ArrayList<Process> checkQueue(ArrayList<Process> includedProcesses) { 
		for(Process process : includedProcesses) {
			if(process.arrivalTime<=currentTime) {
					processes.add(process.clone());
			}
		}
		includedProcesses.removeIf(process -> process.arrivalTime<=currentTime);
		return includedProcesses;
	}
	
	static ArrayList<Process> run(ArrayList<Process> allProcesses, int contextSwitch) throws InterruptedException {
		//Copy to external ArrayList to Fill The Ready Queue
		ArrayList<Process> includedProcesses = Utilities.cloneProcesses(allProcesses); 
		
		//Set Priorities to Zero
		includedProcesses = nullPriority(includedProcesses);
		
		//Initialize Priority Queue - Add processes from ArrayList to Priority Queue
		processes = new PriorityQueue<Process>(allProcesses.size(),new ArrivalComparator());
				
		System.out.println("Process Name\tArrival Time\tBurst Time\tWaiting Time\tTurn Around Time\t");
		
		int i=1;
		while(!includedProcesses.isEmpty() || !processes.isEmpty()) { 
			//Check New Arrivals
			includedProcesses = checkQueue(includedProcesses);
			
			//Remove processes that have arrived
			includedProcesses.removeIf(process -> process.arrivalTime<=currentTime);
			
			if(!processes.isEmpty()){
				Process process = processes.peek();
				int processNum = Utilities.getIndex(process.processName, allProcesses); //Gets Index of Process in Original Array
				
				allProcesses.get(processNum).addInterval(currentTime); //Start
				
				//While the process has the shortest-remaining time: decrease its burst time by 1 and increment time
				while(processes.peek().processName.equals(process.processName) && process.burstTime>0) { 
					currentTime++;
					process.decBurstTime(1);
					
					//Check new arrivals 
					includedProcesses = checkQueue(includedProcesses); 
				}
				
				allProcesses.get(processNum).addInterval(currentTime); //End
				
				//Process Has Finished
				if(process.burstTime==0) {
					System.out.print(process.processName + "\t\t");
					allProcesses = Utilities.calcTime(process.processName, allProcesses);
					processes.remove(process);
				}
				
				//Context Switch - Load Process State
				Thread.sleep(contextSwitch*1000);
				currentTime += contextSwitch;
				
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
		//Priority of Process 1 >= Priority of Process 2, and Burst Time of Process 1> Burst Time of Process 2 
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
		    
		
