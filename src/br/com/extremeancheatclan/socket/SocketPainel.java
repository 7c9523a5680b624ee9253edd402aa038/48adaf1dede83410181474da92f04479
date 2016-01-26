package br.com.extremeancheatclan.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.macro.CheckMacros;
import br.com.extremeantcheatclan.util.Util;
import br.com.extremeantcheatclan.view.PainelServicoSystemTray;

public class SocketPainel extends GenericSocket implements SocketXK{

	public static boolean isConect = true;
	
	public static void main(String[] args) {
		new SocketPainel().serviceOpenConnection();
	}
	
	@Override
	public void openConnection() {
		try{
			client = new Socket(LOCAL_HOST_IP, LOCAL_HOST_PORT); 
			
			if(isConect){
				System.out.println("Cliente conectado - SocketPainel");
				isConect = false;
			}
			
			try{
				ObjectInputStream entrada = new ObjectInputStream(client.getInputStream());
				Object object = entrada.readObject();
				
				boolean isNotNulClient = (object != null && !((String) object).isEmpty());
				
				if(isNotNulClient){
					String retorno = (String) object;
					String parseDadosRetorno = "";
					if(!GenericSocket.COMMANDO_SOCKET_STOP_PAINEL_SERVICE.equals(retorno) && retorno.contains("-")){
						parseDadosRetorno = retorno.split("-")[1];
						retorno = retorno.split("-")[0];
					}
					
					switch (retorno) {
					case GenericSocket.COMMANDO_SOCKET_SCREENSHOT:
						new XitersDAOImpl().sendRetornoByComando(Util.capturePrintTela(), 1, Integer.valueOf(parseDadosRetorno));
						//remove o comando executado da lista de espera
						new XitersDAOImpl().deleteComandoAntXiterTrom(Integer.valueOf(parseDadosRetorno));
						parseDadosRetorno = "";
						break;
					case GenericSocket.COMMANDO_SOCKET_THEMA_BASIC:
						Util.executeComandoCMD(Util.CMD_THEMA_BAISC);
						//remove o comando executado da lista de espera.
						new XitersDAOImpl().deleteComandoAntXiterTrom(Integer.valueOf(parseDadosRetorno));
						parseDadosRetorno = "";
						break;
					case GenericSocket.COMMANDO_SOCKET_STOP_PAINEL_SERVICE:
						client.close();
						PainelServicoSystemTray.closePainelServico();
						break;
					default://Cheack os macros
						new CheckMacros(retorno);
						break;
					}
					
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			client.close();
		}catch(UnknownHostException e){
			System.out.println("Sem conexao com servidor.."+(e.getMessage())); 
			isConect = true;
			Util.timeOut(5000);
		}catch(java.net.ConnectException e){
			System.out.println("Sem conexao com servidor.."+(e.getMessage())); 
			isConect = true;
			Util.timeOut(5000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void serviceOpenConnection(){
		Thread service = new Thread(new Runnable() {
			@Override
			public void run() {
				Util.timeOut(10000);
				while(true){
					Util.timeOut(1000);
					try{
						openConnection();
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		});
		
		service.setName(" Socket Painel - Client");
		service.start();
	}
	
}
//if(!retorno.contains("&")){
//	isMacro = true;
//	new CheckMacros(retorno);
//	
//	boolean shutdown = true;
//	while(shutdown){ 
//		Util.timeOut(1000);
//		if(LeitorImagemMacroRazer.larguraAlturaToWindowRazer != null && !LeitorImagemMacroRazer.larguraAlturaToWindowRazer.isEmpty()){
//			retornoToServer = LeitorImagemMacroRazer.larguraAlturaToWindowRazer;
//			shutdown = false;
//		}
//	}
//}else{
//	JOptionPane.showMessageDialog(null, "Ta no painel");
//	String[] parseAlturaLarguraRazer = retorno.split("&");
//	Integer altura = Integer.valueOf(parseAlturaLarguraRazer[2]);
//	Integer largura = Integer.valueOf(parseAlturaLarguraRazer[1]);
//	
//	new Razer(altura, largura);
//}