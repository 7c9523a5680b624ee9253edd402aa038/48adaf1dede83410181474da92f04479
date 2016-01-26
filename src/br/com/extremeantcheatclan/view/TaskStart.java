package br.com.extremeantcheatclan.view;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import br.com.extremeancheatclan.socket.SocketTask;
import br.com.extremeantcheatclan.business.TromServiceBO;
import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.entity.Versao;
import br.com.extremeantcheatclan.layout.LayoutGenericXK;
import br.com.extremeantcheatclan.util.Util;

public class TaskStart {

	static TromServiceBO coreSystem;

	public static void main(String[] args) throws Exception {
		boolean isBanido = new XitersDAOImpl().isBanido(Util.getJogadorWarface().getNickJogo(), Util.verificaMacEnderec());
		if(isBanido){
			ImageIcon imageIconXKInfo = LayoutGenericXK.getImageIconXKInfo(LayoutGenericXK.PATH_ICO_XK_45_64);
			JOptionPane.showMessageDialog(null, "Seu Nick ou Endereco Mac foi banido em nosso sistema!", 
					"Banido", JOptionPane.INFORMATION_MESSAGE,imageIconXKInfo);
			System.exit(0);
		}
		
		System.out.println("Iniciando insercao de player on");
		Integer playerOnId = new XitersDAOImpl().insertPlayerOn(Util.getJogadorWarface().getNickJogo(), Util.getJogadorWarface().getId(), 
				Util.getJogadorWarface().getCodigoSala(), Util.getJogadorWarface().getLigaRemetenteId());
		
		System.out.println("Criando sessao do player on no client");
		Util.saveSession(playerOnId);
		
		System.out.println("Iniciando threads de verificacao dos cheats");
		coreSystem = new TromServiceBO();
		Thread start = new Thread(coreSystem);
		start.setName("TromServiceBO");
		start.start();
		
		System.out.println("Validando versao");
		isVersionCorrect();
		
		//Abrindo conexao entreo task eo painel
		new SocketTask().openConnection();
    }
	
	private static void isVersionCorrect(){
		String versaoAtualDesktop = Util.getVersaoAtualExtemeAntCheat(false);
		Versao versaoExtremeAntChet = new XitersDAOImpl().getVersaoExtremeAntChet();
		
		if(!versaoAtualDesktop.equals(Util.parseHashMD5(versaoExtremeAntChet.getUltimaVersao()))
				&& !versaoAtualDesktop.equals(Util.parseHashMD5(versaoExtremeAntChet.getNovaVersao()))){
			System.out.println("Finalizando app - versao incompativel.");
			Util.executeComandoCMD(String.format(LoginHelper.STOP_SERVICE, "TaskExtreme"));
		}
	}
	
}