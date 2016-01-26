package br.com.extremeantcheatclan.macro;

import br.com.extremeantcheatclan.leitormacro.LeitorImagemMacroRazer;
import br.com.extremeantcheatclan.util.Util;
import br.com.extremeantcheatclan.view.RazerView;

public class CheckMacros {

	public static String typeProcessMouse = null;
	public static String subProcessMouse = null;
	
	public CheckMacros(String macro) {
		switch (macro) {
		case "RzSynapse.exe":
			Util.timeOut(2000);
			//Inciar Tela de aviso e starta 
			//a verificacao
			new RazerView();
			
			//Adiciona o tipo e macro e um segundo processo
			//para reverficar o servico do mouse
			typeProcessMouse = macro;
			subProcessMouse = "Razer Configurator";
			
			//Start thread para verificar se o razer foi aberto novamente.
			this.startThreadCheckProcessRazerAgain();
			break;
		case "X-Mouse.exe":	
			//Impl
			break;
		case "Multilazer.exe":	
			//Impl
			break;	
		case "seilaoque.exe":	
			//Impl
			break;	
		default:
			break;
		}
	}

	private void startThreadCheckProcessRazerAgain(){
		Thread checkRazerAgainThread = new Thread(new Runnable() {
			@Override
			public void run() {
				//Enquanto o mouse da razer
				//nao e checkado ele aguarda.
				waitingNotCheckRazer();
				
				int altura = Integer.valueOf(LeitorImagemMacroRazer.larguraAlturaToWindowRazer.split("&")[1]);
				int largura = Integer.valueOf(LeitorImagemMacroRazer.larguraAlturaToWindowRazer.split("&")[0]);
				
				while(true){
					String processosAbertos = Util.executeComandoCMDReturn(Util.TASKLIST_RUNNING);

					if(processosAbertos != null && !processosAbertos.isEmpty()){
						if(processosAbertos.contains(typeProcessMouse) && 
								processosAbertos.contains(subProcessMouse)){
							
							new Razer(altura, largura);
							
							//Enquanto o mouse da razer
							//nao e checkado ele aguarda.
							waitingNotCheckRazer();
						}
					}
				}
			}
		});
		checkRazerAgainThread.setName("startThreadCheckProcessRazerAgain - " + typeProcessMouse);
		checkRazerAgainThread.start();
	}
	
	private void waitingNotCheckRazer(){
		boolean isNotCheckadoRazer = true;
		while(isNotCheckadoRazer){
			if(Razer.waitingNotCheckRazer){
				isNotCheckadoRazer = false;
			}
			Util.timeOut(1000);
		}
	}	
	
}