package br.com.extremeantcheatclan.dao.factory;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FactoryConnectJDBC {

	/***
	 * usuairo = extremekillers
	 *	email = giovanasilveiracardoso@gmail.com
	 */
	
	private static String usuario = null;
	private static String senha = null;
	private static String url = null;
	//private static String driver = "org.postgresql.Driver";
	
	public static void createUrlServer() {
		try{
			if(new File("C:\\eclipse\\setup-jaderson\\dev.properties").exists()){
				usuario = "postgres";
				senha = "admin";
				url = "jdbc:postgresql://localhost:5432/extremeKillers";
			}else{
				usuario = "berti_bd";
				senha = "Jb!1mexupa";
				url = "jdbc:postgresql://216.218.192.170:5432/berti_extreme";
			}
		}catch(Exception e){
			usuario = "berti_bd";
			senha = "Jb!1mexupa";
			url = "jdbc:postgresql://216.218.192.170:5432/berti_extreme";
		}
	}
	
	public static Connection getConnection(){
		try {
			usuario = "berti_bd";
			senha = "Jb!1mexupa";
			url = "jdbc:postgresql://216.218.192.170:5432/berti_extreme";
			
			Connection connection = DriverManager.getConnection(url,usuario,senha);
			return connection;
		 } catch (SQLException e) {
			 e.printStackTrace();
			 System.err.println("Erro ao conectar");
		}
		return null;
	}
	
}