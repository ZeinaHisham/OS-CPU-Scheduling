import java.awt.Color;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Process {
	int quantum;
	int burstTime;
	int agatFactor;
	int arrivalTime;
	String processName;
	int priorityNumber;
	Color processColor;
	int processTurnaround;
	int processWaitingTime;
	ArrayList<Integer> intervals = new ArrayList<Integer>();
	
	void setAgatFactor(float v2,float v1) {
		agatFactor = (int) ((10-this.priorityNumber) + Math.ceil(this.arrivalTime/v1) + Math.ceil(this.burstTime/v2));
	}
	
	void addInterval(int i) {
		intervals.add(i);
	}
	
	protected Process clone(){
	    Process p = new Process(this.processName,"",this.arrivalTime,this.burstTime, this.priorityNumber, this.quantum);
	    p.processColor = this.processColor;
	    p.agatFactor = this.agatFactor;
	    p.processWaitingTime = this.processWaitingTime;
	    p.processTurnaround = this.processTurnaround;
	    for(Integer i : this.intervals)
	    	p.intervals.add(i);
	    return p;
	 }

	Process(String name, String color, int arrivalTime, int burstTime, int priorityNum, int quantum){
		this.processName = name;
		this.arrivalTime = arrivalTime;
		this.burstTime = burstTime;
		this.priorityNumber = priorityNum;
		this.quantum = quantum;
		try {
		    Field field = Class.forName("java.awt.Color").getField(color.toLowerCase());
		    processColor = (Color)field.get(null);
		} catch (Exception e) {
		    color = null; 
		}
	}
	
	void setWaitingTime(int waitingTime) {
		this.processWaitingTime = waitingTime;
	}
	
	void setTurnaround(int turnaround) {
		this.processTurnaround = turnaround;
	}
	
	void priorityNumAging(int factor) {
		switch (factor) {
		case -1: //Aging in Priority Scheduling. Decrement by one as the smallest number (highest priority) is picked
			if(priorityNumber>0)
				this.priorityNumber -= 1;
			break;
		case 1: //Aging in SJF & SRTF. Increment by one as the highest number is picked
			this.priorityNumber += 1;
		}
	}
	
	void setName(String name) {
		this.processName=name;
	}
	
	void setColor(Color color) {
		this.processColor=color;
	}
	
	int getRoundQuantum() {
		return (int) Math.round(quantum*0.4);
	}

	void decBurstTime(int burst) {
		this.burstTime -= burst;
		if(this.burstTime<=0) {
			this.burstTime=0;
		}
	}
	
	void incQuantum(int quantum) {
		this.quantum += quantum;
	}
	
	void nullifyPriority() {
		this.priorityNumber = 0;
	}
	
}
