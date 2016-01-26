package br.com.extremeantcheatclan.view;

import java.io.File;

import br.com.extremeancheatclan.socket.GenericSocket;
import br.com.extremeantcheatclan.business.TromServiceBO;
import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.util.Util;

public class TaskStop {

	public static void main(String[] args) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Util.timeOut(2000);
					
					System.out.println("Inciando fechamento das threas");
					TromServiceBO.SHUTDOWN = false;
					
					System.out.println("Deletando player on");
					new XitersDAOImpl().deletePlayerOff(Util.getIdByFileSession());
					
					String src = String.format("%s/FileSession.data", LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT);
					new File(src).deleteOnExit();
					
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Erro ao deslogar ou deletar jogador. Exeptipn : " + e.getMessage());
				}	
			}
		}).start();
		
		System.out.println("Iniciando fechamento do painel-service");
		GenericSocket.send(GenericSocket.COMMANDO_SOCKET_STOP_PAINEL_SERVICE);
	}
	
}