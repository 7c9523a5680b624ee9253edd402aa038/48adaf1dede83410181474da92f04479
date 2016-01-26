package br.com.extremeancheatclan.socket;

import br.com.extremeantcheatclan.dao.factory.FactoryConnectJDBC;


public class Teste {

	public static void main(String[] args) throws Exception {
		//String src = String.format("%s/FileSession.data", LoginHelper.PATH_SERVICE_EXTREME_ANT_CHEAT);
		//new File(src).deleteOnExit();
		
		//delete from return_comandos
		//delete from player_on where id = 462
		//delte from 
		FactoryConnectJDBC.getConnection().prepareStatement("delete from xiter").execute();
	}
	
}