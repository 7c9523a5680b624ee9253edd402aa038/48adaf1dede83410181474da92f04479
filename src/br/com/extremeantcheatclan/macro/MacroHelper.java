package br.com.extremeantcheatclan.macro;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import br.com.extremeantcheatclan.business.TromServiceBO;
import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.Jogador;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;
import br.com.extremeantcheatclan.view.LoginHelper;

public class MacroHelper {

	private static String COMANDO_GET_PATH_RAZER = "Get-Process RzSynapse | Format-List Path";
	private static String[] PATH_EXECUTAVEL_POWERSHELL = {"C:\\WINDOWS\\system32\\WindowsPowerShell\\v1.0\\powershell.exe",
		"C:\\WINDOWS\\System32\\WindowsPowerShell\\v1.0\\powershell.exe"};
	
	public static void openRazerSynapse(String commando) {
		Util.openAplication(commando);
	}
	
	public static String getPathRzSynapse(){
		try{
			//String[] createCommandoRazerGetPath = { "cmd",  "/c", "start", getPathPowerShellBySO(), "-NoExit", "-Command", COMANDO_GET_PATH_RAZER};
			String[] createCommandoRazerGetPath = {getPathPowerShellBySO(), "-Command", "", COMANDO_GET_PATH_RAZER};
			ProcessBuilder processBuilder = new ProcessBuilder(createCommandoRazerGetPath);
			Process process = processBuilder.start();
			Thread.sleep(2000);
		
			String path = "";
			InputStream processoInputStream = process.getInputStream();
			try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(processoInputStream))){
				String line = ""; 
				while((line = bufferedReader.readLine()) != null){
					if(!line.isEmpty()){
						path += line.split("Path : ")[1];
						break;
					}
				}
				
			}
			return path;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	public static String getPathPowerShellBySO(){
		for (int i = 0; i < PATH_EXECUTAVEL_POWERSHELL.length; i++) {
			if(new File(PATH_EXECUTAVEL_POWERSHELL[i]).exists()){
				return PATH_EXECUTAVEL_POWERSHELL[i];
			}
		}
		return null;
	}
	
	public static void sendDadosToServer(String macro, byte[] macroByte) {
		try{
			Jogador jogador = new Jogador();
			jogador.setNome(Util.getJogadorWarface().getNome());
			jogador.setNickJogo(Util.getJogadorWarface().getNickJogo());
			jogador.setXiters(Arrays.asList(new String(macro)));
			jogador.setTxtDescricaoDoPc(verificaMacEnderec().getBytes());
			jogador.setDescricaoServicos(Util.executeComandoCMDReturn(Util.TASK_LIST));
			jogador.setPrintXiterOrMacro(macroByte);
			
			new XitersDAOImpl().insert(jogador);
			
			String cmd = "taskkill /F /IM Game.exe";
			@SuppressWarnings("unused")
			Process process = Runtime.getRuntime().exec(cmd);
			
			try(InputStream inAudio = TromServiceBO.class.getResourceAsStream("/audio/alarme.mp3")){
				BufferedInputStream leitorAudio = new BufferedInputStream(inAudio);
				final Player player = new Player(leitorAudio);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							player.play();
						} catch (JavaLayerException e) {
							e.printStackTrace();
						}
					}
				}).start();

				String desc = "Usuario Banido!\nNotificacao para servidor enviado.";
				LayoutGenericXK.mesageDialogIcoInfo("'Macro' Indetificado", desc, LayoutGenericXK.PATH_ICO_XK_45_64);
				player.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static String verificaMacEnderec() {
		try{
			Process process = Runtime.getRuntime().exec(Util.ENDERECO_MAC);	
			BufferedReader leitor = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String linha,retorno = null;
			while((linha = leitor.readLine()) != null){
				retorno += linha+"\n %";
			}
			
			return retorno.split("%")[3];
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void deteleProvas(){
		File extremeAntCheat = new File(LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT);
		File[] arquivos = extremeAntCheat.listFiles();
		for (File arquivo : arquivos) {
			if(arquivo.getName().contains("prova")){
				arquivo.delete();
			}
		}
	}
	
}