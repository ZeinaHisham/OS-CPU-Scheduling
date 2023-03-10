import java.util.ArrayList;

public class Utilities {
	//Deep Copying
	static ArrayList<Process> cloneProcesses(ArrayList<Process> allProcesses){
		ArrayList<Process> process = new ArrayList<Process>();
		for(Process p : allProcesses) {
		    process.add(p.clone());
		}
		return process;
	}
	
	//Helper Function to Get Index by Name
	static int getIndex(String name, ArrayList<Process> allProcesses) {
        for (int i = 0; i < allProcesses.size(); i++) {
            if (allProcesses.get(i).processName.equals(name)) {
                return i;
            }
        }
        return -1;
	 }
	
	// Turnaround Time = Completion Time - Arrival Time
	// Waiting Time = Turnaround Time - Burst Time
	static void calcTime(int currentTime, int processNum, ArrayList<Process> allProcesses) {
		Process process = allProcesses.get(processNum);
		process.setTurnaround(currentTime-process.arrivalTime);
		process.setWaitingTime(process.processTurnaround-process.burstTime);
		System.out.println(process.arrivalTime+"\t\t"+process.burstTime+"\t\t"+process.processWaitingTime+"\t\t"+ process.processTurnaround);
	}
	
	static ArrayList<Process> calcTime(String name, ArrayList<Process> allProcesses) {
		int index = getIndex(name, allProcesses);
		Process process = allProcesses.get(index);
		
		int completionTime = process.intervals.get(process.intervals.size()-1);
		
		int turnaround = completionTime-process.arrivalTime;
		int waitingTime = turnaround - process.burstTime;
		
		allProcesses.get(index).setTurnaround(turnaround);
		allProcesses.get(index).setWaitingTime(waitingTime);
		System.out.println(process.arrivalTime+"\t\t"+process.burstTime+"\t\t"+process.processWaitingTime+"\t\t"+ process.processTurnaround);
		
		return allProcesses;
	}
	
	// Calculate Average Waiting Time
	static float calcAvgWaitingTime(ArrayList<Process> processes) {
		float sum = 0;
		for(Process process: processes) {
			sum += process.processWaitingTime;
		}
		return (sum/(float)processes.size());
	}
		
	// Calculate Average Turnaround Time 
	static float calcAvgTurnaround(ArrayList<Process> processes) {
		float sum = 0;
		for(Process process: processes) {
			sum += process.processTurnaround;
		}
		return (sum/(float)processes.size());
	}
}

