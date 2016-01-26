package br.com.extremeantcheatclan.business;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;

import br.com.extremeantcheatclan.dao.server.impl.XitersDAOImpl;
import br.com.extremeantcheatclan.macro.ListaMacro;
import br.com.extremeantcheatclan.util.Util;
import br.com.extremeantcheatclan.view.LoginHelper;

public class ServicoCheckSuspeito extends Thread {

	public ServicoCheckSuspeito() {
		setName("ServicoCheckSuspeito");
	}
	
	@Override
	public void run() {
		Util.timeOut(300000);//5 min
		Object[] provas = new Object[3];
		int index = -1;
		
		String src = LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT;
		File extremeAntCheat = new File(src);
		File[] arquivos = extremeAntCheat.listFiles();
		for (File arquivo : arquivos) {
			if(arquivo.getName().contains("prova")){
				byte[] imageInByte = null;
				try(ByteArrayOutputStream baos = new ByteArrayOutputStream()){
					ImageIO.write(ImageIO.read(arquivo), "PNG", baos);
					imageInByte = baos.toByteArray();
				}catch(Exception e){
					e.printStackTrace();
				}
				provas[index++] = imageInByte;
				
				Util.timeOut(1000);
				arquivo.delete();
			}
		}
		
		if(provas[0] != null){
			String descricao = "Suspeito nao so fecho o razer como mato o processo do painel";
			new XitersDAOImpl().saveSuspeito("macro-"+ListaMacro.macrosExistentes.get(0), provas, descricao);	
		}
	}

}
