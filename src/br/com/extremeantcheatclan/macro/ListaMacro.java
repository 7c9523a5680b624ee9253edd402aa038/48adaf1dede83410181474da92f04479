package br.com.extremeantcheatclan.macro;

import java.util.ArrayList;
import java.util.List;

public class ListaMacro {
	
	public static List<String> macrosExistentes = load();

	public static List<String> load(){
		List<String> macrosExistentes = new ArrayList<>();
		macrosExistentes.add("RzSynapse.exe");
		
		return macrosExistentes;
	}
	
}
