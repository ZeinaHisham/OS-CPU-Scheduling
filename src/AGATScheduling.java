import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class AGATScheduling {
	static float V2;
	static float V1 = 1;
	static int currentTime;
	static float avgTurnaround;
	static float avgWaitingTime;
	static boolean processFinished = false;
	static PriorityQueue<Process> processes;
	static Queue<Process> queue = new LinkedList<>();
	static Queue<Process> deadlist = new LinkedList<>();

	//Calculate V1
	static void setV1(ArrayList<Process> processes) {
		float lastArrival = 0;
		
		for(int i=0; i<processes.size(); i++) {
			if(processes.get(i).arrivalTime>=lastArrival)
				lastArrival = processes.get(i).arrivalTime;
		}
		
		if(lastArrival>10)
			V1 = lastArrival /10;
	}
	
	//Calculate V2
	static float setV2() {
			float maxBurst = 0;
			
			for(Process p : queue) {
				if(p.burstTime>=maxBurst)
					maxBurst = p.burstTime;
			}
			if(maxBurst>10) {
				V2 = maxBurst /10;
			}
			else {
				V2 = 1;
			}
			return maxBurst;
	}
	
	static void checkConditions(Process process, int timeSpent) {
		if(process.quantum-timeSpent==0 && process.burstTime !=0) { // Process used all its quantum time and still has job to do
			process.incQuantum(2);
			queue.add(process);
			processFinished = true;
		}
		else if(process.quantum-timeSpent!=0 && process.burstTime != 0) { // Process didn't use all its quantum time
			process.incQuantum((process.quantum-timeSpent));
			queue.add(process);
		}
		else if(process.burstTime==0) { //Process has finished
			processFinished = true;
			deadlist.add(process);
		}
	}

	static ArrayList<Process> checkQueue(ArrayList<Process> includedProcesses) { //Add processes that have arrived to ready queue and remove from array list
		for(Process process : includedProcesses) {
			if(process.arrivalTime<=currentTime) {
					queue.add(process);
			}
		}
		includedProcesses.removeIf(process -> process.arrivalTime<=currentTime);
		return includedProcesses;
	}

	
	static ArrayList<Process> run(ArrayList<Process> allProcesses) throws InterruptedException {
		ArrayList<Process> includedProcesses = Utilities.cloneProcesses(allProcesses); //To fill the ready queue
		processes = new PriorityQueue<Process>(allProcesses.size(), new AGATComparator()); //Compare AGATs
		
		setV1(allProcesses); //Set Value of V1
		
		System.out.println("Process Name\tArrival Time\tBurst Time\tWaiting Time\tTurn Around Time\t");
		
		while(!includedProcesses.isEmpty() || !queue.isEmpty()) { //Until all elements have arrived
			includedProcesses = checkQueue(includedProcesses); //Check new arrivals
			if(!queue.isEmpty()) {
				refreshPriorityQueue();
				
				Process process = null;
				if(processFinished) { //Condition 1,3 : Running process used all its quantum time/finished, pick next from queue
					processFinished = false;
					process = queue.peek().clone();
				}
				else { //Condition 2 : Running process didn't use all its quantum time, interrupted by better AGAT
					process = processes.peek().clone();
				}
						
				//Process will execute 40% of its quantum first
				int startTime = currentTime;
				allProcesses = addIntervals(allProcesses, startTime, process.processName); // Start
				currentTime += process.getRoundQuantum(); //Increase current time by rounded quantum
				process.decBurstTime(process.getRoundQuantum()); //Decrease burst time by rounded quantum
				
				int timeSpent = 0;
				includedProcesses = checkQueue(includedProcesses);
				float maxBurst = setV2();
				calcAGAT();
				calcAGAT(allProcesses); //Initial value - Only actually matters the first time it executes
				if(!queue.isEmpty()) 
					refreshPriorityQueue();
				
				//To print execution steps later
				addSteps(startTime, process, allProcesses,maxBurst);
				
				//If a better element with better AGAT hasn't arrived -AND- Process burst time is not zero -AND- process hasn't exceeded it's quantum time
				while(processes.peek().processName.equals(process.processName) && process.burstTime!= 0 && currentTime-startTime < process.quantum) {
					currentTime++;
					process.decBurstTime(1);
					includedProcesses = checkQueue(includedProcesses);
					setV2();
					calcAGAT();
					if(!queue.isEmpty()) 
						refreshPriorityQueue();
				}
				allProcesses = addIntervals(allProcesses, currentTime, process.processName); //End
				
				//Print Process Details if Finished
				if(process.burstTime==0) {
					System.out.print(process.processName + "\t\t");
					allProcesses=Utilities.calcTime(process.processName, allProcesses);
					
					//Set Quantum to Zero
					int index = Utilities.getIndex(process.processName, allProcesses);
					allProcesses.get(index).quantum=0;
				}
				
				timeSpent = currentTime - startTime;
				
				//Process in priority queue has the old attributes of process before decreasing burst time and changing quantum
				//Replace with new values
				process = setCurrentAGAT(process);
				
				//Remove recently executed process from queue and check to see if it will be added to the back of the queue or added to deadlist
				String removeName = process.processName;
				queue.removeIf(p -> p.processName.equals(removeName));
				checkConditions(process, timeSpent);
				
			}
			else { //No Process in Queue - Increment Time
				currentTime++;
			}
			
		}

		avgWaitingTime = Utilities.calcAvgWaitingTime(allProcesses);
		avgTurnaround = Utilities.calcAvgTurnaround(allProcesses);
		System.out.println("Average Waiting Time = " + avgWaitingTime + "\nAverage Turnaround Time = " + avgTurnaround+"\n");
		
		System.out.println("\t\t •AGAT Execution Steps•");
		System.out.println(executionSteps);
		
		return allProcesses;
	}
	
	static Process setCurrentAGAT(Process process) {
		for(Process p : queue) {
			if(p.processName.equals(process.processName))
				process.agatFactor = p.agatFactor;
		}
		return process;
	}
		
	static class AGATComparator implements Comparator<Process> {
		    public int compare(Process firstProcess, Process secondProcess)
		    {	
	    		if (firstProcess.agatFactor < secondProcess.agatFactor)
	    			return -1;
		    	else
		            return 1;
		    }
		}
	
	//Toggles AGAT Calculation
	static void calcAGAT() {
		for(Process p: queue)
			p.setAgatFactor(V2, V1);
	}
	
	//Gives initial values when printing steps
	static void calcAGAT(ArrayList<Process> allProcesses) {
		for(Process p: allProcesses)
			p.setAgatFactor(V2, V1);
	}
	
	
	//Refresh Priority Queue
	static void refreshPriorityQueue() {
		processes.clear();
		for(Process p : queue) {
			processes.add(p.clone());
		}
	}
	
	static ArrayList<Process> addIntervals(ArrayList<Process> allProcesses, int time, String name) {
		for(Process p : allProcesses) {
			if(p.processName.equals(name))
				p.addInterval(time);
		}
		return allProcesses;
	}
	
	private static String executionSteps = "";
	
	static private void addSteps(int time, Process process, ArrayList<Process> allProcesses, float maxBurst) {
		executionSteps += "Current Time: " + time + "\n";
		
		executionSteps += "Quantum (";
		for(int i=0; i<allProcesses.size(); i++) {
			boolean found = false;
				for(Process p : queue) {
					if(p.processName.equals(allProcesses.get(i).processName)) {
						executionSteps += p.quantum + ", ";
						found = true;
					}
				}
			if(!found)
				executionSteps += (allProcesses.get(i).quantum!=0 ? allProcesses.get(i).quantum + ", " : "-, ");
		}
		executionSteps = executionSteps.substring(0, executionSteps.length()-2);
		executionSteps += ")\n";
		
		executionSteps += "Currently Executing: " + process.processName + ", Quantum = " + process.quantum 
				+ " --> round(40%) = " + process.getRoundQuantum() + "\n";
		
		executionSteps += (V2!=1? "V2 = (" + maxBurst +"/10) = " + V2 : "V2 = 1, Max Burst: " + maxBurst + " <= 10") + "\n";
	
		executionSteps += "AGAT (";
		for(int i=0; i<allProcesses.size(); i++) {
			boolean found = false;
				for(Process p : queue) {
					if(p.processName.equals(allProcesses.get(i).processName)) {
						executionSteps += p.agatFactor + ", ";
						found = true;
					}
				}
			if(!found)
				executionSteps += (allProcesses.get(i).quantum!=0? allProcesses.get(i).agatFactor + ", " : "-, " );
		}
		executionSteps = executionSteps.substring(0, executionSteps.length()-2);
		executionSteps += ")\n";
		
		executionSteps += "\t\t\t ----\n" ;
		
	}
}

