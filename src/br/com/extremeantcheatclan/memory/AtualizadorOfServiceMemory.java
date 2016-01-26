package br.com.extremeantcheatclan.memory;

import java.util.HashMap;
import java.util.Map;

public class AtualizadorOfServiceMemory {

	public static Boolean SHUTDOWN = Boolean.TRUE;
	
	private static Map<Integer, String> valueProgress =  new HashMap<>();
	
	public void updateProgressBar(int value, String desc){
		valueProgress.put(value, desc);
	}
	
	public Map<Integer, String> getProgress(){
		return valueProgress;
	}
			
	public void cleanValueProgress(){
		valueProgress.clear();
	}
			
	public static AtualizadorOfServiceMemory getInstance(){
		return new AtualizadorOfServiceMemory();
	}
}
