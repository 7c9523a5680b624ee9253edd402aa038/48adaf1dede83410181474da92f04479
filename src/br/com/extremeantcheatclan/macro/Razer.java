package br.com.extremeantcheatclan.macro;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.leitormacro.LeitorImagemMacroRazer;
import br.com.extremeantcheatclan.util.Util;
import br.com.extremeantcheatclan.view.LoginHelper;

public class Razer {

	//Media de verificacao 22 segundos
	
	private LeitorImagemMacroRazer leitorImagemMacroRazer;
	public static List<byte[]> screenShotsList = new ArrayList<>();
	private static boolean shutdown = true;
	public static boolean waitingNotCheckRazer = false;
	
	public Razer(int altura, int largura) {
		Date inicoVerificacao = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		//JOptionPane.showMessageDialog(null, "Entro aqui");
		
		//Servico para escrever os screenShots
		this.threadEscritaScreenShot();

		boolean virificaSeEstaNaAbaMacro = false;
		for (int i = 0; i < 10; i++) {
			leitorImagemMacroRazer = new LeitorImagemMacroRazer(altura, largura);
			virificaSeEstaNaAbaMacro = leitorImagemMacroRazer.virificaSeEstaNaAbaMacro();
			
			if(virificaSeEstaNaAbaMacro){
				break;
			}
			
			//JOptionPane.showMessageDialog(null, "Nao acho");
			
			String processosAbertos = Util.executeComandoCMDReturn(Util.TASKLIST_RUNNING);
			Util.timeOut(1000);
			if(processosAbertos != null && !processosAbertos.isEmpty()){
				if(!processosAbertos.contains(CheckMacros.subProcessMouse)){
					//JOptionPane.showMessageDialog(null, "Como o processo foi fechado vamo dar uma parada");
					break;
				}
			}	
		}
		
		if(virificaSeEstaNaAbaMacro){
			boolean hasWindowConfgRazerIsOpen = leitorImagemMacroRazer.hasWindowConfgRazerIsOpen();
			boolean hasMacro = leitorImagemMacroRazer.hasMacro();
			
			if(hasWindowConfgRazerIsOpen || hasMacro){
				if(hasMacro){
					//Banir usuario
					MacroHelper.sendDadosToServer(ListaMacro.macrosExistentes.get(0), (byte[]) leitorImagemMacroRazer.screenShotCompleto[1]);
					MacroHelper.deteleProvas();
					
					Util.executeComandoCMD(String.format(LoginHelper.STOP_SERVICE, "TaskExtreme"));
					Util.timeOut(1000);
					Util.executeComandoCMD(String.format(LoginHelper.COMANDO_DELETE_SERVICO, "TaskExtreme"));
					Util.timeOut(1000);
					
					System.exit(0);
				}
			}else{
				Object[] provas = leitorImagemMacroRazer.screenShotCompleto;
				String descricao = "Verificacao iniciada as "+
						dateFormat.format(inicoVerificacao)+" finalizada as "+dateFormat.format(new Date());
				
				new XitersDAOImpl().saveSuspeito("macro-"+ListaMacro.macrosExistentes.get(0), provas, descricao);
			}
		}
		
		this.confirmCheckRazer();
		MacroHelper.deteleProvas();
		shutdown = false;
		
	}
	
	public Razer(JFrame razerViewAtual, JFrame sucess, JFrame macroDetect) {
		String pathRzSynapse = MacroHelper.getPathRzSynapse();
		Date inicoVerificacao = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		//Servico para escrever os screenShots
		this.threadEscritaScreenShot();
		
		razerViewAtual.setVisible(false);
		MacroHelper.openRazerSynapse(pathRzSynapse);
		
		razerViewAtual.setLocation(340, 570);
		razerViewAtual.setVisible(true);
		Util.timeOut(1000);
		
		byte[] capturePrintTela = Util.capturePrintTela();
		leitorImagemMacroRazer = new LeitorImagemMacroRazer(Util.byteToInputStream(capturePrintTela));
		
		razerViewAtual.setVisible(false);
		boolean hasWindowConfgRazerIsOpen = leitorImagemMacroRazer.hasWindowConfgRazerIsOpen();
		boolean hasMacro = leitorImagemMacroRazer.hasMacro();
		
		//hasWindowConfgRazerIsOpen = true;
		if(hasWindowConfgRazerIsOpen || hasMacro){
			parseVeiewByMacro(hasMacro, sucess, macroDetect);
		}else{
			Object[] provas = leitorImagemMacroRazer.screenShotCompleto;
			String descricao = "Verificacao iniciada as "+
					dateFormat.format(inicoVerificacao)+" finalizada as "+dateFormat.format(new Date());
			
			new XitersDAOImpl().saveSuspeito("macro-"+ListaMacro.macrosExistentes.get(0), provas, descricao);
		}
		
		if(!hasWindowConfgRazerIsOpen && !hasMacro){
			LayoutGenericXK.mesageDialogIcoInfo("Em andamento", "Nao foi possivel finalizar a verficacao\n"
					+ "porem os dados forao enviado para o servidor, IA do sistema ira apurar o restante\n"
					+ "da verificacao. Enquanto nao sai o resultado voce podera continuar a ultilizar o Extreme Ant Cheat", 
					LayoutGenericXK.PATH_ICO_XK_45_64);	
		}

		this.confirmCheckRazer();
		MacroHelper.deteleProvas();
		shutdown = false;
	}
	
	public void parseVeiewByMacro(boolean hasMacro, JFrame sucess, JFrame macroDetect){
		if(hasMacro){
			macroDetect.setVisible(true);
			//Banir usuario
			MacroHelper.sendDadosToServer(ListaMacro.macrosExistentes.get(0), (byte[]) leitorImagemMacroRazer.screenShotCompleto[1]);
			macroDetect.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			MacroHelper.deteleProvas();
			
			Util.executeComandoCMD(String.format(LoginHelper.STOP_SERVICE, "TaskExtreme"));
			Util.timeOut(1000);
			Util.executeComandoCMD(String.format(LoginHelper.COMANDO_DELETE_SERVICO, "TaskExtreme"));
			Util.timeOut(1000);
			
			System.exit(0);
		}else{
			sucess.setVisible(true);
			Util.timeOut(2000);
			Util.executeComandoCMD(String.format(Util.TASK_KILL_BY_TITLE, "Razer Configurator"));
			sucess.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			Util.timeOut(1000);
			this.confirmCheckRazer();
			sucess.setVisible(false);
		}
	}

	/** Esta thread e iniciada para caso o usuario fecha aplicacao,
	 * eas prints sejao guardadas e enviadas para o banco via o extreme servico. **/
	public void threadEscritaScreenShot(){
		Thread screenShot = new Thread(new Runnable() {
			@Override
			public void run() {
				while(shutdown){
					Util.timeOut(500);
					if(screenShotsList != null && !screenShotsList.isEmpty()){
						for (int i = 0; i < screenShotsList.size(); i++) {
							try{
								InputStream screenShotStream = Util.byteToInputStream(screenShotsList.get(i)); 
								String src = LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT+"/prova&"+System.nanoTime()+".png";
								ImageIO.write(ImageIO.read(screenShotStream), "PNG", new File(src));
								screenShotsList.remove(i);
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
		screenShot.setName("ScreenShot");
		screenShot.start();
	}
	
	private void confirmCheckRazer(){
		Razer.waitingNotCheckRazer = true;
	}
	
}