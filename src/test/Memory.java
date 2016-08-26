package test;

public class Memory {
	public static void main(String[] args) {
		Runtime runTime=Runtime.getRuntime();
		long maxTime=runTime.maxMemory();
		long freeTime=runTime.freeMemory();
		long t=runTime.totalMemory();
	}
}
