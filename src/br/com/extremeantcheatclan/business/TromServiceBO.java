package br.com.extremeantcheatclan.business;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JOptionPane;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import br.com.extremeancheatclan.socket.GenericSocket;
import br.com.extremeantcheatclan.dao.server.XitersDAO;
import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.Jogador;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.macro.ListaMacro;
import br.com.extremeantcheatclan.util.Util;

public class TromServiceBO implements Runnable{

	private List<String> xiters;
	private List<String> xitersExistente;
	public static Map<String, String> macrosJaVerificados;
	private XitersDAO dao;

	public static boolean SHUTDOWN = true;
	
	private List<String> loadXiters(){
		List<String> lista = new ArrayList<String>();
		try(InputStream xitersInputStream = TromServiceBO.class.getResourceAsStream("/tiposXiters/xiters.txt");
				BufferedReader leitorTxt = new BufferedReader(new InputStreamReader(xitersInputStream))){
			
			String linha = "";
			while((linha = leitorTxt.readLine()) != null){
				lista.add(linha);
			}
			
			return lista;
		}catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	@Override
	public void run() {
		//Inicia servico para validar player
		Thread playerOnIsLifeThread = this.playerOnIsLifeThread();
		
		while (SHUTDOWN) {
			try {
				Thread.sleep(5000);
				//Pergunta se tem comando para o trom executar
				this.hasComandoToTrom();
				//Verificar os xiters e macro nos processos do 'SO'
				this.verificaServicoSO();
			} catch (Exception e) {
				String mesage = "Exepition : "+e.getMessage()+"\nIniciando tentativas de conexao..";
				System.out.println(mesage);
				//Inicia verificacao e tentativas de conexao.
				startVerificaInternet();
				try {
					//Limpa o player da maquina e inicia um novo
					System.out.println("Reniciando insercao de player on");
					Integer playerOnId = new XitersDAOImpl().insertPlayerOn(Util.getJogadorWarface().getNickJogo(), Util.getJogadorWarface().getId(), 
							Util.getJogadorWarface().getCodigoSala(), Util.getJogadorWarface().getLigaRemetenteId());
				
					System.out.println("Recriando sessao do player on no client");
					Util.saveSession(playerOnId);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		this.stopPlayerOnIsLifeThread(playerOnIsLifeThread);
	}
	
	private Thread playerOnIsLifeThread(){
		Thread playerOnIsLifeThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(SHUTDOWN){
					Util.timeOut(240000);//4min
					//Atualiza status no servidor
  					dao.playerOnIsLife(Util.getIdByFileSession());
				}
			}
		});
		playerOnIsLifeThread.setName(" playerOnIsLifeThread ");
		playerOnIsLifeThread.start();
		
		return playerOnIsLifeThread;
	}

	@SuppressWarnings("deprecation")
	public void stopPlayerOnIsLifeThread(Thread playerOnIsLifeThread){
		playerOnIsLifeThread.stop();
	}
	
	private void startVerificaInternet() {
		while(true){
			try{
				Thread.sleep(60000);
				
	            java.net.URL mandarMail = new java.net.URL("http://www.guj.com.br");
	            java.net.URLConnection conn = mandarMail.openConnection();
	            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) conn;
	            httpConn.connect();
	            int x = httpConn.getResponseCode();
	            if(x == 200){
	            	System.out.println("Conctado status 200");
	            	httpConn.disconnect();
	            	return;
	            }
	        }catch(java.net.MalformedURLException urlmal){
	        	System.out.println(urlmal.getMessage());
	        }catch(java.io.IOException ioexcp){
	        	System.out.println(ioexcp.getMessage());
	        } catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}	
	}

	public void startThreadCheckColorImage(){
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (SHUTDOWN) {
					try{
						Thread.sleep(5000);
						byte[] capturePrintTela = Util.capturePrintTela();
						if(checkContentsImageIsBlack(capturePrintTela)){
							Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		thread.setName("ThreadCheckColorImage");
		thread.start();
	}
	
	public void hasComandoToTrom(){
		switch (dao.findComenadosAll()) {
		case 1://Tira print
			//Atualmente um processo em modo servico no SO nao tem poermissao para manipular dados visiveis
			//Entao a solucao foi enviar esse comando para o painel.
			GenericSocket.send(GenericSocket.COMMANDO_SOCKET_SCREENSHOT+"-"+Util.getJogadorWarface().getId());
			break;
		case 2://copia todos processos
			dao.sendRetornoByComando(Util.executeComandoCMDReturn(Util.TASK_LIST).getBytes(), 2,Util.getJogadorWarface().getId());
			//remove o comando executado da lista de espera.
			dao.deleteComandoAntXiterTrom(Util.getJogadorWarface().getId());
			break;
		case 3://Muda o thema basic do win
			//Atualmente um processo em modo servico no SO nao tem poermissao para manipular dados visiveis
			//Entao a solucao foi enviar esse comando para o painel.
			GenericSocket.send(GenericSocket.COMMANDO_SOCKET_SCREENSHOT+"-"+Util.getJogadorWarface().getId());
			break;
		case 4://Servidor Off
			Icon figura = LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK_45_64);
			//remove o comando executado da lista de espera.
			dao.deleteComandoAntXiterTrom(Util.getJogadorWarface().getId());
			JOptionPane.showMessageDialog(null, "Administrador fecho servidor","Servidor Off",JOptionPane.ERROR_MESSAGE,figura);
			
			try {
				//new XitersDAOImpl().deletePlayerOff(new MonitorandoPlayer().getIdByFileSession());
				Util.removeJogadorWarfaceLocal();
			} catch (Exception e) {
				e.printStackTrace();
			}	

			System.exit(0);
			break;
		case 0:
			//Comando nao existe
			break;
		default :
			break;
		}
	}
	
	
	public TromServiceBO() {
		xiters = new ArrayList<>();
		xitersExistente = this.loadXiters();
		macrosJaVerificados = new HashMap<String, String>();
		dao = new XitersDAOImpl();
	}

	private String verificaMacEnderec() {
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

	public void verificaServicoSO() {
		try {
			String cmd = "tasklist";
			Process process = Runtime.getRuntime().exec(cmd);
			Scanner leitor = new Scanner(process.getInputStream());
			while (leitor.hasNext()) {
				String linha = leitor.next();
				linha = linha.toLowerCase();
				
				//Verifica os xiters que existem no jogo.
				for(String xiterExistente : xitersExistente){
					if (linha.equalsIgnoreCase(xiterExistente)) {
						// nome do xiter com toLowerCase mais o tipo mais nome original
						xiters.add(xiterExistente+"-xiter-"+linha);
						System.out.println(leitor.next());
					}
				}
				
				//Verifica os macro que existem no jogo.
				for(String macro : ListaMacro.macrosExistentes){
					if (linha.equalsIgnoreCase(macro) && !macrosJaVerificados.containsKey(macro)) {
						//Adiciona na lista de verificados
						macrosJaVerificados.put(macro, "");
						
						//Envia via socket para painel scaniar o usuario
						GenericSocket.send(macro);
						
						//Caso aja provas ele ira efeturar leitura 
						//e enviar apra servidor
						new ServicoCheckSuspeito().start();
						
						System.out.println(linha);
					}
				}
				
			}
			
			leitor.close();
			if(xiters != null &&  !xiters.isEmpty()){
				sendDadosToServer(xiters);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static boolean checkContentsImageIsBlack(byte[] img){
		try(@SuppressWarnings("resource")ByteArrayInputStream in = new ByteArrayInputStream(img)){
			BufferedImage bufferedImage = ImageIO.read(in); 
			int rgbCorPreta = -16777216;
			
			for (int y = 0; y < bufferedImage.getHeight(); y++)  
			for (int x = 0; x < bufferedImage.getWidth(); x++){  
	            Color pixel = new Color(bufferedImage.getRGB(x, y));  
	            if(rgbCorPreta != pixel.getRGB()){
	            	return false;
	            }
			}  
			
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void sendDadosToServer(List<String> listXiter) throws Exception {
		Jogador jogador = new Jogador();
		jogador.setNome(Util.getJogadorWarface().getNome());
		jogador.setNickJogo(Util.getJogadorWarface().getNickJogo());
		jogador.setXiters(listXiter);
		jogador.setTxtDescricaoDoPc(verificaMacEnderec().getBytes());
		if(listXiter.contains("-xiter-")){
			byte[] img = Util.capturePrintTela();
			if(checkContentsImageIsBlack(img)){
				Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
				Thread.sleep(7000);
				jogador.setPrintXiterOrMacro(Util.capturePrintTela());
			}else{
				jogador.setPrintXiterOrMacro(img);
			}
			
			
		}
		jogador.setDescricaoServicos(Util.executeComandoCMDReturn(Util.TASK_LIST));
		new XitersDAOImpl().insert(jogador);
		
		String cmd = "taskkill /F /IM Game.exe";
		@SuppressWarnings("unused")
		Process process = Runtime.getRuntime().exec(cmd);

		for(String xiter : listXiter){
			String[] nameXiterProcess = xiter.split("-");
			@SuppressWarnings("unused")
			Process processoKillTask = Runtime.getRuntime().exec("taskkill /F /IM "+nameXiterProcess[2].toString());
		}
		xiters = new ArrayList<>();
		
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
					
					JOptionPane.showMessageDialog(null, "'Xiter' indetificado\nNotifica��o para servidor enviado.");
					player.close();
					System.exit(0);
				}
			}).start();
		}
	}

}